package com.gzonesoft.sg623;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.domain.BroadCastDomain;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;


public class NotiSettingActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context mContext = null;
    public static final int RETRY_START = 29999;   // 재시작 프로세스 ( 업데이트 지연 이슈 )
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case RETRY_START:
                    retryStart(); // 재시작 프로세스
                    break;


            }


        }
    };
    /**
     * 재시작 프로세스
     *
     *
     */
    void retryStart() {
        Toast.makeText(mContext, "앱을 재구동 바랍니다.", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_setting);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        findViewById(R.id.btn_message).setOnClickListener(this);
        findViewById(R.id.btn_comment).setOnClickListener(this);
        findViewById(R.id.btn_notice).setOnClickListener(this);
        findViewById(R.id.btn_setting_message).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_delete_comment).setOnClickListener(this);

        mContext = this;

        checkDeviceAuth();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_message:
                NotiManager.sendNotification(this, 1, NotiManager.Channel.MESSAGE, "Message title", "Message body");
                break;
            case R.id.btn_comment:
                NotiManager.sendNotification(this, 2, NotiManager.Channel.COMMENT, "Comment title", "Comment body");
                break;
            case R.id.btn_notice:
                NotiManager.sendNotification(this, 3, NotiManager.Channel.NOTICE, "Notice title", "Notice body");
                break;
            case R.id.btn_setting_message:
                goToNotificationSettings(NotiManager.Channel.MESSAGE);
                break;
            case R.id.btn_setting:
                goToNotificationSettings();
                break;
            case R.id.btn_delete_comment:
                NotiManager.deleteChannel(this, NotiManager.Channel.COMMENT);
                break;
        }

    }

    /**
     * Send Intent to load system Notification Settings for this app.
     */
    public void goToNotificationSettings() {
        Intent i = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(i);
    }

    /**
     * Send intent to load system Notification Settings UI for a particular channel.
     *
     * @param channel Name of channel to configure
     */
    public void goToNotificationSettings(String channel) {
        Intent i = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        i.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
        startActivity(i);
    }

    /**
     * FCM 정보세팅하기
     */
    public void setFCM() {
        try {

            // FCM 토큰 얻기
            String token = FirebaseMessaging.getInstance().getToken().toString(); //FirebaseInstanceId.getInstance().getToken();
            Loggers.d("FCM 토큰 = " + token);
            // 토큰정보 저장
//            AppSetting.fcmToken = token;
            if (token == null) {
                // 토큰정보 얻기 실패. 앱재구동 유도...
//                CommonUtil.with().ToastMsg(mContext, getString(R.string.msg_token_empty), Toast.LENGTH_SHORT).show();
                return;
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic("ALL_MESSAGE")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Loggers.d("푸쉬구독완료..");
                                //CommonUtil.with().ToastMsg(mContext, "푸쉬구독완료...", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 브로드캐스트 메시지 수신부
     */
    private void regBroadcastReceiverMessage() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mainReceiver,
                new IntentFilter(Svc.FCM_RECEIVER));
    }

    /**
     * 리시버 세팅
     */
    BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String recvMain = intent.getStringExtra(BroadCastDomain.BROADCAST_FCM_SVC);

            Loggers.d("\n\n리시버 수신[NotiSettingActivity] = " + recvMain);
            //setMessageSelect();

            //setFloatingView();


        }
    };



    /**
     * 앱 권한 확인
     */
    public final int MY_PERMISSIONS_REQUEST_CHECK = 1;

    private void checkDeviceAuth() {

        try {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //M 이상에서만 퍼미션 확인(그 이하에서는 자동으로 허용됨)
                //다른 앱 위에서 그리기 권한에 대한 허용 여부 체크
                if(Settings.canDrawOverlays(mContext)) {
                    //이미 권한 설정 되어있음
                }else{
                    //권한 없음
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext.getPackageName()));
                    mContext.startActivity(intent);    //startActivityForResult로 대체 가능
                }
            }

            // 사용 권한 체크( 사용권한이 없을경우 -1 )
            if (
                //(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
//                            (ContextCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
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
//                                || (ContextCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
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
//                            , android.Manifest.permission.REQUEST_INSTALL_PACKAGES
                            , Manifest.permission.INSTALL_SHORTCUT
                            , Manifest.permission.CALL_PHONE
                            , Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.READ_SMS
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
//                            , android.Manifest.permission.REQUEST_INSTALL_PACKAGES
                            , Manifest.permission.INSTALL_SHORTCUT
                            , Manifest.permission.CALL_PHONE
                            , Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.READ_SMS
                            , Manifest.permission.READ_PHONE_NUMBERS
                            , Manifest.permission.SYSTEM_ALERT_WINDOW
                    }, MY_PERMISSIONS_REQUEST_CHECK);
                }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotiManager.createChannel(this);
                }

                setFCM();

                // 브로드 캐스트 리시버 등록
                regBroadcastReceiverMessage();

                Loggers.d("getPackageName() = " + getPackageName());

//                // 공통코드 가져오기
//                requestCommCode();


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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotiManager.createChannel(this);
                    }

                    setFCM();

                    // 브로드 캐스트 리시버 등록
                    regBroadcastReceiverMessage();

                    Loggers.d("getPackageName() = " + getPackageName());

//                // 공통코드 가져오기
//                requestCommCode();


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
}
