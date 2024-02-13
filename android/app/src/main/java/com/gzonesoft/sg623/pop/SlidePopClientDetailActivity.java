package com.gzonesoft.sg623.pop;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.gzonesoft.cookzzang.ImageShowActivity;
//import com.gzonesoft.cookzzang.Layout.ClientPopFavItemLayout;
//import com.gzonesoft.cookzzang.LoginActivity;
import com.gzonesoft.sg623.Layout.DeliveryItemLayout;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
//import com.gzonesoft.cookzzang.data.ClientCustInfo;
//import com.gzonesoft.cookzzang.data.FavItemInfo;
//import com.gzonesoft.cookzzang.domain.JoinStatus;
//import com.gzonesoft.cookzzang.model.ClientCustManager;
//import com.gzonesoft.cookzzang.ui.UserKeyinPaymentActivity;
//import com.gzonesoft.cookzzang.ui.WizSujuListActivity;
import com.gzonesoft.sg623.data.DeliveryItemInfo;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.model.DeliveryListManager;
import com.gzonesoft.sg623.ui.BoardActivity;
import com.gzonesoft.sg623.ui.DeliveryListActivity;
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

public class SlidePopClientDetailActivity extends AppCompatActivity {
    public static Context mContext = null;

    private String curAreaType = "";

    private TextView tvInfo01, tvInfo02, tvInfo03, tvInfo04, tvInfo05, tvInfo06;
    private LinearLayout llItemList; // 아이템 리스트
    private ScrollView scFavitemList;

    private Button btnPayment, btn01, btn02;

    ArrayList<DeliveryItemInfo> deliveryItemInfos = new ArrayList<DeliveryItemInfo>();

    private String mClCode;
//    private ClientCustInfo mCurInfo = new ClientCustInfo();
    private DeliveryListInfo mCurInfo = new DeliveryListInfo();

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int ERP_DISPATCH_ITEM_SEL = 10000;   // 관심품목 정보 가져오기
    public static final int SMS_SEND = 10001;   // 문자보내기

    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SMS_SEND:
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case ERP_DISPATCH_ITEM_SEL:
                    showProgress(false);
                    // 아이템 세팅
                    setItemInfo();
                    break;
                case RequesterSession.REQ_OK:
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // 넘어온 데이타 확인
        Intent intent = getIntent();
        mClCode = String.valueOf(intent.getStringExtra("CLCODE"));

        // 거래처 정보 가져오기
        mCurInfo = DeliveryListManager.getInstance().getInfo(mClCode);

        // UI 초기화
        initUI();

        // 프로그래스 다이얼로그 초기화
        initProgressDlg();

        // 배차 상품정보 조회
        requestDeliveryItemList();
    }


//    public void showImageViewer(String imagePath) {
//        // 이미지 보기 액티비티 이동...
//        Intent intent = new Intent(mContext, ImageShowActivity.class);
//        intent.putExtra("SHOW_IMAGE_PATH", imagePath);
//        startActivity(intent);
//    }

    @Override
    public void setRequestedOrientation(int requestedOrientation){
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            super.setRequestedOrientation(requestedOrientation);
        }
    }

    /**
     * UI 초기화
     */
    private void initUI() {
        setContentView(R.layout.activity_pop_client_detail);

        // 상태바 색상 지정(검은색으로)
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().hide();

        // 뷰 매핑
        tvInfo01 = (TextView) findViewById(R.id.tvInfo01);
        tvInfo02 = (TextView) findViewById(R.id.tvInfo02);
        tvInfo03 = (TextView) findViewById(R.id.tvInfo03);
        tvInfo04 = (TextView) findViewById(R.id.tvInfo04);
        tvInfo05 = (TextView) findViewById(R.id.tvInfo05);
        tvInfo06 = (TextView) findViewById(R.id.tvInfo06);

        String htmlString = "";
        // ------------------------------------------------------------
        htmlString = "<BIG><B>" + mCurInfo.getCLNAME() + "</B></BIG>" +
                " - <B><SMALL>" + mCurInfo.getCLCEO() + "</B></SMALL>" +
                "";
        CommonUtil.with().setHtmlMarqueeText(tvInfo01, htmlString);
        // ------------------------------------------------------------
        htmlString = "UPDATE<BR>" + mCurInfo.getLAST_UPDATEDATE();
        CommonUtil.with().setHtmlText(tvInfo02, htmlString);
        // ------------------------------------------------------------
        htmlString = mCurInfo.getADDRESS();
        CommonUtil.with().setHtmlText(tvInfo03, htmlString);
        // ------------------------------------------------------------
//        htmlString = "";
//        htmlString += (mCurInfo.getCLTEL().replace(" ", "").length()>0)? mCurInfo.getCLTEL() : "";
//        htmlString += (mCurInfo.getCLPHONE().replace(" ", "").length()>0)? (htmlString.length()>0)?" / ":"" + mCurInfo.getCLPHONE() : "";
//        htmlString += (mCurInfo.getCLFAX().replace(" ", "").length()>0)? (htmlString.length()>0)?" / ":"" + mCurInfo.getCLFAX() : "";
//        CommonUtil.with().setHtmlMarqueeText(tvInfo04, htmlString);
        // ------------------------------------------------------------
        htmlString = mCurInfo.getCLCEO();
        CommonUtil.with().setHtmlMarqueeText(tvInfo04, htmlString);
        // ------------------------------------------------------------
        htmlString = mCurInfo.getCLPHONE();
        CommonUtil.with().setHtmlMarqueeText(tvInfo05, htmlString);
        // ------------------------------------------------------------
        htmlString = "" +
                "<BIG>" + mCurInfo.getITEM_INFO() + "</BIG>" +
                // "(" + mCurInfo.getITEM_CNT() + "<small>건</small>)" +
                " - <BIG>" + mCurInfo.getORDER_AMT() + "</BIG>" +
                "<small>원</small>";
        CommonUtil.with().setHtmlMarqueeText(tvInfo06, htmlString);
        // ------------------------------------------------------------
        // ------------------------------------------------------------
        // ------------------------------------------------------------
        // ------------------------------------------------------------


        llItemList = (LinearLayout) findViewById(R.id.llItemList); // 아이템 리스트

        btnPayment = (Button) findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.with().ToastMsg(mContext, "준비중입니다..", Toast.LENGTH_SHORT).show();
//                try {
//                    // 수기결제화면으로 이동
//                    Intent intent = new Intent(getApplicationContext(), UserKeyinPaymentActivity.class);
//                    intent.putExtra("CUST_INFO", mCurInfo);
////                    intent.putExtra("TARGET_CLIENT", mCurInfo.getCLCODE());
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        btn01 = (Button) findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurInfo.getCLPHONE().length()>0) {

                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                        // 안드로이드 M 이상일 경우
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // 전화걸기 시도...
                            String mdn = mCurInfo.getCLPHONE().replace("-", "");
                            Loggers.d("[fromMobileMessage] mdn =  " + mdn);
                            // 전화걸기
                            final String callString = "tel:" + mdn;
                            Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse(callString));
                            startActivity(tt);
                        }
                        else {
                            //전화 권한 없을 경우
                            CommonUtil.with().ToastMsg(mContext, "전화걸기 권한을 확인바랍니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else { // 안드로이드 M 이하일 경우
                        // 전화걸기 시도...
                        String mdn = mCurInfo.getCLPHONE().replace("-", "");
                        Loggers.d("[fromMobileMessage] mdn =  " + mdn);
                        // 전화걸기
                        final String callString = "tel:" + mdn;
                        Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse(callString));
                        startActivity(tt);
                    }

                } else {
                    CommonUtil.with().ToastMsg(mContext, "등록된 전화번호를 확인바랍니다.", Toast.LENGTH_SHORT).show();
                }
//                try {
//                    // 주문내역보기
//
//                    // 당일주문 표시..
//                    Intent intent = new Intent(getApplicationContext(), WizSujuListActivity.class);
//                    intent.putExtra("SEARCH_TYPE", "10");
//                    intent.putExtra("TARGET_CLIENT", mCurInfo.getCLCODE());
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        btn02 = (Button) findViewById(R.id.btn02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TMAP
                ((DeliveryListActivity) DeliveryListActivity.mContext).launchTMapNavi(mCurInfo);
                // 카카오내비
                //((DeliveryListActivity) DeliveryListActivity.mContext).launchKakaoNavi(mCurInfo);
            }
        });

        llItemList.removeAllViews(); // 뷰지우기..

        scFavitemList = (ScrollView) findViewById(R.id.scFavitemList);
        // 스크롤뷰 마지막 위치 이벤트 감지하기
//        scFavitemList.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged() {
//
//                View view = scFavitemList.getChildAt(scFavitemList.getChildCount() - 1);
//                int diff = (view.getBottom() - (scFavitemList.getHeight() + scFavitemList.getScrollY()));
//
//                if (diff == 0) { // 스크롤 bottom
//                    Loggers.d("스크롤바닥..");
//                    pageNum = pageNum + 1;
//                    if (curResultCount < pageSize) {
//                        Loggers.d("추가요청필요없음..");
//                    } else {
//                        requestDeliveryItemList();
//                    }
//                }
//
//            }
//        });

    }


    public void viewItemImage(String urlString) {
        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
        intent.putExtra("MOVE_URL", urlString);
        intent.putExtra("MOVE_TYPE", CommType._배송상품이미지);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }
    /**
     * 주문내역보기
     */
    private void showOrderList() {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 아이템 세팅
     */
    private void setItemInfo() {
        try {
//            ArrayList<DeliveryItemInfo> deliveryItemInfos = new ArrayList<DeliveryItemInfo>();
//
//            String jsonData = "{\"RecordSet\":" + mCurInfo.getETC_DATA() + "}";
//            JsonElement jelement = new JsonParser().parse(jsonData);
//            JsonObject recvObj = jelement.getAsJsonObject();
//            JsonArray jarrayShif = recvObj.getAsJsonArray("RecordSet");
//
//            deliveryItemInfos.clear();
//            for (int idx = 0; idx < jarrayShif.size(); ++idx) {
//                JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();
//
//                Gson gson = new Gson();
//                DeliveryItemInfo info = gson.fromJson(jobject, DeliveryItemInfo.class);
//                deliveryItemInfos.add(info); // 정보 추가
//            }
//
//            int nIdx = 0;
            for(DeliveryItemInfo info: deliveryItemInfos) {
                addItem(info); // 정보 추가
            }
//            String htmlString = "" +
//                    "최근 포인트 사용정보(" +
//                    String.format("%d", nIdx) +
//                    "건)" +
//                    "";
//            CommonUtil.with().setHtmlMarqueeText(tvSummary01, htmlString);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 아이템 추가
     * @param info
     */
    private void addItem(DeliveryItemInfo info) {
        DeliveryItemLayout itemLayout1 = new DeliveryItemLayout(getApplicationContext(), info);
        llItemList.addView(itemLayout1);
    }

    /**
     * HTML 텍스트로 세팅(마퀴즈효과는 덤으로..)
     * @param tvTarget
     * @param htmlString
     */
    private void setHtmlMarqueeText(TextView tvTarget, String htmlString) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                tvTarget.setText(Html.fromHtml(htmlString));
            } else {
                tvTarget.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
            }
            // 글자가 흐르기위해서...
            tvTarget.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvTarget.setSingleLine(true);
            tvTarget.setMarqueeRepeatLimit(-1);
            tvTarget.setSelected(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 배경영역을 터치시.(View XML에서 기술함..)
     * @param view
     */
    public void hideQuickMenu(View view) {
//        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.AUTH_POP_YN.name(), "Y");
//        ((LoginActivity) LoginActivity.mContext).checkDeviceAuth();
        FinishWork();
    }

    /**
     * 종료작업 - 팝업 닫기
     */
    private void FinishWork() {

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
    ProgressDialog pDialog;
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
     * 배차상품정보 요청
     */
    public void requestDeliveryItemList() {

        try {

            showProgress(true);

            // 뷰지우기..
            llItemList.removeAllViews();
            //FavItemListManager.getInstance().clear();
            deliveryItemInfos.clear();

            Map<String, String> jsonMap = new HashMap<String, String>();
            // 운영용..
            jsonMap.put("@I_DDATE", mCurInfo.getDDATE());
            jsonMap.put("@I_WSD_NUM", mCurInfo.getWSD_NUM());
            jsonMap.put("@I_CLCODE", mCurInfo.getCLCODE());
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
     * SMS 문자전송하기
     */
    private void smsSend(String sRecvNum, String sMessage) {

        if (sMessage.length() == 0) {
            CommonUtil.with().ToastMsg(mContext, "전송할 메시지가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);

        Map<String, String> jsonMap = new HashMap<String, String>();

        jsonMap.put("@I_SEND_NUMBER", "");
        jsonMap.put("@I_RECV_NUMBER", sRecvNum);
        jsonMap.put("@I_MESSAGE", sMessage);
        jsonMap.put("@I_INPUT_USER", "");

        RequesterSession.with().RestAPIServiceCall(Svc.SMS_SEND, jsonMap, smsSendCallback);
    }

    /**
     * REST-API Callback - SMS 문자전송하기 콜백..
     */
    private Callback smsSendCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "smsSendCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "smsSendCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "smsSendCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "smsSendCallback, 수신데이타:" + strJsonOutput);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject jObject = jelement.getAsJsonObject();

                String ResultCode = jObject.get("ResultCode").getAsString();
                String ResultMsg = jObject.get("ResultMsg").getAsString();
                String RecordCount = jObject.get("RecordCount").getAsString();
                JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();

                if (!ResultCode.equals("00")) {
                    handler.obtainMessage(SMS_SEND, ResultMsg).sendToTarget();
                } else {
                    handler.obtainMessage(SMS_SEND, "정상처리되었습니다.").sendToTarget();
                }
            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }

        }

    };


}
