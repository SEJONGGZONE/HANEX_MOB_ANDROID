package com.gzonesoft.sg623.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.kt.cookzzang.KakaoService;
import com.gzonesoft.sg623.DriverMainActivity;
import com.gzonesoft.sg623.Layout.ClientCustTabLayout;
import com.gzonesoft.sg623.Layout.ClientImageTabLayout;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.ClientCustInfo;
import com.gzonesoft.sg623.data.CvoNowPosition;
import com.gzonesoft.sg623.data.CvoSpotInfo;
import com.gzonesoft.sg623.data.DeliveryImageInfo;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.data.UploadFileInfo;
import com.gzonesoft.sg623.domain.BroadCastDomain;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.domain.MarkerType;
import com.gzonesoft.sg623.domain.TodayStatus;
import com.gzonesoft.sg623.domain.UserType;
import com.gzonesoft.sg623.domain.WorkStatus;
import com.gzonesoft.sg623.model.ClientCustManager;
import com.gzonesoft.sg623.model.CvoNowPositionListManager;
import com.gzonesoft.sg623.pop.PopCommonActivity;
import com.gzonesoft.sg623.pop.PopDetailVehicleActivity;
import com.gzonesoft.sg623.service.LocationService;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.FlowLayout;
import com.gzonesoft.sg623.util.ImageResizeUtils;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.CameraUpdateParams;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.skt.Tmap.TMapTapi;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CvoMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = this.getClass().getSimpleName();;

    public static Context mContext = null;

    // 애니메이션 효과
    Animation animationStart = null;
    Animation animationStartSpeed = null;
    Animation animationStartSlow = null;
    Animation animationStop = null;

    public int mBoundDistance = 5; // 반경거리(km) - 기본값 5km
    public double mCurZoomLevel = 0; // 줌레벨
    public ClientCustInfo mCurCust = new ClientCustInfo();

    private AutoCompleteTextView actvKeyword;
    private Button btnDeleteKeyword;
    //
    private Button btnCenterStart, btnMapType01, btnMapType02, btnFoldYn, btnCamera, btnNavi;
    private FrameLayout clientSpotlightFL;

    private TextView tvInfo31, tvTitleInfo;
    private ScalableLayout scEmptyArea, scRecentCustList, scMenuTop, scMenuLeftTop, scMenuLeftBottom, scMiddleArea, scDummy; //scMenuRightTop, scMenuRightBottom, scMenuRightAdd;

    private LinearLayout llNearByInfo, llHScrollItemArea;
    private HorizontalScrollView scHScrollView;
    private Button btnMethod01, btnMethod02, btnMethod03, btnMethod04;

    // NaverMap
    private MapView mMapView;
    private static NaverMap mNaverMap;

    private static final int LOCATION_REQUEST_INTERVAL = 1000;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // 현재위치 저장..
    public LatLng mCurrentCoord = null;
    public LatLng mLastCoord = null;
    public LatLngBounds mCurrentBounds = null;
    private boolean locationEnabled;
    private boolean waiting;

    public boolean mFirstYn = true;

    public static String soundId;

    public ArrayList<Marker> arrCustMarkers = new ArrayList<Marker>();
    public ArrayList<LatLng> mCustPoints = new ArrayList<LatLng>();
    public ArrayList<Marker> arrVehicleMarkers = new ArrayList<Marker>();
    public ArrayList<LatLng> mVehiclePoints = new ArrayList<LatLng>();

    // 마커이미지 레이아웃뷰...
    private FrameLayout fl_cust_marker, fl_cust_marker_dept, fl_source_marker, fl_order_marker, fl_truck_marker;
    private LinearLayout ll_source_marker, ll_order_marker, ll_order_dispatch_y, ll_order_dispatch_n, llTruckInfo, ll_select_start_date;
    private TextView tvCustTitle, tvCustSubTitle, tvImageCnt, tvSourceTitle, tvSourceSubTitle, tvOrderTitle_y, tvOrderTitle_n, tvOrderSubTitle, tv_order_dispatch_n_tag, tv_order_dispatch_y_tag;
    private TextView tvTruckTitle1, tvTruckTitle2, tvTruckSubTitle, tvSelectCustName, tvDistanceMe;
    private ImageView ivSourceMarkerPoint, ivTruckPoint, iv_order_marker_point;
    // 플로팅 버튼들...
    private Button btnFloat01, btnFloat02, btnFloat03, btnSpotlight;


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

    // 납품이미지 관련..
    private ArrayList<DeliveryImageInfo> mDeliveryImageInfos = new ArrayList<>();
    // 센터정보 관련..
    private ArrayList<CvoSpotInfo> mCvoSpotInfos = new ArrayList<>();


    // 납품정보리스트..
    private ScrollView scrollItemList; // 스크롤뷰
    private LinearLayout llItemList; // 레이아웃
    private FlowLayout flClientImageList; // 가로넘치면아래로 내려가는 레이아웃..



    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int RETRY_PROCESS = 10000;   // 재처리 프로세스
    public static final int LIST_MESSAGE = 20000;   // 재처리 프로세스
    public static final int CVO_NOWPOSITION_SEL = 30001;   // 현위치 가져오기
    public static final int ERP_CLIENT_SEL = 30002;   // ERP 거래처 위치 가져오기
    public static final int NEXT_UPLOAD_WORK = 40000; // 다음파일 업로드
    public static final int CVO_CLIENTIMAGE_SEL = 50000; // 납품이미지 조회
    public static final int CVO_623_SPOT_SEL = 60000; // Cvo센터정보 조회
    public static final int CVO_623_SPOT_START = 70000; // Cvo센터출발 보고



    // 새로고침후 거래처를 선택해주는 여부
    public boolean refreshNselectYn = false;

    public static final int PLAY_SOUND_NEXT = 90001;   // 사운드 재생
    public static final int PLAY_EFFECT = 90002;   // 효과음 재생
    public static final int PLAY_SOUND_CLEAR = 90003;   // 효과음 재생
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public MediaPlayer ring;

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            double lat, lon;

            switch (msg.what) {
                case CVO_623_SPOT_START:
                    // 대기화면 닫기..
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, "센터출발보고가 정상처리되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case CVO_623_SPOT_SEL:
                    // 대기화면 닫기..
                    showProgress(false);
                    break;
                case CVO_CLIENTIMAGE_SEL:
                    flClientImageList.removeAllViews();
                    String prevDDate = "";
                    for (DeliveryImageInfo info : mDeliveryImageInfos) {
                        if (!prevDDate.equals(info.getDDATE())) {
                            // 날짜와 이미지를..
                            addClientImageLayoutItem(info, true); // 날짜..
                            addClientImageLayoutItem(info, false); // 이미지..
                        } else {
                            // 이미지만
                            addClientImageLayoutItem(info, false); // 이미지..
                        }
                        prevDDate = info.getDDATE();
                    }

                    String htmlString = "";
                    if (mDeliveryImageInfos.size()>0) {
                        htmlString = "-납품사진 <small>(전체 : </small>" +
                                String.format("%d", mDeliveryImageInfos.size()) + "" +
                                "<small>장)-<small>";
                        tvTitleInfo.setBackgroundColor(Color.parseColor("#1F96FF"));
                    } else {
                        htmlString = "- 등록된 사진이 없습니다. -";
                        tvTitleInfo.setBackgroundColor(Color.parseColor("#8E8D8D"));
                    }
                    CommonUtil.with().setHtmlMarqueeText(tvTitleInfo, htmlString);

                    scrollItemList.scrollTo(0, 0);
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
                            // 새로고침후 지정거래처를 선택해주는...
                            targetRefreshAction();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case ERP_CLIENT_SEL : // ERP 거래처 리스트 조회...
                    showProgress(false);

                    if (ClientCustManager.getInstance().values().size()>0) {
                        // 1.지도-마커추가
                        mCustPoints.clear();
                        for (ClientCustInfo info : ClientCustManager.getInstance().values()) {
                            try {
                                // 거래처 마커추가..
                                arrCustMarkers.add(
                                        addCustMarker(info)
                                );

                                // 포인트 정보 추가
                                mCustPoints.add(new LatLng(
                                        Double.parseDouble(info.getLAT()),
                                        Double.parseDouble(info.getLON())));

                            } catch (Exception ex) {
                                Loggers.d(info.toString());
                                ex.printStackTrace();
                            }
                        }
                        // 2.지도-지도영역 조절..(키워드를 입력했다면..)
                        if (mCustPoints.size()>0 && (actvKeyword.getText().toString().length()>0))
                            setBoundsMap(mCustPoints);

                        // 3.가로,리스트 작업
                        for (ClientCustInfo info : ClientCustManager.getInstance().values()) {
                            addClientCustTabLayout(info);
                        }

                        btnFoldYn.setVisibility(View.VISIBLE);
                        scHScrollView.setVisibility(View.VISIBLE);

                        if (refreshNselectYn) {
                            try {
                                if (mCurCust != null) {
                                    if (mCurCust.getLAT().length()>0) {
                                        moveNaverMap(new LatLng(
                                                Double.parseDouble(mCurCust.getLAT()),
                                                Double.parseDouble(mCurCust.getLON())
                                        ), mCurCust);
                                        refreshNselectYn = false;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        // 가로 스크롤 감추기..(영역을 지워서 터치에 방해없도록..)
                        btnFoldYn.setVisibility(View.GONE);
                        scHScrollView.setVisibility(View.GONE);
                    }

                    break;
                case CVO_NOWPOSITION_SEL: // 차량최종위치 조회..

//                    showProgress(false);

                    mCurrentBounds = mNaverMap.getContentBounds();
                    LatLng startPos = mCurrentBounds.getNorthWest();
                    LatLng endPos = mCurrentBounds.getSouthEast();

                    double curLat, curLng;
                    double minLat, minLng;
                    double maxLat, maxLng;

                    minLat = endPos.latitude;
                    minLng = startPos.longitude;
                    maxLat = startPos.latitude;
                    maxLng = endPos.longitude;

                    // 포인트 정보 초기화
                    mVehiclePoints.clear();
                    // 마커지우기
                    for(Marker marker : arrVehicleMarkers) { marker.setMap(null); }
                    arrVehicleMarkers.clear();

                    for (CvoNowPosition info : CvoNowPositionListManager.getInstance().values()) {
                        try {
                            // 마커추가..
                            arrVehicleMarkers.add(
                                    addTruckMarker(info)
                            );
                            curLat = Double.parseDouble(info.getLATITUDE());
                            curLng = Double.parseDouble(info.getLONGITUDE());
                            if (maxLat < curLat) maxLat = curLat;
                            if (maxLng < curLng) maxLng = curLng;
                            if (minLat > curLat) minLat = curLat;
                            if (minLng > curLng) minLng = curLng;

                            // 포인트 정보 추가
                            mVehiclePoints.add(new LatLng(
                                    Double.parseDouble(info.getLATITUDE()),
                                    Double.parseDouble(info.getLONGITUDE())));

                        } catch (Exception ex) {
                            Loggers.d(info.toString());
                            ex.printStackTrace();
                        }
                    }
                    // 지도영역 조절..
//                    if (mVehiclePoints.size()>0)
//                        setBoundsMap(mVehiclePoints);

                    // 반경거리 버튼 스타일설정.. + 줌레벨 설정
                    setDistanceStyle(mBoundDistance);

                    // ERP-API요청(거래처 위치조회)
                    requestErpClientSel(mBoundDistance);

                    break;

                case PLAY_EFFECT :
                    // 효과음 설정 체크..
                    soundId = msg.obj.toString();

                    // 효과음 재생..
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MediaPlayer ring = MediaPlayer.create(mContext, Integer.parseInt(soundId));
                            ring.start();
                        }
                    }, 10);

                    break;


                case PLAY_SOUND_NEXT:
                    // 사운드 재생
                    ring= MediaPlayer.create(mContext, R.raw.next);
                    ring.start();

                    break;

                case PLAY_SOUND_CLEAR:
                    // 사운드 재생
                    ring= MediaPlayer.create(mContext, R.raw.stage_clear);
                    ring.start();

                    break;

                case RETRY_PROCESS:
                    showProgress(false);
                    break;

                case LIST_MESSAGE:
                    showProgress(false);
                    break;

                case RequesterSession.REQ_OK:
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;

                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    showProgress(false);
                    if (msg.obj.toString().length()>0)
                        CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case RequesterSession.NO_DATA_FOUND:
                    // 대기화면 보이기/감추기..
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, "데이타가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }

            return true;
        }
    });

    /**
     * 동적 레이아웃 추가 메소드
     */
    private void addClientImageLayoutItem(DeliveryImageInfo info, boolean firstYn) {

        Loggers.d("거래처명 : " + info.getCLNAME());
        ClientImageTabLayout itemLayout = new ClientImageTabLayout(getApplicationContext(), info, firstYn);
        flClientImageList.addView(itemLayout);

    }

    /**
     * 동적 레이아웃 추가 메소드 - 메인하단 거래처리스트(좌우)
     */
    private void addClientCustTabLayout(ClientCustInfo info) {

        Loggers.d("거래처명 : " + info.getCLNAME());
        ClientCustTabLayout itemLayout = new ClientCustTabLayout(getApplicationContext(), info);
        llHScrollItemArea.addView(itemLayout);
    }


    /**
     * 키보드 내리기..
     */
    private String mKeyword;
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(actvKeyword.getWindowToken(), 0);
    }

    /**
     * 프로그래스 다이얼로그 초기화
     */
    ProgressDialog pDialog;
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

    /**
     * 애니메이션 효과 - 우측에서 나타나기
     */
    private long slideLeftDelayTime = 10;
    private void slideLeftRotate(final View targetView) {
        targetView.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_rotate);
                targetView.startAnimation(anim);
            }
        }, slideLeftDelayTime);
        slideLeftDelayTime = slideLeftDelayTime + 50;
    }


    /**
     * 뷰숨기기, 애니메이션아이디 지정하면서..
     * @param targetView
     * @param resId
     */
    private void showViewWithAnimation(View targetView, int resId) {
        targetView.setVisibility(View.GONE);

        Animation ani = AnimationUtils.loadAnimation(mContext, resId);
        targetView.startAnimation(ani);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.VISIBLE);
            }
        }, 300);
    }
    /**
     * 뷰숨기기, 애니메이션아이디 지정하면서..
     * @param targetView
     * @param resId
     */
    private void hideViewWithAnimation(View targetView, int resId) {
        targetView.setVisibility(View.VISIBLE);

        Animation ani = AnimationUtils.loadAnimation(mContext, resId);
        targetView.startAnimation(ani);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                targetView.setVisibility(View.GONE);
            }
        }, 300);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initUI();

        // BROADCAST 리시버 등록
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastDomain.BROADCAST_TRACE_GPS_SVC);
        registerReceiver(traceReceiver, filter);


        // 프로그래스다이얼로그 초기화
        initProgressDlg();

        // Map초기화
        initMap(savedInstanceState);

        // 위치서비스 활성화..
        tryEnableLocation();

        // 센터정보 조회
        requestCvoSpotSel();
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

    private int nBackPressCnt = 0;
    /**
     * Back뒤로가기 버튼 처리..
     */
    @Override
    public void onBackPressed() {
        // 뒤로가기 차단
        // super.onBackPressed();
        if (nBackPressCnt>0) {

            // 위치서비스 종료
            stopLocationService();

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

        } else {
            nBackPressCnt++;
            CommonUtil.with().ToastMsg(mContext, "한번더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 화면 재진입시...
     */
    @Override
    protected void onResume() {

        nBackPressCnt = 0;

        super.onResume();
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cvo_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    /**
     * UI초기화
     */
    private void initUI() {

        setContentView(R.layout.activity_cvo_main);
        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 애니메이션리소스 매핑
        animationStart = AnimationUtils.loadAnimation(this.mContext, R.anim.animation_start_speed);
        animationStop = AnimationUtils.loadAnimation(this.mContext, R.anim.animation_stop);

        // 센터출발버튼
        btnCenterStart = (Button) findViewById(R.id.btnCenterStart);
        btnCenterStart.startAnimation(animationStart);
        btnCenterStart.setVisibility(View.GONE);
        btnCenterStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNearSpot.getSPOT_CD().length() >0) {
                    // 센터 출발보고 요청..
                    popSpotStart();
                } else {
                    CommonUtil.with().ToastMsg(mContext, "출발센터확인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvInfo31 = (TextView) findViewById(R.id.tvInfo31);
        String htmlString = "<font color=\"#FFFFFF\">위치 확인중입니다. 잠시만 기다려주세요.</font>";
        CommonUtil.with().setHtmlMarqueeText(tvInfo31, htmlString);
        tvInfo31.setBackgroundColor(Color.parseColor("#ACACAC"));

        btnMapType01 = (Button) findViewById(R.id.btnMapType01);
        btnMapType02 = (Button) findViewById(R.id.btnMapType02);

        clientSpotlightFL = (FrameLayout) findViewById(R.id.clientSpotlightFL);
        clientSpotlightFL.setVisibility(View.GONE); // 초기숨겨준다..
        tvTitleInfo  = (TextView) findViewById(R.id.tvTitleInfo);

        scMenuTop = (ScalableLayout) findViewById(R.id.scMenuTop);
        scEmptyArea = (ScalableLayout) findViewById(R.id.scEmptyArea);
        scRecentCustList = (ScalableLayout) findViewById(R.id.scRecentCustList);
//        scMenuRightTop = (ScalableLayout) findViewById(R.id.scMenuRightTop);
        scMenuLeftTop = (ScalableLayout) findViewById(R.id.scMenuLeftTop);
        scMenuLeftBottom = (ScalableLayout) findViewById(R.id.scMenuLeftBottom);
//        scMenuRightBottom = (ScalableLayout) findViewById(R.id.scMenuRightBottom);
//        scMenuRightAdd = (ScalableLayout) findViewById(R.id.scMenuRightAdd);

        llNearByInfo = (LinearLayout) findViewById(R.id.llNearByInfo);
        llHScrollItemArea = (LinearLayout) findViewById(R.id.llHScrollItemArea);
        scHScrollView = (HorizontalScrollView) findViewById(R.id.scHScrollView);

        btnMethod01 = (Button) findViewById(R.id.btnMethod01);
        btnMethod02 = (Button) findViewById(R.id.btnMethod02);
        btnMethod03 = (Button) findViewById(R.id.btnMethod03);
        btnMethod04 = (Button) findViewById(R.id.btnMethod04);
        htmlString = "1<small><small> km</small></small>"; CommonUtil.with().setHtmlText(btnMethod01, htmlString);
        htmlString = "3<small><small> km</small></small>"; CommonUtil.with().setHtmlText(btnMethod02, htmlString);
        htmlString = "5<small><small> km</small></small>"; CommonUtil.with().setHtmlText(btnMethod03, htmlString);
        htmlString = "10<small><small> km</small></small>"; CommonUtil.with().setHtmlText(btnMethod04, htmlString);

        btnFloat01 = (Button) findViewById(R.id.btnFloat01);
        btnFloat02 = (Button) findViewById(R.id.btnFloat02);
        btnSpotlight = (Button) findViewById(R.id.btnSpotlight);
        btnSpotlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSpotlightFL.setVisibility(View.GONE);
            }
        });

        scrollItemList = findViewById(R.id.scrollItemList);
        llItemList = (LinearLayout) findViewById(R.id.llItemList); // 거래처(스포트라이트) 하단, 납품이미지 리스트
        flClientImageList = findViewById(R.id.flClientImageList);

        scDummy = (ScalableLayout) findViewById(R.id.scDummy);
        scDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSpotlightFL.setVisibility(View.GONE);
            }
        });

//        scMiddleArea = (ScalableLayout) findViewById(R.id.scMiddleArea);
//        scMiddleArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clientSpotlightFL.setVisibility(View.GONE);
//            }
//        });

        // 거래처 마커
        fl_cust_marker = (FrameLayout) findViewById(R.id.fl_cust_marker);
        tvCustTitle = (TextView) findViewById(R.id.tvCustTitle);
        tvCustSubTitle = (TextView) findViewById(R.id.tvCustSubTitle);
        tvImageCnt = (TextView) findViewById(R.id.tvImageCnt);
        tvImageCnt.setVisibility(View.GONE);

        // 트럭 마커
        fl_truck_marker = (FrameLayout) findViewById(R.id.fl_truck_marker);
        llTruckInfo = (LinearLayout) findViewById(R.id.llTruckInfo);
        tvTruckTitle1 = (TextView) findViewById(R.id.tvTruckTitle1);
        tvTruckTitle2 = (TextView) findViewById(R.id.tvTruckTitle2);
        tvTruckSubTitle = (TextView) findViewById(R.id.tvTruckSubTitle);

        tvSelectCustName = (TextView) findViewById(R.id.tvSelectCustName);
        tvDistanceMe = (TextView) findViewById(R.id.tvDistanceMe);
//        ivSourceMarkerPoint = (ImageView) findViewById(R.id.ivSourceMarkerPoint);
        ivTruckPoint = (ImageView) findViewById(R.id.ivTruckPoint);
//        iv_order_marker_point = (ImageView) findViewById(R.id.iv_order_marker_point);


        // 검색어 입력
        mKeyword = "";
        actvKeyword = (AutoCompleteTextView) findViewById(R.id.actvKeyword);
        // actvKeyword.setVisibility(View.GONE);

        actvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClientCustManager.getInstance() != null) {
                    int inputLength = actvKeyword.length();
                    if (inputLength == 0) {
                        // scInfoArea.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // 검색어 입력 버튼 - 엔터키 눌렀을때 세팅..
        actvKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    // 키보드내리기
                    hideKeyboard();

                    int inputLength = actvKeyword.length();
                    if (inputLength<2) {
                        CommonUtil.with().ToastMsg(mContext, "최소 2글자 이상입력바랍니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        mKeyword = actvKeyword.getText().toString();

                        // 반경거리 버튼 스타일설정.. + 줌레벨 설정
                        setDistanceStyle(mBoundDistance);

                        // ERP-API요청(거래처 위치조회)
                        showProgress(true);
                        requestErpClientSel(mBoundDistance);
                    }
                    return true;
                }
                return false;
            }
        });
        actvKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Loggers.d("입력값은 = " + s.toString() + ", 폰트크기는 = " + actvKeyword.getTextSize()
                        + ", 폰트크기는 = " + actvKeyword.getTextScaleX());
//                int inputLength = actvKeyword.length();
//                if (inputLength>=0) {
//
//                }
            }
        });
        // 검색어 삭제 버튼 세팅
        btnDeleteKeyword = (Button) findViewById(R.id.btnDeleteKeyword);
        btnDeleteKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 검색어 삭제
                    actvKeyword.setText("");
                    mKeyword = ""; // 검색어 지우기

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        // 새로고침 버튼 세팅
        btnFloat02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 마커삭제하기
                clearMarker(MarkerType._마커_전체);

                //CommonUtil.with().ToastMsg(mContext, "현재 보이는 영역, 검색 요청중..", Toast.LENGTH_SHORT).show();

                // 새로고침 요청..액션
                refreshAction();

            }
        });

        // 접어두기/펼치기
        btnFoldYn = (Button) findViewById(R.id.btnFoldYn);
        btnFoldYn.setVisibility(View.GONE);
        btnFoldYn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scEmptyArea.getVisibility() == View.GONE) {
                    btnFoldYn.setText("▲ 펼 치 기");
                    scEmptyArea.setVisibility(View.VISIBLE);
                    scRecentCustList.setVisibility(View.GONE);
                } else {
                    btnFoldYn.setText("▼ 접어두기");
                    scEmptyArea.setVisibility(View.GONE);
                    scRecentCustList.setVisibility(View.VISIBLE);
                }
            }
        });

        // 버튼 - 카메라촬영
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CvoMainActivity) CvoMainActivity.mContext).takePicture4Client(
                        CommonUtil.with().nowYYYY_MM_DD(),
                        mCurCust.getCLCODE()
                        );
            }
        });

        // 버튼 - 네비게이션안내
        btnNavi = (Button) findViewById(R.id.btnNavi);
        btnNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TMAP내비
                ((CvoMainActivity) CvoMainActivity.mContext).launchTMapNavi(
                        mCurCust.getCLNAME(),
                        mCurCust.getLAT(),
                        mCurCust.getLON()
                );


//                // 카카오내비
//                ((CvoMainActivity) CvoMainActivity.mContext).launchKakaoNavi(
//                        mCurCust.getCLNAME(),
//                        mCurCust.getLAT(),
//                        mCurCust.getLON()
//                );
            }
        });



        // 개인정보 처리방침 버튼을 클릭시..
        TextView tvPrivacy = (TextView) findViewById(R.id.tvPrivacy);
        tvPrivacy.setPaintFlags(tvPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://sjwas.gzonesoft.co.kr:24480/privacy/sg623/sg623_privacy_20231022.html"));
                startActivity(intent);
            }
        });

    }

    /**
     * 이미지 화면에 표시하기..
     * @param ivTarget
     * @param fileURL
     */
    private void setAdmUplodadImage(ImageView ivTarget, String fileURL) {
        if (!fileURL.isEmpty()) {
            try {
                Glide.with(this).load(fileURL).into(ivTarget);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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

            // 현재위치 재설정..
            mCurrentCoord = new LatLng(AppSetting.GPS_MANAGER.getGpsLat(), AppSetting.GPS_MANAGER.getGpsLng());
            // 이전 최종위치와의 거리를 구한다..
            double distanceLastPosition = CommonUtil.with().distance(
                    mLastCoord.latitude, mLastCoord.longitude,
                    mCurrentCoord.latitude, mCurrentCoord.longitude
            );
//            // 중심점과의 거리만 표시한다. 디버깅용..
//            addressInfo = String.format("[%d]%.0f", mBoundDistance, distanceLastPosition);
//            CommonUtil.with().setHtmlMarqueeText(tvInfo31, addressInfo);

            try {
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                if (keyguardManager.inKeyguardRestrictedInputMode()) {
                    // 화면 잠금 상태
                    Loggers.d("화면잠금상태로 처리안함..");
                } else {
                    // 화면 잠금 해제 상태

                    // 현재반경범위를 넘어섰다면..
                    if (distanceLastPosition > mBoundDistance*1000) {
                        CommonUtil.with().ToastMsg(mContext,
                                String.format("[%d]%.0f", mBoundDistance, distanceLastPosition),
                                Toast.LENGTH_SHORT).show();
                        // 최종위치와 현위치를 동일하게 세팅한다.
                        mLastCoord = mCurrentCoord;
                        // 현재 기준 거리탐색을 시도한다.- 반경거리 기준 재탬색
                        resetScreen(mBoundDistance);
                    }
                }

            } catch (Exception ex) {
                CommonUtil.with().ToastMsg(mContext,"위치계산오류.", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

        }catch(Exception e){
            Log.d(TAG, "Eception : " + e.getMessage());
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

            if (gpsMsg.equals(BroadCastDomain.REPORT_CURRENT_LOCATION)) {

                //Loggers.d2(this, "----- [브로드캐스트수신 - GPS] " + gpsMsg);

                // 현재 위치정보 표시 업데이트
                updateLocationInfo();

            }

        }
    };



    @UiThread
    public void onMapReady(@NonNull NaverMap naverMap) {

        Log.d(TAG, "naverMap : " + naverMap);

        this.mNaverMap = naverMap;

        // 맵 초기화(지도유형, 건물높이, 초기위치
        initNaverMap(naverMap);

        // 맵 UI설정(레이어그룹, 기본UI-숨김)
        initNaverMapUI(naverMap);

        // 맵 이벤트 초기화
        initNaverMapEvent(naverMap);

        // 초기지도유형 선택..
        clickMapType(btnMapType01);

    }

    /**
     * Map초기화
     */
    private void initMap(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        //mapView.getMapAsync((OnMapReadyCallback) MainActivity.this);
        mMapView.getMapAsync(this);
    }

    /**
     * 네이버맵 초기화
     * @param naverMap
     */
    private void initNaverMap(NaverMap naverMap) {
        try {
            // 지도유형
            naverMap.setMapType(NaverMap.MapType.Basic);

            // 건물높이
            naverMap.setBuildingHeight(0.5f);

            // 초기위치 세팅 (37.557224, 126.973584)
            CameraPosition cameraPosition = new CameraPosition(
                    new LatLng(37.557224, 126.973584), // 위치 지정
                    11.078748, // 줌 레벨
                    20, // 기울임 각도
                    0 // 방향
            );
            naverMap.setCameraPosition(cameraPosition);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 네이버맵 UI 초기화
     * @param naverMap
     */
    private void initNaverMapUI(NaverMap naverMap) {
        try {

            // 레이어 그룹
            naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
            naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false);

            // UI 설정
            UiSettings uiSettings = naverMap.getUiSettings();
            uiSettings.setCompassEnabled(true); // 나침반
            uiSettings.setScaleBarEnabled(false); // 축척바
            uiSettings.setZoomControlEnabled(false); // 줌 버튼
            uiSettings.setLocationButtonEnabled(false); // 현위치 버튼

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 경로영역으로 지도 카메라를 조정한다.(영역여백 100픽셀)
     * @param routePoints
     */
    public void setBoundsMap(ArrayList<LatLng> routePoints) {
        CameraAnimation animation = CameraAnimation.Fly; // : CameraAnimation.Easing;
        CameraUpdateParams cu = new CameraUpdateParams().tiltBy(45);
        CameraUpdate cameraUpdate = CameraUpdate.withParams(cu).fitBounds(getPointBouds(routePoints), 300)
                .animate(animation, 1500);
        // 카메라(좌표)이동
        mNaverMap.moveCamera(cameraUpdate);


        // 줌레벨 세팅
//        double fitZoomLevel = CameraUtils.getFittableZoom(mNaverMap, getPointBouds(routePoints), 300);
////        CameraPosition cameraPosition = new CameraPosition(
////                mNaverMap.getCameraPosition().target.toLatLng(), // 위치 지정
////                fitZoomLevel, // 줌 레벨
////                20, // 기울임 각도
////                0 // 방향
////        );
////        mNaverMap.setCameraPosition(cameraPosition);
//
//        // 줌레벨 세팅후 다시이동
//        CameraPosition position = new CameraPosition(mNaverMap.getCameraPosition().target.toLatLng(), fitZoomLevel);
//        mNaverMap.moveCamera(CameraUpdate.toCameraPosition(position).animate(animation, 5000));
    }
    /**
     * 좌표배열을 받아서 영역좌표를 만들어서 리턴한다.
     * @param routePoints
     * @return
     */
    public LatLngBounds getPointBouds(ArrayList<LatLng> routePoints) {
        LatLngBounds retValue = null;
        try {
            double sLat = 0, nLat = 0, wLng = 0, eLng = 0;
            int nIdx = 0;
            for (LatLng point : routePoints) {
                if (nIdx == 0) {
                    sLat = point.latitude;
                    nLat = point.latitude;
                    wLng = point.longitude;
                    eLng = point.longitude;
                } else {
                    if (sLat > point.latitude) sLat = point.latitude;
                    if (nLat < point.latitude) nLat = point.latitude;
                    if (wLng > point.longitude) wLng = point.longitude;
                    if (eLng < point.longitude) eLng = point.longitude;
                }
                nIdx++;
            }
            LatLng sw = new LatLng(sLat, wLng), ne = new LatLng(nLat, eLng);
            retValue = new LatLngBounds(sw, ne);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return retValue;
        }
    }


    /**
     * 차량 마커추가
     * @param info
     * @return
     */
    public Marker addTruckMarker(CvoNowPosition info) {
        Marker marker = new Marker();
        boolean bFind = false;
        String sHTML = "";
        try {
            // 마커공통세팅
            marker = setSettingMarker(marker);
            // 마커위치(위/경도) 세팅
            Double lat = Double.parseDouble(info.getLATITUDE());
            Double lon = Double.parseDouble(info.getLONGITUDE());
            if (AppSetting.cvoUserInfo.getUSERTYPE().equals(UserType._배송기사) && // 배송기사이면서..
                    info.getDEVICENO().equals(AppSetting.cvoUserInfo.getDEVICENO())) { // 나의기기번호와 같다면..
                // 지도 현위치에 내 차량정보 표시..
                lat = mCurrentCoord.latitude;
                lon = mCurrentCoord.longitude;
            }
            marker.setPosition(new LatLng(lat, lon));
            // 캡션세팅
            marker.setCaptionColor(Color.parseColor("#175B9B"));
            marker.setCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setCaptionTextSize(16);
            marker.setCaptionText(info.getMARKER_CAPTION());
            // 서브캡션
            marker.setSubCaptionTextSize(17);
            marker.setSubCaptionColor(Color.parseColor("#3F80BA"));
            marker.setSubCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setSubCaptionText(info.getMARKER_SUBCAPTION());
            // 상태 세팅
            tvTruckTitle2.setText(info.getSTATUS_NM());
            // 속도 세팅
            sHTML = "<B><BIG>" + info.getSPEED() + "</BIG><B>";
            CommonUtil.with().setHtmlText(tvTruckTitle1, sHTML);
            // 상단 타이틀 세팅
            //tvTruckSubTitle.setText(info.getMARKER_SUBTITLE());
            CommonUtil.with().setHtmlText(tvTruckSubTitle, info.getMARKER_SUBTITLE());

            // 운행중 확인
            boolean bRunningYn = false;
            try {
                if (Integer.parseInt(info.getSPEED())>0) bRunningYn = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (bRunningYn) {
                // 배경세팅
                llTruckInfo.setBackgroundResource(R.drawable.btn_bg_blue_01_round_bottom_10);
                // 상단 타이틀 세팅
                tvTruckSubTitle.setTextColor(Color.parseColor("#0076BA"));
                tvTruckSubTitle.setBackgroundResource(R.drawable.truck_subtitle_bg_01);
                // 아래포인트이미지 세팅
                ivTruckPoint.setImageResource(R.drawable.marker_point_01);
            } else {
                // 배경세팅
                llTruckInfo.setBackgroundResource(R.drawable.btn_bg_gray_01_round_bottom_10);
                // 상단 타이틀 세팅
                tvTruckSubTitle.setTextColor(Color.parseColor("#838383"));
                tvTruckSubTitle.setBackgroundResource(R.drawable.btn_bg_white_gray_01_round_top_10);
                // 아래포인트이미지 세팅
                ivTruckPoint.setImageResource(R.drawable.marker_point_03);
            }
            // 마커클릭시..
            marker.setOnClickListener(overlay -> {
//                Toast.makeText(getApplicationContext(), info.getDEVICENO() + ", 마커 클릭", Toast.LENGTH_SHORT).show();
                // 마커클릭시 팝업
                Intent intent = new Intent(getApplicationContext(), PopDetailVehicleActivity.class);
                intent.putExtra("DEVICE_NO", info.getDEVICENO());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_change);

                return true;
            });
            // 마커이미지 세팅
            marker.setIcon(OverlayImage.fromView(fl_truck_marker));
            // 마커추가..
            marker.setMap(mNaverMap);

        } catch (Exception ex) {
            ex.printStackTrace();
            Loggers.d("ERROR => " + info.toString());
        } finally {
            return (bFind)? null : marker;
        }
    }

    /**
     * 거래처(인도처)마커추가
     * @param info
     * @return
     */
    public Marker addCustMarker(ClientCustInfo info) {
        Marker marker = new Marker();
        boolean bFind = false;
        String htmlString = "";

        try {
            // 마커공통세팅
            marker = setSettingMarker(marker);
            // 마커위치(위/경도) 세팅
            Double lat = Double.parseDouble(info.getLAT());
            Double lon = Double.parseDouble(info.getLON());
            marker.setPosition(new LatLng(lat, lon));
            // 거래처이름 세팅
            tvCustSubTitle.setText(info.getCLNAME());
            // 금일,이미지건수 세팅
            try {
                if (Integer.parseInt(info.getIMAGE_CNT_TODAY()) > 0) {
                    tvImageCnt.setText(info.getIMAGE_CNT_TODAY());
                    tvImageCnt.setVisibility(View.VISIBLE);
                } else {
                    tvImageCnt.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                tvImageCnt.setVisibility(View.GONE);
            }

            // 거리 세팅
            htmlString = String.format("%.1f", Double.parseDouble(info.getDISTANCE_ME())/1000) + "" +
                    "<small>km</small>";
            CommonUtil.with().setHtmlText(tvCustTitle, htmlString);

//            // 직영주유소의 경우 로고를 표시한다.
//            ivLogoHDO.setVisibility(
//                    (info.getCUST_TYPE().equals("직영주유소"))? View.VISIBLE : View.GONE
//            );
            // 캡션세팅
            marker.setCaptionColor(Color.parseColor("#175B9B"));
            marker.setCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setCaptionTextSize(16);
            marker.setCaptionText(info.getCLJUSO1());
            // 서브캡션
//            marker.setSubCaptionTextSize(12);
//            marker.setSubCaptionColor(Color.parseColor("#3F80BA"));
//            marker.setSubCaptionHaloColor(Color.parseColor("#FFFFFF"));
//            marker.setSubCaptionText(info.getCLJUSO1());


            // 마커클릭시..
            marker.setOnClickListener(overlay -> {
                //Toast.makeText(getApplicationContext(), info.getCUST_SNM() + ",팝업구성 개발중 입니다.", Toast.LENGTH_SHORT).show();
                // 마커클릭시 팝업
//                Intent intent = new Intent(getApplicationContext(), PopDetailCustActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, R.anim.no_change);

                moveNaverMap(new LatLng(
                        Double.parseDouble(info.getLAT()),
                        Double.parseDouble(info.getLON())
                ), info);


                //scHScrollView.scrollTo(500, 0);


                return true;
            });
            // 마커이미지 세팅
            marker.setIcon(OverlayImage.fromView(fl_cust_marker));
            // 마커추가..
            marker.setMap(mNaverMap);

            // 반경원 그리기
            //drawCirclePoint(new LatLng(lat, lon), 200, "#1E4952FF", "#524952FF", mNaverMap);

        } catch (Exception ex) {
            ex.printStackTrace();
            Loggers.d("ERROR => " + info.toString());
        } finally {
            return (bFind)? null : marker;
        }
    }

    /**
     * 반경원 그리기..
     * @param pos - 위치
     * @param distance - 반경(거리)
     * @param bgColor - 배경색
     * @param strokeColor - 테두리색
     * @param targetMap - 지도객체
     * @return
     */
    private CircleOverlay drawCirclePoint(LatLng pos, int distance, String bgColor, String strokeColor, NaverMap targetMap) {
        CircleOverlay circle = new CircleOverlay();
        try {
            circle.setCenter(pos);
            circle.setRadius(distance);
            circle.setColor(Color.parseColor(bgColor));
            circle.setOutlineWidth(10);
            circle.setOutlineColor(Color.parseColor(strokeColor));
            circle.setMap(targetMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return circle;
        }
    }


    /**
     * 마커,공통세팅
     * @param orgMarker
     * @return
     */
    public Marker setSettingMarker(Marker orgMarker) {

        // 마커공통속성 세팅..
        int nCaptionLevel = 12;
        int nSubCaptionLevel = 13;

        orgMarker.setWidth(Marker.SIZE_AUTO);
        orgMarker.setHeight(Marker.SIZE_AUTO);
        orgMarker.setHideCollidedCaptions(false); // 겹치는 캡션 자동 숨김처리
        orgMarker.setHideCollidedMarkers(false); // 겹치는 마커 자동 숨김처리
        orgMarker.setHideCollidedSymbols(false); // 겹치는 지도심벌과의 자동 숨김처리

        orgMarker.setCaptionMinZoom(nCaptionLevel);
        orgMarker.setSubCaptionMinZoom(nSubCaptionLevel);

        return orgMarker;
    }


    /**
     * 위치사용시도
     */
    private void tryEnableLocation() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) == PermissionChecker.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, PERMISSIONS[1]) == PermissionChecker.PERMISSION_GRANTED) {
            enableLocation();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        }

        // ----------------------------------------------------------
        // 서비스로 동작 - 시작
        // ----------------------------------------------------------
        startLocationService();

        // ----------------------------------------------------------
        // 업무시작상태로 변경
        // ----------------------------------------------------------
        CommonUtil.with().ToastMsg(mContext,
                AppSetting.cvoUserInfo.getNAME() + "님, 안녕하세요.",
                Toast.LENGTH_SHORT).show();
        // 출근/퇴근 구분 상태
        AppSetting.todayStatus = TodayStatus._출근;
        // 상태변경
        AppSetting.workStatus = WorkStatus._출근보고;
    }

    /**
     * 위치활성화
     */
    private void enableLocation() {
        showProgress(true);
        new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
                        locationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL);

                        LocationServices.getFusedLocationProviderClient(mContext)
                                .requestLocationUpdates(locationRequest, locationCallback, null);
                        locationEnabled = true;
                        waiting = true;
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .addApi(LocationServices.API)
                .build()
                .connect();
    }

    /**
     * 위치비활성화
     */
    private void disableLocation() {
        if (!locationEnabled) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        locationEnabled = false;
    }

    /**
     * 위치정보 콜백
     */
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (mNaverMap == null) {
                return;
            }

            showProgress(false);

            Location lastLocation = locationResult.getLastLocation();
            LatLng coord = new LatLng(lastLocation);
            LocationOverlay locationOverlay = mNaverMap.getLocationOverlay();
            locationOverlay.setPosition(coord);
            locationOverlay.setBearing(lastLocation.getBearing());

            if (waiting) {
                waiting = false;
                //fab.setImageResource(R.drawable.ic_location_disabled_black_24dp);
                locationOverlay.setVisible(true);

                mNaverMap.moveCamera(CameraUpdate.scrollTo(coord));
                // 현재위치 저장..
                mCurrentCoord = coord;
                // 최종위치도 저장..
                mLastCoord = coord;


                // 새로고침 요청..액션
                mViewType01_Yn = true;
                mViewType02_Yn = false;
                mViewType03_Yn = false;
                mViewType04_Yn = false;
                mViewType05_Yn = false;
                refreshAction();

                mFirstYn = false;
            }
        }
    };

    /**
     * 새로고침후 거래처선택까지...
     */
    public void targetRefreshAction() {
        // 새로고침하고 현재거래처를 선택하는게 낫다.
        clientSpotlightFL.setVisibility(View.GONE); // 스포트라이트 레이어, 숨겨준다..
        // 새로고침후 이전거래처 재선택 효과..여부..
        refreshNselectYn = true;
        // 새로고침
        refreshAction();
    }

    /**
     * 새로고침 요청..액션
     */
    public void refreshAction() {
        try {
            // 마커위치 초기화
            mVehiclePoints.clear();
            mCustPoints.clear();

            // 키워드 초기화
            actvKeyword.setText("");

            // 차량 현위치 조회
            requestCvoNowPosition();

            // 주변센터 확인
            checkNearSpot();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 주변센터 확인
     */
    public CvoSpotInfo mNearSpot = null;
    public void checkNearSpot() {
        try {
            if (mCvoSpotInfos.size()>0) {
                boolean bNearSpotYn = false;
                for (CvoSpotInfo info:mCvoSpotInfos) {
                    double spotLat = Double.parseDouble(info.getLATITUDE());
                    double spotLon = Double.parseDouble(info.getLONGITUDE());

                    // 현위치와의 거리를 구한다..
                    double distance = CommonUtil.with().distance(
                            spotLat, spotLon,
                            mCurrentCoord.latitude, mCurrentCoord.longitude
                    );
                    // 200미터이내의 경우...
                    if (!bNearSpotYn) {
                        if (distance < 200) {
                            bNearSpotYn = true;
                            mNearSpot = info; // 근처센터정보 저장..
                        }
                    }
                }
                // 근처센터확연 체크...
                if (bNearSpotYn) {
                    btnCenterStart.setVisibility(View.VISIBLE); // 버튼보이기..
                } else {
                    btnCenterStart.setVisibility(View.GONE); // 버튼감추기..
                    mNearSpot = null; // 주변센터 초기화..
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 네이버맵 이벤트 초기화
     * @param naverMap
     */
    public boolean bShowMenuYn = true; // 메뉴보이기/숨기기 제어
    private void initNaverMapEvent(NaverMap naverMap) {
        try {
            // 지도클릭시..
            naverMap.setOnMapClickListener((point, coord) ->
                    {
                        try {
                            // 메뉴보였다 사라졌다 처리...
                            if (bShowMenuYn) {
                                bShowMenuYn = false;
                                hideMenuButton();
                            } else {
                                bShowMenuYn = true;
                                showMenuButton();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
            );

            // 지도롱클릭
            naverMap.setOnMapLongClickListener((point, coord) ->
                    {
                        // 모든 마커 지우기
                        clearMarker(MarkerType._마커_전체);
                    }
            );
            // 지도이동시..
// 이동하는동안 계속발생...
//            naverMap.addOnCameraChangeListener((reason, animated) -> {
//                CameraPosition position = naverMap.getCameraPosition();
//                Loggers.d(String.format("카메라정보 = [%d] %f / %f / %f / %f / %f",
//                        reason,
//                        position.target.latitude, position.target.longitude,
//                        position.tilt, position.bearing, position.zoom));
//                if (position.zoom < 12.0f) {
//                    mPleaseZoomYn = false;
//                } else {
//                    mPleaseZoomYn = true;
//
//                    if (position.zoom >= 13.0f) {
//                        btnFloat02.setText("자동\n조회");
//                        mAutoRefreshYn = true;
//                    } else {
//                        btnFloat02.setText("새로\n고침");
//                        mAutoRefreshYn = false;
//                    }
//                    // 자동조회(조건에 따라)
//                    if (mAutoRefreshYn) refreshAction();
//                }
//            });
            // 이동이 마친후 발생...
            naverMap.addOnCameraIdleListener(() -> {
                CameraPosition position = naverMap.getCameraPosition();
                Loggers.d(String.format("카메라정보 = %f / %f / %f / %f / %f",
                        position.target.latitude, position.target.longitude,
                        position.tilt, position.bearing, position.zoom));

//                // 지도보기유형이 거래처의 경우 줌레벨이하는 차단시킨다..
//                double fZoomLevel = getZoomLevel();
//                if (fZoomLevel>0 && fZoomLevel<AppSetting.Config.MAP_ZOOM_MAX) {
//                    // 검색차단
//                    btnFloat02.setBackgroundResource(R.drawable.bg_fill_blue_05_round_200_off);
//                    btnFloat02.setEnabled(false);
//                } else {
//                    // 검색가능
//                    btnFloat02.setBackgroundResource(R.drawable.bg_fill_blue_05_round_200);
//                    btnFloat02.setEnabled(true);
//                }

//                if (
//                        curViewType.equals(String.format("%d", SearchMethod._지도보기유형_거래처))
//                ) {
//                    // 지도보기유형이 거래처의 경우 줌레벨이하는 차단시킨다..
//                    double fZoomLevel = getZoomLevel();
//                    if (fZoomLevel>0 && fZoomLevel<AppSetting.Config.MAP_ZOOM_MAX) {
//                        // 검색차단
//                        btnFloat02.setBackgroundResource(R.drawable.bg_fill_blue_05_round_200_off);
//                        btnFloat02.setEnabled(false);
//                    } else {
//                        // 검색가능
//                        btnFloat02.setBackgroundResource(R.drawable.bg_fill_blue_05_round_200);
//                        btnFloat02.setEnabled(true);
//                    }
//                } else {
//                    // 검색가능
//                    btnFloat02.setBackgroundResource(R.drawable.bg_fill_blue_05_round_200);
//                    btnFloat02.setEnabled(true);
//                }
//                if (!mFirstYn) {
//                    if (position.zoom >= AppSetting.Config.MAP_ZOOM_MAX) {
////                        btnFloat02.setText("자동\n조회");
////                        btnFloat02.setBackgroundResource(R.drawable.bg_fill_orange_02_round_200);
//                        mAutoRefreshYn = true;
//                    } else {
////                        btnFloat02.setText("새로\n고침");
////                        btnFloat02.setBackgroundResource(R.drawable.bg_fill_blue_05_round_200);
//                        mAutoRefreshYn = false;
//                    }
//                    // 자동조회(조건에 따라)
//                    if (mAutoRefreshYn) refreshAction();
//                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 지도유형변경
     * @param view
     */
    public void clickMapType(View view) {
        int nTag = Integer.parseInt(view.getTag().toString());
        Loggers.d("nTag : " + nTag);

        switch (nTag) {
            case 10: // 일반지도
                btnMapType01.setBackgroundResource(R.drawable.btn_bg_white_01_round_top_10_on);
                btnMapType02.setBackgroundResource(R.drawable.btn_bg_white_01_round_bottom_10);
                btnMapType01.setTextColor(Color.parseColor("#FFFFFF"));
                btnMapType02.setTextColor(Color.parseColor("#165997"));
                mNaverMap.setMapType(NaverMap.MapType.Basic);
                break;
            case 20: // 위성지도
                btnMapType01.setBackgroundResource(R.drawable.btn_bg_white_01_round_top_10);
                btnMapType02.setBackgroundResource(R.drawable.btn_bg_white_01_round_bottom_10_on);
                btnMapType01.setTextColor(Color.parseColor("#165997"));
                btnMapType02.setTextColor(Color.parseColor("#FFFFFF"));
                mNaverMap.setMapType(NaverMap.MapType.Hybrid);
                break;
            default:
                break;
        }
    }


    /**
     * 지도줌레벨 리턴
     * @return
     */
    public double getZoomLevel() {
        double fRet = 0.0f;
        try {
            if (mNaverMap != null) {
                CameraPosition position = mNaverMap.getCameraPosition();
                fRet = position.zoom;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return fRet;
    }

    /**
     *
     * @param latLng
     */
    public void moveNaverMap(LatLng latLng, ClientCustInfo info) {

        // CameraAnimation
        // None: 애니메이션 없이 이동합니다. 기본값입니다.
        // Linear: 일정한 속도로 이동합니다.
        // Easing: 부드럽게 가감속하며 이동합니다. 가까운 거리를 이동할 때 적합합니다.
        // Fly: 부드럽게 축소됐다가 확대되며 이동합니다. 먼 거리를 이동할 때 적합합니다.
        try {
            //CameraPosition position = new CameraPosition(latLng, mNaverMap.getCameraPosition().zoom);
            CameraPosition position = new CameraPosition(latLng, 13.399212);
            CameraAnimation animation = CameraAnimation.Fly; // : CameraAnimation.Easing;
            mNaverMap.moveCamera(CameraUpdate.toCameraPosition(position).animate(animation, 2500));

            // 현재위치 저장
            mCurrentCoord = latLng;
            mCurCust = info;

            // 거래처 스포트 라이트, 보이기..
            clientSpotlightFL.setVisibility(View.VISIBLE);

            // 거래처 스포트 라이트, 타이틀 세팅
            String htmlString = info.getCLNAME();
            CommonUtil.with().setHtmlMarqueeText(tvSelectCustName, htmlString);
            htmlString = "- " +
                    "" + String.format("%.1f", Double.parseDouble(info.getDISTANCE_ME())/1000) + "" +
                    "<small>km</small>" +
                    " 주변 -";
            CommonUtil.with().setHtmlText(tvDistanceMe, htmlString);

            // 거래처 스포트 라이트, 하단 납품이미지 웹뷰 세팅
            requestCvoDeliveryImage("", info.getCLCODE());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 카카오 내비 실행
     */
    public void launchKakaoNavi(String destName, String sLat, String sLon) {
        try {
            KakaoService kakao = new KakaoService();
            kakao.launchNavi(this, mContext, destName, sLon, sLat);

//            launchTMapNavi(destName, sLat, sLon);

//            RunTmap(destName, sLat, sLon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * TMap 내비 실행
     */
    public void launchTMapNavi(String destName, String sLat, String sLon) {
        try {

            // 웹을통한 TMAP호출기능
            String url = String.format(
                    "https://apis.openapi.sk.com/tmap/app/routes?appKey=9qVTjoOTzj4HMrrJAiDt44L2gzFvdD5YaLBdx7Bb&name=%s&lon=%s&lat=%s",
                    destName, sLon, sLat);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);

//            TMapTapi tMapTapi = new TMapTapi(mContext);
//            tMapTapi.invokeTmap();
//
//            tMapTapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
//                @Override
//                public void SKTMapApikeySucceed() {
////                    if (tmaptapi.isTmapApplicationInstalled()) {
////                        Log.e("HomeActivity", "INSTALLED");
//                    float fLon = Float.parseFloat(sLon);
//                    float fLat = Float.parseFloat(sLat);
//
//                    tMapTapi.invokeRoute(destName, fLon, fLat);
//////                tMapTapi.invokeRoute("T타워", 126.984098f, 37.566385f);
////                    } else {
////
////                        Log.e("HomeActivity", "NOT INSTALLED"); // ◀ 설치되어있지 않다고 나옴
////
////                    }
//                }
//
//                @Override
//                public void SKTMapApikeyFailed(String s) {
//                    Log.e("CvoMainActivity", "SKTMapApikeyFailed");
//                }
//            });
//            //tMapTapi.setSKTMapAuthentication("9qVTjoOTzj4HMrrJAiDt44L2gzFvdD5YaLBdx7Bb");
//            tMapTapi.setSKTMapAuthentication("l7xx960af40f5f8a45ad921a0af6097ac0e4");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 티맵 실행
     * @param name
     * @param lat
     * @param lng
     */
    public void RunTmap(String name, String lat, String lng) {
        try {
            TMapTapi tmaptapi = new TMapTapi(mContext);
            // TMAP 설치여부 확인...
//            boolean bInstalled = tmaptapi.isTmapApplicationInstalled();
//            if (bInstalled) {
                // 설치됨..(설치여부를 확인하는
                tmaptapi.setSKTMapAuthentication (Svc.TMAP_KEY);
                tmaptapi.invokeRoute(name, Float.valueOf(lat), Float.valueOf(lng));
//            } else {
//                // 미설치됨...
//                Loggers.d("설치유도...");
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.skt.tmap.ku"));
//                mContext.startActivity(i);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 티맵 실행
//     * @param name 목적지 이름
//     * @param fX 좌표X
//     * @param fY 좌표Y
//     */
//    public void RunTmap(String name, String lat, String lng) {
//        try {
//            TMapTapi tmaptapi = new TMapTapi(mContext);
//            // TMAP 설치여부 확인...
////            boolean bInstalled = tmaptapi.isTmapApplicationInstalled();
////            if (bInstalled) {
//            // 설치됨..(설치여부를 확인하는
//            tmaptapi.setSKTMapAuthentication ("9qVTjoOTzj4HMrrJAiDt44L2gzFvdD5YaLBdx7Bb");
//            tmaptapi.invokeRoute(name, Float.valueOf(lat), Float.valueOf(lng));
////            } else {
////                // 미설치됨...
////                Loggers.d("설치유도...");
////                Intent i = new Intent(Intent.ACTION_VIEW);
////                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.skt.tmap.ku"));
////                mContext.startActivity(i);
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    /**
     * 마커지우기
     * @param type
     */
    public void clearMarker(int type) {
        try {
            switch (type) {
//                case MarkerType._거래처:
//                    if (arrCustMarkers.size() >0) {
//                        for(Marker marker : arrCustMarkers) {
//                            marker.setMap(null);
//                        }
//                    }
//                    break;
//                case MarkerType._주문:
//                    if (arrOrderMarkers.size() >0) {
//                        for(Marker marker : arrOrderMarkers) {
//                            marker.setMap(null);
//                        }
//                    }
//                    break;
//                case MarkerType._출하지:
//                    if (arrSourceMarkers.size() >0) {
//                        for(Marker marker : arrSourceMarkers) {
//                            marker.setMap(null);
//                        }
//                    }
//                    break;
                case MarkerType._차량:
                    if (arrVehicleMarkers.size() >0) {
                        for(Marker marker : arrVehicleMarkers) {
                            marker.setMap(null);
                        }
                        arrVehicleMarkers.clear();
                    }
                    break;
                case MarkerType._마커_전체:
//                    for(Marker marker : arrCustMarkers) { marker.setMap(null); }
//                    for(Marker marker : arrOrderMarkers) { marker.setMap(null); }
//                    for(Marker marker : arrSourceMarkers) { marker.setMap(null); }
                    for(Marker marker : arrVehicleMarkers) { marker.setMap(null); }
//                    arrCustMarkers.clear();
//                    arrOrderMarkers.clear();
//                    arrSourceMarkers.clear();
                    arrVehicleMarkers.clear();

//                    for(Marker marker : arrCustMarkerClusters) { marker.setMap(null); }
//                    for(Marker marker : arrCustClusters) { marker.setMap(null); }
//                    arrCustMarkerClusters.clear();
//                    arrCustClusters.clear();
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 메뉴버튼 보이기
     */
    private void showMenuButton() {

        slideLeftDelayTime = 300;

        // 메뉴버튼 보이기
        showSearchArea(); // 통합검색
//        showMenuRightTop(); // 오른쪽위
        showMenuLeftTop(); // 왼쪽위
//        showMenuRightTop(); // 오른쪽위
        showMenuLeftBottom(); // 왼쪽아래
        showMenuRightBottom(); // 오른쪽아래
    }

    /**
     * 메뉴버튼 감추기
     */
    private void hideMenuButton() {
        // 메뉴버튼 감추기
        hideSearchArea(); // 통합검색
//        hideMenuRightTop(); // 오른쪽위
        hideMenuLeftTop(); // 왼쪽위
//        hideMenuRightTop(); // 오른쪽위
        hideMenuLeftBottom(); // 왼쪽아래
        hideMenuRightBottom(); // 오른쪽아래
//        // 추가메뉴버튼이 보여지고있다면, 숨겨준다.
//        if (btnAddMenu05.getVisibility() == View.VISIBLE) hideAddMenuRight();
    }
    /**
     * 통합검색 보이기
     */
    private void showSearchArea() {
        showViewWithAnimation(scMenuTop, R.anim.in_down);
    }
    /**
     * 통합검색 감추기
     */
    private void hideSearchArea() {
        hideViewWithAnimation(scMenuTop, R.anim.out_up);
    }

    /**
     * 오른쪽위 메뉴버튼 보이기
     */
    private void showMenuRightTop() {
        showViewWithAnimation(btnFloat02, R.anim.in_left);
    }

    /**
     * 오른쪽위 메뉴버튼 감추기
     */
    private void hideMenuRightTop() {
        hideViewWithAnimation(btnFloat02, R.anim.out_right);
    }

    /**
     * 왼쪽위 메뉴버튼 보이기
     */
    private void showMenuLeftTop() {
        showViewWithAnimation(scMenuLeftTop, R.anim.in_right);
    }

    /**
     * 왼쪽위 메뉴버튼 감추기
     */
    private void hideMenuLeftTop() {
        hideViewWithAnimation(scMenuLeftTop, R.anim.out_left);
    }

    /**
     * 왼쪽아래쪽 메뉴버튼 보이기
     */
    private void showMenuLeftBottom() {
//        showViewWithAnimation(btnFloat01, R.anim.in_right);
        showViewWithAnimation(llNearByInfo, R.anim.in_up);
    }

    /**
     * 왼쪽아래쪽 메뉴버튼 감추기
     */
    private void hideMenuLeftBottom() {
//        hideViewWithAnimation(btnFloat01, R.anim.out_left);
        showViewWithAnimation(llNearByInfo, R.anim.out_down);
        llNearByInfo.setVisibility(View.GONE);
    }

    /**
     * 오른쪽아래쪽 메뉴버튼 보이기
     */
    private void showMenuRightBottom() {
        showViewWithAnimation(llNearByInfo, R.anim.in_up);
    }

    /**
     * 오른쪽아래쪽 메뉴버튼 감추기
     */
    private void hideMenuRightBottom() {
        hideViewWithAnimation(llNearByInfo, R.anim.out_down);
    }

//    /**
//     * 우측하단, 추가버튼 메뉴보이기
//     */
//    private void showAddMenuRight() {
//        btnAddMenu05.setVisibility(View.VISIBLE);
//        slideLeftDelayTime = 0;
//        slideLeftRotate(btnSetting);
//        //slideLeftRotate(btnAddMenu04);
//        slideLeftRotate(btnAddMenu03);
//        slideLeftRotate(btnAddMenu02);
//        slideLeftRotate(btnAddMenu01);
//
//        switch (mSearchOption) {
//            case SearchMethod._필터_개인:
//                break;
//            case SearchMethod._필터_지사:
//                slideLeftRotate(btnStatus01);
//                slideLeftRotate(btnStatus02);
//                break;
//        }
//
//        layoutBgForAddButton.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * 우측하단, 추가버튼 메뉴감추기
//     */
//    private void hideAddMenuRight() {
//        btnFloat03.setVisibility(View.VISIBLE);
//
//        hideViewWithAnimation(btnAddMenu01, R.anim.out_down);
//        hideViewWithAnimation(btnAddMenu02, R.anim.out_down);
//        hideViewWithAnimation(btnAddMenu03, R.anim.out_down);
//        //hideViewWithAnimation(btnAddMenu04, R.anim.out_down);
//        hideViewWithAnimation(btnAddMenu05, R.anim.out_down);
//        hideViewWithAnimation(btnSetting, R.anim.out_right);
//
//        switch (mSearchOption) {
//            case SearchMethod._필터_개인:
//                break;
//            case SearchMethod._필터_지사:
//                hideViewWithAnimation(btnStatus01, R.anim.out_down);
//                hideViewWithAnimation(btnStatus02, R.anim.out_down);
//                break;
//        }
//
//        layoutBgForAddButton.setVisibility(View.GONE);
//    }

    /**
     * 배경을 터치할때..
     * @param v
     */
    public void hideBgForAddButton(View v) {
        // 우측하단, 추가버튼 메뉴감추기
        hideAddMenuRight();
    }

    /**
     * 보기유형 버튼클릭 (태크로 구분)
     * @param view
     */
    public boolean mViewType01_Yn = false;
    public boolean mViewType02_Yn = false;
    public boolean mViewType03_Yn = false;
    public boolean mViewType04_Yn = false;
    public boolean mViewType05_Yn = false;
    public void clickViewType(View view) {
        int nTag = Integer.parseInt(view.getTag().toString());
        Loggers.d("nTag : " + nTag);

        // 반경거리 기준 거래처 조회하기..
        mBoundDistance = nTag;

        // 반경거리 기준 재탬색
        resetScreen(mBoundDistance);
    }

    /**
     * 반경거리 기준 재탬색, 다른데서 호출을위해 밖으로 분리한다.
     * @param boundDistance
     */
    public void resetScreen(int boundDistance) {
        // 키워드 지우기
        actvKeyword.setText("");

        // 반경거리 버튼 스타일설정.. + 줌레벨 설정
        setDistanceStyle(boundDistance);

        // 줌레벨 세팅후 다시이동
        CameraPosition position = new CameraPosition(mCurrentCoord, mCurZoomLevel);
        CameraAnimation animation = CameraAnimation.Fly; // : CameraAnimation.Easing;
        mNaverMap.moveCamera(CameraUpdate.toCameraPosition(position).animate(animation, 1000));


        // 새로고침 요청...
        refreshAction();
    }

    /**
     * 반경거리 버튼 스타일설정..
     * @param nTag
     */
    private void setDistanceStyle(int nTag) {
        switch (nTag) {
            case 1:
                btnMethod01.setBackgroundResource(R.drawable.btn_bg_blue_01_round_left_10);
                btnMethod02.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
                btnMethod03.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
                btnMethod04.setBackgroundResource(R.drawable.btn_bg_white_01_round_right_10);
                btnMethod01.setTextColor(Color.parseColor("#FFFFFF"));
                btnMethod02.setTextColor(Color.parseColor("#165997"));
                btnMethod03.setTextColor(Color.parseColor("#165997"));
                btnMethod04.setTextColor(Color.parseColor("#165997"));
                mCurZoomLevel = 13.399212;
                break;
            case 3:
                btnMethod01.setBackgroundResource(R.drawable.btn_bg_white_01_round_left_10);
                btnMethod02.setBackgroundResource(R.drawable.btn_bg_blue_01_round_0);
                btnMethod03.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
                btnMethod04.setBackgroundResource(R.drawable.btn_bg_white_01_round_right_10);
                btnMethod01.setTextColor(Color.parseColor("#165997"));
                btnMethod02.setTextColor(Color.parseColor("#FFFFFF"));
                btnMethod03.setTextColor(Color.parseColor("#165997"));
                btnMethod04.setTextColor(Color.parseColor("#165997"));
                mCurZoomLevel = 11.795328;
                break;
            case 5:
                btnMethod01.setBackgroundResource(R.drawable.btn_bg_white_01_round_left_10);
                btnMethod02.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
                btnMethod03.setBackgroundResource(R.drawable.btn_bg_blue_01_round_0);
                btnMethod04.setBackgroundResource(R.drawable.btn_bg_white_01_round_right_10);
                btnMethod01.setTextColor(Color.parseColor("#165997"));
                btnMethod02.setTextColor(Color.parseColor("#165997"));
                btnMethod03.setTextColor(Color.parseColor("#FFFFFF"));
                btnMethod04.setTextColor(Color.parseColor("#165997"));
                mCurZoomLevel = 11.078748;
                break;
            case 10:
                btnMethod01.setBackgroundResource(R.drawable.btn_bg_white_01_round_left_10);
                btnMethod02.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
                btnMethod03.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
                btnMethod04.setBackgroundResource(R.drawable.btn_bg_blue_01_round_right_10);
                btnMethod01.setTextColor(Color.parseColor("#165997"));
                btnMethod02.setTextColor(Color.parseColor("#165997"));
                btnMethod03.setTextColor(Color.parseColor("#165997"));
                btnMethod04.setTextColor(Color.parseColor("#FFFFFF"));
                mCurZoomLevel = 10.099580;
                break;

        }
    }

    /**
     * 우측하단, 추가버튼 메뉴감추기
     */
    private void hideAddMenuRight() {
//        btnFloat03.setVisibility(View.VISIBLE);
//
//        hideViewWithAnimation(btnAddMenu01, R.anim.out_down);
//        hideViewWithAnimation(btnAddMenu02, R.anim.out_down);
//        hideViewWithAnimation(btnAddMenu03, R.anim.out_down);
//        //hideViewWithAnimation(btnAddMenu04, R.anim.out_down);
//        hideViewWithAnimation(btnAddMenu05, R.anim.out_down);
//        hideViewWithAnimation(btnSetting, R.anim.out_right);
//
//        hideViewWithAnimation(btnStatus01, R.anim.out_down);
//        hideViewWithAnimation(btnStatus02, R.anim.out_down);
//        hideViewWithAnimation(btnStatus03, R.anim.out_down);
//        hideViewWithAnimation(btnStatus04, R.anim.out_down);
//        hideViewWithAnimation(btnStatus05, R.anim.out_down);
//        hideViewWithAnimation(btnStatus06, R.anim.out_down);
//        hideViewWithAnimation(btnStatus07, R.anim.out_down);
//        hideViewWithAnimation(btnStatus08, R.anim.out_down);
//
//
//        layoutBgForAddButton.setVisibility(View.GONE);
//
//        // 필터링 정보가 변경되었을때만 새로고침하자..
//        // 새로고침..
//        refreshAction();
    }

    /**
     * CVO 차량 최종위치 조회(차량)
     */
    public void requestCvoNowPosition() {
        try {

            // 위치정보가 없으면 리턴...
            //if (AppSetting.GPS_MANAGER.getGpsLat()<=0) return;

            showProgress(true);

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            if (AppSetting.cvoUserInfo.getUSERTYPE().equals(UserType._배송기사)) {
                paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
                paraMap.put("@I_DEVICENO", AppSetting.cvoUserInfo.getDEVICENO()); // 기사는 자기자산만 조회한다..
                paraMap.put("@I_VEHICLENO", "");
                paraMap.put("@I_KEYWORD", actvKeyword.getText().toString());
                paraMap.put("@I_LAT", Double.toString(mCurrentCoord.latitude));
                paraMap.put("@I_LON", Double.toString(mCurrentCoord.longitude));
                paraMap.put("@I_INPUT_USER", AppSetting.cvoUserInfo.getNAME());
            } else {
                paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
                paraMap.put("@I_DEVICENO", "");
                paraMap.put("@I_VEHICLENO", "");
                paraMap.put("@I_KEYWORD", actvKeyword.getText().toString());
                paraMap.put("@I_LAT", Double.toString(mCurrentCoord.latitude));
                paraMap.put("@I_LON", Double.toString(mCurrentCoord.longitude));
                paraMap.put("@I_INPUT_USER", AppSetting.cvoUserInfo.getNAME());
            }


            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_NOWPOSITION_SEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoNowPositionCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            showProgress(false);
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - CVO 현위치 조회
     */
    private Callback requestCvoNowPositionCallback = new Callback() {
        String tag = "requestCvoNowPositionCallback";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, tag + "---- onFailure:" + e.toString());
            showProgress(false);
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                showProgress(false);
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();
                return;
            }
            Loggers.d2(this, tag + ", ---- onResponse");

            String strJsonOutput = response.body().string();

            Loggers.d2(this, tag +",수신데이타:" + strJsonOutput);

            JsonElement jelement = new JsonParser().parse(strJsonOutput);
            JsonObject recvObj = jelement.getAsJsonObject();

            JsonArray jarrayShif = recvObj.getAsJsonArray("RecordSet");

            // 기존 정보 삭제
            CvoNowPositionListManager.getInstance().clear();

            for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                Gson gson = new Gson();
                CvoNowPosition info = gson.fromJson(jobject, CvoNowPosition.class);
                // 신규 정보 추가
                CvoNowPositionListManager.getInstance().add(info);
            }


            handler.obtainMessage(CVO_NOWPOSITION_SEL, "").sendToTarget();
        }
    };

    /**
     * ERP 주변 거래처 조회
     */
    public CircleOverlay mCircleOverlay = null;
    public void requestErpClientSel(int distance) {
        try {

            // showProgress(true);

            // 기존 거래처 마커 지우기
            for(Marker marker : arrCustMarkers) { marker.setMap(null); }

            // 가로스크롤 지우기
            btnFoldYn.setVisibility(View.GONE);
            scHScrollView.setVisibility(View.GONE);
            llHScrollItemArea.removeAllViews();

            // 반경원을 그린다.
            if (mCircleOverlay != null) mCircleOverlay.setMap(null);
            mCircleOverlay = drawCirclePoint(mCurrentCoord, distance*1000, "#5CFFBC00", "#52FF9300", mNaverMap);

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_DEVICENO", AppSetting.cvoUserInfo.getDEVICENO()); // 기사는 자기자산만 조회한다..
            paraMap.put("@I_BOUND_DIST", String.format("%d", distance)); // 반경거리
            paraMap.put("@I_KEYWORD", actvKeyword.getText().toString());
            paraMap.put("@I_CENTER_LAT", Double.toString(mCurrentCoord.latitude));
            paraMap.put("@I_CENTER_LON", Double.toString(mCurrentCoord.longitude));

            RequesterSession.with().RestAPIServiceCall(Svc.ERP_CLIENT_SEL, paraMap, requestErpClientSelCallback);

        } catch (Exception ex) {
            showProgress(false);
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - ERP 현위치 조회
     */
    private Callback requestErpClientSelCallback = new Callback() {
        String tag = "requestErpClientSelCallback";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, tag + "---- onFailure:" + e.toString());
            showProgress(false);
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                showProgress(false);
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();
                return;
            }
            Loggers.d2(this, tag + ", ---- onResponse");

            String strJsonOutput = response.body().string();

            Loggers.d2(this, tag +",수신데이타:" + strJsonOutput);

            JsonElement jelement = new JsonParser().parse(strJsonOutput);
            JsonObject recvObj = jelement.getAsJsonObject();

            JsonArray jarrayShif = recvObj.getAsJsonArray("RecordSet");

            // 기존 정보 삭제
            ClientCustManager.getInstance().clear();

            for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                Gson gson = new Gson();
                ClientCustInfo info = gson.fromJson(jobject, ClientCustInfo.class);
                // 신규 정보 추가
                ClientCustManager.getInstance().add(info);
            }


            handler.obtainMessage(ERP_CLIENT_SEL, "").sendToTarget();
        }
    };




    /**
     * 차량경로조회 화면 호출
     * @param deviceNo 단말기번호
     */
    public void openRouteInfo(String deviceNo) {
        try {
            Intent intent = new Intent(mContext, RouteSearchActivity.class);
            String startDtm = CommonUtil.with().nowDiffHour(-6, "yyyyMMddHH") + "0000"; // 6시간이전
            String endDtm = CommonUtil.with().nowYYYYMMDDHHMMSS().substring(0,8) + "235959";
            intent.putExtra("TRACE_START_DTM", startDtm);
            intent.putExtra("TRACE_END_DTM", endDtm);
            intent.putExtra("DEVICENO", deviceNo);
//            intent.putExtra("MIN_NO", "01075915736");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.no_change); // 위로 슬라이드..
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 거래처 카메라 호출하기...
     * @param ddate
     * @param clcode
     */
    DeliveryListInfo mDeliveryInfo = null;
    public void takePicture4Client(String ddate, String clCode) {
        try {
            // 거래처 코드 저장..
            mDeliveryInfo = new DeliveryListInfo();
            mDeliveryInfo.setDDATE(ddate);
            mDeliveryInfo.setCLCODE(clCode);

            Loggers.d("ddate=" + ddate + ", clCode=" + clCode);
            // 카메라 호출
            runCamera();
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
            mTempFile = new File(savePath, "/" + mDeliveryInfo.getCLCODE() + "_" + mDeliveryInfo.getDDATE() + "_" + CommonUtil.getSystemTime() + ".jpg");
            Loggers.d("FilePath=" + mTempFile.getPath());
            // 3.카메라 준비..
            mImageCaptureUri = FileProvider.getUriForFile(mContext, "com.gzonesoft.sg623", mTempFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            // 4.카메라 호출..
            startActivityForResult(takePictureIntent, ACT_TAKE_PIC);
        }
    }

    /**
     * 사진앨범 호출..
     */
    public void runGallery() {

        // 팝업닫기...
        ((PopCommonActivity) PopCommonActivity.mContext).FinishWork();

        // 앨범불러오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_GALLERY);
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

                    ImageResizeUtils.resizeFile(mTempFile, mTempFile, 1280, isCamera);

//                    scMainTopArea.setDrawingCacheEnabled(true);
//                    Bitmap bmpLocationInfo = scMainTopArea.getDrawingCache();
//                    ImageResizeUtils.attachImage(mTempFile, mTempFile, 1280, isCamera, bmpLocationInfo);
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
     * @param ddate
     * @param clCode
     * @param docType
     * @return
     */
    private String getSaveFileName(String ddate, String clCode, String docType) {
        String nowHHMMSS = CommonUtil.with().nowHHMMSS();

        return Svc.CVO_COMPANYCD + "_" +
                "" + ddate + "_" +
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
                    mDeliveryInfo.getDDATE().replace("-", ""),
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
     * CVO - 납품이미지 조회
     * @param ddate
     * @param clCode
     */
    public void requestCvoDeliveryImage(String ddate, String clCode) {
        try {


            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_DDATE", ddate);
            paraMap.put("@I_CLCODE", clCode);
            paraMap.put("@I_CLNAME", mCurCust.getCLNAME());

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_CLIENTIMAGE_SEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoDeliveryImageCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - 납품이미지 조회
     */
    private Callback requestCvoDeliveryImageCallback = new Callback() {
        String tag = "requestCvoDeliveryImageCallback";

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
            mDeliveryImageInfos.clear();

            for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                Gson gson = new Gson();
                DeliveryImageInfo info = gson.fromJson(jobject, DeliveryImageInfo.class);
                // 정보 추가
                mDeliveryImageInfos.add(info);
            }

            handler.obtainMessage(CVO_CLIENTIMAGE_SEL, "").sendToTarget();

        }
    };

    /**
     * 이미지 상세보기(웹뷰활용)
     * @param ddate
     * @param clCode
     */
    public void viewItemImage(String ddate, String clCode, String clName) {
        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
        intent.putExtra("MOVE_TYPE", CommType._납품사진이미지);
        intent.putExtra("DDATE", ddate);
        intent.putExtra("CLCOCE", clCode);
        intent.putExtra("CLNAME", clName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);

        // openPopYn = true; // 화면갱신 자동실행을 막기위해..
    }


    /**
     * CVO - 물류센터 조회
     */
    public void requestCvoSpotSel() {
        try {


            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_BOUND_DIST", "");
            paraMap.put("@I_KEYWORD", "");
            paraMap.put("@I_CENTER_LAT", "");
            paraMap.put("@I_CENTER_LON", "");

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_623_SPOT_SEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoSpotSelCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - CVO - 물류센터 조회
     */
    private Callback requestCvoSpotSelCallback = new Callback() {
        String tag = "requestCvoSpotSelCallback";

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

            // 기존정보 삭제
            mCvoSpotInfos.clear();

            for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                Gson gson = new Gson();
                CvoSpotInfo info = gson.fromJson(jobject, CvoSpotInfo.class);
                // 정보 추가
                mCvoSpotInfos.add(info);
            }

            handler.obtainMessage(CVO_623_SPOT_SEL, "").sendToTarget();

        }
    };


    /**
     * 팝업호출 - 센터출발..
     */
    public void popSpotStart(){
        try {
            String title = "'" + mNearSpot.getSPOT_NAME() + "'센터 출발";
            String message01 = "";
            String message02 = "<big><big><b>출발보고</b></big></big> 하시겠습니까?";

            Intent intent = new Intent(getApplicationContext(), PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName()); //  현재 엑티비티명 확인
            intent.putExtra("REQ_POP_CODE", WorkStatus._센터출발);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "출발보고취소");
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 공통팝업 콜백
     */
    public void popUpCallback(String p_req_pop_code) {
        try {
            // 1.사운드 재생
            playSound();

            // 2.팝업유형에 따른 작업처리..
            String htmlString = "";
            if(p_req_pop_code.equals(WorkStatus._센터출발)) {
                requestCvoSpotStart();
            }

            // 3.팝업닫기
            ((PopCommonActivity) PopCommonActivity.mContext).FinishWork();
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
     * CVO - 센터출발 보고..
     */
    public void requestCvoSpotStart() {
        try {


            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_COMPANY_NAME", AppSetting.cvoUserInfo.getCOMPANY_NAME());
            paraMap.put("@I_DEVICE_NO", AppSetting.cvoUserInfo.getDEVICENO());
            paraMap.put("@I_FROM_SPOT_CD", mNearSpot.getSPOT_CD() ); // 근처 센터코드
            paraMap.put("@I_TO_SPOT_CD", "0000000004"); // 목적지 센터코드
            paraMap.put("@I_LAT", "");
            paraMap.put("@I_LON", "");

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_623_SPOT_START),
                    "POST",
                    "",
                    paraMap,
                    requestCvoSpotStartCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - CVO - 센터출발 보고
     */
    private Callback requestCvoSpotStartCallback = new Callback() {
        String tag = "requestCvoSpotStartCallback";

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

            handler.obtainMessage(CVO_623_SPOT_START, "").sendToTarget();

        }
    };



}