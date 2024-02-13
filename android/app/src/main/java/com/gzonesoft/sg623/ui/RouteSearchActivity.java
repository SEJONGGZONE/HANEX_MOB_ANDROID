package com.gzonesoft.sg623.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.CvoTraceInfo;
import com.gzonesoft.sg623.domain.MarkerType;
import com.gzonesoft.sg623.domain.SearchMethod;
import com.gzonesoft.sg623.util.CommonUtil;
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
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RouteSearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = this.getClass().getSimpleName();;

    public static Context mContext = null;

    // NaverMap
    private MapView mMapView;
    private static NaverMap mNaverMap;


    // 마커이미지 레이아웃뷰...
    private FrameLayout fl_trace_marker;
    private LinearLayout llTruckInfo;
    private TextView tvTraceTitle;
    private ImageView ivTruckDir;
    //
    private FrameLayout fl_cur_trace_marker;
    private TextView tvTruckSubTitle, tvTruckTitle1, tvTruckTitle2;


    // 화면뷰...
    private Button btnBack;
    private Button btnStartDateTime, btnEndDateTime, btnMapType01, btnMapType02;
    private TextView tvTraceInfo;
    private Button btnFloat01, btnFloat02, btnPlay;
    private SeekBar seekPosition;
    private ScalableLayout scDateTimeArea, scMenuRightTop, scMenuLeftTop, scTitleArea, scRouteInfo;

    // 달력+시간 컨트롤 관련
    private LinearLayout ll_select_start_date;
    private DatePicker simpleDatePicker;
    private TimePicker simpleTimePicker;
    private Button btn_date_ok, btn_date_cancel;
    int  nDateType = 0; // 0-시작일자, 1-종료일자
    String startDate = CommonUtil.with().now();     // 1시간전으로 세팅해줘야하는데....
    String endDate = CommonUtil.with().now();

    // 경로조회정보 저장..
    String mTraceStartDtm = "";
    String mTraceEndDtm = "";
    String mDeviceNo = "";

    public boolean mFirstYn = true;

    // 경로정보 배열객체
    public static ArrayList<CvoTraceInfo> traceInfos = new ArrayList<CvoTraceInfo>();
    // 경로마커 배열객체
    public ArrayList<Marker> arrTraceInfoMarkers = new ArrayList<Marker>();
    public Marker curTraceMarker = new Marker();
    // 경로 재생관련
    private boolean mPlayYn = false;

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스
    public static final int LIST_MESSAGE = 20000;   // 재처리 프로세스
    public static final int CVO_TRACE_SEL = 30001;   // 경로상세 조회 결과
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            double lat, lon;

            switch (msg.what) {

                case CVO_TRACE_SEL: // 경로상세 조회 결과 완료시..

                    showProgress(false);

                    if (traceInfos.size()>0) {
                        // 차량마커 추가..
                        setVehicleMarker(traceInfos);
                    }

                    nPlayIndex = 0;

                    break;

                case RETRY_PROCESS:
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
     * 차량마커 추가
     * @param arrData 차량데이타
     */
    double totDistance = 0;
    public void setVehicleMarker(ArrayList<CvoTraceInfo> arrData) {

        clearMarker(MarkerType._마커_전체);

        // 기존 경로지우기
        removePath();

        ArrayList<LatLng> paths = new ArrayList<LatLng>();
        for (CvoTraceInfo info : arrData) {
            try {
                // 마커추가..
                arrTraceInfoMarkers.add(
                        addTruckMarker(info)
                );
                // 경로정보 추가
                Double lat = Double.parseDouble(info.getLATITUDE());
                Double lon = Double.parseDouble(info.getLONGITUDE());
                paths.add(new LatLng(lat, lon));

                if (info.getINTERVALDISTANCE().length()>0) {
                    totDistance += Double.parseDouble(info.getINTERVALDISTANCE());
                }
            } catch (Exception ex) {
                Loggers.d(info.toString());
                ex.printStackTrace();
            }
        }

        if (paths.size()>1) {
            // 경로그리기
            drawPath(paths);
            // 경로정보 표시
            String startDtm = traceInfos.get(0).getTIMESTAMP();
            String endDtm = traceInfos.get(paths.size()-1).getTIMESTAMP();

//            CommonUtil.with().nowDiffHour()

            String maxSpeed = traceInfos.get(0).getMAX_SPEED();
            String avgSpeed = traceInfos.get(0).getAVG_SPEED();
            String distance1 = traceInfos.get(0).getDISTANCE_01();
            String distance2 = traceInfos.get(0).getDISTANCE_02();
            String distance3 = traceInfos.get(0).getDISTANCE_03();
            String durationInfo = traceInfos.get(0).getDURATION_INFO();

            //
            distance1 = String.format("%.1f", CommonUtil.with().getTotDistance(paths)/1000);
            String htmlString = "" +
                    "거리 <font color='#FFFFFF'><big><big><b>" + distance1 + "</b></big></big>km</font> &nbsp;" +
                    "최고 <font color='#FFFFFF'><big><big><b>" + maxSpeed + "</b></big></big>km/h</font> &nbsp;" +
                    "소요 <font color='#FFFFFF'><big><big><b>" + durationInfo + "</b></big></big></font> &nbsp;" +
//                    "경로수 <font color='#FFFFFF'><big><big><b>" + String.format("%d", traceInfos.size()) + "</b></big></big></font>" +
                    " ";
            CommonUtil.with().setHtmlMarqueeText(tvTraceInfo, htmlString);
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
        orgMarker.setHideCollidedCaptions(true); // 겹치는 캡션 자동 숨김처리
//            marker.setHideCollidedMarkers(true); // 겹치는 마커 자동 숨김처리
        orgMarker.setHideCollidedSymbols(true); // 겹치는 지도심벌과의 자동 숨김처리

        orgMarker.setCaptionMinZoom(nCaptionLevel);
        orgMarker.setSubCaptionMinZoom(nSubCaptionLevel);

        return orgMarker;
    }

    /**
     * 경로 마커추가
     * @param info
     * @return
     */
    public Marker addTruckMarker(CvoTraceInfo info) {
        Marker marker = new Marker();
        boolean bFind = false;
        String htmlString = "";
        try {
            // 마커공통세팅
            marker = setSettingMarker(marker);
            // 마커위치(위/경도) 세팅
            Double lat = Double.parseDouble(info.getLATITUDE());
            Double lon = Double.parseDouble(info.getLONGITUDE());
            marker.setPosition(new LatLng(lat, lon));
            // 캡션세팅
            marker.setCaptionColor(Color.parseColor("#175B9B"));
            marker.setCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setCaptionTextSize(16);
            marker.setCaptionRequestedWidth(1000);
            marker.setCaptionText(info.getMARKER_CAPTION());
            // 서브캡션
            marker.setSubCaptionTextSize(15);
            marker.setSubCaptionRequestedWidth(800);
            marker.setSubCaptionColor(Color.parseColor("#3F80BA"));
            marker.setSubCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setSubCaptionText(info.getMARKER_SUBCAPTION());

            htmlString = info.getROUTE_TITLE();
            CommonUtil.with().setHtmlText(tvTraceTitle, htmlString);
            // 방향표시(회전)
            float fDir = Float.parseFloat(info.getDIRECTION());
            ivTruckDir.setRotation(fDir);


//            }
            // 마커클릭시..
            marker.setOnClickListener(overlay -> {
//                Toast.makeText(getApplicationContext(), info.getVEHICLE_NO() + ", 마커 클릭", Toast.LENGTH_SHORT).show();
//                // 마커클릭시 팝업
//                Intent intent = new Intent(getApplicationContext(), PopDetailVehicleActivity.class);
//                intent.putExtra("VEHICLE_NO", info.getVEHICLE_NO());
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, R.anim.no_change);
                return true;
            });
            // 마커이미지 세팅
            marker.setIcon(OverlayImage.fromView(fl_trace_marker));
            // 위치보정
            marker.setAnchor(new PointF(0.5f, 0.75f));
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
     * 현재 마커추가
     * @param info
     * @return
     */
    public Marker addCurTruckMarker(CvoTraceInfo info) {
        Marker marker = new Marker();
        boolean bFind = false;
        String sHTML = "";
        try {
            // 마커공통세팅
            marker = setSettingMarker(marker);
            // 마커위치(위/경도) 세팅
            Double lat = Double.parseDouble(info.getLATITUDE());
            Double lon = Double.parseDouble(info.getLONGITUDE());
            marker.setPosition(new LatLng(lat, lon));
            // 캡션세팅
            marker.setCaptionColor(Color.parseColor("#175B9B"));
            marker.setCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setCaptionTextSize(16);
            marker.setCaptionRequestedWidth(1000);
            marker.setCaptionText(info.getMARKER_CAPTION());
            // 서브캡션
            marker.setSubCaptionTextSize(15);
            marker.setSubCaptionRequestedWidth(800);
            marker.setSubCaptionColor(Color.parseColor("#3F80BA"));
            marker.setSubCaptionHaloColor(Color.parseColor("#FFFFFF"));
            marker.setSubCaptionText(info.getMARKER_SUBCAPTION());

            //tvTruckSubTitle.setText("[" + info.getRNK() + "] 전북86사9470");
//            String htmlString = "" +
//                    "<small>" +
//                    "[" + info.getRANK() + "] <b>" +
//                    info.getTIMESTAMP() +
//                    "</b>" +
//                    "</small>";
            CommonUtil.with().setHtmlText(tvTruckSubTitle, info.getMARKER_SUBCAPTION());
            tvTruckTitle1.setText(info.getSPEED());
            tvTruckTitle2.setText(info.getSTATUS_NM());


            // 마커클릭시..
            marker.setOnClickListener(overlay -> {
//                Toast.makeText(getApplicationContext(), info.getVEHICLE_NO() + ", 마커 클릭", Toast.LENGTH_SHORT).show();
//                // 마커클릭시 팝업
//                Intent intent = new Intent(getApplicationContext(), PopDetailVehicleActivity.class);
//                intent.putExtra("VEHICLE_NO", info.getVEHICLE_NO());
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, R.anim.no_change);
                return true;
            });

            // 마커이미지 세팅
            marker.setIcon(OverlayImage.fromView(fl_cur_trace_marker));
            // 위치보정
            marker.setAnchor(new PointF(0.5f, 0.85f));
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
     * 현재위치 표시하기
     * @param nIdx
     */
    public void setCurPosition(int nIdx) {
        try {
            if (traceInfos.size()>0) {
                // 이전마커지우기
                if (curTraceMarker != null) curTraceMarker.setMap(null);
                // 새로운 마커표시
                curTraceMarker = addCurTruckMarker(traceInfos.get(nIdx));
                curTraceMarker.setZIndex(10000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 경로위치 플레이하기
     */
    private long playDelayTime = 100;
    public int nPlayIndex = 0;
    public void routePlayStart() {

        if (traceInfos.size()>0) {

            if (traceInfos.size() == nPlayIndex) {
                nPlayIndex = 0;
                mPlayYn = false;
                btnPlay.setText("▶");
                return;
            }
            // 현재위치 표시하기...
            setCurPosition(nPlayIndex);

            if (mPlayYn) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        nPlayIndex++;
                        routePlayStart();
                    }
                }, playDelayTime);
            }
        }
    }

    /**
     * 경로위치 플레이 정지하기
     */
    public void routePlayStop() {
        mPlayYn = false;
//        if (mPlayYn) {
//        } else {
//            mPlayYn = true;
//        }
    }

    public PathOverlay mPath = new PathOverlay();

    /**
     * 경로 그리기
     * @param routePoints
     */
    private void drawPath(ArrayList<LatLng> routePoints) {
        try {
            mPath.setCoords(routePoints);
            mPath.setWidth(30); // 경로 두께
            mPath.setColor(Color.parseColor("#b5ff6b6b")); // 경로 색상
            mPath.setOutlineWidth(1); // 테두리 두께
            mPath.setOutlineColor(Color.parseColor("#99385C9B")); // 테두리 색상
            mPath.setMap(mNaverMap);

            // 경로영역으로 지도 크기조정
            setBoundsMap(routePoints);

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
        CameraUpdate cameraUpdate = CameraUpdate.withParams(cu).fitBounds(getPointBouds(routePoints), 100).animate(animation, 1500);

        mNaverMap.moveCamera(cameraUpdate);

    }
    // 파라미터가 없는경우..현재경로정보로 세팅한다..
    public void setBoundsMap() {
        if (traceInfos.size()>0) {

            ArrayList<LatLng> paths = new ArrayList<LatLng>();
            for (CvoTraceInfo info : traceInfos) {
                try {
                    // 경로정보 추가
                    Double lat = Double.parseDouble(info.getLATITUDE());
                    Double lon = Double.parseDouble(info.getLONGITUDE());
                    paths.add(new LatLng(lat, lon));
                } catch (Exception ex) {
                    Loggers.d(info.toString());
                    ex.printStackTrace();
                }
            }
            // 경로영역으로 지도 크기조정
            setBoundsMap(paths);

            // 모든 마커 지우기
            clearMarker(MarkerType._마커_전체);
        }
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
                    if (sLat >= point.latitude) sLat = point.latitude;
                    if (nLat < point.latitude) nLat = point.latitude;
                    if (wLng >= point.longitude) wLng = point.longitude;
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
     * 경로 지우기
     */
    private void removePath() {
        mPath.setMap(null);
    }

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
//                case MarkerType._차량:
//                    if (arrVehicleMarkers.size() >0) {
//                        for(Marker marker : arrVehicleMarkers) {
//                            marker.setMap(null);
//                        }
//                    }
//                    break;
                case MarkerType._마커_전체:
//                    for(Marker marker : arrCustMarkers) { marker.setMap(null); }
//                    for(Marker marker : arrOrderMarkers) { marker.setMap(null); }
//                    for(Marker marker : arrSourceMarkers) { marker.setMap(null); }
                    for(Marker marker : arrTraceInfoMarkers) { marker.setMap(null); }
//                    arrCustMarkers.clear();
//                    arrOrderMarkers.clear();
//                    arrSourceMarkers.clear();
                    arrTraceInfoMarkers.clear();

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mContext = this;

        // 넘어온 데이타 확인
        mTraceStartDtm = String.valueOf(getIntent().getStringExtra("TRACE_START_DTM"));
        mTraceEndDtm = String.valueOf(getIntent().getStringExtra("TRACE_END_DTM"));
        mDeviceNo = String.valueOf(getIntent().getStringExtra("DEVICENO"));


        // UI초기화
        initUI();

        // 프로그래스다이얼로그 초기화
        initProgressDlg();

        // Map초기화
        initMap(savedInstanceState);

        // 기본갑셋팅..
        setViewTypeButton(SearchMethod._지도보기유형_전체);

        // 경로조회 시간정보 버튼 세팅
        setTraceStartEndDtmButtonText();

        // 경로조회 서비스 호출
        traceSearch(mTraceStartDtm, mTraceEndDtm, mDeviceNo);


    }

    /**
     * 사용자 설정 액티비티 호출
     */
//    public void openUserSettings() {
//        // 사용자 설정 액티비티
//        Intent intent = new Intent(getApplicationContext(), UserSettingActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
//    }
    /**
     * UI초기화
     */
    private void initUI() {

        setContentView(R.layout.activity_route_search);
        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 트럭 마커
        fl_trace_marker = (FrameLayout) findViewById(R.id.fl_trace_marker);
        llTruckInfo = (LinearLayout) findViewById(R.id.llTruckInfo);
        tvTraceTitle = (TextView) findViewById(R.id.tvTraceTitle);
        ivTruckDir = (ImageView) findViewById(R.id.ivTruckDir);
        // 순간 마커
        fl_cur_trace_marker = (FrameLayout) findViewById(R.id.fl_cur_trace_marker);
        tvTruckSubTitle = (TextView) findViewById(R.id.tvTruckSubTitle);
        tvTruckTitle1 = (TextView) findViewById(R.id.tvTruckTitle1);
        tvTruckTitle2 = (TextView) findViewById(R.id.tvTruckTitle2);

        btnMapType01 = (Button) findViewById(R.id.btnMapType01);
        btnMapType02 = (Button) findViewById(R.id.btnMapType02);

        // 뒤로가기 버튼 세팅
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_right, R.anim.no_change);
            }
        });

        // 검색 시작일시 버튼 세팅
        btnStartDateTime = (Button) findViewById(R.id.btnStartDateTime);
        btnStartDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nDateType = 0; // 0-시작일자, 1-종료일자
                ll_select_start_date.setVisibility(View.VISIBLE);
                ll_select_start_date.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_up));

                setDateTimePicker(
                        Integer.valueOf(mTraceStartDtm.substring(0,4)),
                        Integer.valueOf(mTraceStartDtm.substring(4,6)),
                        Integer.valueOf(mTraceStartDtm.substring(6,8)),
                        Integer.valueOf(mTraceStartDtm.substring(8,10)),
                        Integer.valueOf(mTraceStartDtm.substring(10,12))
                );
            }
        });
        // 검색 종료일시 버튼 세팅
        btnEndDateTime = (Button) findViewById(R.id.btnEndDateTime);
        btnEndDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nDateType = 1; // 0-시작일자, 1-종료일자
                ll_select_start_date.setVisibility(View.VISIBLE);
                ll_select_start_date.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_up));

                setDateTimePicker(
                        Integer.valueOf(mTraceEndDtm.substring(0,4)),
                        Integer.valueOf(mTraceEndDtm.substring(4,6)),
                        Integer.valueOf(mTraceEndDtm.substring(6,8)),
                        Integer.valueOf(mTraceEndDtm.substring(8,10)),
                        Integer.valueOf(mTraceEndDtm.substring(10,12))
                );
            }
        });

        btnFloat01 = (Button) findViewById(R.id.btnFloat01);
        btnFloat01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnFloat02 = (Button) findViewById(R.id.btnFloat02);
        btnFloat02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 경로조회 서비스 호출
                traceSearch(mTraceStartDtm, mTraceEndDtm, mDeviceNo);
            }
        });

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 모든 마커 지우기
                clearMarker(MarkerType._마커_전체);

                if (mPlayYn) {
                    mPlayYn = false;
                    btnPlay.setText("▶");
                    // 경로재생 정지
                    routePlayStop();
                } else {
                    mPlayYn = true;
                    btnPlay.setText("| |");
                    // 경로영역으로 지도 크기조정
                    setBoundsMap();
                    // 경로재생 시작..
                    routePlayStart();
                }
            }
        });



        tvTraceInfo = (TextView) findViewById(R.id.tvTraceInfo);
        seekPosition = (SeekBar) findViewById(R.id.seekPosition);
        // OnSeekBarChange 리스너 - Seekbar 값 변경시 이벤트처리 Listener
        seekPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // onProgressChange - Seekbar 값 변경될때마다 호출
                try {
                    int totalCnt = traceInfos.size();
                    int curVal = Integer.parseInt(String.format("%.0f", totalCnt*progress*0.01));
//                    Log.d(TAG, String.format("onProgressChanged 값 변경 중 : progress [%d] fromUser [%b]", curVal, fromUser));

                    // 현재위치 표시하기...
                    setCurPosition(curVal);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // onStartTeackingTouch - SeekBar 값 변경위해 첫 눌림에 호출
//                Log.d(TAG, String.format("onStartTrackingTouch 값 변경 시작 : progress [%d]", seekBar.getProgress()));
                // 모든 마커 지우기
                clearMarker(MarkerType._마커_전체);

                // 메뉴보였다 사라졌다 처리...
                if (bShowMenuYn) {
                    bShowMenuYn = false;
                    hideMenuButton();
                } else {
                    bShowMenuYn = true;
                    showMenuButton();
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // onStopTrackingTouch - SeekBar 값 변경 끝나고 드래그 떼면 호출
//                Log.d(TAG, String.format("onStopTrackingTouch 값 변경 종료: progress [%d]", seekBar.getProgress()));
                // 메뉴보였다 사라졌다 처리...
                if (bShowMenuYn) {
                    bShowMenuYn = false;
                    hideMenuButton();
                } else {
                    bShowMenuYn = true;
                    showMenuButton();
                }
            }
        });

        scDateTimeArea = (ScalableLayout) findViewById(R.id.scDateTimeArea);
        scMenuRightTop = (ScalableLayout) findViewById(R.id.scMenuRightTop);
        scMenuLeftTop = (ScalableLayout) findViewById(R.id.scMenuLeftTop);
        scTitleArea = (ScalableLayout) findViewById(R.id.scTitleArea);
        scRouteInfo = (ScalableLayout) findViewById(R.id.scRouteInfo);

        // 날짜 컨트롤 관련 ..
        ll_select_start_date = (LinearLayout) findViewById(R.id.ll_select_start_date);
        ll_select_start_date.setVisibility(View.INVISIBLE);
        // 날짜 선택에서 확인
        btn_date_ok = (Button) findViewById(R.id.btn_date_ok);
        btn_date_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year =  simpleDatePicker.getYear();
                int month = simpleDatePicker.getMonth()+1;
                int day = simpleDatePicker.getDayOfMonth();
                int hour = simpleTimePicker.getHour();
                int minute = simpleTimePicker.getMinute()*10; // 10분간격 세팅이므로 곱해준다...

                Loggers.d("year = " + year);
                Loggers.d("month = " + month);
                Loggers.d("day = " + day);
                Loggers.d("hour = " + hour);
                Loggers.d("minute = " + minute);

                String date_day = "", date_month="", time_hour="", time_minute="";

                date_month = (month <= 9) ? "0" + month : month + "";
                date_day = (day < 10) ? "0" + day: day + "";
                time_hour = (hour < 10) ? "0" + hour: hour + "";
                time_minute = (minute < 10) ? "0" + minute: minute + "";

                String dateTimeString = year + date_month + date_day + time_hour + time_minute + "00";
                Loggers.d("dateTimeString = " + dateTimeString);

                if (nDateType == 0) {
                    // 시작일시
                    mTraceStartDtm = dateTimeString;
                } else {
                    // 종료일시
                    mTraceEndDtm = dateTimeString;
                }
                // 감추기
                ll_select_start_date.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_down));
                ll_select_start_date.setVisibility(View.INVISIBLE);

                // 경로조회 시간정보 버튼 세팅
                setTraceStartEndDtmButtonText();

                // 경로조회 서비스 호출
                traceSearch(mTraceStartDtm, mTraceEndDtm, mDeviceNo);


            }
        });
        // 날짜 선택에서 취소
        btn_date_cancel = (Button) findViewById(R.id.btn_date_cancel);
        btn_date_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_select_start_date.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_down));
                ll_select_start_date.setVisibility(View.INVISIBLE);
            }
        });
        // 날짜
        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        // 시간
        simpleTimePicker = (TimePicker) findViewById(R.id.simpleTimePicker);

        setTimePickerInterval(simpleTimePicker, 10);
    }

    /**
     * Set TimePicker interval by adding a custom minutes list
     *
     * @param timePicker
     */
    private void setTimePickerInterval(TimePicker timePicker, int TIME_PICKER_INTERVAL) {
        try {

            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    /**
     * 경로조회 시간정보 버튼 세팅
     */
    public void setTraceStartEndDtmButtonText() {
        CommonUtil.with().setHtmlMarqueeText(btnStartDateTime, CommonUtil.with().getMM_DD_HH_MM(mTraceStartDtm));
        CommonUtil.with().setHtmlMarqueeText(btnEndDateTime, CommonUtil.with().getMM_DD_HH_MM(mTraceEndDtm));
    }
    /**
     * 날짜시간 피커 세팅
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     */
    public void setDateTimePicker(int year, int month, int day, int hour, int minute) {
        simpleDatePicker.init(year, month-1, day, null);
        simpleTimePicker.setHour(hour);
        simpleTimePicker.setMinute(minute);
    }

    /**
     * 유형 설정하기..
     * @param nTag
     */
    public String curViewType = String.format("%d", SearchMethod._지도보기유형_전체);
    private void setViewTypeButton(int nTag) {

        clearMarker(MarkerType._차량);

        switch (nTag) {
            case SearchMethod._지도보기유형_차량:
//                scMenuRightTop.setVisibility(View.GONE); // 주문필터링 감추기
//                btnViewType01.setBackgroundResource(R.drawable.btn_bg_white_01_round_top_10);
//                btnViewType02.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
//                btnViewType03.setBackgroundResource(R.drawable.btn_bg_white_01_round_0);
//                btnViewType04.setBackgroundResource(R.drawable.btn_bg_white_01_round_0_on);
//                btnViewType05.setBackgroundResource(R.drawable.btn_bg_white_01_round_bottom_10);
//                btnViewType01.setTextColor(Color.parseColor("#165997"));
//                btnViewType02.setTextColor(Color.parseColor("#165997"));
//                btnViewType03.setTextColor(Color.parseColor("#165997"));
//                btnViewType04.setTextColor(Color.parseColor("#FFFFFF"));
//                btnViewType05.setTextColor(Color.parseColor("#165997"));
//                curViewType = String.format("%d", SearchMethod._지도보기유형_차량);
//                if (mCurrentCoord != null) {
//                    // 새로고침 요청..액션
//                    refreshAction();
//                }
                break;
            default:
                break;
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
                    12, // 줌 레벨
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
            uiSettings.setZoomControlEnabled(true); // 줌 버튼
            uiSettings.setLocationButtonEnabled(false); // 현위치 버튼

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
        showMenuRightTop(); // 오른쪽위
        showMenuLeftTop(); // 왼쪽위
        showMenuLeftBottom(); // 왼쪽아래
        showMenuRightBottom(); // 오른쪽아래

        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true); // 줌 버튼(보이기)
    }

    /**
     * 메뉴버튼 감추기
     */
    private void hideMenuButton() {
        // 메뉴버튼 감추기
        hideSearchArea(); // 통합검색
        hideMenuRightTop(); // 오른쪽위
        hideMenuLeftTop(); // 왼쪽위
        hideMenuLeftBottom(); // 왼쪽아래
        hideMenuRightBottom(); // 오른쪽아래
        // 추가메뉴버튼이 보여지고있다면, 숨겨준다.
//        if (btnAddMenu05.getVisibility() == View.VISIBLE) hideAddMenuRight();

        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(false); // 줌 버튼(감추기)
    }
    /**
     * 통합검색 보이기
     */
    private void showSearchArea() {
//        showViewWithAnimation(scMenuTop, R.anim.in_down);
    }
    /**
     * 통합검색 감추기
     */
    private void hideSearchArea() {
//        hideViewWithAnimation(scMenuTop, R.anim.out_up);
    }

    /**
     * 오른쪽위 메뉴버튼 보이기
     */
    private void showMenuRightTop() {
        showViewWithAnimation(scMenuRightTop, R.anim.in_left);
    }

    /**
     * 오른쪽위 메뉴버튼 감추기
     */
    private void hideMenuRightTop() {
        hideViewWithAnimation(scMenuRightTop, R.anim.out_right);
    }

    /**
     * 왼쪽위 메뉴버튼 보이기
     */
    private void showMenuLeftTop() {
        showViewWithAnimation(scMenuLeftTop, R.anim.in_right);
        showViewWithAnimation(scTitleArea, R.anim.in_down);
        showViewWithAnimation(scDateTimeArea, R.anim.in_down);

    }

    /**
     * 왼쪽위 메뉴버튼 감추기
     */
    private void hideMenuLeftTop() {
        hideViewWithAnimation(scMenuLeftTop, R.anim.out_left);
        hideViewWithAnimation(scTitleArea, R.anim.out_up);
        hideViewWithAnimation(scDateTimeArea, R.anim.out_up);
    }

    /**
     * 왼쪽아래쪽 메뉴버튼 보이기
     */
    private void showMenuLeftBottom() {
//        showViewWithAnimation(btnFloat01, R.anim.in_right);
        showViewWithAnimation(btnFloat02, R.anim.in_up);
    }

    /**
     * 왼쪽아래쪽 메뉴버튼 감추기
     */
    private void hideMenuLeftBottom() {
//        hideViewWithAnimation(btnFloat01, R.anim.out_left);
        hideViewWithAnimation(btnFloat02, R.anim.out_down);
    }

    /**
     * 오른쪽아래쪽 메뉴버튼 보이기
     */
    private void showMenuRightBottom() {
        showViewWithAnimation(btnPlay, R.anim.in_left);
        showViewWithAnimation(scRouteInfo, R.anim.in_up);
    }

    /**
     * 오른쪽아래쪽 메뉴버튼 감추기
     */
    private void hideMenuRightBottom() {
        hideViewWithAnimation(btnPlay, R.anim.out_right);
        hideViewWithAnimation(scRouteInfo, R.anim.out_down);
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

            // 이동이 마친후 발생...
            naverMap.addOnCameraIdleListener(() -> {
                CameraPosition position = naverMap.getCameraPosition();
                Loggers.d(String.format("카메라정보 = %f / %f / %f / %f / %f",
                        position.target.latitude, position.target.longitude,
                        position.tilt, position.bearing, position.zoom));

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

            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 지도줌레벨 리턴
     * @return
     */
//    public double getZoomLevel() {
//        double fRet = 0.0f;
//        try {
//            if (mNaverMap != null) {
//                CameraPosition position = mNaverMap.getCameraPosition();
//                fRet = position.zoom;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return fRet;
//    }

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


    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
        slideLeftDelayTime = slideLeftDelayTime + 100;
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


    /**
     * 경로상세 조회
     */
    public void traceSearch(String traceStartDtm, String traceEndDtm, String minNo){
        try {

            showProgress(true);

            if (traceInfos.size()>0) traceInfos.clear();
            // 마커지우기-1, 경로마커
            clearMarker(MarkerType._마커_전체);
            // 마커지우기-2, 지정위치 마커
            if (curTraceMarker != null) curTraceMarker.setMap(null);

            Map<String, String> paraMap = new LinkedHashMap<String, String>();

            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_DEVICENO", minNo);
            paraMap.put("@I_TRACE_START_DTM", traceStartDtm);
            paraMap.put("@I_TRACE_END_DTM", traceEndDtm);

            //RequesterSession.with().RestAPIServiceCall(Svc.POST, Svc.TRACE_SEARCH, paraMap, traceSearchCallback);
            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_TRACE_SEL),
                    "POST",
                    "",
                    paraMap,
                    traceSearchCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            showProgress(false);
        }
    }


    /**
     * 콜백처리 - CVO 경로상세 조회
     */
    private Callback traceSearchCallback = new Callback() {
        String tag = "traceSearchCallback";

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

            JsonArray jarrayRecordSet = recvObj.getAsJsonArray("RecordSet");

            traceInfos.clear();

            for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
                JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
                Gson gson = new Gson();

                // 데이타 추가..
                CvoTraceInfo info = gson.fromJson(jobjectStore, CvoTraceInfo.class);
                traceInfos.add(info);
            }


            handler.obtainMessage(CVO_TRACE_SEL, "").sendToTarget();
        }
    };


}
