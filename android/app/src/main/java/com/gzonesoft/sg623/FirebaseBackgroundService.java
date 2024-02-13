package com.gzonesoft.sg623;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.FcmMessageInfo;
import com.gzonesoft.sg623.domain.BroadCastDomain;
import com.gzonesoft.sg623.domain.FcmMsgType;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.SettingPref;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FirebaseBackgroundService extends WakefulBroadcastReceiver {

    private static final String TAG = "FB_BackgroundService";

    public static Context mContext = null;
    public static int curVolume = 0;

    // 브로드캐스트관련.
    private Intent fcmBroadCastIntent = null;

    // TTS관련
    public static TextToSpeech tts;

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int PLAY_SOUND = 10000;   // 사운드 재생
    public static final int DRIVER_CONFIRM = 10001;   // 차주승인

    public static final int PLAY_TTS = 30001;   // TTS 재생

    public static String soundId;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PLAY_TTS:
                    // TTS초기설정 확인
                    String TTS_YN = SettingPref.with(mContext.getApplicationContext()).loadPref(ConstValue.UserSettingInfos.TTS_YN.toString(), "Y");
                    if (TTS_YN.equals("Y")) {
                        tts.speak(msg.obj.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    break;
                case PLAY_SOUND :

                    soundId = msg.obj.toString();

                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MediaPlayer ring= MediaPlayer.create(mContext, Integer.parseInt(soundId));
                            ring.start();
                        }
                    }, 100);

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


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");

        mContext = context;

        // TTS세팅
        initTTS(context);

        // 브로드캐스트 준비
        fcmBroadCastIntent = new Intent(BroadCastDomain.BROADCAST_FCM_SVC);

        if (intent.getExtras() != null) {

            try {

                Map<String, String> jsonMap = new HashMap<String, String>();
                for (String key : intent.getExtras().keySet()) {
                    Object value = intent.getExtras().get(key);
                    Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);

                    jsonMap.put(key, String.format("%s", value));
                }
                JSONObject object = new JSONObject(jsonMap);
                Log.e(TAG, "[FCM수신데이타] " + object.toString());

                Gson gson = new Gson();
                JsonObject jobjectStore = new JsonParser().parse(object.toString()).getAsJsonObject();
                FcmMessageInfo info = gson.fromJson(jobjectStore, FcmMessageInfo.class);
                Log.e(TAG, "[FcmMessageInfo] " + info.toString());

                // 전역변수에 수신정보 저장..
                AppSetting.fcmRecvMessage = info;

                String body = object.toString(); // info.getBody();

                // 알림-소리재생하기
                setPlaySound(info.getFcmType());

                // 브로드캐스트 전송.
                sendBroadCast(info.getFcmType());


//                String channelId = "location_notification_channel";
//                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                Intent resultIntent = new Intent();
//                PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext.getApplicationContext(), channelId);
//                builder.setContentTitle("@string/app_name");
//                builder.setDefaults(NotificationCompat.DEFAULT_ALL);
//                builder.setContentText("[12345]");
//                builder.setContentIntent(pendingIntent);
//                builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//                builder.setAutoCancel(false);
//                builder.setPriority(NotificationCompat.PRIORITY_MAX);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    //                if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
//                    NotificationChannel notificationChannel = new NotificationChannel(channelId, "Location Service Channel", NotificationManager.IMPORTANCE_HIGH);
//                    notificationChannel.setDescription("이 채널은 알림 수신을 위해서 사용됩니다.");
//                    notificationChannel.setShowBadge(false);
//                    notificationManager.createNotificationChannel(notificationChannel);
//
//                    int notifyID = 2;
//                    String CHANNEL_ID = "CALLTRUCK_CHANNEL";
//                    Notification notification = new Notification.Builder(FirebaseMessageService.this)
//                            .setContentTitle(URLDecoder.decode("타이틀", "UTF-8"))
//                            .setContentText(URLDecoder.decode("메시지내용..", "UTF-8"))
//                            .setSmallIcon(R.drawable.icons_truck)
//                            .setChannelId(CHANNEL_ID)
//                            .setNumber(0)
//                            //.setContentIntent(pendingIntent)
//                            .build();
//
//                    //            mediaPlayer = MediaPlayer.create(this,R.raw.dispatch_calltruck);
//                    //            mediaPlayer.start();
//
//                    notificationManager.notify(notifyID, notification);
//                    //                }
//                }


//                // 스마트배차, 협의요청의 경우 팝업..
//                if (
//                        (info.getFcmType().equals(FcmMsgType._협의시작))
//                        ||
//                        (info.getFcmType().equals(FcmMsgType._협의완료))
//                        ||
//                        (info.getFcmType().equals(FcmMsgType._배차확정))
//                        ||
//                        (info.getFcmType().equals(FcmMsgType._배차수정))
//                ) {
//                    Handler mHandler = new Handler(Looper.getMainLooper());
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 액티비티 띄우기
//                            // setFloatingView(mContext);
//
//                            // 기존 액티비티 종료
//                            AlertActivity prevActivity = (AlertActivity) AlertActivity.alertActivity;
//                            if (prevActivity != null) prevActivity.finish();
//
//                            // 알럿 종료 차단, 저장..
//                            SettingPref.with(mContext).savePrefWithString(ConstValue.UserSettingInfos.ALERT_FINISH_YN.name(), "N");
//
//                            // 신규 액티비티로 띄운다..
//                            Intent intent;
//                            intent = new Intent(context, AlertActivity.class)
//                                    .putExtra("BODY_DATA", body)
//                                    //.setAction(Intent.ACTION_MAIN)
//                                    //                                .addCategory(Intent.CATEGORY_LAUNCHER)
//                                    //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            ;
//                            context.getApplicationContext().startActivity(intent);
//
//                        }
//                    }, 1500);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
     * FCM 브로드캐스트 전송..
     * @param fcmType
     */
    public void sendBroadCast(String fcmType) {
        try {
            if (fcmType.equals("999")) {
                restartApp(mContext);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // 브로드캐스트..
        fcmBroadCastIntent.putExtra(BroadCastDomain.BROADCAST_FCM_RELOAD, fcmType);
        mContext.getApplicationContext().sendBroadcast(fcmBroadCastIntent);
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

                int soundId = 0;
                if (fcmType.equals(FcmMsgType._공차등록)) {
//                    soundId = R.raw.recv_treinfo;
                    soundId = 0; // 무음처리..
                } else if (fcmType.equals(FcmMsgType._협의시작)) {
                    // soundId = R.raw.calltruck_recv_dispatch_try;
                    handler.obtainMessage(PLAY_TTS, "콜트럭.배차협의가 도착하였습니다.").sendToTarget();
                } else if (fcmType.equals(FcmMsgType._협의완료)) {
//                    soundId = 0; // 무음처리..
                    handler.obtainMessage(PLAY_TTS, "콜트럭.배차협의가 완료되었습니다.").sendToTarget();
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
     * 지정리소스 재생
     * @param resId
     */
    public static void playDispatch(Context context, int resId) {

        handler.obtainMessage(PLAY_SOUND, String.format("%d", resId)).sendToTarget();

    }


    /**
     * TTS 서비스 초기화
     */
    private void initTTS(Context ctx) {
        // TTS초기설정 확인
        String TTS_YN = SettingPref.with(ctx.getApplicationContext()).loadPref(ConstValue.UserSettingInfos.TTS_YN.toString(), "Y");
        if (TTS_YN.equals("Y")) {
            tts = new TextToSpeech(ctx.getApplicationContext(), new TextToSpeech.OnInitListener() {
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

}