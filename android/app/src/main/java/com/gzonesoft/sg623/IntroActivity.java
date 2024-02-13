package com.gzonesoft.sg623;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.AppChkInfo;
import com.gzonesoft.sg623.data.CvoUserInfo;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.pop.SlidePopAuthInfoActivity;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.ui.PhoneAuthActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.DeviceUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.PermissionSupport;
import com.gzonesoft.sg623.util.SettingPref;
import com.gzonesoft.sg623.util.Svc;
import com.gzonesoft.sg623.util.UDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IntroActivity extends AppCompatActivity {

    private String TAG = "#IntroActivity";
    public static Context mContext = null;

    public static final int ERP_APPCHECK = 1000;
    public static final int CVO_USERINFO_CHK = 1001;
    public static final int REQ_PERM_ACCESS_BACKGROUND_LOCATION = 2002;

    public static final int RETRY_START = 29999;   // 재시작 프로세스 ( 업데이트 지연 이슈 )
    public static final int UPDATE_PERCENT = 9999;   // 다운로드 상태..

    //private AppChkInfo appChkInfo;
    private PermissionSupport permissionSupport;

    String mPackageName;

    private View mWaitProgressView;
    private TextView versionInfo, tvLoading;

    /**
     * 권한안내 팝업 슬라이드
     */
    private void popAuthInfo() {
        Intent intent = new Intent(mContext, SlidePopAuthInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change); // 위로 슬라이드..
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        try {
            // 대기뷰 매핑
            mWaitProgressView = findViewById(R.id.wait_progress);
            // 버전정보 매핑
            versionInfo = (TextView) findViewById(R.id.id_version_info);
            versionInfo.setText(checkVersion());    // 앱 버전정보 확인..

            mContext = this;

            //test();

            mPackageName = this.getPackageName();

            // 권한체크..
            String AUTH_POP_YN = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.AUTH_POP_YN.toString(), "N");
            if (AUTH_POP_YN.equals("Y")) {

                Log.d(TAG, "Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
                Log.d(TAG, "Build.VERSION_CODES.M : " + Build.VERSION_CODES.M);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionCheck();
                }else{
                    // 모든 권한체크(허용) 완료후 처리사항 => 앱시작 지점..
                    init();
                }

//                // 권한안내 팝업 호출(개발용)
//                popAuthInfo();

                checkBatteryOptiPermission();
            } else {
                // 권한안내 팝업 호출
                popAuthInfo();
            }

            bFirst = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 배터리 최적화 예외 앱 설정 확인(Dose모드 방지)
     */
    private boolean bFirst = true;  // 처음실행여부 확인
    private void checkBatteryOptiPermission() {

// 아이사랑은 적용하지않는다. - 2023.09.30
//        Loggers.d2(this, "[Build.VERSION.SDK_INT]" + Build.VERSION.SDK_INT);
//        Loggers.d2(this, "[Build.VERSION_CODES.M]" + Build.VERSION_CODES.M);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            String packageName = getPackageName();
//            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//
//                if (bFirst) {   // 처음이면 확인창을 띄워준다
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                    intent.setData(Uri.parse("package:" + packageName));
//                    startActivity(intent);
//                    bFirst = false;  // 처음실행여부 확인
//                } else { // 처음이 아니면 앱을 종료시킨다. (2017-10-27 신웅조 과장님 협의)
//                    // 종료작업 호출
//                    FinishWork();
//                }
//            }
//        }
    }

    public void init(){
        Log.d(TAG, "init() Start");

        // 통합테스트용 코드
        //setTruckInfo();

        String StoragePath = Environment.getExternalStorageDirectory() + Svc.CAMERA_IMG_PATH;
        //String StoragePath = Environment.getExternalStorageDirectory() + Svc.SIGN_IMG_PATH;
        CommonUtil.with().removeDir(StoragePath);

        //removeDir();

        // 앱구동 시작
        launchApp();

    }


    /**
     * 앱구동 시작
     */
    private void launchApp() {
        // 디바이스 정보 수집
        chkDeviceInfo();

        // 설치앱 빌드버전 확인
        AppSetting.App.VERSION_CODE = getBuildVersionCode();
        AppSetting.App.VERSION_NAME = getBuildVersionName();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // 모든 파일 액세스 권한확인
//            lastAuthCheck();
//        } else {
            // 앱 업데이트 체크
            appUpdateCheck();
//        }
    }

    /**
     * 모든 파일 액세스 권한확인하여 미설정시 설정화면을 띄워주고 앱을 강제종료시킨다. - 자동업데이트로직에 필수 권한(구글 정책으로 앱에서 설정할수가 없다.)
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void lastAuthCheck() {
        Boolean bRet = Environment.isExternalStorageManager();
        if (bRet) {
            Log.d(TAG,"OK");
        } else {
            Log.d(TAG,"NO");

            String sHtml = "<BR>" +
                    "원활한 업데이트진행을 위하여 <BR>" +
                    "아래설정 확인이 필요합니다.<BR>" +
                    "<BR>" +
                    "'모든파일관리 허용'<BR>" +
                    "또는<BR>" +
                    "'" + Svc.APP_NAME + "'에 체크<BR><BR>" +
                    "부탁드립니다. 감사합니다.";
            // 팝업다이얼로그 호출
            UDialog.withHtmlTwoCustom_okCancel(mContext, Svc.APP_NAME, sHtml, "설정으로 이동 >>", "취소", View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                @Override
                public void onClick_yes() {
                    try {
                        Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                        startActivity(intent);
                    } catch (Exception ex){
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    }
                    finish();
                }

                @Override
                public void onClick_no() {
                    finish();
                }
            });

            return;
        }

        // 앱 업데이트 체크
        appUpdateCheck();
    }


    /**
     * 디바이스 정보 수집
     */
    public void chkDeviceInfo(){

        String READ_PHONE_PERMISSION = CommonUtil.with().readPhonePermission();

        Log.d(TAG,"권한 READ_PHONE_PERMISSION : " + READ_PHONE_PERMISSION);
        Log.d(TAG,"권한 ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_PERMISSION) : " + ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_PERMISSION));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            // 폰번호
            AppSetting.Device.PHONE_NUMBER = DeviceUtil.with().getDeviceInfo(mContext, "PHONE_NO"); // 폰번호(실번호)
        }

        // DEVICE ID
        AppSetting.Device.DEVICE_ID = DeviceUtil.with().getDeviceInfo(mContext,"DEVICE_ID");

        // MAC ADDRESS
        AppSetting.Device.MAC_ADDRESS = DeviceUtil.with().getHardwareAddress();

        // 충전여부
        String isCharging = DeviceUtil.with().getBatteryStatus(mContext);
        Log.d(TAG, "isCharging : " + isCharging);

        // 충전방법
        String howCharge = DeviceUtil.with().getBatteryChargeType(mContext);
        Log.d(TAG, "howCharge : " + howCharge);

        // 배터리 잔량
        int batteryPercent = DeviceUtil.with().getBatteryPercent(mContext);
        Log.d(TAG, "batteryPercent : " + batteryPercent);

    }


    /**
     * 메인화면 이동
     */
    public void goMain(){
        try {
            Log.d(TAG, " ---- 메인화면 이동-- 1");
            String AUTH_PHONE_NO = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.AUTH_PHONE_NO.toString(), "");
            Log.d(TAG, " ---- 메인화면 이동-- 2");
            if (AUTH_PHONE_NO.length()<=0) {
                // 휴대폰인증 여부확인하기
                Intent intent = new Intent(getApplicationContext(), PhoneAuthActivity.class);
                startActivity(intent);
            } else {

                Log.d(TAG,"인증된 휴대전화번호로 사용자 정보가져오기");
                requestCvoUserInfo(AUTH_PHONE_NO);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        Log.d(TAG,"keyCode : " + keyCode);
        Log.d(TAG,"event.getKeyCode() : " + event.getKeyCode());
        Log.d(TAG,"event.getAction() : " + event.getAction());

        if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN ) {

            finish();
            android.os.Process.killProcess(android.os.Process.myPid());

            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("프로그램을 종료 하시겠습니까?");
            builder.setCancelable(true);
            builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.d(TAG, "setPositiveButton");
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.d(TAG, "setNegativeButton");
                    dialog.dismiss();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.d(TAG, "setOnCancelListener");
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                @Override
                public void onDismiss(DialogInterface dialog){
                    Log.d(TAG, "setOnDismissListener");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            */
        }

        return super.onKeyDown(keyCode, event);

    }


    /**
     * 현재 앱 빌드버전(버전코드) 가져오기
     */
    public int getBuildVersionCode(){
        int version = 1;
        try {
            final PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ){
                version = (int) packageInfo.getLongVersionCode();
            }else{
                version = (int) packageInfo.versionCode;
            }
        }catch(Exception e){
            Log.d(TAG,"Exception : " + e.getMessage());
        }
        return version;
    }


    /**
     * 현재 앱 빌드버전(버전명) 가져오기
     */
    public String getBuildVersionName(){
        String version = "1";
        try {
            final PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        }catch(Exception e){
            Log.d(TAG,"Exception : " + e.getMessage());
        }
        return version;
    }




    /**
     * 앱 업데이트 버전체크
     */
    public void appUpdateCheck(){

        Map<String, String> paraMap = new LinkedHashMap<String, String>();
        paraMap.put("I_APP_TYPE", "10");
        paraMap.put("I_OS_TYPE", "10");
//        CommonUtil.with().ToastMsg(this, "[ERP_APPCHECK-호출전..]", Toast.LENGTH_SHORT).show();
        RequesterSession.with().RestAPIServiceCall(Svc.ERP_APPCHECK, paraMap, appUpdateCheckCallback);
//        CommonUtil.with().ToastMsg(this, "[ERP_APPCHECK-호출후..]", Toast.LENGTH_SHORT).show();
    }

    /**
     * REST-API Callback - 앱버전 체크
     */
    private Callback appUpdateCheckCallback = new Callback() {

        String mTag = "appUpdateCheckCallback()";

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "---------------------------------------------");
            Log.d(TAG, mTag + " ---- onFailure() : " + e.getMessage());
            Log.d(TAG, mTag + " ---- onFailure() : " + e.toString());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Log.d(TAG, mTag + ", ---- body : " + bodyString);
            Log.d(TAG, "---------------------------------------------");

            handler.obtainMessage(RequesterSession.REQ_FAIL, getString(R.string.msg_conn_failure)).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, mTag + " ---- onResponse : " + response.code());

            if( response.code() == 200 ){

                try {
                    String strJsonOutput = response.body().string();

                    Log.d(TAG, mTag + " ---- 수신데이타 : " + strJsonOutput);

                    JsonElement jelement = new JsonParser().parse(strJsonOutput);
                    JsonObject jObject = jelement.getAsJsonObject();

                    String resultCode = jObject.get("ResultCode").getAsString();
                    String resultMsg = jObject.get("ResultMsg").getAsString();

                    JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();


                    if (!resultCode.equals("00")) {
                        // 응답은 정상이지만 서비스 결과 오류

                    } else {

                        if (jarrayRecordSet.size() == 0) {
                            // 응답은 정상 - 서비스 결과 없음

                        } else {

                            //Log.d(TAG, mTag + " ---- 데이타 매핑 : " + strJsonOutput);

                            // 데이타 초기화
//                            for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
//                                JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
//                                Gson gson = new Gson();
//                                AppSetting.appChkInfo = gson.fromJson(jobjectStore, AppChkInfo.class);
//                            }
                            Log.d(TAG, mTag + " ---- 데이타 매핑 : 1");
                            JsonObject jobjectStore = jarrayRecordSet.get(0).getAsJsonObject();
                            Log.d(TAG, mTag + " ---- 데이타 매핑 : 2");
                            Gson gson = new Gson();
                            Log.d(TAG, mTag + " ---- 데이타 매핑 : 3");
                            AppSetting.appChkInfo = gson.fromJson(jobjectStore, AppChkInfo.class);
                            Log.d(TAG, mTag + " ---- 데이타 매핑 : 4");

                            Log.d(TAG,"appChkInfo.toString() : " + AppSetting.appChkInfo.toString());

                            handler.obtainMessage(ERP_APPCHECK, getString(R.string.login_complete)).sendToTarget();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG,"Exception : " + e.getMessage());
                }

            }else if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }else{
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[" + response.code() + "]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }
        }
    };

    /**
     * 인증된 휴대전화번호로 사용자 정보가져오기
     * @param phoneNo
     */
    private void requestCvoUserInfo(String phoneNo) {
        try {
            Log.d(TAG, "제조사 : [ Build.MANUFACTURER ] >>> " + Build.MANUFACTURER);
            Log.d(TAG, "브랜드 : [ Build.MANUFACTURER ] >>> " + Build.BRAND);
            Log.d(TAG, "모델명 : [ Build.MANUFACTURER ] >>> " + Build.MODEL);
            Log.d(TAG, "OS 버전 : [ Build.MANUFACTURER ] >>> " + Build.VERSION.RELEASE);
            Log.d(TAG, "SDK 버전 : [ Build.MANUFACTURER ] >>> " + Build.VERSION.SDK_INT);

            String deviceInfo;
            try {
                deviceInfo= "" +
                        Build.MANUFACTURER + "/" +
                        Build.BRAND + "/" +
                        Build.MODEL + "/" +
                        Build.VERSION.RELEASE + "/" +
                        Build.VERSION.SDK_INT + "/";
            } catch (Exception e) {
                e.printStackTrace();
                deviceInfo= "00/00/00/00/00/";
            }
            deviceInfo += checkVersion() + "";

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_PHONENO", phoneNo);
            paraMap.put("@I_DEVICE_INFO", deviceInfo);

            //paraMap.put("I_PHONENO", "01049419950"); // 신동학
            //paraMap.put("I_PHONENO", "01053470466"); // 신민호

            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_USERINFO_CHK),
                    "POST",
                    "",
                    paraMap,
                    requestCvoUserInfoCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            Log.d(TAG,"오류 - 인증된 휴대전화번호로 사용자 정보가져오기");
            ex.printStackTrace();
        }
    }
    private Callback requestCvoUserInfoCallback = new Callback() {

        String mTag = "requestCvoUserInfoCallback()";

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "---------------------------------------------");
            Log.d(TAG, mTag + " ---- onFailure() : " + e.getMessage());
            Log.d(TAG, mTag + " ---- onFailure() : " + e.toString());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Log.d(TAG, mTag + ", ---- body : " + bodyString);
            Log.d(TAG, "---------------------------------------------");

            handler.obtainMessage(RequesterSession.REQ_FAIL, getString(R.string.msg_conn_failure)).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, mTag + " ---- onResponse : " + response.code());

            if( response.code() == 200 ){

                try {
                    String strJsonOutput = response.body().string();

                    JsonElement jelement = new JsonParser().parse(strJsonOutput);
                    JsonObject jObject = jelement.getAsJsonObject();

                    String resultCode = jObject.get("ResultCode").getAsString();
                    String resultMsg = jObject.get("ResultMsg").getAsString();

                    JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();


                    if (!resultCode.equals("00")) {
                        // 응답은 정상이지만 서비스 결과 오류
                        handler.obtainMessage(RequesterSession.REQ_FAIL, "서버 점검중입니다. 잠시뒤 재사용바랍니다.").sendToTarget();

                        // 앱종료처리..
                        finishAffinity();
                        System.runFinalization();
                        System.exit(0);
                    } else {

                        if (jarrayRecordSet.size() == 0) {
                            // 응답은 정상 - 서비스 결과 없음
                            handler.obtainMessage(RequesterSession.REQ_FAIL, "로그인이 차단되었습니다.\n관리자에게 문의바랍니다.").sendToTarget();

                            // 앱종료처리..
                            finishAffinity();
                            System.runFinalization();
                            System.exit(0);
                        } else {

                            // 데이타 세팅..
                            JsonObject jobjectStore = jarrayRecordSet.get(0).getAsJsonObject();
                            Gson gson = new Gson();
                            AppSetting.cvoUserInfo = gson.fromJson(jobjectStore, CvoUserInfo.class);

                            handler.obtainMessage(CVO_USERINFO_CHK, getString(R.string.login_complete)).sendToTarget();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG,"Exception : " + e.getMessage());
                }

            }else if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }else{
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[" + response.code() + "]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }
        }
    };


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CVO_USERINFO_CHK:

                    // 메인이동
                    Intent intent = new Intent(getApplicationContext(), CvoMainActivity.class);
                    intent.putExtra("CALL_TYPE", CommType._인트로진입후실행);
                    startActivity(intent);

                    break;
                case UPDATE_PERCENT:
                    versionInfo.setTextColor(Color.parseColor("#000000"));
                    versionInfo.setText(
                            String.format(msg.obj.toString())
                    );
                    break;

                case ERP_APPCHECK:
                    String remoteVersion = AppSetting.appChkInfo.getLAST_VERSION();
                    String localVersion = checkVersion();
                    String sTemp = remoteVersion.replace(".", "");
                    int nRemoteVersion = Integer.parseInt(sTemp);
                    int nLocalVersion = Integer.parseInt(localVersion.replace(".", ""));

                    // 로컬버전이 업데이트버전보다 작을경우 업데이트..
                    if (nRemoteVersion > nLocalVersion) {
                        // 확인 팝업다이얼로그 호출
                        String title = Svc.APP_NAME;
                        String htmlString = "" +
                                "<BR><big>최신버전으로 업데이트가 필요합니다.<BR><BR>확인 버튼을 누르시면 구글플레이 스토어로 이동됩니다.</big>";
                        UDialog.withHtmlTwoCustom_okCancel(mContext, title, htmlString, "<big>확 인</big>", "닫 기", View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                            @Override
                            public void onClick_yes() {
                                try {
                                    // 구글플레이스토어 연결..
                                    String packageName = mContext.getPackageName();
                                    String url = "market://details?id=" + packageName;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onClick_no() {

                            }
                        });
                    } else {
                        Log.d(TAG, " ---- 콜백-- 5-2");
                        goMain();
                    }

                    break;

                case RequesterSession.REQ_FAIL:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case RETRY_START:
                    retryStart(); // 재시작 프로세스
                    break;
            }

            return true;
        }
    });


    /**
     * 재시작 프로세스
     *
     *
     */
    void retryStart() {
        // 재처리여부 팝업다이얼로그 호출
        UDialog.withTwoCustom_okCancel(mContext, Svc.APP_NAME, "통신상태가 원활하지 않습니다. 앱을 재실행 해주세요.", "확인", "취소", View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
            @Override
            public void onClick_yes() {

                showProgress(true);

                // 사용자 로그인, 재처리 시도..
                finish();
            }

            @Override
            public void onClick_no() {
//                CommonUtil.with().ToastMsg(mContext, getString(R.string.msg_cancel), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mWaitProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mWaitProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mWaitProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mWaitProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                //            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * App 버전정보 체그
     */
    private String checkVersion() {
        String returnVersion = "";
        try {
            String packageName = this.getPackageName();
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(packageName, 0);
//            String thisVersion = packageInfo.versionName;
            returnVersion = String.valueOf(packageInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return returnVersion;
        }
    }

    /**
     * 권한 체크(10.11 이전권한들.. 주석처리.. )
     */
//    public void permissionCheck(){
//        Log.d(TAG, "permissionCheck()");
//        permissionSupport = new PermissionSupport(this, this);
//
//        if (!permissionSupport.checkPermission()){
//            Log.d(TAG, "미허용된 권한 요청");
//
//            if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ) {
//                Log.d(TAG, "shouldShowRequestPermissionRationale - ACCESS_COARSE_LOCATION");
//            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ){
//                Log.d(TAG, "shouldShowRequestPermissionRationale - ACCESS_FINE_LOCATION");
////            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ){
////                Log.d(TAG, "shouldShowRequestPermissionRationale - ACCESS_BACKGROUND_LOCATION");
////            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ){
////                Log.d(TAG, "shouldShowRequestPermissionRationale - READ_EXTERNAL_STORAGE");
////            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ){
////                Log.d(TAG, "shouldShowRequestPermissionRationale - WRITE_EXTERNAL_STORAGE");
//            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ){
//                Log.d(TAG, "shouldShowRequestPermissionRationale - CAMERA");
//            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) ){
//                Log.d(TAG, "shouldShowRequestPermissionRationale - MANAGE_EXTERNAL_STORAGE");
////            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) ){
////                Log.d(TAG, "shouldShowRequestPermissionRationale - REQUEST_INSTALL_PACKAGES");
//            }else if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ){
//                Log.d(TAG, "shouldShowRequestPermissionRationale - CALL_PHONE");
//            }else {
//                Log.d(TAG, "shouldShowRequestPermissionRationale!");
//            }
//
//            permissionSupport.requestPermission();
//
//        }else{
//            Log.d(TAG, "권한 체크후 모든권한 허용(ACCESS_BACKGROUND_LOCATION 제외)");
//
//            // 위치권한 항상허용 체크(별도로 체크해야 함)
////            check_ACCESS_BACKGROUND_LOCATION();
//        }
//    }

    public final int MY_PERMISSIONS_REQUEST_CHECK = 1;
    public void permissionCheck() {

        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                //M 이상에서만 퍼미션 확인(그 이하에서는 자동으로 허용됨)
//                //다른 앱 위에서 그리기 권한에 대한 허용 여부 체크
//                if (Settings.canDrawOverlays(mContext)) {
//                    //이미 권한 설정 되어있음
//                } else {
//                    //권한 없음
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext.getPackageName()));
//                    mContext.startActivity(intent);    //startActivityForResult로 대체 가능
//                }
//            }

            // 사용 권한 체크( 사용권한이 없을경우 -1 )
            if (
                //(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
//                            (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) ||
//                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
//                            (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED)
            ) {
                // 권한이 없을경우

                // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
                if (
                    // ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
//                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
//                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED)
                ) {
                    // 사용자가 임의로 권한을 취소시킨 경우
                    // 권한 재요청
                    ActivityCompat.requestPermissions(this, new String[]{
                            //                        android.Manifest.permission.READ_PHONE_STATE
                            Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE
//                            , Manifest.permission.REQUEST_INSTALL_PACKAGES
                            , Manifest.permission.INSTALL_SHORTCUT
                            , Manifest.permission.CALL_PHONE
                            , Manifest.permission.READ_PHONE_STATE
//                            , Manifest.permission.READ_SMS
//                            , Manifest.permission.RECEIVE_SMS
                            , Manifest.permission.READ_PHONE_NUMBERS
                            , Manifest.permission.SYSTEM_ALERT_WINDOW
                    }, MY_PERMISSIONS_REQUEST_CHECK);

                } else {

                    // 최초로 권한을 요청하는 경우(첫실행)
                    ActivityCompat.requestPermissions(this, new String[]{
                            //                        android.Manifest.permission.READ_PHONE_STATE
                            Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE
//                            , Manifest.permission.REQUEST_INSTALL_PACKAGES
                            , Manifest.permission.INSTALL_SHORTCUT
                            , Manifest.permission.CALL_PHONE
                            , Manifest.permission.READ_PHONE_STATE
//                            , Manifest.permission.READ_SMS
//                            , Manifest.permission.RECEIVE_SMS
                            , Manifest.permission.READ_PHONE_NUMBERS
                            , Manifest.permission.SYSTEM_ALERT_WINDOW
                    }, MY_PERMISSIONS_REQUEST_CHECK);
                }
            } else {
                // 시작로직추가...
                init();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Loggers.d("catch : checkDeviceAuth");
            handler.obtainMessage(RETRY_START, "").sendToTarget();
        }
    }

    /**
     * 권한 요청의 결과 수신 - 누락된 권한이 있을때는 앱을 종료처리하는 로직 포함..
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            // 전화번호 상태 권한얻기
            case MY_PERMISSIONS_REQUEST_CHECK: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한허용
                    Loggers.d2(this, "*************************************** 권한허용");

                    init();

                } else {
                    // 권한거부
                    Loggers.d2(this, "*************************************** 권한이 필요합니다");
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

//    /**
//     * requestPermissions 콜백
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult()");
//
//        if(requestCode == permissionSupport.MULTIPLE_PERMISSIONS){
//
////            // 위치권한 항상허용 체크(별도로 체크해야 함)
////            check_ACCESS_BACKGROUND_LOCATION();
//
//            if (!permissionSupport.resultPermission(requestCode, permissions, grantResults)) {
//                // 요청한 권한중 하나이상 거부
//                //CommonUtil.with().ToastMsg(getApplicationContext(), "권한을 허용해 주세요.", Toast.LENGTH_LONG).show();
//                Log.d(TAG, "요청한 권한중 하나이상 거부");
//
//                String debug_msg = "\n\n";
//                String msg = "\n\n";
//
//                for(int i=0; i<grantResults.length; i++){
//                    if(grantResults[i] == -1){
//                        if(permissions[i].equals(Manifest.permission.READ_PHONE_NUMBERS)){
//                            debug_msg = debug_msg + "\nREAD_PHONE_NUMBERS";
//                        }else if(permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)){
//                            debug_msg = debug_msg + "\nACCESS_COARSE_LOCATION";
//                        }else if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
//                            debug_msg = debug_msg + "\nACCESS_FINE_LOCATION";
////                        }else if(permissions[i].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
////                            debug_msg = debug_msg + "\nACCESS_BACKGROUND_LOCATION";
////                        }else if(permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
////                            debug_msg = debug_msg + "\nREAD_EXTERNAL_STORAGE";
////                        }else if(permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
////                            debug_msg = debug_msg + "\nWRITE_EXTERNAL_STORAGE";
////                        }else if(permissions[i].equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE)){
////                            debug_msg = debug_msg + "\nMANAGE_EXTERNAL_STORAGE";
//                        }else if(permissions[i].equals(Manifest.permission.CAMERA)){
//                            debug_msg = debug_msg + "\nCAMERA";
////                        }else if(permissions[i].equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE)){
////                            debug_msg = debug_msg + "\nMANAGE_EXTERNAL_STORAGE";
////                        }else if(permissions[i].equals(Manifest.permission.REQUEST_INSTALL_PACKAGES)){
////                            debug_msg = debug_msg + "\nREQUEST_INSTALL_PACKAGES";
//                        }else if(permissions[i].equals(Manifest.permission.CALL_PHONE)){
//                            debug_msg = debug_msg + "\nCALL_PHONE";
//                        }
//                        //debug_msg = "";
//
//                        if( permissions[i].equals(Manifest.permission.READ_PHONE_NUMBERS) ){
//                            msg = msg + "\n전화 : 권한 - 전화 - '허용' 선택";
////                        }else if( permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) || permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) || permissions[i].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION) ){
////                            msg = msg + "\n위치 : 권한 - 위치 - '항상 허용' 선택, '정확한 위치 사용' 체크";
////                        }else if( permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) || permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permissions[i].equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE) ){
////                            msg = msg + "\n파일 : 권한 - 파일 및 미디어 - '모든 파일 관리 허용' 선택";
//                        }else if(permissions[i].equals(Manifest.permission.CAMERA)){
//                            msg = msg + "\n카메라 : 권한 - 카메라 - '앱 사용 중에만 허용' 선택";
//                        }
//                    }
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("권한 설정");
//                builder.setMessage("권한을 허용 하시면 앱을 원할히 사용할 수 있습니다.\n권한 설정에서 앱 권한을 허용 해주세요.\n설정 - 애플리케이션 - " + Svc.APP_NAME + "" + msg + debug_msg);
//                builder.setCancelable(true);
//                builder.setPositiveButton("권한 설정", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Log.d(TAG, "setPositiveButton");
//                        try {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            intent.setData(Uri.parse("package:" + getPackageName()));
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            e.printStackTrace();
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
//                            startActivity(intent);
//                        }
//                    }
//                });
//                builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Log.d(TAG, "setNegativeButton");
//                    }
//                });
//                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        Log.d(TAG, "setOnCancelListener");
//                    }
//                });
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
//                    @Override
//                    public void onDismiss(DialogInterface dialog){
//                        Log.d(TAG, "setOnDismissListener");
//
//                        finish();
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//            }else{
//                Log.d(TAG, "권한 요청후 모든권한 허용(ACCESS_BACKGROUND_LOCATION 제외)");
//
////                // 위치권한 항상허용 체크(별도로 체크해야 함)
////                check_ACCESS_BACKGROUND_LOCATION();
//            }
//
////        }else if(requestCode == REQ_PERM_ACCESS_BACKGROUND_LOCATION){
////            Log.d(TAG, "REQ_PERM_ACCESS_BACKGROUND_LOCATION");
////
////            for(int i=0; i<grantResults.length; i++){
////                Log.d(TAG,"REQ_PERM_ACCESS_BACKGROUND_LOCATION - permissions[" + i + "]:" + permissions[i] + ", grantResults[" + i + "]:" + grantResults[i]);
////
////                if( permissions[i].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)  && grantResults[i] == PackageManager.PERMISSION_DENIED ){
////                    // 앱 설정화면으로 이동
////                    requestSetting_ACCESS_BACKGROUND_LOCATION();
////                    break;
////                }else if( permissions[i].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)  && grantResults[i] == PackageManager.PERMISSION_GRANTED ){
////                    //모든 권한체크(허용) 완료후 처리사항
////                    init();
////                    break;
////                }
////            }
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    /**
     * 위치권한 항상허용 체크
     */
    public void check_ACCESS_BACKGROUND_LOCATION(){

//        if( ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED ){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("권한 설정");
//            builder.setMessage("위치 액세스 권한 설정에서 위치권한을 '항상 허용'으로 선택해 주시고 '정확한 위치 사용'을 켜주세요.");
//            builder.setCancelable(true);
//            builder.setPositiveButton("권한 설정", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    Log.d(TAG, "setPositiveButton");
//                    request_ACCESS_BACKGROUND_LOCATION();
//                }
//            });
//            builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    Log.d(TAG, "setNegativeButton");
//                    finish();
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                }
//            });
//            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    Log.d(TAG, "setOnCancelListener");
//                    finish();
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                }
//            });
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
//                @Override
//                public void onDismiss(DialogInterface dialog){
//                    Log.d(TAG, "setOnDismissListener");
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//
//        }else{
//            //모든 권한체크(허용) 완료후 처리사항
//            init();
//        }
    }

    /**
     * 위치권한 항상허용 권한요청
     */
    public void request_ACCESS_BACKGROUND_LOCATION(){
        Log.d(TAG, "request_ACCESS_BACKGROUND_LOCATION()");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQ_PERM_ACCESS_BACKGROUND_LOCATION);
    }

    /**
     * 권한 세팅화면 호출 (위치권한 항상허용)
     */
    public void requestSetting_ACCESS_BACKGROUND_LOCATION(){
        Log.d(TAG, "requestSetting_ACCESS_BACKGROUND_LOCATION()");

        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }

        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }





    public int prev_percent = 0;

    /**
     * 파일다운로드 - 업데이트 버전을 내려받기위함..
     */
    private class FileDownload extends AsyncTask<String, Void, String> {

        ProgressDialog mProgressDialog = new ProgressDialog(mContext);

        //dialog.setProgress(30);    // 진행 정도를 30%로 설정한다.

        Handler fileDownloadhandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {

                    case UPDATE_PERCENT:
//                        mProgressDialog.setMessage(String.format(msg.obj.toString()));
//                        mProgressDialog.onProgressUpdate();

                        mProgressDialog.show(
                                IntroActivity.this, "",
                                String.format(msg.obj.toString()), false
                        );
                        break;

                }

                return true;
            }
        });


        @Override
        protected void onPreExecute() {

            mProgressDialog.show(IntroActivity.this, "", "설치파일을 다운로드하는 중 입니다.");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);    // 바 형태로 변경

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(params[0]).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.obtainMessage(RequesterSession.REQ_OK, "접속에 실패했습니다.").sendToTarget();
                        return;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 404) {
                            handler.obtainMessage(RequesterSession.REQ_OK, "[404]접속에 실패했습니다.").sendToTarget();
                            return;
                        }
                        FileOutputStream outputStream;
                        String[] filedata = params[0].split("\\/");
                        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Svc.FILE_SAVE_PATH);
                        if (!dir.exists()) {
                            dir.mkdir();
                        } else {
                            dir.delete();
                        }
                        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + Svc.FILE_SAVE_PATH + filedata[filedata.length - 1];


                        InputStream is = response.body().byteStream();
                        OutputStream os = new FileOutputStream(fileName);

                        final int BUFFER_SIZE = 2048;
                        byte[] data = new byte[BUFFER_SIZE];

                        int count;
                        long total = 0;
                        int percent;

                        long fileTotalSize = response.body().contentLength();


                        while ((count = is.read(data)) != -1) {
                            total += count;
                            os.write(data, 0, count);

                            //                        int curSize = Math.toIntExact(total);
                            //                        int totalSize = Math.toIntExact(fileTotalSize);
                            percent = (int) ((double) total / (double) fileTotalSize * 100.0);

                            //                        percent = (int) Math.floor(curSize/totalSize);
                            try {
                                // 진행율이 왔다갔다한다..보정로직 추가..
                                if (prev_percent < percent) {
                                    //if (percent%5==0) {
                                    //String downloadStatus = String.format(" 설치파일을 다운로드 중 입니다...\n(진행율=%d, %d, %d) ", percent, totalSize, curSize);

                                    String downloadStatus = String.format("설치파일을 다운로드 중 입니다...진행율=%d/100", percent);
                                    Log.d(TAG, "downloadStatus = " + downloadStatus + " , prev_percent=" + prev_percent + " ,percent=" + percent);
                                    handler.obtainMessage(UPDATE_PERCENT, downloadStatus).sendToTarget();

                                    if (percent%10 == 0) {
                                        fileDownloadhandler.obtainMessage(UPDATE_PERCENT, downloadStatus).sendToTarget();
                                    }

                                    prev_percent = percent;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                        os.flush();
                        os.close();
                        is.close();
                        //                        outputStream = new FileOutputStream(new File(fileName), true);
                        //                        outputStream.write(response.body().byteStream());
                        //                        outputStream.close();
                        installApk(fileName);
                        //                        Toast.makeText(this, "Download complete.", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            super.onPostExecute(s);
        }


        /**
         * 업데이트된 앱을 설치..
         *
         * @param filePath
         */
        private void installApk(String filePath) {

            File apkfile = new File(filePath);
//        Uri apkUri = Uri.fromFile(apkfile);


            try {
                String packageName = mPackageName;

                Uri apkUri = FileProvider.getUriForFile(mContext, packageName, apkfile);
                if (apkUri != null) {


//                File dir = new File(filePath);
//                if (dir.exists()) {
//                    handler.obtainMessage(REQ_DOWNLOAD_WORK, "발견...!").sendToTarget();
//                }

                    Log.d(TAG, "installApk - 1, filePath=" + filePath);
//                handler.obtainMessage(REQ_DOWNLOAD_WORK, "filePath=" + filePath).sendToTarget();
//                handler.obtainMessage(REQ_DOWNLOAD_WORK, "업데이트 설치중 - 1/4").sendToTarget();
                    Intent packageinstaller = new Intent(Intent.ACTION_VIEW);
                    packageinstaller.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    packageinstaller.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    mContext.startActivity(packageinstaller);
                    //finish();

                    FinishWork();


                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG,"catch : installApk");
                handler.obtainMessage(RETRY_START, "").sendToTarget();
            }
        }



    }


    /**
     * 종료작업
     */
    public void FinishWork() {
        Log.d(TAG,"installApk에서 종료요청함..");
        // 닫기
        finish();
    }

}

