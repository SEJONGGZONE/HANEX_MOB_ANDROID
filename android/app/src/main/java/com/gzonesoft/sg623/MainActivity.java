package com.gzonesoft.sg623;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.databinding.ActivityMainBinding;
import com.gzonesoft.sg623.ui.PhoneAuthActivity;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public Context mConText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConText = this;

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationView navigationView = binding.navView;
        if (navigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings)
                    .setOpenableLayout(binding.drawerLayout)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }


        // 권한체크로직 호출
        checkDeviceAuth();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /**
     * FCM 정보세팅하기
     */
//    public void setFCM() {
//        try {
//
//            // FCM 토큰 얻기
//            String token = FirebaseMessaging.getInstance().getToken().toString(); //FirebaseInstanceId.getInstance().getToken();
//            Loggers.d("FCM 토큰 = " + token);
//            // 토큰정보 저장
//            AppSetting.fcmToken = token;
//            if (AppSetting.fcmToken == null) {
//                // 토큰정보 얻기 실패. 앱재구동 유도...
//                CommonUtil.with().ToastMsg(mContext, getString(R.string.msg_token_empty), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                FirebaseMessaging.getInstance().subscribeToTopic("ALL_MESSAGE")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Loggers.d("푸쉬구독완료..");
//                                //CommonUtil.with().ToastMsg(mContext, "푸쉬구독완료...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//
//            // 채널생성
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            // 채널의 ID
//            String CHANNEL_ID = "CALLTRUCK_CHANNEL";
//            CharSequence name = "스마트배차 알림";   // 사용자에게 보이는 채널의 이름
//            String description = "콜트럭에서 보내주는 스마트배차알림입니다.";    // 사용자에게 보이는 채널의 설명
//            int importance = NotificationManager.IMPORTANCE_LOW;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            // 알림 채널을 설정
//            mChannel.setDescription(description);
//            mChannel.setShowBadge(false);
//            mNotificationManager.createNotificationChannel(mChannel);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(mChannel);
//
//
////        NotificationManager notificationManager =
////                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        // The id of the channel.
////        String id = "my_channel_02";
////        notificationManager.deleteNotificationChannel(id);
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


















    /**
     * 앱 권한 확인
     */
    public final int MY_PERMISSIONS_REQUEST_CHECK = 1;

    public void checkDeviceAuth() {

        try {

            // 약관동의 체크..
//            String AGREE_YN = SettingPref.with(LoginActivity.this).loadPref(ConstValue.UserSettingInfos.AGREE_YN.toString(), "N");
//            if (!AGREE_YN.equals("Y")) {
//                // 약관동의 팝업 호출
//                showAgreeActivity(JoinType._신규);
//            } else {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    //M 이상에서만 퍼미션 확인(그 이하에서는 자동으로 허용됨)
//                    //다른 앱 위에서 그리기 권한에 대한 허용 여부 체크
//                    if (Settings.canDrawOverlays(mContext)) {
//                        //이미 권한 설정 되어있음
//                    } else {
//                        //권한 없음
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext.getPackageName()));
//                        mContext.startActivity(intent);    //startActivityForResult로 대체 가능
//                    }
//                }

                // 사용 권한 체크( 사용권한이 없을경우 -1 )
                if (
                        (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                                //                                (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) ||
                                //                                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) ||
                                (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED)
                ) {
                    // 권한이 없을경우

                    // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
                    if (
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    //                                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED)
                                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                                    //                                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED)
                                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED)
                    ) {
                        // 사용자가 임의로 권한을 취소시킨 경우
                        // 권한 재요청
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
//                                , Manifest.permission.REQUEST_INSTALL_PACKAGES
                                , Manifest.permission.INSTALL_SHORTCUT
                                , Manifest.permission.CALL_PHONE
                                , Manifest.permission.READ_PHONE_STATE
//                                , Manifest.permission.READ_SMS
                                , Manifest.permission.READ_PHONE_NUMBERS
                                , Manifest.permission.SYSTEM_ALERT_WINDOW
                        }, MY_PERMISSIONS_REQUEST_CHECK);

                    } else {

                        // 최초로 권한을 요청하는 경우(첫실행)
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.CAMERA
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
//                                , Manifest.permission.REQUEST_INSTALL_PACKAGES
                                , Manifest.permission.INSTALL_SHORTCUT
                                , Manifest.permission.CALL_PHONE
                                , Manifest.permission.READ_PHONE_STATE
//                                , Manifest.permission.READ_SMS
                                , Manifest.permission.READ_PHONE_NUMBERS
                                , Manifest.permission.SYSTEM_ALERT_WINDOW
                        }, MY_PERMISSIONS_REQUEST_CHECK);
                    }
                } else {

                    // FCM 정보세팅하기
                    setFCM();


                    String AUTH_PHONE_NO = SettingPref.with(this).loadPref(ConstValue.UserSettingInfos.AUTH_PHONE_NO.toString(), "");
                    if (AUTH_PHONE_NO.length()<=0) {
                        // 휴대폰인증 여부확인하기
                        Intent intent = new Intent(getApplicationContext(), PhoneAuthActivity.class);
                        startActivity(intent);
                    } else {
                        // 업데이트 버전확인(구글플레이 이동유도..)
//                    checkUpdateAPK();
                    }

                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
//            Loggers.d("catch : checkDeviceAuth");
//            handler.obtainMessage(RETRY_START, "").sendToTarget();
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

//                    // 약관동의 체크..
//                    String AGREE_YN = SettingPref.with(LoginActivity.this).loadPref(ConstValue.UserSettingInfos.AGREE_YN.toString(), "N");
//                    if (!AGREE_YN.equals("Y")) {
//                        // 약관동의 팝업 호출
//                        showAgreeActivity(JoinType._신규);
//                    } else {

                        // FCM 정보세팅하기
                        setFCM();

                    String AUTH_PHONE_NO = SettingPref.with(mConText).loadPref(ConstValue.UserSettingInfos.AUTH_PHONE_NO.toString(), "");
                    if (AUTH_PHONE_NO.length()<=0) {
                        // 휴대폰인증 여부확인하기
                        Intent intent = new Intent(getApplicationContext(), PhoneAuthActivity.class);
                        startActivity(intent);
                    } else {
                        // 업데이트 버전확인(구글플레이 이동유도..)
//                    checkUpdateAPK();
                    }

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


    /**
     * FCM 정보세팅하기
     */
    public void setFCM() {
        try {

            FirebaseMessaging.getInstance().setAutoInitEnabled(true);

            // FCM 토큰 얻기
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("MAIN", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast
//                            String msg = getString(R.string.msg_token_fmt, token);
//                            Log.d("MAIN", msg);

                            // FCM토크저장
                            SettingPref.with(mConText).savePrefWithString(ConstValue.UserSettingInfos.FCM_TOKEN.name(), token);
                            // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });

            FirebaseMessaging.getInstance().subscribeToTopic("ALL_MESSAGE")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Loggers.d("푸쉬구독완료..");
                            //CommonUtil.with().ToastMsg(mContext, "푸쉬구독완료...", Toast.LENGTH_SHORT).show();
                        }
                    });


//            String token = String.format("%s", FirebaseMessaging.getInstance().getToken()); //FirebaseInstanceId.getInstance().getToken();
//            Loggers.d("FCM 토큰 = " + token);
//            // 토큰정보 저장
////            AppSetting.fcmToken = token;
//            if (token == null) {
//                // 토큰정보 얻기 실패. 앱재구동 유도...
////                CommonUtil.with().ToastMsg(mContext, getString(R.string.msg_token_empty), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                FirebaseMessaging.getInstance().subscribeToTopic("ALL_MESSAGE")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Loggers.d("푸쉬구독완료..");
//                                //CommonUtil.with().ToastMsg(mContext, "푸쉬구독완료...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}