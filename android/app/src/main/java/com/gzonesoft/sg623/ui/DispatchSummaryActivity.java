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
import com.gzonesoft.sg623.Layout.DispatchSummaryItemLayout;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.DeliveryItemInfo;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.model.DeliveryListManager;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DispatchSummaryActivity extends AppCompatActivity {

    private ScrollView scrollItemList;
    private LinearLayout ll_main, llItemList; // 아이템 리스트
    private ImageView iv_back;
    private TextView tvRefresh;
    private Button btnSave;

    public static Context mContext = null;
    public String SELECT_DEVICE_NO = "";

    ProgressDialog pDialog;

    ArrayList<DeliveryItemInfo> deliveryItemInfos = new ArrayList<DeliveryItemInfo>();

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int CVO_DISPATCH_SUMMARYT_SEL = 10000;   // 배차일보 정보 가져오기
    public static final int PLAY_TTS = 30001;   // TTS 재생
    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case CVO_DISPATCH_SUMMARYT_SEL:

                    // 대기화면 보이기/감추기..
                    showProgress(false);

                    // 아이템 세팅
                    setDeliveryItemInfo();

                    break;
                case RETRY_PROCESS:

                    // 대기화면 닫기..
                    showProgress(false);

                    // 재처리 프로세스 호출
                    retryProcess(msg.obj.toString());

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

    /**
     * 재처리 프로세스
     *
     * @param jsonBodyString
     */
    void retryProcess(final String jsonBodyString) {
//        // 재처리여부 팝업다이얼로그 호출
//        UDialog.withTwoCustom_okCancel(mContext, Svc.APP_NAME, getString(R.string.msg_conn_retry), getString(R.string.title_confirm), getString(R.string.title_cancel), View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//            @Override
//            public void onClick_yes() {
//
//                showProgress(true);
//
//                // 사용자 로그인, 재처리 시도..
//                RequesterSession.with().RestAPIServiceCall(Svc.CHECK_LOGIN, jsonBodyString, requestLoginCallback);
//            }
//
//            @Override
//            public void onClick_no() {
//                CommonUtil.with().ToastMsg(mContext, getString(R.string.msg_cancel), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    public void CreatePdf(String title) {

        PdfGenerator.getBuilder()
                .setContext(DispatchSummaryActivity.this)
                .fromViewIDSource()
                .fromViewID(DispatchSummaryActivity.this, R.id.llItemList)
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 컨텍스트 저장
        mContext = this;

        Intent intent = getIntent();
        SELECT_DEVICE_NO = String.valueOf(intent.getStringExtra("SELECT_DEVICE_NO"));
        if (SELECT_DEVICE_NO.equals("null")) {
            SELECT_DEVICE_NO = "";
        }

        // 프로그래스 다이얼로그 초기화
        initProgressDlg();

        // 메인 UI 초기화
        initMainUI();

        // 데이타 요청
        requestCvoDispatchSummaryList();


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

            setContentView(R.layout.activity_dispatch_summary);
            // 배차일보
            ll_main = (LinearLayout) findViewById(R.id.ll_main); // 전체 레이아웃
            scrollItemList = (ScrollView) findViewById(R.id.scrollItemList);
            llItemList = (LinearLayout) findViewById(R.id.llItemList); // 아이템 리스트


            // 뒤로가기 버튼
            iv_back = findViewById(R.id.iv_back);
            iv_back.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // 새로고침 버튼
            tvRefresh = findViewById(R.id.tvRefresh);
            tvRefresh.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCvoDispatchSummaryList();
                }
            });

            // 하단버튼..
            btnSave = (Button) findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // PDF출력..
                    String title = search_date_sch + "-배차일보-" + AppSetting.cvoUserInfo.getNAME();
                    CreatePdf(title);
                }
            });


            curDeliveryDate = CommonUtil.with().nowYYYYMMDDHHMMSS();
            cal_sch.set(Calendar.YEAR , Integer.parseInt(curDeliveryDate.substring(0,4)));
            cal_sch.set(Calendar.MONTH , Integer.parseInt(curDeliveryDate.substring(4,6))-1);
            cal_sch.set(Calendar.DAY_OF_MONTH , Integer.parseInt(curDeliveryDate.substring(6,8)));
            searchDate = (TextView) findViewById(R.id.searchDate);
            setSearchDate(0); // 초기 오늘날짜..

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 날짜 더하기 빼기 클릭시..
     * @param view
     */
    public void moveSearchDate(View view) {

        int nTag = Integer.parseInt(view.getTag().toString());

        // 날짜 텍스트 변경처리
        setSearchDate(nTag);

        // 배차일보 조회요청..
        requestCvoDispatchSummaryList();

//        recyclerView.removeAllViews();
//
//        // 새로고침처리..
//        clickRefreshButton(null);
    }

    // 배차날짜검색 - 날짜선택
    public String curDeliveryDate;
    private TextView searchDate;
    public SimpleDateFormat dtFormat_msg = new SimpleDateFormat("MM/dd"); // 출력용
    public SimpleDateFormat dtFormat_sch = new SimpleDateFormat("yyyy-MM-dd"); // 검색용
    String search_date_msg; // 출력용
    String search_date_sch; // 날짜검색용
    public Calendar cal_sch = Calendar.getInstance();

    /**
     * 날짜 텍스트 변경처리
     * @param clacDay
     */
    public void setSearchDate(int clacDay){
        try {
            cal_sch.add(Calendar.DATE, clacDay);
            search_date_msg = dtFormat_msg.format(cal_sch.getTime()); // 출력용
            search_date_sch = dtFormat_sch.format(cal_sch.getTime()); // 검색용
            searchDate.setText(search_date_msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
     * 배송일지-아이템 세팅
     */
    private void setDeliveryItemInfo() {
        int nIdx = 0;
        for(DeliveryListInfo info: DeliveryListManager.getInstance().values()) {
            addDeliveryItem(info);
            nIdx++;
        }
    }

    /**
     * 배송일지-아이템 추가
     * @param info
     */
    private void addDeliveryItem(DeliveryListInfo info) {
        DispatchSummaryItemLayout itemLayout1 = new DispatchSummaryItemLayout(getApplicationContext(), info);
        llItemList.addView(itemLayout1);
    }



    /**
     * CVO - 배차일보 조회
     *
     * 0070	박준상	01046550048
     * 0061	송신호	01071881379
     * 0004	임수호	01055013694
     * 0066	홍성훈	01021355562
     *
     */
//    private int nCheckPoint = 0;
    public void requestCvoDispatchSummaryList() {
        try {
//            String selectDeviceNo = "";
//            switch(nCheckPoint%4) {
//                case 0: selectDeviceNo = "01046550048"; break;
//                case 1: selectDeviceNo = "01071881379"; break;
//                case 2: selectDeviceNo = "01055013694"; break;
//                case 3: selectDeviceNo = "01021355562"; break;
//            }
//            nCheckPoint ++;

            showProgress(true);

            llItemList.removeAllViews();

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_DDATE", search_date_sch);
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            if (SELECT_DEVICE_NO.length()>0) {
                paraMap.put("@I_DEVICENO", SELECT_DEVICE_NO);
            } else {
                paraMap.put("@I_DEVICENO", AppSetting.cvoUserInfo.getDEVICENO());
            }
            paraMap.put("@I_VEHICLENO", AppSetting.cvoUserInfo.getVEHICLENO());
            paraMap.put("@I_KEYWORD", "");
            paraMap.put("@I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("@I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_DISPATCH_SUMMARYT_SEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoDispatchSummaryListCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - CVO 배송리스트 조회
     */
    private Callback requestCvoDispatchSummaryListCallback = new Callback() {
        String tag = "requestCvoDispatchSummaryListCallback";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, tag + "---- onFailure:" + e.toString());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();

            showProgress(false);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();
                return;
            }

            try {
                Loggers.d2(this, tag + ", ---- onResponse");

                String strJsonOutput = response.body().string();

                Loggers.d2(this, tag + ",수신데이타:" + strJsonOutput);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject recvObj = jelement.getAsJsonObject();

                JsonArray jarrayShif = recvObj.getAsJsonArray("RecordSet");

                // 기존 배송정보 삭제
                DeliveryListManager.getInstance().clear();

                for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                    JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                    Gson gson = new Gson();
                    DeliveryListInfo info = gson.fromJson(jobject, DeliveryListInfo.class);
                    // 배송지 정보 추가
                    DeliveryListManager.getInstance().add(info);
                }
                handler.obtainMessage(CVO_DISPATCH_SUMMARYT_SEL, "").sendToTarget();
            } catch (Exception ex) {
                ex.printStackTrace();
                handler.obtainMessage(RequesterSession.REQ_OK, "서버요청에 실패하였습니다. 잠시뒤 재시도바랍니다.").sendToTarget();
            }
        }
    };
















    /**
     * 거래명세서 화면으로 이동...
     * @param info
     */
    public void requestDeliveryItemList(DeliveryListInfo info) {

        try {
            // 거래명세서 관리(전용화면)으로 이동...
            Intent intent = new Intent(mContext, InvoiceMngActivity.class);
            intent.putExtra("CUR_DELIVERY_LIST_INFO", info);
            startActivity(intent);
        }
        catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }
    }





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
