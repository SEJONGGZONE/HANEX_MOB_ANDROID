package com.gzonesoft.sg623.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.Layout.InvoiceItemLayout;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.DeliveryItemInfo;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InvoiceMngActivity extends AppCompatActivity {

    private ScrollView  scrollInvoiceList;
    private LinearLayout ll_invoice, llInvoiceList; // 아이템 리스트
    private ImageView iv_back;
    private Button btnSave, btnClose;

    public static Context mContext = null;

    ProgressDialog pDialog;

    ArrayList<DeliveryItemInfo> deliveryItemInfos = new ArrayList<DeliveryItemInfo>();

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int ERP_DISPATCH_ITEM_SEL = 10001;   // 배차상품 정보 가져오기
    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case ERP_DISPATCH_ITEM_SEL:

                    // 대기화면 보이기/감추기..
                    showProgress(false);


                    // 거래명세서 준비..
                    setInvoiceItemInfo();


//                    // PDF출력준비..
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            showProgress(false);
//                            // PDF출력..
//                            String title = mDeliveryListInfo.getDDATE() + "-" + mDeliveryListInfo.getCLNAME();
//                            CreatePdf(title);
//                        }
//                    }, 1000);

                    break;

                case RETRY_PROCESS:

                    // 대기화면 닫기..
                    showProgress(false);

                    break;
                case RequesterSession.REQ_OK:

                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    // 대기화면 보이기/감추기..
                    showProgress(false);

                    break;

                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    // 대기화면 보이기/감추기..
                    showProgress(false);
                    break;

            }


        }
    };


    public void CreatePdf(String title) {

        PdfGenerator.getBuilder()
                .setContext(InvoiceMngActivity.this)
                .fromViewIDSource()
                .fromViewID(InvoiceMngActivity.this, R.id.ll_invoice)
                .setFileName(title)
                .setFolderNameOrPath("MyFolder/MyDemoLandscape/")
                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);

                    }

                    @Override
                    public void onStartPDFGeneration() {

                    }

                    @Override
                    public void onFinishPDFGeneration() {

                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);

                    }
                });
    }

    private DeliveryListInfo mDeliveryListInfo = new DeliveryListInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mDeliveryListInfo = (DeliveryListInfo) intent.getSerializableExtra("CUR_DELIVERY_LIST_INFO");

        // 컨텍스트 저장
        mContext = this;

        // 프로그래스 다이얼로그 초기화
        initProgressDlg();

        // 메인 UI 초기화
        initMainUI();

        // 배송상품정보 요청
        requestDeliveryItemList();

    }

    // 팝업여부 확인
    public boolean popOpenYN = true;
    @Override
    protected void onResume() {
        super.onResume();
        Loggers.d("MainActivity, 화면 복귀시..");

        popOpenYN = false;
    }

    /**
     * 메인 UI 초기화
     */
    private void initMainUI() {
        try {
            // 화면을 portrait(세로) 화면으로 고정
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            setContentView(R.layout.activity_invoice_mng);

            // 거래명세서
            ll_invoice = (LinearLayout) findViewById(R.id.ll_invoice); // 전체 레이아웃
            scrollInvoiceList = (ScrollView) findViewById(R.id.scrollInvoiceList); // 스크롤
            llInvoiceList = (LinearLayout) findViewById(R.id.llInvoiceList); // 아이템 리스트


            // 뒤로가기 버튼
            iv_back = findViewById(R.id.iv_back);
            iv_back.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinishWork();
                }
            });

            String htmlString = "";
            TextView tvInvoiceTitle = findViewById(R.id.tvInvoiceTitle);
            htmlString = "<small><small>생활의건강-</small></small> (주) 목원푸드";
            CommonUtil.with().setHtmlText(tvInvoiceTitle, htmlString);

//            TextView tvInvoiceTitle = findViewById(R.id.tvInvoiceTitle);
//            htmlString = "<small><small>생활의건강-</small></small> (주) 목원푸드";
//            CommonUtil.with().setHtmlText(tvInvoiceTitle, htmlString);

            // 하단버튼..
            btnSave = (Button) findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // PDF출력..
                    String title = mDeliveryListInfo.getDDATE() + "-" + mDeliveryListInfo.getCLNAME();
                    CreatePdf(title);
                }
            });

            // (거래명세서 공유) 닫기버튼
            btnClose = (Button) findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinishWork();
                }
            });



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 종료작업 - 팝업 닫기
     */
    public void FinishWork() {

        // 닫기
        finish();
        overridePendingTransition(R.anim.slide_down, R.anim.no_change);
    }


    /**
     * 프로그래스 다이얼로그 초기화
     */
    private void initProgressDlg() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setTitle("잠시만 기다려주세요.");
        pDialog.setMessage("서버 요청중입니다.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
    }

    /**
     * 프로그레스 컨트롤 제어
     *
     * @param show 보이기 여부
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            if (show) {
                pDialog.show();
            }
            else {
                pDialog.dismiss();
            }
        } else {
            if (show) {
                pDialog.show();
            }
            else {
                pDialog.dismiss();
            }
        }
    }

    /**
     * 거래명세서-아이템 세팅
     */
    private void setInvoiceItemInfo() {
        try {
            String htmlString = "";

            TextView tvInvInfo1 = (TextView) findViewById(R.id.tvInvInfo1); // 거래번호
            htmlString = "<div style='line-height:150%;'><small>거래번호 : </small>" + mDeliveryListInfo.getWSD_NUM();
            CommonUtil.with().setHtmlText(tvInvInfo1, htmlString);

            TextView tvInvInfo2 = (TextView) findViewById(R.id.tvInvInfo2); // 거래일자
            htmlString = "<small>거래일자 :</small>" + CommonUtil.with().nowYYYY_MM_DD();
            CommonUtil.with().setHtmlText(tvInvInfo2, htmlString);

            // 업체정보
            TextView tvTaxReceiveInfo1 = (TextView) findViewById(R.id.tvTaxReceiveInfo1);
            TextView tvTaxReceiveInfo2 = (TextView) findViewById(R.id.tvTaxReceiveInfo2);
            TextView tvTaxReceiveInfo3 = (TextView) findViewById(R.id.tvTaxReceiveInfo3);
            TextView tvTaxReceiveInfo4 = (TextView) findViewById(R.id.tvTaxReceiveInfo4);
            TextView tvTaxReceiveInfo5 = (TextView) findViewById(R.id.tvTaxReceiveInfo5);
            TextView tvTaxReceiveInfo6 = (TextView) findViewById(R.id.tvTaxReceiveInfo6);

            CommonUtil.with().setHtmlText(tvTaxReceiveInfo1, "713-86-02466");
            CommonUtil.with().setHtmlText(tvTaxReceiveInfo2, mDeliveryListInfo.getCLNAME());
            CommonUtil.with().setHtmlText(tvTaxReceiveInfo3, mDeliveryListInfo.getPIC_NAME());
            CommonUtil.with().setHtmlText(tvTaxReceiveInfo4, mDeliveryListInfo.getADDRESS());
            CommonUtil.with().setHtmlText(tvTaxReceiveInfo5, "도소매");
            CommonUtil.with().setHtmlText(tvTaxReceiveInfo6, "음식점");

            int nIdx = 0;
            double totQty = 0, totAmt = 0;
            for (DeliveryItemInfo info : deliveryItemInfos) {
                nIdx++;
                totQty += Double.parseDouble(info.getWSD_QTY().replace(",", ""));
                totAmt += Double.parseDouble(info.getWSD_TOT().replace(",", ""));
                if (deliveryItemInfos.size() == nIdx) {
                    info.setbLastYn(true);
                    info.setTOT_QTY(CommonUtil.with().toNumFormat(totQty));
                    info.setTOT_AMOUNT(CommonUtil.with().toNumFormat(totAmt));

                    htmlString = "<BR>" +
                            "농협 351-0777-4973-23 / (주)목원푸드<BR>" +
                            "043-233-5338 / 010-8829-1753<BR>" +
                            "<BR>" +
                            "<small>배송사원 : </small>" + mDeliveryListInfo.getEPNAME() + "/ <small>전화번호 : </small>" + mDeliveryListInfo.getEPPHONE();
                    info.setFOOTER_INFO(htmlString);
                }
                addInvoiceItem(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 거래명세서-아이템 추가
     * @param info
     */
    private void addInvoiceItem(DeliveryItemInfo info) {
        InvoiceItemLayout itemLayout1 = new InvoiceItemLayout(getApplicationContext(), info);
        llInvoiceList.addView(itemLayout1);
    }


    /**
     * 배송상품정보 요청
     */
    public void requestDeliveryItemList() {

        try {

            showProgress(true);

            // 거래명세서 리스트 삭제
            llInvoiceList.removeAllViews();

            Map<String, String> jsonMap = new HashMap<String, String>();
            // 운영용..
            jsonMap.put("@I_DDATE", mDeliveryListInfo.getDDATE());
            jsonMap.put("@I_WSD_NUM", mDeliveryListInfo.getWSD_NUM());
            jsonMap.put("@I_CLCODE", mDeliveryListInfo.getCLCODE());
            jsonMap.put("@I_INPUT_USER", AppSetting.cvoUserInfo.getNAME());

            RequesterSession.with().RestAPIServiceCall(Svc.ERP_DISPATCH_ITEM_SEL, jsonMap, requestFavItemListCallback);

        }
        catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }
    }

    /**
     * REST-API Callback - 관심품목 요청, 콜백
     */
    private Callback requestFavItemListCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "requestFavItemListCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "requestFavItemListCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "requestFavItemListCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestFavItemListCallback, 수신데이타:" + strJsonOutput);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject jObject = jelement.getAsJsonObject();

                String ResultCode = jObject.get("ResultCode").getAsString();
                String ResultMsg = jObject.get("ResultMsg").getAsString();
                String RecordCount = jObject.get("RecordCount").getAsString();
                JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();

                if (!ResultCode.equals("00")) {
                    // 요청 실패
                } else {
                    // 요청 성공
                    if (jarrayRecordSet.size() == 0) {
                        // 데이타가 없음 - 서비스 호출 실패
                        //handler.obtainMessage(RequesterSession.REQ_OK, getString(R.string.api_has_nodata)).sendToTarget();
                        // 서비스 호출 성공
                        handler.obtainMessage(ERP_DISPATCH_ITEM_SEL, "").sendToTarget();
                    } else {

                        // 데이타 초기화
                        deliveryItemInfos.clear();

                        for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
                            JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
                            Gson gson = new Gson();

                            // 데이타 추가..
                            DeliveryItemInfo info = gson.fromJson(jobjectStore, DeliveryItemInfo.class);
                            deliveryItemInfos.add(info);
                        }

                        // 서비스 호출 성공
                        handler.obtainMessage(ERP_DISPATCH_ITEM_SEL, "").sendToTarget();
                    }
                }

            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }
        }
    };










    /**
     * 애니메이션 효과 - 우측에서 나타나기
     */
    private long slideLeftDelayTime = 10;
    private void slideLeft(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_middle);
                targetView.startAnimation(anim);
            }
        }, slideLeftDelayTime);
        slideLeftDelayTime = slideLeftDelayTime + 100;
    }

    /**
     * 애니메이션 효과 - 좌측에서 나타나기
     */
    private long slideRightDelayTime = 10;
    private void slideRight(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_middle);
                targetView.startAnimation(anim);
            }
        }, slideRightDelayTime);
        slideRightDelayTime = slideRightDelayTime + 100;
    }

    /**
     * 애니메이션 효과 - 아래에서 나타나기
     */
    private long slideUpDelayTime = 100;
    private void slideUp(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_slow);
                targetView.startAnimation(anim);
            }
        }, slideUpDelayTime);
        slideUpDelayTime = slideUpDelayTime + 100;
    }

    /**
     * 애니메이션 효과 - 아래에서 나타나기
     */
    private long slideDownDelayTime = 100;
    private void slideDown(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_down_slow);
                targetView.startAnimation(anim);
            }
        }, slideDownDelayTime);
        slideDownDelayTime = slideDownDelayTime + 100;
    }

}
