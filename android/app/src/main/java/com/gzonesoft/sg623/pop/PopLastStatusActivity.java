package com.gzonesoft.sg623.pop;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.gzonesoft.cookzzang.DriverDispatchListActivity;
import com.gzonesoft.sg623.DriverMainActivity;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PopLastStatusActivity extends AppCompatActivity {

    public static Context mContext = null;

    private Button btnDispatchYes, btnDispatchNo;
    private TextView tvDispatchYes, tvDispatchNo;


    public static final int DELI_FINISH_SAV = 10002;   // 운송완료 보고

    /**
     * 요청 처리 핸들러
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case RequesterSession.REQ_OK:
                    //showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;
                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    //showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;

                case DELI_FINISH_SAV:
                    // 배차내역조회 화면 열려있다면 .. 새로고침 호출..
//                    if (((DriverDispatchListActivity) DriverDispatchListActivity.mContext) != null) {
//                        // 최종작업상태 조회
//                        ((DriverDispatchListActivity) DriverDispatchListActivity.mContext).getLastWorkCall();
//                    }

                    break;
            }


            return true;
        }
    });

    public String  mLAST_REPORT_POP_YN;
    public String  mLAST_DISPATCH_ID;
    public String  mLAST_DELI_NO;
    public String  mLAST_ROUTE_SEQ;
    public String  mLAST_STOP_SEQ;
    public String  mLAST_DEST_CD;
    public String  mLAST_INVOICE_RESULT_URL;
    public String  mLAST_REASON_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        try {
            // 저장된 데이타 확인
            mLAST_REPORT_POP_YN = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_REPORT_POP_YN.toString(), "N");
            mLAST_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_DISPATCH_ID.name(), "");
            mLAST_DELI_NO = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_DELI_NO.name(), "");
            mLAST_ROUTE_SEQ = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_ROUTE_SEQ.name(), "");
            mLAST_STOP_SEQ = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_STOP_SEQ.name(), "");
            mLAST_DEST_CD = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_DEST_CD.name(), "");
            mLAST_INVOICE_RESULT_URL = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_INVOICE_RESULT_URL.name(), "");
            mLAST_REASON_TYPE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_REASON_TYPE.name(), "");

            Loggers.d("mLAST_REPORT_POP_YN=" + mLAST_REPORT_POP_YN);
            Loggers.d("mLAST_DISPATCH_ID=" + mLAST_DISPATCH_ID);
            Loggers.d("mLAST_DELI_NO=" + mLAST_DELI_NO);
            Loggers.d("mLAST_ROUTE_SEQ=" + mLAST_ROUTE_SEQ);
            Loggers.d("mLAST_STOP_SEQ=" + mLAST_STOP_SEQ);
            Loggers.d("mLAST_DEST_CD=" + mLAST_DEST_CD);
            Loggers.d("mLAST_INVOICE_RESULT_URL=" + mLAST_INVOICE_RESULT_URL);
            Loggers.d("mLAST_REASON_TYPE=" + mLAST_REASON_TYPE);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        initMainUI();


//        // 임시로 세팅...
//        // 최종보고상태 팝업여부 로컬저장(강제팝업 차단)
//        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.LAST_REPORT_POP_YN.name(), "N");
//        // 팝업종료..
//        finish();

    }


    /**
     * 메인UI 초기화..
     */
    private void initMainUI() {
        setContentView(R.layout.activity_pop_laststatus);
        getSupportActionBar().hide();

        btnDispatchYes = (Button) findViewById(R.id.btnDispatchYes);
        btnDispatchNo = (Button) findViewById(R.id.btnDispatchNo);
        tvDispatchYes = (TextView) findViewById(R.id.tvDispatchYes);
        tvDispatchNo = (TextView) findViewById(R.id.tvDispatchNo);


        String htmlString = "";

        htmlString = "" +
                "<big><big>출하지</big></big>로 이동<BR>" +
                "<small><small><small><small><small><small>&nbsp;</small></small></small></small></small></small><BR>" +
                "<small>- <font color=\"#C4C4C4\">다음배차가</font></small> <font color=\"#8CC456\">있음</font><small> -</small>" +
                "";
        CommonUtil.with().setHtmlText(tvDispatchYes, htmlString);

        htmlString = "" +
                "<big><big>차고지</big></big>로 이동<BR>" +
                "<small><small><small><small><small><small>&nbsp;</small></small></small></small></small></small><BR>" +
                "<small>- <font color=\"#C4C4C4\">다음배차가</font></small> <font color=\"#040204\">없음</font><small> -</small>" +
                "";
        CommonUtil.with().setHtmlText(tvDispatchNo, htmlString);

        // 버튼 클릭리스너 세팅...(출하지로이동)
        tvDispatchYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CVO보고..
                reportCVO("B"); // 출하지로 이동 - GPS 유지
                // 최종보고상태 팝업여부 로컬저장(강제팝업 차단)
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.LAST_REPORT_POP_YN.name(), "N");
                // 팝업종료..
                finish();
            }
        });

        // 버튼 클릭리스너 세팅...(차고지로이동)
        tvDispatchNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CVO보고..
                reportCVO("E"); // 차고지로 이동 - GPS OFF
//                // 퇴근보고..
//                if (((DriverDispatchListActivity) DriverDispatchListActivity.mContext) != null) {
//                    ((DriverDispatchListActivity) DriverDispatchListActivity.mContext).leaveTheOffice();
//                }
                // 최종보고상태 팝업여부 로컬저장(강제팝업 차단)
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.LAST_REPORT_POP_YN.name(), "N");
                // 팝업종료..
                finish();
            }
        });

    }

    /**
     * CVO 상태 보고
     *     S:시작(Start)
     *     L:출하(Load)
     *     T:수송(Transport)
     *     U:하화(Unload)
     *     B:회차(Backhaul)
     *     E:공차(Empty)
     *     R:휴차(Relax)
     * @param status
     */
    private void reportCVO(String status) {
        try {
            // CVO 업무보고로직 추가..
            if (((DriverMainActivity) DriverMainActivity.mContext) != null) {
//                String dispatchId = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_DISPATCH_ID.toString(), "");
//                String destCd = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_DEST_CD.toString(), "");
//                // CVO 업무보고 호출..
//                ((DriverMainActivity) DriverMainActivity.mContext).reportCvoEvent(
//                        status,
//                        dispatchId,
//                        "",
//                        destCd
//                );
//                // TMS 완료보고 호출..
//                deliFinishSav();
            }
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
     * Back뒤로가기 버튼 처리..
     */
    @Override
    public void onBackPressed() {
        // 뒤로가기 차단
        if (false) {
            super.onBackPressed();
        }
    }




    /**
     * 완료보고 API 요청
     */
    public void deliFinishSav(){
        try {

//            Map<String, String> paraMap = new LinkedHashMap<String, String>();
//
//            paraMap.put("I_DISPATCH_NO", mLAST_DISPATCH_ID);
//            paraMap.put("I_DELI_NO", mLAST_DELI_NO);
//            paraMap.put("I_ROUTE_SEQ", mLAST_ROUTE_SEQ);
//            paraMap.put("I_STOP_SEQ", mLAST_STOP_SEQ);
//            paraMap.put("I_DRV_CD", AppSetting.cvoUserInfo.getDRV_CD());
//            paraMap.put("I_VEHICLE_CD", AppSetting.cvoUserInfo.getVHCL_PCL_CD());
//            paraMap.put("I_MIN_NO", AppSetting.Device.PHONE_NUMBER);
//            paraMap.put("I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
//            paraMap.put("I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));
//            paraMap.put("I_DEST_CD", mLAST_DEST_CD);
//            paraMap.put("I_INVOICE_RESULT_URL", mLAST_INVOICE_RESULT_URL);
//            paraMap.put("I_REASON_TYPE", mLAST_REASON_TYPE);
//
//            RequesterSession.with().RestAPIServiceCall(Svc.DELI_FINISH_SAV, paraMap, deliFinishSavCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * REST-API Callback - 도착보고
     */
    private Callback deliFinishSavCallback = new Callback() {

        String mTag = "deliFinishSavCallback()";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, "---------------------------------------------");
            Loggers.d2(this, mTag + " ---- onFailure() : " + e.getMessage());
            Loggers.d2(this, mTag + " ---- onFailure() : " + e.toString());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Loggers.d2(this, mTag + ", ---- body : " + bodyString);
            Loggers.d2(this, "---------------------------------------------");

            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, mTag + " ---- onResponse : " + response.code());

            if (response.code() == 200) {

                try {
                    String strJsonOutput = response.body().string();

                    Loggers.d2(this, mTag + " ---- 수신데이타 : " + strJsonOutput);

                    JsonElement jelement = new JsonParser().parse(strJsonOutput);
                    JsonObject jObject = jelement.getAsJsonObject();

                    String resultCode = jObject.get("ResultCode").getAsString();
                    String resultMsg = jObject.get("ResultMsg").getAsString();

                    //JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();


                    if (!resultCode.equals("00")) {
                        // 응답은 정상이지만 서비스 결과 오류

                    } else {

                        handler.obtainMessage(DELI_FINISH_SAV, getString(R.string.complete)).sendToTarget();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Loggers.d("Exception : " + e.getMessage());
                }

            } else if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;

            } else {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[" + response.code() + "]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }
        }
    };



}