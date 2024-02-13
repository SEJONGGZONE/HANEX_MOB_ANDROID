package com.gzonesoft.sg623.screen;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.domain.FcmMsgType;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlertActivity extends AppCompatActivity {

    public static AlertActivity alertActivity;

    public static Context mContext = null;
    public static int curVolume = 0;

    private String bodyData = "", fcmType = "", title = "", body = "", bgColor = "", txtColor = "", btnTitle1 = "", btnTitle2="";
    private String refcode1 = "", refcode2 = "", refcode3 = "";

    public int showTime = 0;

    private FrameLayout flRootLayout;
    private TextView tvTitle, tvMessage, tvMessage2, tvCount;
    private Button btnAction, btnDetail, btnClose;

    // TTS관련
    private TextToSpeech tts;

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int COUNT_PROCESS = 10000;   // 카운트 처리
    public static final int DRIVER_CONFIRM = 10001;   // 차주승인

    public static final int PLAY_TTS = 30001;   // TTS 재생


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PLAY_TTS:
                    String TTS_YN = SettingPref.with(getApplicationContext()).loadPref(ConstValue.UserSettingInfos.TTS_YN.toString(), "Y");
                    if (TTS_YN.equals("Y")) {
                        tts.speak(msg.obj.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    break;
                case COUNT_PROCESS :
                    CommonUtil.with().setHtmlText(tvCount, String.format("<big>%d초</big><small><small> 후 다음 기사님께 전달됩니다.</small></small>", showTime));

                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (showTime>1) {
                                try {
                                    showTime--;
                                    handler.obtainMessage(COUNT_PROCESS, "").sendToTarget();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                FinishWork();
                            }
                        }
                    }, 1000);

                    break;
                case RequesterSession.REQ_OK:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;
                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;
            }


        }
    };


    /**
     * 백그라운드 진입전..
     */
    @Override
    protected void onPause() {
        super.onPause();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 홈버튼을 눌렀을때..
     */
    @Override
    protected void onUserLeaveHint()
    {
        // TODO Auto-generated method stub
        super.onUserLeaveHint();
        FinishWork();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        FinishWork();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
    }


    /**
     * App 재구동
     */
    public void restart(){
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // 스마트 배차, 알림 종료시간 초과여부 확인.
        String alertFinishYn = SettingPref.with(mContext).loadPref(ConstValue.UserSettingInfos.ALERT_FINISH_YN.toString(), "Y");
        if (alertFinishYn.equals("Y")) {
            // App 재구동
            restart();
        }

        // 종료를 위해 저장..
        alertActivity = this;

        // 넘어온 데이타 확인
        Intent intent = getIntent();
        try {
            bodyData = String.valueOf(intent.getStringExtra("BODY_DATA"));
//    {
//            "refcode1": "",
//            "refcode2": "TRE20200618183123745",
//            "refcode3": "0000000024",
//            "google.original_priority": "high",
//            "google.sent_time": "1592992804448",
//            "notiSoundUrl": "calltruck_recv_dispatch_try",
//            "notiType": "0",
//            "google.delivered_priority": "high",
//            "showTime": "11",
//            "gcm.notification.title": "콜트럭-배차",
//            "btnTitle1": "",
//            "google.c.sender.id": "1097782698144",
//            "fullScreenYn": "Y",
//            "btnTitle2": "",
//            "body": "홍명보님, 배차정보가 도착하였습니다.",
//            "title": "콜트럭-배차",
//            "google.message_id": "0:1592992804467842%4609f91f4609f91f",
//            "collapse_key": "calltruck.trucker",
//            "gcm.notification.e": "1",
//            "google.c.a.e": "1",
//            "google.ttl": "2419200",
//            "fcmType": "130",
//            "from": "1097782698144",
//            "gcm.notification.body": "배차정보가 도착하였습니다."
//    }

            Loggers.d2(this, "bodyData = " + bodyData);

            fcmType = AppSetting.fcmRecvMessage.getFcmType();
            title = AppSetting.fcmRecvMessage.getTitle();
            body = AppSetting.fcmRecvMessage.getBody();
            btnTitle1 = AppSetting.fcmRecvMessage.getBtnTitle1();
            btnTitle2 = AppSetting.fcmRecvMessage.getBtnTitle2();
            bgColor = "3856B3"; // jObject.get("bgColor").getAsString();
            txtColor = "FFFFFF"; // jObject.get("txtColor").getAsString();
            showTime = Integer.parseInt(AppSetting.fcmRecvMessage.getShowTime());
            refcode1 = AppSetting.fcmRecvMessage.getRefcode1();
            refcode2 = AppSetting.fcmRecvMessage.getRefcode2();
            refcode3 = AppSetting.fcmRecvMessage.getRefcode3();
        } catch (Exception e) {
            e.printStackTrace();
            title = "";
            body = "";
            fcmType = "ERROR";
        }

        // TTS세팅
        initTTS();

        // UI 세팅
        initUI();

        //playSound();
//        setPlaySound();

    }
    @Override
    public void setRequestedOrientation(int requestedOrientation){
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            super.setRequestedOrientation(requestedOrientation);
        }
    }

    private void initUI() {

        setContentView(R.layout.activity_alert);

        // 상태바 색상 지정(검은색으로)
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }
        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 배경설정
        flRootLayout = (FrameLayout) findViewById(R.id.flRootLayout);
        flRootLayout.setVisibility(View.VISIBLE);


        // 타이틀 세팅
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        CommonUtil.with().setHtmlText(tvTitle, title.replace("_", "&nbsp;"));

        // 메인 메시지
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        if (!txtColor.isEmpty()) tvMessage.setTextColor(Color.parseColor("#" + txtColor));
        tvMessage2 = (TextView) findViewById(R.id.tvMessage2);
        if (!txtColor.isEmpty()) tvMessage2.setTextColor(Color.parseColor("#" + txtColor));


        // 카운트
        tvCount = (TextView) findViewById(R.id.tvCount);
        // 하단버튼
        btnAction = (Button) findViewById(R.id.btnAction);
        btnDetail = (Button) findViewById(R.id.btnDetail);
        btnClose = (Button) findViewById(R.id.btnClose);

        if (fcmType.equals(FcmMsgType._협의시작)) {
            // 배경색 설정
            flRootLayout.setBackgroundColor(Color.parseColor("#3856B3"));

            // 메인 메시지 출력
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage2.setVisibility(View.GONE);
            CommonUtil.with().setHtmlText(tvMessage, body.replace("_", "&nbsp;"));

            // 카운트 메시지
            tvCount.setVisibility(View.VISIBLE);
            if (!txtColor.isEmpty()) tvCount.setTextColor(Color.parseColor("#" + txtColor));

            // 카운트 메시지 출력
            handler.obtainMessage(COUNT_PROCESS, "").sendToTarget();

            // '배차요청'버튼 설정..
            btnAction.setVisibility(View.VISIBLE);
            btnDetail.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);

            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 차주 승인 요청.
                    requestAdmDriverConfirm();

                    try {
                        // 전화걸기 - FCM 참조변수 1번째로 전화를 건다.
                        Intent tt = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + AppSetting.fcmRecvMessage.getRefcode1()));
                        startActivity(tt);
                    } catch (Exception ex) {
                        CommonUtil.with().ToastMsg(mContext, "전화걸기 시도중, 오류가 발생하였습니다.", Toast.LENGTH_SHORT);
                        ex.printStackTrace();
                    }

                }
            });

        } else if (fcmType.equals(FcmMsgType._배차확정) || fcmType.equals(FcmMsgType._배차수정)) {
            // 배경색 설정
            flRootLayout.setBackgroundColor(Color.parseColor("#54B25F"));

            // 메인 메시지 출력
            tvMessage.setVisibility(View.GONE);
            tvMessage2.setVisibility(View.VISIBLE);
            CommonUtil.with().setHtmlText(tvMessage2, body.replace("_", "&nbsp;"));

            // 카운트 감추기
            tvCount.setVisibility(View.GONE);

            // 버튼설정
            btnAction.setVisibility(View.GONE);
            btnDetail.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.VISIBLE);

//            btnDetail.setTextColor(Color.parseColor("#531B93"));
//            btnDetail.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!refcode1.isEmpty()) {
                        // 현재 배차정보 저장
                        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.DPID.name(), refcode1);
                        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.TRECODE.name(), refcode2);
                        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.JUSUN_TELNO.name(), refcode3);
                        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.AGREEDATE.name(), CommonUtil.with().nowYYYY_MM_DD_HH_MM_SS());
                    }
                    // 앱 재구동
                    restartApp(mContext);
                }
            });
//            btnClose.setTextColor(Color.parseColor("#531B93"));
//            btnClose.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!refcode3.isEmpty()) {
                        // 주선사에게 전화걸기..
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + refcode3));
                        startActivity(intent);
                    }
                    // 종료..
                    FinishWork();
                }
            });
        }


    }

    /**
     * 닫기버튼을 눌렀을때..
     * @param view
     */
    public void clickCloseButton(View view) {
        FinishWork();
    }

    /**
     * 종료작업 - 닫기
     */
    public void FinishWork() {

        // 알럿 종료 저장..
        flRootLayout.setVisibility(View.GONE);
        SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.ALERT_FINISH_YN.name(), "Y");
        // 닫기
        finish();
    }

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
     * 지정리소스 재생
     * @param resId
     */
    public static void playDispatch(Context context, int resId) {
        MediaPlayer ring= MediaPlayer.create(context, resId);
        ring.start();

    }

    /**
     * TTS 서비스 초기화
     */
    private void initTTS() {
        // TTS초기설정 확인
        String TTS_YN = SettingPref.with(getApplicationContext()).loadPref(ConstValue.UserSettingInfos.TTS_YN.toString(), "Y");
        if (TTS_YN.equals("Y")) {
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.KOREAN);
                    }
                }
            });
            tts.setPitch(1.3f); // 음성톤 설정
            tts.setSpeechRate(1.1f); // 읽는 속도 설정
        }
    }
    /**
     * 알림-소리재생하기
     */
    public void setPlaySound(String fcmType) {

        // 오디오 매니저 얻기
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        // 현재 볼륨 가져오기
        curVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 볼륨키우기(90%)
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 50 * 20 / 10, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);


        playDispatch(mContext, R.raw.next);

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 사용하고자 하는 코드

                // if (fcmType)
                int soundId = 0;
                if (fcmType.equals(FcmMsgType._공차등록)) {
                    //soundId = R.raw.recv_treinfo;
                    soundId = 0; // 무음처리..
                } else if (fcmType.equals(FcmMsgType._협의시작)) {
                    // soundId = R.raw.calltruck_recv_dispatch_try;
                    handler.obtainMessage(PLAY_TTS, "콜트럭. 배차협의가 도착하였습니다.").sendToTarget();
                } else if (fcmType.equals(FcmMsgType._협의완료)) {
                    soundId = 0; // 무음처리..
                } else if (fcmType.equals(FcmMsgType._배차확정)) {
                    //soundId = R.raw.calltruck_confirm_dispatch;
                    handler.obtainMessage(PLAY_TTS, "콜트럭. 배차확정이 완료되었습니다. 상세정보를 확인바랍니다.").sendToTarget();
                } else if (fcmType.equals(FcmMsgType._배차수정)) {
                    //soundId = R.raw.calltruck_confirm_dispatch;
                    handler.obtainMessage(PLAY_TTS, "콜트럭. 배차정보가 수정되었습니다. 변경된 정보를 확인바랍니다.").sendToTarget();
                } else if (fcmType.equals(FcmMsgType._배차취소)) {
                    //soundId = R.raw.calltruck_cancel_dispatch_info;
                    handler.obtainMessage(PLAY_TTS, "콜트럭.배차가 취소되었습니다.").sendToTarget();
                }

                if (soundId != 0) {
                    playDispatch(mContext, soundId);
                }
            }
        }, 500);


        // 볼륨원상복귀
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 오디오 매니저 얻기
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
            }
        }, 5000);
    }

    /**
     * 스마트배차 - 차주배차 승인 요청
     */
    public void requestAdmDriverConfirm() {

        try {

            // 입력 파라미터 세팅

//            Map<String, String> jsonMap = new HashMap<String, String>();
//            jsonMap.put("@I_TRECODE", AppSetting.fcmRecvMessage.getRefcode2());
//            jsonMap.put("@I_ADM_WORKNO", AppSetting.fcmRecvMessage.getRefcode3());
//            if (AppSetting.CUR_LOCATION == null) {
//                jsonMap.put("@I_CONFIRM_X", "");
//                jsonMap.put("@I_CONFIRM_Y", "");
//            } else {
//                jsonMap.put("@I_CONFIRM_X", String.format("%f", AppSetting.CUR_LOCATION.getLongitude()));  // 대기(시작)위치 - 경도
//                jsonMap.put("@I_CONFIRM_Y", String.format("%f", AppSetting.CUR_LOCATION.getLatitude()));  // 대기(시작)위치 - 위도
//            }
//
//            RequesterSession.with().RestAPIServiceCall(Svc.ADM_DRIVER_CONFIRM, jsonMap, requestAdmDriverConfirmCallback);

        }
        catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }
    }

    /**
     * REST-API Callback - 스마트배차 - 차주배차 승인 요청, 콜백
     */
    private Callback requestAdmDriverConfirmCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "requestAdmDriverConfirmCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "requestAdmDriverConfirmCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "requestAdmDriverConfirmCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestAdmDriverConfirmCallback, 수신데이타:" + strJsonOutput);

                handler.obtainMessage(DRIVER_CONFIRM, "").sendToTarget();

            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }
        }
    };


}
