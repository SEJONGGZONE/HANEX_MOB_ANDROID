package com.gzonesoft.sg623.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.MobileAuth;
import com.gzonesoft.sg623.domain.BroadCastDomain;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;
import com.gzonesoft.sg623.util.Svc;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PhoneAuthActivity extends AppCompatActivity {

    public static Context mContext = null;

    private static MobileAuth mobileAuth;

    // UI 매핑 변수
    ProgressDialog pDialog;
    private Button btnRequestAuth, btnSave;
    private AutoCompleteTextView tvPhoneNo, tvAuthNo;
    private String mPhoneNo;

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스
    public static final int FINISH_WORK = 29999;

    public static final int REQ_PHONEAUTH = 10001;
    public static final int REQ_PHONEAUTH_FAIL = 10002;
    public static final int REQ_PHONEAUTH_FINISH = 10003;
    public static final int REQ_PHONEAUTH_FINISH_FAIL = 10004;




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case FINISH_WORK: // 화면을 종료한다..
                    // 대기화면 닫기..
                    showProgress(false);

                    // 키보드 내리기..
                    hideKeyboard();

                    // 화면닫기..
                    finish();
                    overridePendingTransition(R.anim.no_change, R.anim.no_change);

                    break;

                case REQ_PHONEAUTH:
                    // 대기화면 닫기..
                    showProgress(false);
                    if (mobileAuth.getAUTH_NO().length()>0) {
                        tvAuthNo.requestFocus();
                    }
                    break;

                case REQ_PHONEAUTH_FAIL:
                    // 대기화면 닫기..
                    showProgress(false);

                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;

                case REQ_PHONEAUTH_FINISH:
                    // 대기화면 닫기..
                    showProgress(false);

                    CommonUtil.with().ToastMsg(mContext, "휴대폰 인증이 완료되었습니다.", Toast.LENGTH_LONG).show();

                    // 앱재구동...
                    restartApp(mContext);
//                    // 메인이동
//                    Intent intent = new Intent(getApplicationContext(), DriverMainActivity.class);
//                    intent.putExtra("CALL_TYPE", CommType._휴대번호인증후실행);
//                    startActivity(intent);

                    // 앱재구동..

                    break;

                case REQ_PHONEAUTH_FINISH_FAIL:
                    // 대기화면 닫기..
                    showProgress(false);

                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

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
     * 앱 재구동
     * @param context
     */
    public void restartApp(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
            System.exit(0);
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
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                if (show) {
                    pDialog.show();
                    //                mRASpecialOrderList.setVisibility(View.GONE);
                }
                else {
                    pDialog.dismiss();
                    //                mRASpecialOrderList.setVisibility(View.VISIBLE);
                }
            } else {
                if (show) {
                    pDialog.show();
                    //                mRASpecialOrderList.setVisibility(View.GONE);
                }
                else {
                    pDialog.dismiss();
                    //                mRASpecialOrderList.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        mobileAuth = new MobileAuth();

        initUI();

        // 프로그래스 다이얼로그 초기화
        initProgressDlg();

        // 기존 인증정보 초기화
        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.AUTH_PHONE_NO.name(), "");

        // 포커스 주기
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 자동입력
                tvPhoneNo.setText(mPhoneNo);
                // 전화번호를 못가져왔을경우에 포커스...
                if (mPhoneNo.length()<=0) {
                    // 커서 포커스
                    tvPhoneNo.requestFocus();
                    // 키보드 보이기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 1000);

        // 휴대폰인증 리시버 등록
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BroadCastDomain.BROADCAST_PHONEAUTH);
//        registerReceiver(authReceiver, filter);
    }

    /**
     * UI 초기화
     */
    private void initUI() {
        setContentView(R.layout.activity_phone_auth);

        try {
            // 화면을 portrait(세로) 화면으로 고정
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // 상태바 색상 지정(검은색으로)
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }

        tvPhoneNo = (AutoCompleteTextView) findViewById(R.id.tvPhoneNo);
        // 입력체크-전화번호
        tvPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//                Loggers.d("입력값은 = " + s.toString());
                checkNext();    // 다음단계 진행여부 체크...
            }
        });
        // 전화번호 자동입력
        try {
            mPhoneNo = String.format("%s", CommonUtil.with().getLine1Number(mContext));

        } catch (Exception ex) {
            CommonUtil.with().ToastMsg(mContext, "전화번호를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        tvAuthNo = (AutoCompleteTextView) findViewById(R.id.tvAuthNo);
        // 입력체크-전화번호
        tvAuthNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//                Loggers.d("입력값은 = " + s.toString());
                checkNext();    // 다음단계 진행여부 체크...
            }
        });

        // 버튼세팅
        btnRequestAuth = (Button) findViewById(R.id.btnRequestAuth);
        btnRequestAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = String.format("%s", tvPhoneNo.getText().toString());
                if (phoneNo.length()>0) {
                    // 휴대폰인증 요청..
                    requestPhoneAuth();
                } else {
                    CommonUtil.with().ToastMsg(mContext, "전화번호를 입력해주시 바랍니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setBackgroundResource(R.drawable.selector_style_11_off);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authNo = String.format("%s", tvAuthNo.getText().toString());
                if (authNo.length()>0) {
                    if (tvPhoneNo.getText().toString().equals("01088889999")) {
                        // 구글 등록용 테스트계정
                        // 휴대전화번호 로컬저장..
                        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.AUTH_PHONE_NO.name(), "01088889999");
                        handler.obtainMessage(REQ_PHONEAUTH_FINISH, "").sendToTarget();
                    } else {
                        // 인증번호 체크
                        if (checkAuth(authNo)) {
                            // 휴대전화번호 로컬저장..
                            SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.AUTH_PHONE_NO.name(), tvPhoneNo.getText().toString());
                            handler.obtainMessage(REQ_PHONEAUTH_FINISH, "").sendToTarget();
                        }
                    }
                } else {
                    CommonUtil.with().ToastMsg(mContext, "인증번호를 입력해주시 바랍니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * 인증번호 체크
     * @param authNo
     * @return
     */
    private boolean checkAuth(String authNo) {
        boolean bReturn = false;
        try {
            if (authNo.equals(mobileAuth.getAUTH_NO())) {
                bReturn = true;
            } else {
                bReturn = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            bReturn = false;
        } finally {
            return  bReturn;
        }
    }


    /**
     * 다음진행여부 체크
     */
    private boolean bNext = false;
    private void checkNext() {
//        String carTypeCd = CodeListManager.getInstance().getCodeCd(CodeType._차종, txtCarType.getText().toString());
//        String carTonCd = CodeListManager.getInstance().getCodeCd(CodeType._톤수, txtCarTon.getText().toString());
        if (
                (tvPhoneNo.length()>0) && (tvAuthNo.length()>0)
        ) {
            btnSave.setBackgroundResource(R.drawable.selector_style_11);
            bNext = true;
        } else {

            btnSave.setBackgroundResource(R.drawable.selector_style_11_off);
            bNext = false;
        }
    }


    private long slideLeftDelayTime = 100;
    private void slideLeft(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_slow);
                targetView.startAnimation(anim);
            }
        }, slideLeftDelayTime);
        slideLeftDelayTime = slideLeftDelayTime + 100;
    }

    private long slideRightDelayTime = 100;
    private void slideRight(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_slow);
                targetView.startAnimation(anim);
            }
        }, slideRightDelayTime);
        slideRightDelayTime = slideRightDelayTime + 100;
    }

    private long slideUpDelayTime = 1000;
    private void slideUp(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_slow);
                targetView.startAnimation(anim);
            }
        }, slideUpDelayTime);
        slideUpDelayTime = slideUpDelayTime + 100;
    }

    /**
     * 키보드 내리기..
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvPhoneNo.getWindowToken(), 0);
    }


    /**
     * 휴대폰인증번호 요청
     */
    public void requestPhoneAuth() {
        try {

            showProgress(true);

            Map<String, String> jsonMap = new HashMap<String, String>();

            jsonMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            jsonMap.put("@I_RECV_NUMBER", tvPhoneNo.getText().toString());
            jsonMap.put("@I_INPUT_USER", "");

            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.REQ_PHONEAUTH),
                    "POST",
                    "",
                    jsonMap,
                    requestPhoneAuthCallback,
                    keyMap,
                    false
            );



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * REST-API Callback - 휴대폰인증번호 요청 콜백..
     */
    private Callback requestPhoneAuthCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "requestPhoneAuthCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "requestPhoneAuthCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "requestPhoneAuthCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestPhoneAuthCallback, 수신데이타:" + strJsonOutput);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject jObject = jelement.getAsJsonObject();

                String ResultCode = jObject.get("ResultCode").getAsString();
                String ResultMsg = jObject.get("ResultMsg").getAsString();
                String RecordCount = jObject.get("RecordCount").getAsString();
                JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();

                if (!ResultCode.equals("00")) {
                    handler.obtainMessage(REQ_PHONEAUTH_FAIL, ResultMsg).sendToTarget();
                } else {
                    if (jarrayRecordSet.size() == 0) {
                        handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "requestPhoneAuth," + getString(R.string.api_has_nodata)).sendToTarget();
                    } else {

                        JsonObject jobjectStore = jarrayRecordSet.get(0).getAsJsonObject();
                        Gson gson = new Gson();

                        mobileAuth = new MobileAuth();
                        mobileAuth = gson.fromJson(jobjectStore, MobileAuth.class);

                        handler.obtainMessage(REQ_PHONEAUTH, getString(R.string.promote_add_save_success)).sendToTarget();

                    }
                }
            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }

        }

    };

//    /**
//     * 인증완료 처리(추천관계정보 업데이트)
//     */
//    public void finishPhoneAuth() {
//        try {
//
//            showProgress(true);
//
//            Map<String, String> jsonMap = new HashMap<String, String>();
//
//            jsonMap.put("@I_RECV_NUMBER", tvPhoneNo.getText().toString());
//            jsonMap.put("@I_AUTH_NO", tvAuthNo.getText().toString());
//            jsonMap.put("@I_INPUT_USER", "");
//
//            RequesterSession.with().RestAPIServiceCall(Svc.REQ_PHONEAUTH_FINISH, jsonMap, finishPhoneAuthCallback);
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    /**
//     * REST-API Callback - 인증완료 처리(추천관계정보 업데이트) 콜백..
//     */
//    private Callback finishPhoneAuthCallback = new Callback() {
//
//        @Override
//        public void onFailure(Call call, IOException e) {
//
//            Loggers.d2(this, "finishPhoneAuthCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
//            Loggers.d2(this, "finishPhoneAuthCallback, ---- onFailure:" + e.toString());
////            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();
//
//            String bodyString = RequesterSession.with().bodyToString(call.request());
//            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Loggers.d2(this, "finishPhoneAuthCallback, ---- onResponse, " + response.code());
//            if (response.code() == 404) {
//                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
//                return;
//            }
//
//            try {
//                String strJsonOutput = response.body().string();
//
//                Loggers.d2(this, "finishPhoneAuthCallback, 수신데이타:" + strJsonOutput);
//
//                JsonElement jelement = new JsonParser().parse(strJsonOutput);
//                JsonObject jObject = jelement.getAsJsonObject();
//
//                String ResultCode = jObject.get("ResultCode").getAsString();
//                String ResultMsg = jObject.get("ResultMsg").getAsString();
//                String RecordCount = jObject.get("RecordCount").getAsString();
//                JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();
//
//                if (!ResultCode.equals("00")) {
//                    handler.obtainMessage(REQ_PHONEAUTH_FAIL, ResultMsg).sendToTarget();
//                } else {
//                    handler.obtainMessage(REQ_PHONEAUTH_FINISH, getString(R.string.promote_add_save_success)).sendToTarget();
//                }
//            } catch (Exception e) {
//                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
//                e.printStackTrace();
//            }
//
//        }
//
//    };


    /**
     * 브로드 캐스트 리시버(웹용)
     */
    BroadcastReceiver authReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // SMS 핸들링
            String smsMessage = intent.getStringExtra(BroadCastDomain.RECV_SMS);
            if (smsMessage != null) {
                Loggers.d("==== [BroadcastReceiver.RECV_SMS] " + smsMessage);
                try {
                    // 목원푸드APP - 인증번호는 [131683] 입니다.
                    String authNo = smsMessage.substring(smsMessage.length()-12, smsMessage.length()-6);
                    // 인증번호 자동입력
                    tvAuthNo.setText(authNo);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    };


}