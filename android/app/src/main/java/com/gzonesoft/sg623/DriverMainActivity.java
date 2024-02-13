package com.gzonesoft.sg623;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.gzonesoft.sg623.Layout.DispatchSummaryItemLayout;
import com.gzonesoft.sg623.Layout.DispatchTabLayout;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.data.LastWorkInfo;
import com.gzonesoft.sg623.data.UploadFileInfo;
import com.gzonesoft.sg623.domain.BroadCastDomain;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.domain.TodayStatus;
import com.gzonesoft.sg623.domain.UserType;
import com.gzonesoft.sg623.domain.WorkStatus;
import com.gzonesoft.sg623.model.DeliveryListManager;
import com.gzonesoft.sg623.service.LocationService;
import com.gzonesoft.sg623.ui.BoardActivity;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.ui.DeliveryListActivity;
import com.gzonesoft.sg623.ui.DispatchSummaryActivity;
import com.gzonesoft.sg623.ui.ItemMngListActivity;
import com.gzonesoft.sg623.pop.PopCommonActivity;
import com.gzonesoft.sg623.pop.PopLastStatusActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.ImageResizeUtils;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;
import com.gzonesoft.sg623.util.Svc;
import com.gzonesoft.sg623.util.UDialog;
import com.ssomai.android.scalablelayout.ScalableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DriverMainActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();

    public static Context mContext = null;
    public static String CALL_TYPE = "";

    private LastWorkInfo mLastWorkInfo;

    private LocationService mLocationService = null;


    private AppBarConfiguration mAppBarConfiguration;


    private TextView tvInfo1, tvInfo2, tvInfo3, tvInfo4, tvInfo5;
    private TextView tvInfo6, tvInfo7, tvInfo8, tvInfo8_1, tvInfo9, tvInfo10;
    private TextView tvInfo11, tvInfo11_1, tvInfo11_2, tvInfo12, tvInfo13, tvInfo14, tvInfo15;
    private TextView tvInfo16, tvInfo17, tvInfo18, tvInfo19, tvInfo20;
    private TextView tvInfo21, tvInfo22, tvInfo23, tvInfo24, tvInfo25;
    private TextView tvInfo26, tvInfo27, tvInfo28, tvInfo29, tvInfo30;
    private TextView tvInfo31, tvInfo32;
    private TextView tvInfo33;
    private TextView operate01, operate02, operate03, operate04, operate05;

    private TextView tvDispatchInfo2, tvWaitInfo;

    private ImageView ivImage1, ivImage2, ivImage3, ivImage4, ivImage5;
    private ImageView ivImage6, ivImage7, ivImage8, ivImage9, ivImage10;
    private ImageView ivImage11;

    private View view1, view2, view3, view4, view5;
    private View view6, view7, view8, view9, view10;

    private ScalableLayout scMainTopArea, scStartFinish, scDispInfoArea, scSelfMenuPart1, scMenuPart1, scMenuPart2;
    private ScalableLayout scArea6, scArea7, scNoticeArea;
    private LinearLayout llCVOInfo, llNearByInfo, llHScrollItemArea, llItemList;
    private ScrollView scrollItemList;

    private ScrollView scMain;

    // 애니메이션 효과
    Animation animationStart = null;
    Animation animationStartSpeed = null;
    Animation animationStop = null;

    public Boolean mUserReloadYN = false;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public MediaPlayer ring;

    // ---------------------------------------------------
    // 카메라 촬영/업로드 관련
    // ---------------------------------------------------
    // 업로드 파일처리 관련
    private boolean bUploadYn = false;
    private ArrayList<File> mUploadWorkFiles = new ArrayList<>();   // 업로드 작업정보 저장 - 파일정보
    private ArrayList<String> mUploadDocType = new ArrayList<>();   // 업로드 작업정보 저장 - 서류종류
    private int mLastWorkIndex = -1;    // 최종 업로드 작업 인덱스
    private ArrayList<UploadFileInfo> uploadFileResultInfos = new ArrayList<UploadFileInfo>();    // 업로드 성공 리턴정보 저장
    // 카메라 촬영 관련
    private File mTempFile;
    private int nCurrentCameraButtonTag = 0;
    private Uri mImageCaptureUri;
    // 이미지 처리관련..
    private Boolean isCamera = false;
    private final int ACT_TAKE_PIC = 20000;
    private final int PICK_FROM_GALLERY = 20001;
    private final int REQ_CODE_SELECT_IMAGE = 20002;


    // 배송지 목록정보 저장
    private ArrayList<DeliveryListInfo> deliveryListItems = new ArrayList<DeliveryListInfo>();

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스
    public static final int CVO_MAINDATA_SEL = 10000; // 메인업무 데이타 조회
    public static final int CVO_WORKSTAT_SAV = 10001; // 업무보고 완료
    public static final int CVO_DELIVERY_SEL = 20000; // 배차리스트 조회(배차번호가 있는경우)
    public static final int NEXT_UPLOAD_WORK = 30000; // 다음파일 업로드
    public static final int CVO_DISPATCH_SUMMARYT_SEL = 40000;   // 배차일보 정보 가져오기
    public static final int ERP_DISPATCH_SEL = 50000;   // ERP 배송목록 조회
    public static final int CVO_DELIVERY_START = 50001;   // CVO 배송확정 호출..






    public static final int PLAY_SOUND_NEXT = 30031;   // 사운드 재생
    public static final int PLAY_SOUND_CLEAR = 30032;   // 사운드 재생
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            //Log.d(TAG, "msg.obj.toString() : " + msg.obj.toString());
            //Log.d(TAG, "msg.what : " + msg.what);

            showProgress(false);

            switch (msg.what) {
                case CVO_DELIVERY_START:
                    // CVO 배송일자,배차번호 로컬영역 저장.(화면재진입시 CVO 배송정보가져오기 위함)
                    try {

                        //String DDATE = deliveryListItems.get(0).getDDATE();
                        String DDATE = CommonUtil.with().nowYYYY_MM_DD(); // 배송일자는 당일로 설정한다.(이전주문 당일배송가능-아이사랑,준비편)
                        String CVO_DISPATCH_ID = deliveryListItems.get(0).getDISPATCH_ID();
                        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), DDATE);
                        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), CVO_DISPATCH_ID);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    break;
                case ERP_DISPATCH_SEL:
                    // 개발용으로 바로 확정처리를한다..
                    // CVO 배송시작(배송확정 호출)
                    requestCvoDeliveryStart();
                    break;
                case CVO_DISPATCH_SUMMARYT_SEL:

                    // 대기화면 보이기/감추기..
                    showProgress(false);

                    // 아이템 세팅
                    setDeliveryItemInfo();

                    break;

                case NEXT_UPLOAD_WORK:
                    // 대기화면 닫기..
                    showProgress(false);
                    try {
                        // 업로드 요청..
                        mLastWorkIndex++;
                        if (mUploadWorkFiles.size() > mLastWorkIndex) {
                            // 다음 업로드 파일 시도..
                            uploadImage(mUploadWorkFiles.get(mLastWorkIndex).getPath(), mUploadDocType.get(mLastWorkIndex));
                        } else {

                            // 화면 업데이트..
                            updateDispatchInfo();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case CVO_DELIVERY_SEL:
                    if (DeliveryListManager.getInstance().values().size()>0) {

//                        tvWaitInfo.startAnimation(animationStop);
                        tvWaitInfo.setVisibility(View.GONE);

                        llHScrollItemArea.removeAllViews();

                        for(DeliveryListInfo info: DeliveryListManager.getInstance().values()) {
                            Loggers.d("[전]" + info.getCLNAME() + ':' + info.getDISTANCE_ME_VALUE());
                        }
                        // 아이템 정렬 - 거리순
                        DeliveryListManager.getInstance().sortByDistance();

                        for(DeliveryListInfo info: DeliveryListManager.getInstance().values()) {
                            Loggers.d("[후]" + info.getCLNAME() + ':' + info.getDISTANCE_ME_VALUE());
                        }

                        for (DeliveryListInfo info : DeliveryListManager.getInstance().values()) {
                            addLayoutItem(info);
                        }
                    } else {
                        tvWaitInfo.setText("-아직 배송시작 전입니다.-");
                    }

                    // 대기화면 닫기..
                    showProgress(false);

                    break;

                case CVO_MAINDATA_SEL:

                    AppSetting.LAST_WORK_INFO = mLastWorkInfo;

                    AppSetting.userConfig.GPS_USE_YN = mLastWorkInfo.getGPS_YN(); // 위치정보 수집대상 여부
                    AppSetting.userConfig.GPS_GET_CYCLE = Integer.parseInt(mLastWorkInfo.getGPS_GET_CYCLE()); // 위치정보 수집주기
                    AppSetting.userConfig.GPS_SEND_TIME = Integer.parseInt(mLastWorkInfo.getGPS_SEND_TIME()); // 위치정보 전송주기

                    if( AppSetting.LAST_WORK_INFO.getLAST_TODAY_CD().equals(WorkStatus._출근보고) ) {

                        // 위치보고 시작 - Y
                        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.GPS_REPORT_START.name(), "Y");

                        // 출근/퇴근 구분 상태
                        AppSetting.todayStatus = TodayStatus._출근;

                        // 상태변경
                        AppSetting.workStatus = WorkStatus._출근보고;

                    } else if( AppSetting.LAST_WORK_INFO.getLAST_TODAY_CD().equals(WorkStatus._퇴근보고) ) {

                        // 위치보고 시작 - N
                        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.GPS_REPORT_START.name(), "N");

                        // 출근/퇴근 구분 상태
                        AppSetting.todayStatus = TodayStatus._퇴근;

                        // 상태변경
                        AppSetting.workStatus = WorkStatus._퇴근보고;

                    }

                    Log.d(TAG, "REQ_LASTWORK_OK - AppSetting.todayStatus : " + AppSetting.todayStatus);
                    Log.d(TAG, "REQ_LASTWORK_OK - AppSetting.workStatus : " + AppSetting.workStatus);


                    // 출근/퇴근 버튼 (상태)변경
                    setTodayButton();

                    // 메인 화면 데이터 세팅
                    setMainData();


                    // 위치서비스 리셋여부 확인(2022.10.31추가)
                    if (mLastWorkInfo.getRESET_LOCATION_SERVICE_YN().equals("Y")) {
                        // 위치서비스 리셋하기
                        resetLocationService();
                    }

                    // 배차정보 업데이트
                    updateDispatchInfo();

                    break;

                case PLAY_SOUND_NEXT:
                    // 사운드 재생
                    ring = MediaPlayer.create(mContext, R.raw.next);
                    ring.start();
                    break;

                case PLAY_SOUND_CLEAR:
                    // 사운드 재생
                    ring = MediaPlayer.create(mContext, R.raw.stage_clear);
                    ring.start();
                    break;

                case CVO_WORKSTAT_SAV:
                    // 출근보고 API 완료

                    if (curWorkStatus.equals(WorkStatus._출근보고)) {

                        CommonUtil.with().ToastMsg(mContext,
                                AppSetting.cvoUserInfo.getNAME() + "님, 안녕하세요.",
                                Toast.LENGTH_SHORT).show();
                        // 출근/퇴근 구분 상태
                        AppSetting.todayStatus = TodayStatus._출근;
                        // 상태변경
                        AppSetting.workStatus = WorkStatus._출근보고;
                        // 위치 트래킹 활성화
                        Log.d(TAG, "GO_TO_THE_OFFICE_OK - 위치 트래킹 활성화");

                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "위치 트래킹 권한요청");
                            ActivityCompat.requestPermissions(DriverMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                        } else {
                            // 위치서비스 시작
                            startLocationService();
                        }

                        // 최종작업상태 조회
                        getMainData();

                        // 배차내역
                        //goDriverDispatchList();

                        CommonUtil.with().ToastMsg(mContext, "위치정보 확인중입니다. 잠시만 기다려주세요..", Toast.LENGTH_SHORT).show();

                    } else if (curWorkStatus.equals(WorkStatus._퇴근보고)) {
                        CommonUtil.with().ToastMsg(mContext, "오늘도 힘써 주심에 감사드립니다.\n고생 많으셨습니다~", Toast.LENGTH_SHORT).show();
                        // 출근/퇴근 구분 상태
                        AppSetting.todayStatus = TodayStatus._퇴근;
                        // 상태변경
                        AppSetting.workStatus = WorkStatus._퇴근보고;

                        // 위치 트래킹 비활성화
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "위치 트래킹 권한요청");
                            ActivityCompat.requestPermissions(DriverMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                        } else {
                            stopLocationService();
                        }

                        Handler mHandler = new Handler(Looper.getMainLooper());
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 앱종료처리..
                                finishAffinity();
                                System.runFinalization();
                                System.exit(0);
                            }
                        }, 500);

                    }

                    break;

                case RequesterSession.REQ_OK_MESSAGE:
                    if (!"".equals(msg.obj.toString())) CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case RequesterSession.REQ_OK:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }

            return true;
        }
    });

    /**
     * 동적 레이아웃 추가 메소드
     */
    private void addLayoutItem(DeliveryListInfo info) {

        Loggers.d("거래처명 : " + info.getCLNAME());
        DispatchTabLayout itemLayout = new DispatchTabLayout(getApplicationContext(), info);
        llHScrollItemArea.addView(itemLayout);
    }

    /**
     * 프로그래스 다이얼로그 초기화
     */
    ProgressDialog pDialog;
    private void initProgressDlg() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setTitle("잠시만 기다려주세요.");
        pDialog.setMessage("GPS 확인중입니다...");
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initProgressDlg();

//        // 최종보고상태 팝업여부 로컬저장(강제팝업 차단) - 개발용 임시코드(주석풀시,디버깅해서 실행할것)
//        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.LAST_REPORT_POP_YN.name(), "N");

//        if (AppSetting.LOGIN_INFO == null) {
//            // 닫기
//            finish();
//        }


        // 넘어온 데이타 확인
        try {
            Intent intent = getIntent();
            CALL_TYPE = intent.getStringExtra("CALL_TYPE");
            // 토스트메시지 표시..
            if (CALL_TYPE == null) {
                CALL_TYPE = CommType._노티바에서실행;
            }
            //handler.obtainMessage(RequesterSession.REQ_OK, CALL_TYPE).sendToTarget();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 배차정보 강제 초기화(개발용)
//        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "");
//        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "");

        // 배차정보 확인 - 초기화하지않으면 이전 배차번호로 현재위치를 보고한다. 그래서 초기화...
        try {
            // 당일 배송일자가 아니라면 무조건 초기화하자..
            String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
            String toDay = CommonUtil.with().nowYYYY_MM_DD();
            if (
                    (!DDATE.equals(toDay))
                            ||
                            (!toDay.replace("-", "").equals(CVO_DISPATCH_ID.substring(3,11)))
            ){
                // 배차정보 강제 초기화
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "");
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 메인 UI 초기화
        initMainUI();

        // BROADCAST 리시버 등록
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastDomain.BROADCAST_TRACE_GPS_SVC);
        registerReceiver(traceReceiver, filter);

        // 진입하자마자로 변경, 실제 위치전송은 출근이후에만 보내도록 수정함..
        // 위치관제 시작
        startLocationService();

        // 초기/새로고침 화면
        reloadScreen();

        if (AppSetting.cvoUserInfo.getUSERTYPE().equals(UserType._배송기사)) {
            // 자동출근보고 및 관제시작
            ((DriverMainActivity) DriverMainActivity.mContext).workReportSav(WorkStatus._출근보고);
        }


        //lanuchWeb();
    }

    private void lanuchWeb() {
        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
        intent.putExtra("MOVE_URL", "https://me2.do/x58H6CH8");
        intent.putExtra("MOVE_TYPE", CommType._일반호출);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    /**
     * 진행중인 배차건을 확인..
     */
    public boolean updateLocationYn = false;
    void checkRunningDispatch() {
        try {

            String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");

            // 당일배송일자건이 있고..
            if (DDATE.equals(CommonUtil.with().nowYYYY_MM_DD())) {
                // 배차번호 체크..
                if (
                        !CVO_DISPATCH_ID.isEmpty()
                ) {
                    if (AppSetting.cvoUserInfo.getUSERTYPE().equals(UserType._배송기사)) {
                        // 배차내역 조회로 이동 시도...
                        goDriverDispatchList();
                    }
                }
            } else {
                // 당일배송건이 아닌경우 강제 초기화..
                // 배차정보 초기화
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "");
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "");
                //CommonUtil.with().ToastMsg(mContext, "이전 배차건이 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 초기/새로고침 화면
     */
    private void reloadScreen() {
        // 최종보고상태 선택 팝업 강제호출 - 내부에서 체크
        reportLastStatus();

        // 화면효과 시작..
        setActivate();

        if (mUserReloadYN) {
            mUserReloadYN = false;
            // 최종작업상태 조회
            getMainData();

            // 배차확정 정보가 없다면 ERP 배송목록 조회..
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
            if (CVO_DISPATCH_ID.length() == 0) {
                //  ERP 배송목록 조회
                requestErpDispatchSel();
            }
        }
    }


    /**
     * 화면 재진입시...
     */
    @Override
    protected void onResume() {
        super.onResume();

        checkPowerMode();

        if (openPopYn) { // 화면자동 갱신을 피하기위함...
            openPopYn = false;
            return;
        }

        // 최종작업상태 조회
        getMainData();

    }

    /**
     * 절전모드 확인. 감지시 세팅으로 보내고 앱을 종료한다.
     */
    public void checkPowerMode() {
        try {
            //절전모드 체크
            PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && powerManager.isPowerSaveMode()) {


                String sHtml = "" +
                        "<BIG>" +
                        "원활한 업무진행을 위하여 아래설정" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "확인이 필요합니다." +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "'절전모드 사용 <BIG><font color='#ED7D31'>OFF(사용안함)</font></BIG> 설정'<BR>" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "부탁드립니다. 감사합니다." +
                        "</BIG>";
                // 팝업다이얼로그 호출
                UDialog.withHtmlTwoCustom_okCancel(mContext, Svc.APP_NAME, sHtml, "<BIG>설정으로 이동 >></BIG>", "취소", View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                    @Override
                    public void onClick_yes() {

                        //절전모드 셋팅으로 보내기
                        Intent batterySaverIntent = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                        startActivity(batterySaverIntent);

                        // 앱종료처리..
                        finishAffinity();
                        System.runFinalization();
                        System.exit(0);
                    }

                    @Override
                    public void onClick_no() {
                        finish();
                    }
                });

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 최종보고상태 팝업 호출
     */
    private void reportLastStatus() {
        try {
            String LAST_REPORT_POP_YN = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.LAST_REPORT_POP_YN.toString(), "N");
            if (LAST_REPORT_POP_YN.equals("Y")) {
                Intent intent = new Intent(getApplicationContext(), PopLastStatusActivity.class);
                startActivity(intent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    /**
     * 메인 UI 초기화.
     */
    private void initMainUI() {
        try {
            // 화면을 portrait(세로) 화면으로 고정
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            setContentView(R.layout.activity_driver_main);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


//            navigationView = (NavigationView) findViewById(R.id.navigationView);
//            View navigationHeader = navigationView.getHeaderView(0);
//
//            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    int id = item.getItemId();
//                    return true;
//                }
//            });
//            // 스와이프 차단..
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            // 상태바 색상 지정(검은색으로)
            if (Build.VERSION.SDK_INT >= 21) {
                // 21 버전 이상일 때
                getWindow().setStatusBarColor(Color.BLACK);
            }

            // 애니메이션리소스 매핑
            animationStart = AnimationUtils.loadAnimation(mContext, R.anim.animation_start);
            animationStartSpeed = AnimationUtils.loadAnimation(mContext, R.anim.animation_start_speed);
            animationStop = AnimationUtils.loadAnimation(mContext, R.anim.animation_stop);

            scMain = (ScrollView) findViewById(R.id.scMain);

//            tvInfo1 = (TextView) findViewById(R.id.tvInfo1);
            tvInfo2 = (TextView) findViewById(R.id.tvInfo2);
            tvInfo3 = (TextView) findViewById(R.id.tvInfo3);
            tvInfo4 = (TextView) findViewById(R.id.tvInfo4);
            tvInfo5 = (TextView) findViewById(R.id.tvInfo5);
            tvInfo6 = (TextView) findViewById(R.id.tvInfo6);
            tvInfo7 = (TextView) findViewById(R.id.tvInfo7);
            tvInfo8 = (TextView) findViewById(R.id.tvInfo8);
            tvInfo8_1 = (TextView) findViewById(R.id.tvInfo8_1);
            tvInfo9 = (TextView) findViewById(R.id.tvInfo9);
//            tvInfo10 = (TextView) findViewById(R.id.tvInfo10);
            tvInfo11 = (TextView) findViewById(R.id.tvInfo11);

            tvInfo11_1 = (TextView) findViewById(R.id.tvInfo11_1);
            tvInfo11_2 = (TextView) findViewById(R.id.tvInfo11_2);

            tvInfo12 = (TextView) findViewById(R.id.tvInfo12);
            tvInfo13 = (TextView) findViewById(R.id.tvInfo13);

            tvInfo14 = (TextView) findViewById(R.id.tvInfo14);
            tvInfo15 = (TextView) findViewById(R.id.tvInfo15);
            tvInfo16 = (TextView) findViewById(R.id.tvInfo16);
            tvInfo17 = (TextView) findViewById(R.id.tvInfo17);
            tvInfo18 = (TextView) findViewById(R.id.tvInfo18);
            tvInfo19 = (TextView) findViewById(R.id.tvInfo19);
            tvInfo20 = (TextView) findViewById(R.id.tvInfo20);
            tvInfo21 = (TextView) findViewById(R.id.tvInfo21);
            //tvInfo22 = (TextView) findViewById(R.id.tvInfo22);
            tvInfo23 = (TextView) findViewById(R.id.tvInfo23);
            tvInfo24 = (TextView) findViewById(R.id.tvInfo24);
            tvInfo25 = (TextView) findViewById(R.id.tvInfo25);
            tvInfo26 = (TextView) findViewById(R.id.tvInfo26);
            tvInfo27 = (TextView) findViewById(R.id.tvInfo27);
            tvInfo28 = (TextView) findViewById(R.id.tvInfo28);
            tvInfo29 = (TextView) findViewById(R.id.tvInfo29);
            tvInfo30 = (TextView) findViewById(R.id.tvInfo30);
            tvInfo31 = (TextView) findViewById(R.id.tvInfo31);
            tvInfo32 = (TextView) findViewById(R.id.tvInfo32);
            tvInfo33 = (TextView) findViewById(R.id.tvInfo33);

            operate01 = (TextView) findViewById(R.id.operate01);
            operate02 = (TextView) findViewById(R.id.operate02);
            operate03 = (TextView) findViewById(R.id.operate03);
            operate04 = (TextView) findViewById(R.id.operate04);
            operate05 = (TextView) findViewById(R.id.operate05);

            ivImage1 = (ImageView) findViewById(R.id.ivImage1);
//            ivImage2 = (ImageView) findViewById(R.id.ivImage2);
            ivImage3 = (ImageView) findViewById(R.id.ivImage3);
            ivImage4 = (ImageView) findViewById(R.id.ivImage4);
            ivImage5 = (ImageView) findViewById(R.id.ivImage5);
            //ivImage6 = (ImageView) findViewById(R.id.ivImage6);
            ivImage7 = (ImageView) findViewById(R.id.ivImage7);
            ivImage8 = (ImageView) findViewById(R.id.ivImage8);
            ivImage9 = (ImageView) findViewById(R.id.ivImage9);
            ivImage10 = (ImageView) findViewById(R.id.ivImage10);
            ivImage11 = (ImageView) findViewById(R.id.ivImage11);

            view1 = (View) findViewById(R.id.view1);
            view2 = (View) findViewById(R.id.view2);
            view3 = (View) findViewById(R.id.view3);
            view4 = (View) findViewById(R.id.view4);
            view5 = (View) findViewById(R.id.view5);
            view6 = (View) findViewById(R.id.view6);
            view7 = (View) findViewById(R.id.view7);
            view8 = (View) findViewById(R.id.view8);
            view9 = (View) findViewById(R.id.view9);

            scMainTopArea = (ScalableLayout) findViewById(R.id.scMainTopArea);
            scStartFinish = (ScalableLayout) findViewById(R.id.scStartFinish);
            scDispInfoArea = (ScalableLayout) findViewById(R.id.scDispInfoArea);

            scSelfMenuPart1 = (ScalableLayout) findViewById(R.id.scSelfMenuPart1);
            scMenuPart1 = (ScalableLayout) findViewById(R.id.scMenuPart1);
            scMenuPart2 = (ScalableLayout) findViewById(R.id.scMenuPart2);

//            scArea6 = (ScalableLayout) findViewById(R.id.scArea6);
            scArea7 = (ScalableLayout) findViewById(R.id.scArea7);
            scNoticeArea = (ScalableLayout) findViewById(R.id.scNoticeArea);

            llCVOInfo = (LinearLayout) findViewById(R.id.llCVOInfo);
            llNearByInfo = (LinearLayout) findViewById(R.id.llNearByInfo);
            llHScrollItemArea = (LinearLayout) findViewById(R.id.llHScrollItemArea);


            scMainTopArea.setVisibility(View.GONE);
            scStartFinish.setVisibility(View.GONE);
            scDispInfoArea.setVisibility(View.GONE);

            llNearByInfo.setVisibility(View.GONE);
            llHScrollItemArea.setVisibility(View.GONE);

            scSelfMenuPart1.setVisibility(View.GONE);
            scMenuPart1.setVisibility(View.GONE);
            scMenuPart2.setVisibility(View.GONE);
//            scArea6.setVisibility(View.GONE);
            scArea7.setVisibility(View.GONE);

            // 메인 사용자 정보
            String htmlString = "";
            String trans_nm = AppSetting.cvoUserInfo.getCOMPANY_NAME();
//            if (trans_nm != null && !trans_nm.equals("")) {
//                trans_nm = " - " + trans_nm;
//            }
//
            // 회사명
            htmlString = AppSetting.cvoUserInfo.getCOMPANY_NAME();
            CommonUtil.with().setHtmlMarqueeText(tvInfo2, htmlString);
            // 사용자이름/직책
            htmlString = "<big><b>" + AppSetting.cvoUserInfo.getNAME() + "</b></big>" +
                    " <small><b>" + AppSetting.cvoUserInfo.getJOBTITLE() + "</b></small> ";
            CommonUtil.with().setHtmlText(tvInfo8, htmlString);
            // 운송회사(숨김처리)
            htmlString = "<small>" + trans_nm + "</small>";
            CommonUtil.with().setHtmlMarqueeText(tvInfo8_1, htmlString);
            //tvInfo32.setText(AppSetting.LOGIN_INFO.getVHCL_NO());

            // 차량번호(코드)
            htmlString = "" +
                    AppSetting.cvoUserInfo.getVEHICLENO() + " " +
                    "<small>(" + AppSetting.cvoUserInfo.getVEHICLECD() + ")</small> " +
                    "";
            CommonUtil.with().setHtmlMarqueeText(tvInfo32, htmlString);

//            // 좌측 메뉴 사용자정보
//            TextView usr_trans_nm = navigationHeader.findViewById(R.id.usr_trans_nm);
//            htmlString = "" +
//                    "" + AppSetting.cvoUserInfo.getVHCL_PCL_CD() + "" +
//                    "";
//            CommonUtil.with().setHtmlMarqueeText(usr_trans_nm, htmlString);
//
//            TextView usr_drv_nm = navigationHeader.findViewById(R.id.usr_drv_nm);
//            usr_drv_nm.setText(AppSetting.cvoUserInfo.getDRV_NM());
//
//            TextView usr_vhcl_no = navigationHeader.findViewById(R.id.usr_vhcl_no);
//            usr_vhcl_no.setText(AppSetting.cvoUserInfo.getVHCL_NO());


            // 앱버전
//            TextView app_ver = navigationHeader.findViewById(R.id.app_ver);
//            app_ver.setText("Version. " + AppSetting.App.VERSION_NAME);
//            tvInfo7.setText("Version. " + AppSetting.App.VERSION_NAME);

            // 현재속도 표시
            //setCurSpeedInfo(0);

            htmlString = "<font color=\"#FFFFFF\">위치 확인중입니다. 잠시만 기다려주세요.</font>";
            CommonUtil.with().setHtmlMarqueeText(tvInfo31, htmlString);
            tvInfo31.setBackgroundColor(Color.parseColor("#ACACAC"));

//            htmlString = "<font color=\"#ACACAC\">- 대기중 -</font>";
//            CommonUtil.with().setHtmlMarqueeText(tvInfo10, htmlString);

//            TextView tvKotlinFunc = (TextView) findViewById(R.id.tvKotlinFunc);
//            //tvKotlinFunc.setText(HelloKt);
//            Hello.formatMessage("abcderg");

            tvWaitInfo = (TextView) findViewById(R.id.tvWaitInfo);
//            tvWaitInfo.startAnimation(animationStart);

            scrollItemList = (ScrollView) findViewById(R.id.scrollItemList);
            llItemList = (LinearLayout) findViewById(R.id.llItemList); // 아이템 리스트

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
     * 현재 속도 표시
     * @param speed
     */
    public void setCurSpeedInfo(int speed) {
        try {
            if(speed <= 10) speed = 0;
            // 속도
            String htmlString = "" +
                    "<big><big><B>" + String.format("%d", speed) + "</B></big></big><BR>" +
                    "<small><small>km/h</small></small>" +
                    "";
            CommonUtil.with().setHtmlText(tvInfo9, htmlString);
            //tvInfo9.setText(String.format("%d", speed));

            if (speed > 60) {
                tvInfo9.setBackgroundResource(R.drawable.bg_fill_red_01_round_10);
            } else if (speed > 0) {
                tvInfo9.setBackgroundResource(R.drawable.bg_fill_blue_01_round_10);
            } else {
                tvInfo9.setBackgroundResource(R.drawable.bg_fill_gray_01_round_10);
            }
        } catch (Exception ex) {
            tvInfo9.setText("-");
            ex.printStackTrace();
        }
    }

    private int getAbsoluteY(View targetView) {
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        return y;
    }

    /**
     * 공지사항 터치시..
     * @param view
     */
    public void clickNotice(View view) {
        try {

            String sHtmlTitle = "<SMALL>" +
                    "" + AppSetting.LAST_WORK_INFO.getNOTICE_TITLE() + "" +
                    " <SMALL><SMALL>- " + AppSetting.LAST_WORK_INFO.getNOTICE_DT() + "</SMALL></SMALL>" +
                    "</SMALL>";
            String sHtmlContent = "<BIG><BIG>" +
                    AppSetting.LAST_WORK_INFO.getNOTICE_CONTENT() +
                    "</BIG></BIG>";
            // 팝업다이얼로그 호출
            UDialog.withHtmlTwoCustom_okCancel(mContext, sHtmlTitle, sHtmlContent, "<BIG><BIG>확인</BIG></BIG>", "취소", View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                @Override
                public void onClick_yes() {
                }
                @Override
                public void onClick_no() {
                    finish();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 버튼클릭 공용 메서드(태크로 구분)
     * @param view
     */
    public void clickActiveButtons(View view) {

        int nTag = Integer.parseInt(view.getTag().toString());
        Log.d(TAG, "nTag :: " + nTag);

        Intent intent = new Intent();

        switch (nTag) {
            case 01:
                // 네비게이션 뷰 오픈
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;

            case 10:
                // 배차내역
                goDriverDispatchList();
                break;

            case 20:
                // 배차일보
                intent = new Intent(getApplicationContext(), DispatchSummaryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_fast, R.anim.no_change);
                break;

            case 30:
                // 주유비관리
                CommonUtil.with().ToastMsg(mContext, "개발중입니다.\n잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
//                intent = new Intent(getApplicationContext(), GasHistoryMngActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_left_fast, R.anim.no_change);
                break;

            case 40:
                // 전자영수증
                CommonUtil.with().ToastMsg(mContext, "개발중입니다.\n잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
                //goDriverAccidentListActivity();
                break;

            case 50:
                // 센터업무
                intent = new Intent(getApplicationContext(), ItemMngListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_fast, R.anim.no_change);
                break;

            case 60:
                // CVO
                intent = new Intent(getApplicationContext(), CvoMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_fast, R.anim.no_change);

                break;

            case 500:
                // 출근보고
                goToTheOfficePop();
                break;

            case 600:
                // 퇴근보고
                double curlat = AppSetting.GPS_MANAGER.GPS_LAT;
                double curlng = AppSetting.GPS_MANAGER.GPS_LNG;
//                if (!(curlat>0 && curlng>0)) {
//                    CommonUtil.with().ToastMsg(mContext, "현위치가 확인 되지 않았습니다.\n잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                leaveTheOfficePop();
                break;

            case 700:
                // 리로딩
                userRefreshScreenButon();
                break;

            default:
                break;
        }
    }


    /**
     * 뷰 세팅..
     */
    private void setActivate() {

        slideLeftDelayTime = 10;
        slideRightDelayTime = 10;
        slideUpDelayTime = 100;
        slideDownDelayTime = 100;


        slideUp(scMainTopArea);

        slideUp(scStartFinish);
        slideUp(llNearByInfo);
        slideUp(llHScrollItemArea);

        slideUp(scNoticeArea);
        slideUp(scDispInfoArea);
//        slideUp(scSelfMenuPart1);
        if (AppSetting.cvoUserInfo.getUSERTYPE().equals(UserType._관리자)) {
            // 관리자메뉴 추가..
            slideUp(scMenuPart2);
        }

//        slideUp(scArea6);
        slideUp(scArea7);
//        llCVOInfo.setVisibility(View.GONE);

        // 자차,용차메뉴 구분..
//        if (AppSetting.cvoUserInfo.getVHCL_TYPE_NM().equals(UserType._자차)) {
//            // 자차
//            llCVOInfo.setVisibility(View.GONE);
//        } else {
//            // 용차
//            slideUp(llCVOInfo);
//        }
/*
        slideRight(tvInfo1);
        slideRight(tvInfo2);

        slideLeft(tvInfo3);
        slideLeft(tvInfo4);
        slideLeft(tvInfo5);

        slideLeft(tvInfo6);
        slideRight(tvInfo7);

        slideLeft(tvInfo8);
        slideRight(tvInfo9);
        slideRight(tvInfo10);

        slideLeft(tvInfo11);
        slideRight(tvInfo12);

        slideDown(ivImage1);
        slideLeft(ivImage2);
        slideLeft(ivImage3);

        slideLeft(ivImage4);
        slideRight(ivImage5);

        slideUp(view1);
        slideUp(view2);
        slideUp(view3);

        slideUp(tvInfo13);
        slideUp(tvInfo14);
        slideUp(tvInfo15);

        // 공지사항/Today
        slideUp(view4);
        slideUp(tvInfo16);
        slideLeft(tvInfo17);
        slideLeft(tvInfo18);
        slideLeft(tvInfo19);

        slideRight(tvInfo20);
        slideRight(tvInfo21);
        slideUp(view5);
        */

        // 지도
        //slideLeft(tvInfo22);
        //slideUpDelayTime = 1800;
        //slideUp(ivImage6);

        // 메뉴
        slideLeftDelayTime = 1800;
        slideRightDelayTime = 1800;
        /*
        slideRight(view6);
        slideRight(ivImage7);
        slideRight(tvInfo23);
        slideRight(tvInfo24);

        slideLeft(view7);
        slideLeft(ivImage8);
        slideLeft(tvInfo25);
        slideLeft(tvInfo26);

        slideRight(view8);
        slideRight(ivImage9);
        slideRight(tvInfo27);
        slideRight(tvInfo28);

        slideLeft(view9);
        slideLeft(ivImage10);
        slideLeft(tvInfo29);
        slideLeft(tvInfo30);
        */

        slideUpDelayTime = 2800;
        //slideUp(scArea6);
        //slideUp(scArea7);
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
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_slow);
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
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_slow);
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



    /**
     * 좌측메뉴 클릭
     * @param view
     */
    public void clickNavMenu(View view){
        Log.d(TAG, "클릭한 태그 : " + view.getTag());
        int nTag = Integer.parseInt(view.getTag().toString());

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if(nTag == 100){
            // 배차내역
            Toast.makeText(this, "개발 진행 중입니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
            //goDriverDispatchList();

        }else if(nTag == 200){
            // 사고보고
            Toast.makeText(this, "개발 진행 중입니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
            //goDriverAccidentListActivity();

        }else if(nTag == 300){
            // 전자전표조회
            Toast.makeText(this, "개발 진행 중입니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
            //goDriverInvoiceListActivity();

        }else if(nTag == 400){
            // 저유소정보
            Toast.makeText(this, "개발 진행 중입니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
            //goDriverPlantInfoActivity();

        }else if(nTag == 500){
            // 공지사항
            Toast.makeText(this, "개발 진행 중입니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 배차조회 이동
     */
    public void goDriverDispatchList() {
        try {
            // 위치정보 미확인시 대기..
            try {

                double dLat = AppSetting.GPS_MANAGER.getGpsLat();
                double dLng = AppSetting.GPS_MANAGER.getGpsLng();
                if (dLat>0 && dLng>0) {
                    Loggers.d("위치확인 완료...");
                } else {
                    handler.obtainMessage(RequesterSession.REQ_OK_MESSAGE, "위치확인중입니다. 잠시만 기다려주세요..").sendToTarget();
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                handler.obtainMessage(RequesterSession.REQ_OK_MESSAGE, "위치확인중입니다. 잠시만 기다려주세요..").sendToTarget();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), DeliveryListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_fast, R.anim.no_change);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 사고보고 이동
     */
    public void goDriverAccidentListActivity() {
        try {
//            Intent intent = new Intent(getApplicationContext(), DriverAccidentListActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_left_fast, R.anim.no_change);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 전자출하전표 이동
     */
    public void goDriverInvoiceListActivity() {
        try {
//            Intent intent = new Intent(getApplicationContext(), DriverInvoiceListActivity.class);
//            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 저유소정보 이동
     */
    public void goDriverPlantInfoActivity() {
        try {
//            Intent intent = new Intent(getApplicationContext(), DriverPlantInfoActivity.class);
//            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 위치정보 서비스 시작
     */
    public void startLocationService(){
        try {
            if (!isLocationServiceRunning()) {
                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                intent.setAction(Svc.ACTION_START_LOCATION_SERVICE);
                startService(intent);
//                Toast.makeText(this, "위치관제 서비스를 시작합니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 위치정보 서비스 종료
     */
    public void stopLocationService(){
        try {
            if (isLocationServiceRunning()) {
                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                intent.setAction(Svc.ACTION_STOP_LOCATION_SERVICE);
                startService(intent);
//                Toast.makeText(this, "위치관제 서비스를 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 위치서비스 리셋하기(종료후 1초뒤 재시작..)
     */
    public void resetLocationService() {
        try {

//            if (((DriverDispatchListActivity) DriverDispatchListActivity.mContext) != null) {
//                Toast.makeText(
//                        ((DriverDispatchListActivity) DriverDispatchListActivity.mContext),
//                        "위치관제 서비스를 재시작합니다.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(mContext, "위치관제 서비스를 재시작합니다.", Toast.LENGTH_SHORT).show();
//            }

            if (isLocationServiceRunning()) {
                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                intent.setAction(Svc.ACTION_STOP_LOCATION_SERVICE);
                startService(intent);
            }

            // 1초뒤에 위치서비스 시작 호출
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 위치정보 서비스 시작
                    startLocationService();
                }
            }, 1000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 화면 리로딩
     */
    public void userRefreshScreenButon(){
        try {
//            // 액티비티 재시작
//            Intent intent = new Intent(getApplicationContext(), DriverMainActivity.class);
//            startActivity(intent);
//            finish();

            // 화면리로딩처리로 변경.. 2022.09.29
            mUserReloadYN = true;
            reloadScreen();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 최종작업상태 조회
     */
    public void getMainData() {
        try {

            Map<String, String> paraMap = new LinkedHashMap<String, String>();

//            paraMap.put("I_DISPATCH_DATE", AppSetting.dispatchDate);
//            paraMap.put("I_DRV_CD", AppSetting.LOGIN_INFO.getDRV_CD());
//            paraMap.put("I_VEHICLE_CD", AppSetting.LOGIN_INFO.getVHCL_PCL_CD());
            paraMap.put("I_MIN_NO", AppSetting.Device.PHONE_NUMBER);
            paraMap.put("I_USER_NO", AppSetting.cvoUserInfo.getGEONUM());
            paraMap.put("I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));


            RequesterSession.with().RestAPIServiceCall(Svc.CVO_MAINDATA_SEL, paraMap, getMainDataCallback);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * REST-API Callback - 최종작업상태 조회
     */
    private Callback getMainDataCallback = new Callback() {

        String mTag = "getMainDataCallback()";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, "---------------------------------------------");
            Loggers.d2(this, mTag + " ---- onFailure() : " + e.getMessage());
            Loggers.d2(this, mTag + " ---- onFailure() : " + e.toString());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Loggers.d2(this, mTag + ", ---- body : " + bodyString);
            Loggers.d2(this, "---------------------------------------------");

            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();

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

                    JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();


                    if (!resultCode.equals("00")) {
                        // 응답은 정상이지만 서비스 결과 오류

                    } else {

                        if (jarrayRecordSet.size() == 0) {
                            // 응답은 정상 - 서비스 결과 없음

                        } else {

                            for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
                                JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
                                Gson gson = new Gson();

                                mLastWorkInfo = gson.fromJson(jobjectStore, LastWorkInfo.class);

                                Log.d(TAG, "lastWorkInfo.toString() : " + mLastWorkInfo.toString());

                            }

                            handler.obtainMessage(CVO_MAINDATA_SEL, getString(R.string.complete)).sendToTarget();

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Exception : " + e.getMessage());
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

    // 메시지팝업 여부(팝업복귀후 onResume 프로세스에서 불필요하게 중복호출되는 로직을 걸러내기위함..)
    private boolean openPopYn = false;
    /**
     * 출근보고 확인 팝업 호출
     */
    public void goToTheOfficePop(){
        try {
            String title = "출근보고";
            String message01 = "";
            String message02 = "<big><big><b>출근보고</b></big></big> 를 하시겠습니까?";

            Intent intent = new Intent(getApplicationContext(), PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName());
            intent.putExtra("REQ_POP_CODE", WorkStatus._출근보고);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "배차내역조회");
            startActivity(intent);

            openPopYn = true; // 화면갱신 자동실행을 막기위해..
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 퇴근보고 확인 팝업
     */
    public void leaveTheOfficePop(){
        try {
            String title = "퇴근보고";
            String message01 = "";
            String message02 = "<big><big><b>퇴근보고</b></big></big> 를 하시겠습니까?";

            Intent intent = new Intent(getApplicationContext(), PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName());
            intent.putExtra("REQ_POP_CODE", WorkStatus._퇴근보고);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            startActivity(intent);

            openPopYn = true; // 화면갱신 자동실행을 막기위해..
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 사운드 재생
     */
    public void playSound() {
        // 사운드 재생
        handler.obtainMessage(PLAY_SOUND_NEXT, "사운드재생").sendToTarget();
    }


    /**
     * 공통팝업 콜백
     */
    public void popUpCallback(String p_req_pop_code) {
        try {

            // 팝업닫기
            ((PopCommonActivity) PopCommonActivity.mContext).FinishWork();

            if(p_req_pop_code.equals(WorkStatus._출근보고)) {

                // 사운드 재생
                playSound();

                // MOB 출근보고
                workReportSav(WorkStatus._출근보고);

            } else if(p_req_pop_code.equals(WorkStatus._퇴근보고)) {

                // 사운드 재생
                playSound();

                // MOB 퇴근보고
                //leaveTheOffice();
                workReportSav(WorkStatus._퇴근보고);

            } else if(p_req_pop_code.equals(WorkStatus._납품이미지촬영)) {

                // 카메라 호출..
                runCamera();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 업무보고(출근보고/퇴근보고)
     */
    public String curWorkStatus = "";
    public void workReportSav(String workStatus) {

        try {
            curWorkStatus = workStatus;

            Map<String, String> paraMap = new LinkedHashMap<String, String>();

//            paraMap.put("I_DRV_CD", AppSetting.LOGIN_INFO.getDRV_CD());
//            paraMap.put("I_VEHICLE_CD", AppSetting.LOGIN_INFO.getVHCL_PCL_CD());
            paraMap.put("I_MIN_NO", AppSetting.Device.PHONE_NUMBER);
            paraMap.put("I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));
            paraMap.put("I_WORK_TYPE", workStatus);
            // 업무유형(10:출근, 90:퇴근)

            RequesterSession.with().RestAPIServiceCall(Svc.CVO_WORKSTAT_SAV, paraMap, goToTheOfficeCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * REST-API Callback - 출근보고
     */
    private Callback goToTheOfficeCallback = new Callback() {

        String mTag = "checkLoginCallback()";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, "---------------------------------------------");
            Loggers.d2(this, mTag + " ---- onFailure() : " + e.getMessage());
            Loggers.d2(this, mTag + " ---- onFailure() : " + e.toString());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Loggers.d2(this, mTag + ", ---- body : " + bodyString);
            Loggers.d2(this, "---------------------------------------------");

            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, mTag + " ---- onResponse : " + response.code());

            if (response.code() == 200) {

                try {
                    String strJsonOutput = response.body().string();

                    Loggers.d2(this, mTag + " ---- 수신데이타 : " + strJsonOutput);

                    handler.obtainMessage(CVO_WORKSTAT_SAV, getString(R.string.complete)).sendToTarget();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Exception : " + e.getMessage());
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






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "서비스를 이용하려면 위치정보 권한이 있어야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 위치서비스 가동여부 확인
     * @return
     */
    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                Log.d(TAG, "***>>> LocationService.class.getName() : " + LocationService.class.getName());
                Log.d(TAG, "***>>> service.service.getClassName() : " + service.service.getClassName());
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    Log.d(TAG, "***>>> service.foreground : " + service.foreground);
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    /**
     * 현재 위치정보 화면출력
     */
    public void updateLocationInfo(){

        try {
            // 주소정보
            String addressInfo = AppSetting.GPS_MANAGER.locationInfo.getI_ADDRESS_INFO(); // CommonUtil.with().getAddressString(getApplicationContext(), AppSetting.GPS_MANAGER.getGpsLat(), AppSetting.GPS_MANAGER.getGpsLng());
            addressInfo += " <small>- " + CommonUtil.with().nowMM_DD_HH_MM_SS() + "</small>";
            CommonUtil.with().setHtmlMarqueeText(tvInfo31, addressInfo);
            tvInfo31.setBackgroundColor(Color.parseColor("#5C6BC0"));

//            // 보고시간
//            String htmlString = CommonUtil.with().nowMM_DD_HH_MM_SS();
//            tvInfo10.setText(htmlString);

            // 속도표시
            setCurSpeedInfo(Integer.parseInt(AppSetting.GPS_MANAGER.locationInfo.getI_SPEED()));

            showProgress(false);

            if (!bIsUpdateDispatchInfo) { // 아직 업데이트 하지않았다면..초기1회로 해준다..이후 새로고침에 따라 처리됨..
                // 배차(배송)정보 업데이트 - 좌우스크롤영역
                updateDispatchInfo();
            }

        }catch(Exception e){
            Log.d(TAG, "Eception : " + e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 위치서비스 종료
        stopLocationService();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }


    public boolean isNullLastWorkInfo(){

        boolean isNull = false;

        if( AppSetting.LAST_WORK_INFO == null ){
            Log.d(TAG, "isNullLastWorkInfo() - AppSetting.LAST_WORK_INFO == null");
            isNull = true;
        }else{
            Log.d(TAG, "isNullLastWorkInfo() - AppSetting.LAST_WORK_INFO != null");
            isNull = false;
        }
        //return isNull;

        return (AppSetting.LAST_WORK_INFO == null);
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
     * 출근/퇴근 버튼 (상태)변경
     */
    public void setTodayButton(){
        try {

            // 출근보고
            if( AppSetting.todayStatus.equals(TodayStatus._출근) ){

                Log.d(TAG, "setTodayButton() - 출근");

                // 출근시간
                String today_start_dtm = AppSetting.LAST_WORK_INFO.getTODAY_START_DTM();
                if( today_start_dtm.length() == 11 ){
                    today_start_dtm = "\n" + today_start_dtm.substring(today_start_dtm.length()-5);
                }else{
                    today_start_dtm = "";
                }

                // 출근하기 터치 버튼 비활성화, 배경변경
                view6.setEnabled(false);
                view6.setBackgroundResource(R.drawable.selector_style_round_bg_04_left);

                // 출근하기 보조 이미지 변경
                ivImage4.setImageResource(R.drawable.arrow_white_01);

                // 출근하기 버튼 텍스트 11
                tvInfo11.setVisibility(View.GONE);
                tvInfo12.setTextColor(Color.parseColor("#FFFFFF"));
                tvInfo11.setText("");

                // 출근하기 버튼 텍스트 11_1
                tvInfo11_1.setVisibility(View.VISIBLE);
                tvInfo11_1.setText("업무중 - ");

                // 출근하기 버튼 텍스트 11_2
                tvInfo11_2.setVisibility(View.VISIBLE);
                tvInfo11_2.setText("시작" + today_start_dtm);


                // 퇴근하기 터치 버튼 활성화, 배경변경
                view5.setEnabled(true);

                // 퇴근하기 보조 이미지 변경
                ivImage5.setImageResource(R.drawable.stop_blue_01);

                // 퇴근하기 버튼 텍스트
                tvInfo12.setVisibility(View.VISIBLE);
                tvInfo12.setTextColor(Color.parseColor("#2a5a99"));
                tvInfo12.setText("종료");

                // 위치관제 시작
                startLocationService();

            // 퇴근
            }else if( AppSetting.todayStatus.equals(TodayStatus._퇴근) ){

                Log.d(TAG, "setTodayButton() - 퇴근");

                // 출근하기 터치 버튼 활성화, 배경변경
                view6.setEnabled(true);
                view6.setBackgroundResource(R.drawable.selector_style_round_bg_12_left);

                // 출근하기 보조 이미지 변경
                ivImage4.setImageResource(R.drawable.arrow_blue_01);

                // 출근하기 버튼 텍스트 11
                tvInfo11.setVisibility(View.VISIBLE);
                tvInfo12.setTextColor(Color.parseColor("#2a5a99"));
                tvInfo11.setText("시작");

                // 출근하기 버튼 텍스트 11_1
                tvInfo11_1.setVisibility(View.GONE);
                tvInfo11_1.setText("");

                // 출근하기 버튼 텍스트 11_2
                tvInfo11_2.setVisibility(View.GONE);
                tvInfo11_2.setText("");


                // 퇴근하기 터치 버튼 비활성화, 배경변경
                view5.setEnabled(false);

                // 퇴근하기 보조 이미지 변경
                ivImage5.setImageResource(R.drawable.stop_gray_01);

                // 퇴근하기 버튼 텍스트
                tvInfo12.setVisibility(View.VISIBLE);
                tvInfo12.setTextColor(Color.parseColor("#bfbfbf"));
                tvInfo12.setText("종료");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 메인 화면 데이터 세팅
     */
    public void setMainData(){
        try {
            Log.d(TAG, "setMainData() - Start");

            String htmlString = "";

            // 좌측상단 날짜 (일자)
            tvInfo3.setText(CommonUtil.with().nowDay());

            // 좌측상단 날짜 (월)
            tvInfo4.setText(CommonUtil.with().getMonthString());

            // 좌측상단 날짜 (요일)
            tvInfo5.setText(CommonUtil.with().getDayString());

            // 좌측상단 로그인 시간
            tvInfo6.setText(AppSetting.LAST_WORK_INFO.getLAST_LOGIN_DTM() + " Login.");

            // 속도표시
            setCurSpeedInfo(0);

            // 공지사항
            if( !isNullLastWorkInfo() ) {
                String notice_title = AppSetting.LAST_WORK_INFO.getNOTICE_TITLE();
                if( notice_title != null && !notice_title.equals("") ) {
                    scNoticeArea.setVisibility(View.VISIBLE);
                    //tvInfo14.setText(notice_title);
                    String sHtmlTitle = "" +
                            "" + AppSetting.LAST_WORK_INFO.getNOTICE_TITLE() + "" +
                            " <SMALL><B>- " + AppSetting.LAST_WORK_INFO.getNOTICE_DT() + "</B></SMALL>" +
                            "";
                    String sHtmlContent = "<BIG><BIG>" +
                            AppSetting.LAST_WORK_INFO.getNOTICE_CONTENT() +
                            "</BIG></BIG>";
                    CommonUtil.with().setHtmlMarqueeText(tvInfo14, sHtmlTitle);

    //            }else{
    //                scNoticeArea.setVisibility(View.GONE);
                }
            }

            // 배차 카운트
            tvInfo17.setText("전체\n" + AppSetting.LAST_WORK_INFO.getTMS_INFO_01());
            tvInfo18.setText("현재\n" + AppSetting.LAST_WORK_INFO.getTMS_INFO_02());
            tvInfo19.setText("완료\n" + AppSetting.LAST_WORK_INFO.getTMS_INFO_03());
            tvInfo20.setText("내일예정\n" + AppSetting.LAST_WORK_INFO.getTMS_INFO_04());
            tvInfo21.setText("당월실적\n" + AppSetting.LAST_WORK_INFO.getTMS_INFO_05());

            // 업무버튼별 정보
            tvInfo24.setText(AppSetting.LAST_WORK_INFO.getMENU_SUBTITLE_01());
            tvInfo26.setText(AppSetting.LAST_WORK_INFO.getMENU_SUBTITLE_02());
            tvInfo28.setText(AppSetting.LAST_WORK_INFO.getMENU_SUBTITLE_03());
            tvInfo30.setText(AppSetting.LAST_WORK_INFO.getMENU_SUBTITLE_04());

            // 운행현황
//            operate01.setText("3분정차\n" + AppSetting.LAST_WORK_INFO.getCVO_INFO_01());
//            operate02.setText("과속\n" + AppSetting.LAST_WORK_INFO.getCVO_INFO_02());
//            operate03.setText("정시도착\n" + AppSetting.LAST_WORK_INFO.getCVO_INFO_03());
//            operate04.setText("이동시간\n" + AppSetting.LAST_WORK_INFO.getCVO_INFO_04());
//            operate05.setText("이동거리\n" + AppSetting.LAST_WORK_INFO.getCVO_INFO_05());

            // 버튼 부가정보 표시 - 최종 배차정보 표시
            String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
            tvDispatchInfo2 = (TextView) findViewById(R.id.tvDispatchInfo2);
            if (DDATE.length()>0) {
                htmlString = "<small>최근배송 : </small>" + DDATE;
            } else {
                htmlString = "<small>최근배송 : </small>없음";
            }
            CommonUtil.with().setHtmlMarqueeText(tvDispatchInfo2, htmlString);

            // 마지막 업데이트
            tvInfo33.setText("- Last Update. " + CommonUtil.with().nowMM_DD_HH_MM() + " -");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 브로드캐스트 리시버정의 - GPS위치정보 수집결과 반영
     */
    public static final int ADDINFO_GPS = 30003;
    BroadcastReceiver traceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String gpsMsg = intent.getStringExtra(BroadCastDomain.BROADCAST_TRACE_GPS_STATUS);

//            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");

            if (gpsMsg.equals(BroadCastDomain.REPORT_CURRENT_LOCATION)) {

                //Loggers.d2(this, "----- [브로드캐스트수신 - GPS] " + gpsMsg);

                // 절전모드 체크추가 - 2022.11.02
                checkPowerMode();

                // 현재 위치정보 표시 업데이트
                updateLocationInfo();

                // 배차내역조회 화면도 열려있다면 위치정보업데이트 셋..호출
                if (((DeliveryListActivity) DeliveryListActivity.mContext) != null) {
                    ((DeliveryListActivity) DeliveryListActivity.mContext).updateLocationInfoSet();
                }
//                // 자차,배차내역조회 화면도 열려있다면 위치정보업데이트 셋..호출
//                if (((SelfDriverDispatchListActivity) SelfDriverDispatchListActivity.mContext) != null) {
//                    ((SelfDriverDispatchListActivity) SelfDriverDispatchListActivity.mContext).updateLocationInfoSet();
//                }

            }

        }
    };

    /**
     * 배차(배송)정보 업데이트 - 좌우스크롤영역..
     *  - 위치가있고
     *  - 배차선택을했을경우에 호출하여 업데이트 한다..
     */
    private boolean bIsUpdateDispatchInfo = false;
    public void updateDispatchInfo() {

        double d1 = AppSetting.GPS_MANAGER.getGpsLat();
        double d2 = AppSetting.GPS_MANAGER.getGpsLng();
        if ((d1>0) && (d2>0)) { // 위치정보가 있다면...
            String htmlString = "";
            TextView tvWaitInfoTitle = findViewById(R.id.tvWaitInfoTitle);

            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
            if (CVO_DISPATCH_ID.length()>0) { // 최종배차정보가 있을경우 요청...
                // 배송확정이후...

                htmlString = "- 배송이 시작되었습니다.(거리순 표시) -";
                CommonUtil.with().setHtmlText(tvWaitInfoTitle, htmlString);
                tvWaitInfoTitle.setBackgroundColor(Color.parseColor("#182671"));
                requestCvoDeliveryList(CVO_DISPATCH_ID);
            } else {
                // 배송확정이전...

                // 리스트 지우기...
                llHScrollItemArea.removeAllViews();

                htmlString = "- 잠시만 기다려주세요 -";
                CommonUtil.with().setHtmlText(tvWaitInfoTitle, htmlString);
                tvWaitInfoTitle.setBackgroundColor(Color.parseColor("#9C9C9C"));
                htmlString = "" +
//                        "<small><small><small><small><small><small>&nbsp;</small></small></small></small></small></small><BR>" +
//                        "<small><small><small><small><small><small>&nbsp;</small></small></small></small></small></small><BR>" +
                        "<Big><B><U>배차내역</U></B></Big>확인중입니다..";
                tvWaitInfo = (TextView) findViewById(R.id.tvWaitInfo);
                CommonUtil.with().setHtmlText(tvWaitInfo, htmlString);

            }
        }
    }

    /**
     * CVO - 배송리스트 조회(배송확정이후)
     * @param dispatchId
     */
    public void requestCvoDeliveryList(String dispatchId) {
        try {

            bIsUpdateDispatchInfo = true;

//            tvWaitInfo.startAnimation(animationStart);
            tvWaitInfo.setVisibility(View.VISIBLE);

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_DISPATCH_ID", dispatchId);
            paraMap.put("@I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("@I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_DELIVERY_SEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoDeliveryListCallback,
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
    private Callback requestCvoDeliveryListCallback = new Callback() {
        String tag = "requestCvoDeliveryListCallback";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, tag + "---- onFailure:" + e.toString());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();
                return;
            }
            Loggers.d2(this, tag + ", ---- onResponse");

            String strJsonOutput = response.body().string();

            Loggers.d2(this, tag +",수신데이타:" + strJsonOutput);

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

            handler.obtainMessage(CVO_DELIVERY_SEL, "").sendToTarget();
        }
    };

    /**
     * ERP 배송목록 조회
     */
    public void requestErpDispatchSel() {

        try {

            showProgress(true);


            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_DDATE", "2023-09-27");
//            paraMap.put("@I_EPCODE", "0004");
//            paraMap.put("@I_DDATE", search_date_sch); // CommonUtil.with().nowYYYY_MM_DD());
            paraMap.put("@I_EPCODE", AppSetting.cvoUserInfo.getEMPNO());
            paraMap.put("I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));
            paraMap.put("@I_INPUT_USER", AppSetting.cvoUserInfo.getGEONUM());

            RequesterSession.with().RestAPIServiceCall(Svc.ERP_DISPATCH_SEL, paraMap, requestErpDispatchSelCallback);

        } catch (Exception ex) {
            ex.printStackTrace();
            showProgress(false);
        }

    }

    /**
     * ERP배송목록 조회 콜백처리
     */
    private Callback requestErpDispatchSelCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, "requestErpDispatchSelCallback, ---- onFailure:" + e.toString());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();

                return;
            }
            try {
                Loggers.d2(this, "requestErpDispatchSelCallback, ---- onResponse");

                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestErpDispatchSelCallback,수신데이타:" + strJsonOutput);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject recvObj = jelement.getAsJsonObject();

                JsonArray jarrayShif = recvObj.getAsJsonArray("RecordSet");

                Loggers.d("배송지 데이타 초기화 - 배송지목록");
                DeliveryListManager.getInstance().clear();

                if (jarrayShif.size() == 0) {

                    handler.obtainMessage(RequesterSession.REQ_OK_MESSAGE, "조회된 건수가 없습니다.").sendToTarget();
                } else {

                    for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                        JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                        Gson gson = new Gson();
                        DeliveryListInfo info = gson.fromJson(jobject, DeliveryListInfo.class);

                        // 배송지 정보 추가
                        DeliveryListManager.getInstance().add(info);
                    }
                }

                Loggers.d2(this, "[callBack]건수=" + DeliveryListManager.getInstance().values().size());

                deliveryListItems = DeliveryListManager.getInstance().values();
                Loggers.d("[callBack]리스트어댑터 건수 = " + deliveryListItems.size());

                handler.obtainMessage(ERP_DISPATCH_SEL, "").sendToTarget();
            } catch (Exception ex) {
                ex.printStackTrace();
                // 배송목록 조회요청작업 진행여부 - 풀기
                handler.obtainMessage(RequesterSession.REQ_NOK, "").sendToTarget();
            }
        }
    };

    /**
     * CVO 배송시작(배송확정 호출)
     */
    public void requestCvoDeliveryStart() {
        try {
            showProgress(true);

            // 자동출근보고 및 관제시작
            ((DriverMainActivity) DriverMainActivity.mContext).workReportSav(WorkStatus._출근보고);
            // CVO 배차정보 확인
            String DDATE = CommonUtil.with().nowYYYY_MM_DD(); // 배송일자는 당일로 설정한다.(이전주문 당일배송가능-아이사랑,준비편)
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
            // 배송순서정보 추출..
            JSONArray jArray = new JSONArray();
            JSONObject jObj = new JSONObject();
            try {
                for (int i = 0; i < deliveryListItems.size(); i++) {
                    DeliveryListInfo item = new DeliveryListInfo();
                    item = deliveryListItems.get(i);
                    JSONObject sObject = new JSONObject();//배열 내에 들어갈 json

                    sObject.put("DISPATCH_ID", CVO_DISPATCH_ID);
                    sObject.put("COMPANYCD", Svc.CVO_COMPANYCD);
                    sObject.put("DDATE", DDATE);
                    sObject.put("SHIFT_NO", item.getWSD_NUM());
                    sObject.put("ROUTE_SEQ", String.format("%d", i+1));
                    // 아이사랑은 배차가아직없다.
//                    sObject.put("EPCODE", item.getEPCODE());
//                    sObject.put("EPNAME", item.getEPNAME());
//                    sObject.put("EPPHONE", item.getEPPHONE());
                    // 현재사용자로 세팅
                    sObject.put("EPCODE", AppSetting.cvoUserInfo.getEMPNO());
                    sObject.put("EPNAME", AppSetting.cvoUserInfo.getNAME());
                    sObject.put("EPPHONE", AppSetting.cvoUserInfo.getDEVICENO());

                    sObject.put("CLCODE", item.getCLCODE());
                    sObject.put("CLNAME", item.getCLNAME());
                    sObject.put("LAT", item.getLAT());
                    sObject.put("LON", item.getLON());
                    sObject.put("ADDRESS", item.getADDRESS());
                    sObject.put("PIC_NAME", item.getCLCEO());
                    sObject.put("PIC_TEL", item.getCLPHONE());
                    sObject.put("CLSAUPNO", item.getCLSAUPNO());
                    sObject.put("CLCORPNO", item.getCLCORPNO());
                    sObject.put("CLCEO", item.getCLCEO());
                    sObject.put("CLUPTAE", item.getCLUPTAE());
                    sObject.put("CLJONG", item.getCLJONG());
                    sObject.put("ITEM_INFO", item.getITEM_INFO());
                    sObject.put("ITEM_CNT", item.getITEM_CNT());
                    sObject.put("ORDER_AMT", item.getORDER_AMT());
                    sObject.put("EXPECT_TIME", "");
                    sObject.put("RESULT_TIME", "");
                    sObject.put("ETC_DATA", item.getETC_DATA());


                    jArray.put(sObject);
                }
                jObj.put("recordSet", jArray);
            } catch (JSONException e) {
                Loggers.e("Error " + e.toString(), e);
            }

//            Loggers.d("[변경된 배차순서]--------------------------------------------");
//            Loggers.d(jObj.toString());
//            Loggers.d("[변경된 배차순서]--------------------------------------------");
            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_JSON_DATA", jObj.toString());
            paraMap.put("@I_DATA_SIZE", String.format("%d", deliveryListItems.size()));
            paraMap.put("@I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("@I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_DELIVERY_START),
                    "POST",
                    "",
                    paraMap,
                    requestCvoDeliveryStartCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - CVO 배송시작
     */
    private Callback requestCvoDeliveryStartCallback = new Callback() {
        String tag = "requestCvoDeliveryStartCallback";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, tag + "---- onFailure:" + e.toString());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();
                return;
            }
            Loggers.d2(this, tag + ", ---- onResponse");

            String strJsonOutput = response.body().string();

            Loggers.d2(this, tag +",수신데이타:" + strJsonOutput);

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
            deliveryListItems = DeliveryListManager.getInstance().values();

            handler.obtainMessage(CVO_DELIVERY_START, "").sendToTarget();
        }
    };





    /**
     * 팝업세팅 - 납품이미지 촬영시도..
     * @param dispatchId
     * @param clCode
     */
    public void popTryTakePicture(String dispatchId, String clCode) {
        try {

            String title = "납품사진 촬영";
            String message01 = "";
            String message02 = "<big><big><b>사진촬영</b></big></big> 또는 <big><big><b>앨범</b></big></big>을 선택해주세요." +
                    "";

            Intent intent = new Intent(mContext, PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName()); //  현재 엑티비티명 확인
            intent.putExtra("REQ_POP_CODE", WorkStatus._납품이미지촬영);
            intent.putExtra("TITLE", title);
            intent.putExtra("BUTTON_TITLE_01", "사진촬영");
            intent.putExtra("BUTTON_TITLE_02", "앨범선택");
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "납품이미지촬영");
            mContext.startActivity(intent);

            openPopYn = true; // 화면갱신 자동실행을 막기위해..

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 카메라 호출...
     */
    public void runCamera() {
        // 사진촬영
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 1.디렉토리 생성..
            String savePath = Environment.getExternalStorageDirectory() + Svc.CAMERA_IMG_PATH;
            CommonUtil.with().makeDir(savePath);
            // 2.촬영파일 경로 생성..
            mTempFile = new File(savePath, "/" + mDeliveryInfo.getCLCODE() + "_" + CommonUtil.getSystemTime() + ".jpg");
            Loggers.d("FilePath=" + mTempFile.getPath());
            // 3.카메라 준비..
            mImageCaptureUri = FileProvider.getUriForFile(mContext, "com.gzonesoft.sg415", mTempFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            // 4.카메라 호출..
            startActivityForResult(takePictureIntent, ACT_TAKE_PIC);
        }
    }

    public void runGallery() {

        // 팝업닫기...
        ((PopCommonActivity) PopCommonActivity.mContext).FinishWork();

        // 앨범불러오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    /**
     * 카메라 사진 찍기
     */
    DeliveryListInfo mDeliveryInfo = null;
    public void takePicture(final String dispatchId, final String clCode) {
        try {

            // 거래처 코드 저장..
            mDeliveryInfo = new DeliveryListInfo();
            mDeliveryInfo.setDISPATCH_ID(dispatchId);
            mDeliveryInfo.setCLCODE(clCode);

            popTryTakePicture(dispatchId, clCode);

//            UDialog.withTwoCustom_okCancel(mContext, Svc.APP_NAME, "사진촬영 또는 앨범을 선택해주세요.", "사진촬영", "앨범선택", View.VISIBLE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//                @Override
//                public void onClick_yes() {
//                    // 사진촬영
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        // 1.디렉토리 생성..
//                        String savePath = Environment.getExternalStorageDirectory() + Svc.CAMERA_IMG_PATH;
//                        CommonUtil.with().makeDir(savePath);
//                        // 2.촬영파일 경로 생성..
//                        mTempFile = new File(savePath, "/" + clCode + "_" + CommonUtil.getSystemTime() + ".jpg");
//                        Loggers.d("FilePath=" + mTempFile.getPath());
//                        // 3.카메라 준비..
//                        mImageCaptureUri = FileProvider.getUriForFile(mContext, "com.gzonesoft.sg415", mTempFile);
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                        // 4.카메라 호출..
//                        startActivityForResult(takePictureIntent, ACT_TAKE_PIC);
//                    }
//                }
//
//                @Override
//                public void onClick_no() {
//                    // 앨범불러오기
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    startActivityForResult(intent, PICK_FROM_GALLERY);
//                }
//            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 카메라 촬영 또는 앨범이미지 선택후 결과이벤트 수신부..
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ACT_TAKE_PIC) {
                if (mTempFile != null) {
                    Loggers.d("사진추가1....");
                    isCamera = true;
//                    ImageResizeUtils.resizeFile(mTempFile, mTempFile, 1280, isCamera);

                    scMainTopArea.setDrawingCacheEnabled(true);
                    Bitmap bmpLocationInfo = scMainTopArea.getDrawingCache();
                    ImageResizeUtils.attachImage(mTempFile, mTempFile, 1280, isCamera, bmpLocationInfo);
                }
            } else if (requestCode == PICK_FROM_GALLERY) {

                Uri photoUri = data.getData();

                Cursor cursor = null;
                try {
                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = { MediaStore.Images.Media.DATA };
                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);
                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    mTempFile = new File(cursor.getString(column_index));
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (mTempFile != null) {
                    isCamera = true;
                    ImageResizeUtils.resizeFile(mTempFile, mTempFile, 1280, isCamera);
                }
            }

            // 파일업로드 시도...
            saveFileWork();
        }
    }

    /**
     * 첨부서류들을 저장하는 작업
     */
    public void saveFileWork() {

        mUploadWorkFiles.clear();
        mUploadDocType.clear();
        try {
            // 업로드 대상 확인..
            mUploadWorkFiles.add(mTempFile);
            mUploadDocType.add("300"); // 납품이미지
//            mUploadDocType.add("310"); // 서명이미지(추후개발)

            if (mUploadWorkFiles.size() > 0) {

                // 업로드 파일정보 관리변수 초기화
                uploadFileResultInfos.clear();
                // 업로드 요청..
                mLastWorkIndex = 0;
                // 업로드이후 콜백에서 다음을 확인후 반복 호출된다. 그래서 시작(초기)인덱스만 주어서 호출한다.
                uploadImage(mUploadWorkFiles.get(mLastWorkIndex).getPath(), mUploadDocType.get(mLastWorkIndex));
            } else {
                // 업로드할 파일이 없다면..
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 서버에 최종적으로 저장될 파일명 생성
     * @param dispatchId
     * @param clCode
     * @param docType
     * @return
     */
    private String getSaveFileName(String dispatchId, String clCode, String docType) {
        String nowHHMMSS = CommonUtil.with().nowHHMMSS();

        return Svc.CVO_COMPANYCD + "_" +
                "" + dispatchId + "_" +
                "" + clCode + "_" +
                "" + AppSetting.cvoUserInfo.getEMPNO() + "_" +
                "" + docType + "_" +
                "" + nowHHMMSS;
    }

    /**
     * 파일 업로드 시도요청
     *
     * @param FileUrl
     */
    private void uploadImage(String FileUrl, String docType) {
        try {
//            Loggers.d("파일 업로드 시도요청[0], FileUrl = " + FileUrl);
//            Loggers.d("파일 업로드 시도요청[1], FileUrl = " + Environment.getExternalStorageDirectory().getAbsolutePath() + FileUrl);

            //CommonUtil.with().ToastMsg(mContext, "전송작업중입니다. 통신/네트워크 상황에 따라 다소 지연시간이 생길수 있습니다.\n잠시만 기다려주세요...", Toast.LENGTH_SHORT).show();
            showProgress(true);

            // 서명이미지 파일업로드관련.
            File sourceFile = new File(FileUrl);

            // 서버요청 준비..
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", FileUrl, RequestBody.create(MultipartBody.FORM, sourceFile))
                    .build();

            // 서버 서비스 호출
            String uploadURL = Svc.IMG_UPLOAD_URL
                    + Svc.FILE_TYPE_DELIVERY + "/"
                    + Svc.FILE_FORMAT_JPG + "/false/"
                    + getSaveFileName(
                            mDeliveryInfo.getDISPATCH_ID(),
                            mDeliveryInfo.getCLCODE(),
                            docType);

//            RequesterSession.with().commServiceCall(
//                    requestBody,
//                    uploadURL,
//                    uploadImageCallback);

            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoCommServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER()),
                    "POST",
                    uploadURL,
                    requestBody,
                    uploadImageCallback,
                    keyMap
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * REST-API Callback - 파일 업로드 시도요청, 콜백
     */
    private Callback uploadImageCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "uploadImageCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "uploadImageCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "uploadImageCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "uploadImageCallback, 수신데이타:" + strJsonOutput);

                showProgress(false);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject jObject = jelement.getAsJsonObject();

                String ResultCode = jObject.get("ResultCode").getAsString();
                String ResultMsg = jObject.get("ResultMsg").getAsString();
                String RecordCount = jObject.get("FileCount").getAsString();
                JsonArray jarrayRecordSet = jObject.get("fileDetails").getAsJsonArray();

                if (!ResultCode.equals("00")) {
                    // 요청 실패
                } else {
                    // 요청 성공
                    if (jarrayRecordSet.size() < 0) {
                        Loggers.d("[uploadImageCallback] -- " + getString(R.string.api_has_nodata));
                    } else {
                        // 결과 있음
                        JsonObject jobjectStore = jarrayRecordSet.get(0).getAsJsonObject();
                        Gson gson = new Gson();
                        UploadFileInfo info = gson.fromJson(jobjectStore, UploadFileInfo.class);
                        // 원본데이타 추가..
                        uploadFileResultInfos.add(info);

                        handler.obtainMessage(NEXT_UPLOAD_WORK, "").sendToTarget();

                    }
                }


            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }
        }
    };

    /**
     * 이미지 상세보기(웹뷰활용)
     * @param dispatchId
     * @param clCode
     */
    public void viewItemImage(String dispatchId, String clCode) {
        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
        intent.putExtra("MOVE_TYPE", CommType._납품사진이미지);
        intent.putExtra("DISPATCH_ID", dispatchId);
        intent.putExtra("CLCOCE", clCode);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);

        openPopYn = true; // 화면갱신 자동실행을 막기위해..
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

            showProgress(true);

            llItemList.removeAllViews();

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_DDATE", search_date_sch);
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_DEVICENO", AppSetting.cvoUserInfo.getDEVICENO());
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

}

