package com.gzonesoft.sg623.ui;

import static com.gzonesoft.sg623.comm.RequesterSession.REQ_OK_MESSAGE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

//import com.eksys.gis.sgmobile.co.Coord;
//import com.eksys.gis.streamingmap.observer.LocationSPref;
//import com.eksys.gis.streamingmap.observer.PathInfo;
//import com.eksys.gis.streamingmap.util.Path;
//import com.eksys.gis.streamingmap.util.SGMConfig;
//import com.eksys.gis.streamingmap.util.StateDefine;
//import com.eksys.gis.talmap.TMMapPos;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.gzonesoft.cookzzang.adapter.DeliveryListItemAdapter;
//import com.gzonesoft.cookzzang.data.DeliveryListInfo;
//import com.gzonesoft.cookzzang.data.ListMst;
//import com.gzonesoft.cookzzang.domain.BroadCastDomain;
//import com.gzonesoft.cookzzang.model.DeliveryListManager;
//import com.gzonesoft.cookzzang.service.MsgService;
//import com.gzonesoft.cookzzang.service.TraceService;
import com.gzonesoft.sg623.DriverMainActivity;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.data.ListMst;
import com.gzonesoft.sg623.domain.TodayStatus;
import com.gzonesoft.sg623.domain.WorkStatus;
import com.gzonesoft.sg623.model.DeliveryListManager;
import com.gzonesoft.sg623.pop.SlidePopClientDetailActivity;
import com.gzonesoft.sg623.ui.adapter.DeliveryListItemAdapter;
import com.gzonesoft.sg623.pop.PopCommonActivity;
import com.gzonesoft.sg623.util.Common;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;
import com.gzonesoft.sg623.util.Svc;
import com.gzonesoft.kt.cookzzang.KakaoService;
import com.skt.Tmap.TMapTapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import okhttp3.Response;

/**
 * 베송지 목록(업무메인)
 */
public class DeliveryListActivity extends Activity {
    private String tag = "배송지 목록";

    public static Context mContext = null;

    private LinearLayout mProgress; // 대기화면..

    // 백버튼 눌림횟수 저장(OnResume 이벤트를 받을 때마다 다시 초기화해준다.)
    private int mBackButtonCount = 0;

    // 별도 화면 핸들러 아이디는 10000번 대로 정의한다
    public static final int ERP_DISPATCH_SEL = 10000;
    public static final int CVO_DELIVERY_START = 10001;
    public static final int CVO_DELIVERY_SEL = 10002;



    // 최적화 실행 후 메시지
    public static final int MSG_ROUTE_OPTI = 10100;
    // 순서확정 후 메시지
    public static final int MSG_ROUTE_CONFIRM = 10101;
    // 점포출발 후 메시지
    public static final int MSG_ROUTE_START = 10102;

    public static final int POPMENU_EXIT = 20104;
    // 최종차수정보 수신후
    public static final int RET_LASTWORK_STAT = 10200;

    // 리스트하단 부가정보 표시 - 최적화 결과
    public static final int ADDINFO_ROUTE = 30001;
    public static final int ADDINFO_LOCATION = 30002;
    public static final int ADDINFO_GPS = 30003;


    // 애니메이션 효과
    private  Animation animationStart = null;
    private  Animation animationStop = null;

    // 리스트 뷰 관련...
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder mViewHolder;
    private DeliveryListItemAdapter deliveryListItemAdapter;
    private DeliveryListInfo lastDeptInfo = null;
    // 떠있는 버튼
    //private FloatingActionButton fabRefresh;    // 새로고침

    private LinearLayout llRouteResult; // 최적화 결과 표시
    private TextView tvAddedInfo; // 리스트 하단 부가정보 표시

    private Button btn_opti;        // 최적화 버튼
    private Button btn_confirm;     // 순서확정 버튼
    private Button btn_start;       // 점포출발 버튼
    private Button btnBack, btnSave, btnInit;
    private TextView tvInit;

    // 현재 선택된 아이템인덱스
    public int curItemIndex = -1;
    // 배송지 목록정보 저장
    private ArrayList<DeliveryListInfo> deliveryListItems = new ArrayList<DeliveryListInfo>();
    // 앱 종료체크
    private boolean mExitChk = false;
    // 최종 차수정보 인스턴스얻기
    public static ListMst getLastWorkListInstance() {
        if (mLastWorkList == null)
            mLastWorkList = new ListMst();

        return mLastWorkList;
    }
    private static ListMst mLastWorkList = null;


    // 화면 첫갱신여부 저장 - 초기로드 이후 재진입시 자동으로 배송목록 재조회 호출로직을 위해..
    private boolean bFirstScreen = true;
//    // 우상단 메뉴 호출기준 뷰
//    private LinearLayout root_layout;
//    // 우상단 메뉴 호출영역
//    private LinearLayout deliverylist_showmenu_ll;
    // 우상단 메뉴 호출관련 인텐트
    private Intent mIntent;
    // 배송목록 조회요청작업 진행여부
    public boolean bDeliveryList_Running = false;

    // 서버요청 처리결과를 위한 핸들러..
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CVO_DELIVERY_SEL:
                    mProgress.setVisibility(View.GONE);

                    // 배송목록 조회요청작업 진행여부 - 풀기
                    bDeliveryList_Running = false;
                    // 배송 리스트 데이타 리프레쉬
                    refreshListData();
                    // 화면데이타 세팅
                    updateUISetting();
                    break;

                case CVO_DELIVERY_START:
                    mProgress.setVisibility(View.GONE);

                    // 배송목록 조회요청작업 진행여부 - 풀기
                    bDeliveryList_Running = false;
                    // 배송 리스트 데이타 리프레쉬
                    refreshListData();
                    // 화면데이타 세팅
                    updateUISetting();

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
                // -------------------------------------------------------------------------------------------------------
                // 화면 핸들러 처리
                // -------------------------------------------------------------------------------------------------------
                case ERP_DISPATCH_SEL:  // 배송목록 조회 콜백후...
                    mProgress.setVisibility(View.GONE);

                    // 배송목록 조회요청작업 진행여부 - 풀기
                    bDeliveryList_Running = false;

                    // 배송 리스트 데이타 리프레쉬
                    refreshListData();

                    // 화면데이타 세팅
                    updateUISetting();

                    // 3.배송순서확정단계 체크.
//                    if (deliveryListItem.size() > 0) {
//                        if ("N".equals(mLastWorkList.getROUTE_OPTI_YN())) {
//                            Common.ShowMessage(DeliveryListActivity.this, tag, "최적화를 먼저 하세요.");
//                        } else {
//                            if ("N".equals(mLastWorkList.getROUTE_CONFIRM_YN())) {
//                                Common.ShowMessage(DeliveryListActivity.this, tag, "배송순서를 확정해주세요.");
//                            }
//                        }
//                    }
//
//                    // 4.최종 배송지 출발정보 업데이트
//                    lastDeptInfo = DeliveryListManager.getInstance().getLastDeptInfo();
//                    if (lastDeptInfo != null) {
//                        // 최종배송지 출발정보가 있다면..
//                        Loggers.d("최종 배송지 단축번호 : " + lastDeptInfo.getROUTE_IDX() + "-" + lastDeptInfo.getSORD_NO() + ",최종 배송지 출발시간 : " + lastDeptInfo.getAPP_DEP_TIME());
//                    }

                    bFirstScreen = false;

                    // 1초후 해당 배송건으로 자동 스크롤...
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int nextPosition = DeliveryListManager.getInstance().getNextDeliveryPosition();
                            if (nextPosition>=0) {
                                recyclerView.scrollToPosition(nextPosition);
                            }
                        }
                    }, 500);

                    break;

//                case RET_LASTWORK_STAT: // 최종차수정보 요청콜백후...
//
//                    // 버튼 및 상단 점포출발정보 세팅
//                    updateUISetting();
//
//                    // 배송 목록 요청
//                    bDeliveryList_Running = false;
//                    searchDeliveryMainList();
//
//                    break;
                // -------------------------------------------------------------------------------------------------------
                // 화면 메시지 처리
                // -------------------------------------------------------------------------------------------------------
//                case MSG_ROUTE_OPTI:    // 최적화 콜백처리후...
//
//                    // 최적화 결과 정보 표시
//                    setAddinInfoText(ADDINFO_ROUTE, msg.obj.toString());
//
//                    // 리스트 여백 조정...
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.MATCH_PARENT
//                    );
//                    params.setMargins(0, 0, 0, 25);
//                    recyclerView.setLayoutParams(params);
//
//                    // 최종차수정보 확인 - 목록갱신 포함됨...
//                    searchWorKStatus();
//
//                    // 점포출발 요청 메소드.
//                    //StartStore();
//
//                    break;
//                case MSG_ROUTE_CONFIRM: // 순서확정 콜백처리후..
//                    //Common.ShowMessage(DeliveryListActivity.this, tag, getString(R.string.ROUTE_CONFIRM_OK));
//                    CommonUtil.with().ToastMsg(mContext, getString(R.string.ROUTE_CONFIRM_OK), Toast.LENGTH_SHORT).show();
//
//                    // 최종차수정보 확인 - 목록갱신 포함됨...
//                    searchWorKStatus();
//
//                    break;
//
//                case MSG_ROUTE_START:   // 점포출발 콜백처리후...
//
//                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
//
//                    // 최종 보고정보 저장
//                    Common.saveLastReport("", "", SettingPref.with(mContext).loadPref(ConstValue._점포출발, ""));
//
//                    // 최종차수정보 확인 - 목록갱신 포함됨...
//                    searchWorKStatus();
//
//                    break;
                // -------------------------------------------------------------------------------------------------------
                // 공통 핸들러 처리
                // -------------------------------------------------------------------------------------------------------
                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    mProgress.setVisibility(View.GONE);
                    if (!"".equals(msg.obj.toString())) CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case RequesterSession.REQ_NOK:
                    mProgress.setVisibility(View.GONE);
                    if (!"".equals(msg.obj.toString())) CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case RequesterSession.REQ_OK_MESSAGE:
                    mProgress.setVisibility(View.GONE);
                    if (!"".equals(msg.obj.toString())) CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case RequesterSession.REQ_OK:
                    mProgress.setVisibility(View.GONE);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }

            if (!bDeliveryList_Running) {
                // 화면 갱신시간 대기...1초후 프로그래스 뷰 없애준다..
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.setVisibility(View.GONE);
//                        fabRefresh.setVisibility(View.VISIBLE);
//                        fabViewMap.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        }
    };

    // 서비스 관련
    private ComponentName traceService;
    private Intent traceServiceIntent;
    private ComponentName msgService;
    private Intent msgServiceIntent;
    private static final int JOB_ID_UPDATE = 0x1000;
    /**
     * 서비스 실행 (onCreate 내부 마지막에서 호출되며 전체 앱 실행주기상 1회만 호출된다.)
     */
    private void StartWorkService() {
        Loggers.d("DeliveryListActivity ---------------------- StartWorkService, " + android.os.Process.myPid());

        // 서비스 중지
        StopWorkService();

//        // GPS SERVICE START
//        traceServiceIntent = new Intent(this.getBaseContext(), TraceService.class);
//        traceService = startService(traceServiceIntent);
//        // Message SERVICE START
//        msgServiceIntent = new Intent(this.getBaseContext(), MsgService.class);
//        msgService = startService(msgServiceIntent);

    }

    /**
     * 서비스 중지
     */
    private void StopWorkService() {
        Loggers.d2(this, "위치 서비스 중지");
        if(traceService != null){
            stopService(traceServiceIntent);
        }
        Loggers.d2(this, "메시지 서비스 중지");
        if(msgService != null){
            stopService(msgServiceIntent);
        }
    }

    /**
     * 리시버 해제
     */
    private void StopReceiver() {
//        try {
//            if (traceReceiver != null) {
//                unregisterReceiver(traceReceiver);
//            }
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//        }
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
        bFirstScreen = true;

        Loggers.d("DeliveryListActivity ---------------------- onCreate, " + android.os.Process.myPid());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 절전모드 화면 꺼짐 방지

        mContext = this;

        // 배차정보 확인
        try {

            // 강제로 세팅..
            // SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "2023-09-07");
            //SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "DIS20230907105438238");

//            String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
//            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
//            if (DDATE.length()>0 && CVO_DISPATCH_ID.length()>0) { // 배차번호가 있고..
//                if ( // 당일배차 번호가 아니라면, 오늘자 배송이 아님
//                        DDATE.replace("-", "").equals(CVO_DISPATCH_ID.substring(3,11))
//                ) {
//                    Loggers.d("당일배차정보임..");
//                } else {
//                    // 배차정보 강제 초기화
//                    SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "");
//                    SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "");
//                }
//            }
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

        // 화면 데이타처리
        init();

        // 배송 리스트 셋업..
        setupRecyclerView();

        // 새로고침버튼 호출
        clickRefreshButton(null);



        // 최종차수정보 확인 - 목록갱신 포함됨...
        //searchWorKStatus();
//
//        // BROADCAST 리시버 등록
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BroadCastDomain.BROADCAST_TRACE_GPS_SVC);
//        registerReceiver(traceReceiver, filter);
//
//        // 최적화 버튼...
//        btn_opti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//// 배포시는 반드시 주석처리해야함.
////                // Fabric 크래쉬 테스트..
////                String temp = null;
////                if (temp.equals("ABC")) {
////                    Loggers.d2(this, "패브릭 크래쉬 테스트..");
////                }
//
//                if (!TextUtils.isEmpty(mLastWorkList.getVAN_START_TIME())) {
//                    CommonUtil.with().ToastMsg(mContext, "점포출발이후 더이상 실행하실 수 없습니다.", Toast.LENGTH_SHORT).show();
//                } else {
//                    if ("Y".equals(StringUtil.getNvlStr(mLastWorkList.getROUTE_CONFIRM_YN()))) {
//                        CommonUtil.with().ToastMsg(mContext, "순서확정이후 더이상 실행하실 수 없습니다.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        UDialog.withTwoCustom_okCancel(DeliveryListActivity.this, tag, getString(R.string.BTN_OPTI_MSG_01), "배송순서 최적화", "배송순서 고정", View.VISIBLE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//                            @Override
//                            public void onClick_yes() {
//                                // 최적화 작업 요청 - // 0 : 배송순서 최적화, 1 : 배송순서 고정
//                                requestRouteOpti("0");
//                            }
//
//                            @Override
//                            public void onClick_no() {
//                                // 최적화 작업 요청 - // 0 : 배송순서 최적화, 1 : 배송순서 고정
//                                requestRouteOpti("1");
//                                //handler.obtainMessage(REQ_OK_MESSAGE, getString(R.string.CMM_CANCEL)).sendToTarget();
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        // 순서확정 버튼...
//        btn_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if ("N".equals(mLastWorkList.getROUTE_OPTI_YN())) {
//                    CommonUtil.with().ToastMsg(mContext, "최적화를 먼저 하세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!TextUtils.isEmpty(mLastWorkList.getVAN_START_TIME())) {
//                    CommonUtil.with().ToastMsg(mContext, "점포출발이후 더이상 실행하실 수 없습니다.", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    if ("Y".equals(mLastWorkList.getROUTE_CONFIRM_YN())) {
//                        CommonUtil.with().ToastMsg(mContext, "이미 순서확정 되었습니다.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    UDialog.withTwoCustom_okCancel(DeliveryListActivity.this, tag, getString(R.string.BTN_CONFIRM_YN), "확정", "취소", View.VISIBLE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//                        @Override
//                        public void onClick_yes() {
//                            // 순서확정 작업 요청...
//                            requestRouteConfirm();
//                        }
//
//                        @Override
//                        public void onClick_no() {
//                            handler.obtainMessage(REQ_OK_MESSAGE, getString(R.string.CMM_CANCEL)).sendToTarget();
//                        }
//                    });
//                }
//            }
//        });
//
//        // 점포출발 버튼...
//        btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // 점포출발 요청 메소드.
//                StartStore();
//            }
//        });
//
//        // 서비스 실행..
//        StartWorkService();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!bFirstScreen) {
            Loggers.d("DeliveryListActivity ---------------------- onResume, " + android.os.Process.myPid());

            // 최종차수정보 확인 - 목록갱신 포함됨...
//            searchWorKStatus();
        }

//        tvAddedInfo.setVisibility(View.VISIBLE);

        mBackButtonCount = 0;

    }


    /**
     * 화면초기화
     */
    private void init() {
        setContentView(R.layout.deliverylist);
        mProgress = (LinearLayout) findViewById(R.id.progress_bar);

        // 애니메이션리소스 매핑
        animationStart = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_start_speed);
        animationStop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_stop);

        // 리스트뷰 초기화
        recyclerView = (RecyclerView) findViewById(R.id.simple_recycler_view);
        recyclerView.setHasFixedSize(true);

        // 경로최적화 결과정보 표시
//        llRouteResult = (LinearLayout) findViewById(R.id.llRouteResult);
//        tvAddedInfo = (TextView) findViewById(R.id.tvRouteResult);

        // 화면상단 배송지목록 요약정보 표시
        btn_opti = (Button) findViewById(R.id.btn_opti);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_start = (Button) findViewById(R.id.btn_start);

        // 뒤로가기 버튼 세팅
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //overridePendingTransition(R.anim.slide_right, R.anim.no_change);
            }
        });
        // 초기화
        tvInit = (TextView) findViewById(R.id.tvInit);
        btnInit = (Button) findViewById(R.id.btnInit);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInit();
            }
        });

        curDeliveryDate = CommonUtil.with().nowYYYYMMDDHHMMSS();
        cal_sch.set(Calendar.YEAR , Integer.parseInt(curDeliveryDate.substring(0,4)));
        cal_sch.set(Calendar.MONTH , Integer.parseInt(curDeliveryDate.substring(4,6))-1);
        cal_sch.set(Calendar.DAY_OF_MONTH , Integer.parseInt(curDeliveryDate.substring(6,8)));
        searchDate = (TextView) findViewById(R.id.searchDate);
        setSearchDate(0); // 초기 오늘날짜..

        // 하단 버튼...
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setText("이대로 출발");
        btnSave.setBackgroundResource(R.drawable.selector_style_yellow);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // 배송순서확정 팝업 호출..
//                popRouteConfirm();

                // 배차번호있는, 운행시작이고 관제중일경우 리스트에 위치정보를 업데이트 해주기위함..
                String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
                if (!CVO_DISPATCH_ID.isEmpty()) {
                    String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
                    // 리스트의 날짜와 운행중인 날짜가 일치할때..
                    if (DDATE.equals(search_date_sch)) {
                        // 새로고침 처리
                        clickRefreshButton(null);
                    } else {
                        //CommonUtil.with().ToastMsg(mContext, "다른날짜의 배차진행건이 있습니다.", Toast.LENGTH_SHORT).show();
                        // 배차정보 초기화
                        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "");
                        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "");
                        // 배송순서확정 팝업 호출..
                        popRouteConfirm();
                    }
                } else {
                    // 배송순서확정 팝업 호출..
                    popRouteConfirm();
                }
            }
        });

//        // Floating Action Button을 리스트 뷰에 적용
//        fabRefresh = (FloatingActionButton) findViewById(R.id.deliverylist_fabrefresh);
//
//        // 이벤트 적용
//        fabRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mProgress.getVisibility() != View.VISIBLE) {
//                    // 최종차수정보 확인 - 목록갱신 포함됨...
//                    searchWorKStatus();
//                }
//            }
//        });

    }

    // 순서변경 여부 저장
    public boolean changeRouteYn = false;
    public boolean listEditYn = false;
    public void checkStatus() {
        try {
            if (changeRouteYn) {
                // 배송순서가 변경되었다...
                btnSave.setText("배송순서 변경");
                btnSave.setBackgroundResource(R.drawable.selector_style_red);
                btnSave.setTextColor(Color.parseColor("#FFFFFF"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 메시지팝업 여부(팝업복귀후 onResume 프로세스에서 불필요하게 중복호출되는 로직을 걸러내기위함..)
    private boolean openPopYn = false;
    /**
     * 하단버튼 호출 - 팝업호출
     */
    public void popRouteConfirm(){
        try {
            String title = "배송준비 - 완료";
            String message01 = "";
            String message02 = "<big><big><b>이대로</b></big></big> 출발 하시겠습니까?";
            if (changeRouteYn) {
                title = "배송변경 - 확인";
                message01 = "";
                message02 = "<big><big><b>변경된 순서로</b></big></big> 출발 하시겠습니까?";
            }

            Intent intent = new Intent(getApplicationContext(), PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName()); //  현재 엑티비티명 확인
            intent.putExtra("REQ_POP_CODE", WorkStatus._배송시작);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "배차내역조회");
            startActivity(intent);

            openPopYn = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 초기화버튼 호출 - 팝업호출
     */
    public void popInit(){
        try {
            String title = "초기화";
            String message01 = "";
            String message02 = "모두 <big><big><b>초기화</b></big></big> 하시겠습니까?";

            Intent intent = new Intent(getApplicationContext(), PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName()); //  현재 엑티비티명 확인
            intent.putExtra("REQ_POP_CODE", WorkStatus._초기화);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "배차내역조회");
            startActivity(intent);

            openPopYn = true;
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
            if ( (DriverMainActivity) DriverMainActivity.mContext != null)
                ((DriverMainActivity) DriverMainActivity.mContext).playSound();

            // 2.팝업유형에 따른 작업처리..
            String htmlString = "";
            if(p_req_pop_code.equals(WorkStatus._배송시작)) {

                // 배송순서확정 - 리스트변경차단
                listEditYn = true;

                // 배송시작
                requestCvoDeliveryStart();
            } else if(p_req_pop_code.equals(WorkStatus._초기화)) {
                // 배차정보 초기화
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.DDATE.name(), "");
                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.CVO_DISPATCH_ID.name(), "");
                // 리스트변경차가능
                listEditYn = false;
                // 새로고침버튼 호출
                clickRefreshButton(null);
            }

            // 3.팝업닫기
            ((PopCommonActivity) PopCommonActivity.mContext).FinishWork();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 리스트 하단 부가정보 표시
     * @param nInfoType
     * @param msg
     */
    private void setAddinInfoText(int nInfoType, String msg) {
        // 나중에 switch 문으로 바꿀것...
        switch (nInfoType) {
            case ADDINFO_ROUTE:
                // 경로정보 표시
                //llRouteResult.setBackgroundColor(Color.parseColor("#00D4FF"));

//                String DELIVERY_DT = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DELIVERY_DT.name(), "");
//                String STORE_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.STORE_ID.name(), "");
//                String VAN_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.VAN_ID.name(), "");
//                String SHIFT_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.SHIFT_ID.name(), "");
//                String ROUTE_OPTI_KEY = DELIVERY_DT + STORE_ID + VAN_ID + SHIFT_ID;
//                // 최적화 결과키 저장
//                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.ROUTE_OPTI_KEY.name(), ROUTE_OPTI_KEY);
//                // 최적화 결과 저장
//                SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.ROUTE_OPTI_RESULT.name(), msg);
//                // 최적화 결과 표시
//                tvEndTime.setText(msg);
                break;
            case ADDINFO_LOCATION:
                // 위치정보 표시
                //llRouteResult.setBackgroundColor(Color.parseColor("#e60013"));
            case ADDINFO_GPS:
                // GPS정보 표시
                //llRouteResult.setBackgroundColor(Color.parseColor("#e60013"));
                //msg = tvAddedInfo.getText() + msg;
                break;
        }
        tvAddedInfo.setText(msg);
    }

    // 경로확졍 여부
    public boolean bRouteYn = false;

    /**
     * 경로확졍 여부 체크..
     * @return
     */
    public boolean checkRouteYn() {
        try {
            if (deliveryListItems.size()>0) { // 운행리스트가 있는경우..
                // 경로운행정보
                int totDuration = Integer.parseInt(deliveryListItems.get(0).getTOT_DURATION());
                double totDistance = Double.parseDouble(deliveryListItems.get(0).getTOT_DISTANCE());
                if (totDuration>0 && totDistance>0) {
                    // 누적거리/시간정보가 있다면..
                    bRouteYn = true;
                } else {
                    bRouteYn = false;
                }
            } else {
                bRouteYn = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            bRouteYn = false;
        }
        return bRouteYn;
    }
    /**
     * 각 업무성격에 맞는 UI 세팅
     */
    public void updateUISetting() {
        // 애니메이션실행 여부 - 너무많은 애니메이션은 눈이 피로하다..그래서 이미 실행되었다면 더 실행하지 않는것이 더 낫다..
        boolean bAnimationPlay = false;
        btn_opti.startAnimation(animationStop);
        btn_confirm.startAnimation(animationStop);
        btn_start.startAnimation(animationStop);

        try {

            String htmlString = "";

            // --------------------------------------------------------------------------------
            // 초기화버튼 처리..
            // --------------------------------------------------------------------------------
            if (checkRouteYn()) {
                // 초기화버튼 보이기
                tvInit.setVisibility(View.VISIBLE);
                btnInit.setVisibility(View.VISIBLE);
            } else {
                // 초기화버튼 보이기
                tvInit.setVisibility(View.GONE);
                btnInit.setVisibility(View.GONE);
            }
            // --------------------------------------------------------------------------------
            // 배송일자
            // --------------------------------------------------------------------------------
            TextView tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
            htmlString = "<small><small>배송일자</small></small><BR>" +
                    "<b>" + CommonUtil.with().nowMM_DD() + "</b>";
            CommonUtil.with().setHtmlText(tvDeliveryDate, htmlString);

            // --------------------------------------------------------------------------------
            // 기사명
            // --------------------------------------------------------------------------------
            TextView tvDriverName = (TextView) findViewById(R.id.tvDriverName);
            if (checkRouteYn()) {
                // 경로확정이후..
                int totDuration = Integer.parseInt(deliveryListItems.get(0).getTOT_DURATION());
                double totDistance = Double.parseDouble(deliveryListItems.get(0).getTOT_DISTANCE());
                String sDuration = CommonUtil.with().secondToTimestring(Integer.valueOf(totDuration));
                String sDistance = CommonUtil.with().distanceStringWithUnit(Double.valueOf(totDistance) / 1000);
                htmlString = "<small><small>예상정보</small></small><BR>" +
                        "" +
                        "<b>" + sDuration + "</b>" +
                        "<small> / </small>" +
                        "<b>" + sDistance + "</b><small><small> km</small></small>" +
                        "";
            } else {
                // 경로확정이전..
                if (deliveryListItems.size()>0) {
                    String name = deliveryListItems.get(0).getEPNAME();
                    htmlString = "<small><small>배송담당</small></small><BR>" +
                            "<b>" + name + "</b>";
                } else {
                    htmlString = "<small><small>배송담당</small></small><BR>" +
                            "<b>" + AppSetting.cvoUserInfo.getNAME() + "</b>";
                }
            }
            CommonUtil.with().setHtmlText(tvDriverName, htmlString);

            // --------------------------------------------------------------------------------
            // 배송건수
            // --------------------------------------------------------------------------------
            TextView tvShiftInfo = (TextView) findViewById(R.id.tvShiftInfo);
            //tvShiftInfo.setText(String.format("%d 건", deliveryListItems.size()));    // 배송건수
            htmlString = "<small><small>배송건수</small></small><BR>" +
                    "<b>" + String.format("%d", deliveryListItems.size()) + "</b>" +
                    " <small><small><small>건</small></small></small>";
            CommonUtil.with().setHtmlText(tvShiftInfo, htmlString);

            // --------------------------------------------------------------------------------
            // 리스트위, 안내문구
            // --------------------------------------------------------------------------------
            TextView tvRouteInfo = (TextView) findViewById(R.id.tvRouteInfo);
            if (checkRouteYn()) {
                // 경로확정이후..
                tvRouteInfo.setBackgroundColor(Color.parseColor("#C90072C3"));
                htmlString = "<SMALL>오늘도 <BIG><B>안전운행</B></BIG> 부탁드립니다.</SMALL>";
                CommonUtil.with().setHtmlText(tvRouteInfo, htmlString);
            } else {
                // 경로확정이전..
                tvRouteInfo.setBackgroundResource(R.drawable.selector_style_red);
                htmlString = "<SMALL>리스트를 <BIG><B>길게 눌러</B></BIG>, 순서를 변경해주세요.</SMALL>";
                CommonUtil.with().setHtmlText(tvRouteInfo, htmlString);
            }

            // --------------------------------------------------------------------------------
            // 하단버튼 타이틀 변경
            // --------------------------------------------------------------------------------
            if (checkRouteYn()) {
                int totDuration = Integer.parseInt(deliveryListItems.get(0).getTOT_DURATION());
                double totDistance = Double.parseDouble(deliveryListItems.get(0).getTOT_DISTANCE());
                String sDuration = CommonUtil.with().secondToTimestring(Integer.valueOf(totDuration));
                String sDistance = CommonUtil.with().distanceStringWithUnit(Double.valueOf(totDistance) / 1000);
//                htmlString = "" +
//                        "배송시작" +
//                        "<small> - " + CommonUtil.with().nowHH_MM() + "</small>" +
//                        "";
                htmlString = "" +
                        "<small>" +
                        "<B>" + sDuration + "</B>" +
                        " / " +
                        "<B>" + sDistance + "</B><small><small>km</small></small>" +
                        " - </small>운행 예상" +
                        "<BR>" +
                        "<small><small>- 터치시 시간 업데이트 -</small></small>";
                CommonUtil.with().setHtmlText(btnSave, htmlString);
                // 하단버튼 색상변경
                btnSave.setBackgroundColor(Color.parseColor("#C90072C3"));
                btnSave.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                btnSave.setText("이대로 출발");
                btnSave.setTextColor(Color.parseColor("#5E5E5E"));
                btnSave.setBackgroundResource(R.drawable.selector_style_yellow);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

// 이전소스...
//        // 배송일자 세팅
//        if (!TextUtils.isEmpty(mLastWorkList.getDELIVERY_DT())) {
//            tvDeliveryDate.setText(com.gzonesoft.cookzzang.util.DateUtil.getDateString2(StringUtil.getNvlStr(mLastWorkList.getDELIVERY_DT()).toString()));
//        }
//        // 기사명 세팅
//        if (!TextUtils.isEmpty(mLastWorkList.getDRIVER_NM())) {
//            tvDriverName.setText(StringUtil.getNvlStr(mLastWorkList.getDRIVER_NM()).toString());   // 기사명
////            Crashlytics.setString("DRIVER_NAME", StringUtil.getNvlStr(mLastWorkList.getDRIVER_NM()).toString());
//        }
//        // 차수정보 세팅
//        if (!TextUtils.isEmpty(mLastWorkList.getSHIFT_NM())) {
////            tvShiftInfo.setText(StringUtil.getNvlStr(mLastWorkList.getSHIFT_NM()).toString() + " / " + StringUtil.getNvlStr(deliveryListItem.size()).toString() + "건");    // 차수 및 배송건수
////            Crashlytics.setString("SHIFT_ID", StringUtil.getNvlStr(mLastWorkList.getSHIFT_ID()).toString());
////            Crashlytics.setString("SHIFT_NAME", StringUtil.getNvlStr(mLastWorkList.getSHIFT_NM()).toString());
//        }
//        // 최적화 실행여부
//        String routeOptiYn = StringUtil.getNvlStr(mLastWorkList.getROUTE_OPTI_YN()).toString();
//        if (!TextUtils.isEmpty(routeOptiYn)) {
//            if ("Y".equals(routeOptiYn)) {   // 최적화 실행시..
//                // 최적화 버튼 깜빡이지 않음
//                btn_opti.startAnimation(animationStop);
//
////                String ROUTE_OPTI_KEY = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.ROUTE_OPTI_KEY.name(), "");
////                if (!TextUtils.isEmpty(ROUTE_OPTI_KEY)) {   // 최적화 결과 키값이 있을때만 표시한다.
////                    // 최적화 결과 정보 표시
////                    String DELIVERY_DT = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DELIVERY_DT.name(), "");
////                    String STORE_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.STORE_ID.name(), "");
////                    String VAN_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.VAN_ID.name(), "");
////                    String SHIFT_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.SHIFT_ID.name(), "");
////                    // 최적화 결과키 확인
////                    if (ROUTE_OPTI_KEY.equals(DELIVERY_DT + STORE_ID + VAN_ID + SHIFT_ID)) {
////                        // 최적화 결과 표시
////                        tvEndTime.setText(SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.ROUTE_OPTI_RESULT.name(), ""));
////                    }
////
////                }
//
//            } else {    // 최적화 실행이 안되었을때..
//                // 최적화 버튼 깜빡임
//                if (!bAnimationPlay) btn_opti.startAnimation(animationStart);
//                bAnimationPlay = true;
//            }
//        }

//        // 순서확정 여부
//        String routeConfirmYn = StringUtil.getNvlStr(mLastWorkList.getROUTE_CONFIRM_YN()).toString();
//        if (!TextUtils.isEmpty(routeConfirmYn)) {
//            if ("Y".equals(routeConfirmYn)) {   // 순서확정시..
//                // 최적화 버튼 비활성화
////                btn_opti.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_02));
////                btn_opti.setBackgroundResource(R.drawable.selector_btn_bg_02);
//                // 순서확정 버튼 깜빡이지 않음
//                btn_confirm.startAnimation(animationStop);
//            } else {    // 순서확정 안되었을때..
//                // 최적화 버튼 활성화
////                btn_opti.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_01));
////                btn_opti.setBackgroundResource(R.drawable.selector_btn_bg_01);
//                // 순서확정 버튼 깜빡임
//                if (!bAnimationPlay) btn_confirm.startAnimation(animationStart);
//                bAnimationPlay = true;
//            }
//        }
//
//        // 점포출발 보고 시간
//        String startTime = StringUtil.getNvlStr(mLastWorkList.getVAN_START_TIME()).toString();
//        String endTime = StringUtil.getNvlStr(mLastWorkList.getVAN_END_TIME()).toString();
//        if (!TextUtils.isEmpty(startTime)) {    // 점포출발하였다면...
//            // 점포출발보고 시간 표시
//            tvStartTime.setText("출발      " + startTime.substring(0, 5));
//            if (!TextUtils.isEmpty(endTime)) {    // 점포도착하였다면...
//                // 점포도착 시간 표시
//                tvEndTime.setText("도착      " + endTime.substring(0, 5));
//            }
////            // 최적화 비활성화
////            btn_opti.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_02));
////            btn_opti.setBackgroundResource(R.drawable.selector_btn_bg_02);
////            // 순서확정 비활성화
////            btn_confirm.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_02));
////            btn_confirm.setBackgroundResource(R.drawable.selector_btn_bg_02);
////            btn_start.setText("경로안내");
////            btn_start.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_01));
////            btn_start.setBackgroundResource(R.drawable.selector_btn_bg_09);
//            // 점포출발 버튼 깜빡이지않음
//            btn_start.startAnimation(animationStop);
//        } else {    // 점포출발하지 않았다면..
//
//            // 순서확정 여부
//            routeConfirmYn = StringUtil.getNvlStr(mLastWorkList.getROUTE_CONFIRM_YN()).toString();
//            if (!TextUtils.isEmpty(routeConfirmYn)) {
////                if ("Y".equals(routeConfirmYn)) {   // 순서확정시..
////                    // 최적화 버튼 비활성화
////                    btn_opti.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_02));
////                    btn_opti.setBackgroundResource(R.drawable.selector_btn_bg_02);
////                    // 순서확정 비활성화
////                    btn_confirm.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_02));
////                    btn_confirm.setBackgroundResource(R.drawable.selector_btn_bg_02);
////                    bAnimationPlay = false;
////                } else {    // 순서확정 안되었을때..
////                    // 최적화 버튼 활성화
////                    btn_opti.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_01));
////                    btn_opti.setBackgroundResource(R.drawable.selector_btn_bg_01);
////                    // 순서확정 비활성화
////                    btn_confirm.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_01));
////                    btn_confirm.setBackgroundResource(R.drawable.selector_btn_bg_01);
////                }
//            }
//
//            btn_start.setText("점포출발");
////            btn_start.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.selector_btn_txt_01));
////            btn_start.setBackgroundResource(R.drawable.selector_btn_bg_01);
//            // 점포출발 버튼 깜빡임
//            if (!bAnimationPlay) btn_start.startAnimation(animationStart);
//            bAnimationPlay = true;
//        }
    }


    // 드래그 시작효과
    private void setEffectDragStart(View targetView) {
        try {
            View destView = (View) targetView.findViewById(R.id.ll_listitem);
            View destView2 = (View) targetView.findViewById(R.id.ll_listitem2);
            destView.setBackgroundColor(Color.parseColor("#3BE14D00"));
            destView2.setBackgroundColor(Color.parseColor("#3BE14D00"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // 드래그 종료효과
    private void setEffectDragFinish(View targetView) {
        try {
            View destView = (View) targetView.findViewById(R.id.ll_listitem);
            View destView2 = (View) targetView.findViewById(R.id.ll_listitem2);
            destView.setBackgroundColor(Color.parseColor("#000A44F1"));
            destView2.setBackgroundColor(Color.parseColor("#000A44F1"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 배송 리스트뷰(리사이클러뷰) 셋업..
     */
    private void setupRecyclerView() {
        Loggers.d("배송 리스트뷰 셋업 ------------------------------------------------------------------------------------");

        deliveryListItemAdapter = new DeliveryListItemAdapter(DeliveryListActivity.this, deliveryListItems);

        // Adapter를 설정
        deliveryListItemAdapter.setOnItemViewClickListener(new View.OnClickListener() {

            private View preView = null;
            private int prePosition = -1;

            @Override
            public void onClick(View v) {

                int nCurPosition = recyclerView.getChildAdapterPosition(v);

                Loggers.d2(this, "[onClick]----------------" + nCurPosition);

                // 거래처 상세팝업 호출
                popClientDetailInfo(
                        deliveryListItems.get(nCurPosition).getCLCODE()
                );

//                preView = deliveryListItemAdapter.OldView;
//                prePosition = deliveryListItemAdapter.prePosition;
//
//                if (preView != null) {
//                    Loggers.d2(this, "[onClick]----------------" + nCurPosition + "배경 복귀하기..");
//                    preView.setBackgroundColor(Color.parseColor("#F5F5F5"));
//                    // 버튼 감추기.
//                    LinearLayout prev_item_button_area = (LinearLayout) preView.findViewById(R.id.add_action_area);
//                    prev_item_button_area.setVisibility(View.GONE);
//                    // 하위 텍스트 일반으로 일괄처리..
//                    Common.setTextTypefaceInView(preView, R.id.area_item_text, Typeface.NORMAL);
//                    Common.setTextTypefaceInView(preView, R.id.delivery_status, Typeface.NORMAL);
//                    Common.setTextColorInView(preView, R.id.area_item_text, "#645b5c");
//                    Common.setTextColorInView(preView, R.id.delivery_status, "#645b5c");
//
//                    setEffectDragStart(preView);
//                    // 주소배경처리
////                    View prev_delivery_addinfo_area = (View) preView.findViewById(R.id.ll_listitem);
////                    prev_delivery_addinfo_area.setBackgroundColor(Color.parseColor("#F9F9F9"));
////                    // 주소 글자처리
////                    Common.setTextColorInView(preView, R.id.delivery_addinfo_area, "#656580");
//                    curItemIndex = -1;
//                }
//
//                if (prePosition != nCurPosition) {
//                    Loggers.d2(this, "[onClick]----------------" + nCurPosition + "배경 바꾸기..");
//                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    // 버튼 보이기
//                    LinearLayout item_button_area = (LinearLayout) v.findViewById(R.id.add_action_area);
//                    item_button_area.setVisibility(View.VISIBLE);
//                    // 하위 텍스트 볼드처리..
//                    Common.setTextTypefaceInView(v, R.id.area_item_text, Typeface.BOLD);
//                    Common.setTextTypefaceInView(v, R.id.delivery_status, Typeface.BOLD);
//                    Common.setTextColorInView(v, R.id.area_item_text, "#645b5c");
//                    Common.setTextColorInView(v, R.id.delivery_status, "#645b5c");
//
//                    setEffectDragFinish(v);
//                    // 주소배경처리
////                    View delivery_addinfo_area = (View) v.findViewById(R.id.ll_listitem);
////                    delivery_addinfo_area.setBackgroundColor(Color.parseColor("#FFFFFF"));
////                    // 주소 글자처리
////                    Common.setTextColorInView(v, R.id.delivery_addinfo_area, "#656580");
//
//                    deliveryListItemAdapter.OldView = v;
//                    deliveryListItemAdapter.prePosition = nCurPosition;
//
//                    curItemIndex = nCurPosition;
//
//                } else {
//
//                    deliveryListItemAdapter.OldView = null;
//                    deliveryListItemAdapter.prePosition = -1;
//                    curItemIndex = -1;
//                }

                deliveryListItemAdapter.notifyDataSetChanged();

            }
        });

        // ItemTouchHelper클래스를 구현한다
        // 이에 따라 드래그앤드롭이나 스와이프로 삭제 등을 할 수 있게 된다.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.ACTION_STATE_IDLE,
                ItemTouchHelper.DOWN) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 드래그앤드롭 시
                deliveryListItemAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                // 이동가능한 상태를 인지하기 위해 해당 액션아이템의 배경리소스를 바꿔준다.
                viewHolder.itemView.setBackgroundResource(R.drawable.item_selector_move);
                mViewHolder = viewHolder;
                Loggers.d2(this, "-------- onMove");

                // 변경여부 저장..
                changeRouteYn = true;
                // 상태체크..
                checkStatus();
                return true;
            }

            @Override
            public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int nDir = super.getDragDirs(recyclerView, viewHolder);
                // 이동가능한 상태를 인지하기 위해 해당 액션아이템의 배경리소스를 바꿔준다.
                viewHolder.itemView.setBackgroundResource(R.drawable.item_selector_move);
                mViewHolder = viewHolder;
                Loggers.d2(this, "-------- getDragDirs");
                return nDir;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                // 리스트 드래그 차단..
                if (listEditYn) {
                    CommonUtil.with().ToastMsg(mContext, "순서를 변경하려면 초기화 해야합니다.", Toast.LENGTH_SHORT).show();
                    return 0;
                }

//                String routeConfirmYn = StringUtil.getNvlStr(mLastWorkList.getROUTE_CONFIRM_YN()).toString();
//                if (!TextUtils.isEmpty(routeConfirmYn)) {
//                    if ("Y".equals(routeConfirmYn)) {   // 순서확정시..
//                        //CommonUtil.with().ToastMsg(mContext, "더 이상 수정하실 수 없습니다.", Toast.LENGTH_SHORT).show();
//                        return 0;
//                    }
//                }
                int retFlags = super.getMovementFlags(recyclerView, viewHolder);
                // 이동가능한 상태를 인지하기 위해 해당 액션아이템의 배경리소스를 바꿔준다.
//                viewHolder.itemView.setBackgroundResource(R.drawable.item_selector_move);
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#e60013"));
                mViewHolder = viewHolder;

                // 하단 추가확장 버튼 감추기
                LinearLayout item_button_area = (LinearLayout) mViewHolder.itemView.findViewById(R.id.add_action_area);
                item_button_area.setVisibility(View.GONE);
                // 하위 텍스트 일반으로 일괄처리..
                Common.setTextTypefaceInView(mViewHolder.itemView, R.id.area_item_text, Typeface.NORMAL);
                Common.setTextTypefaceInView(mViewHolder.itemView, R.id.delivery_status, Typeface.NORMAL);

                setEffectDragStart(mViewHolder.itemView);
                // 주소 배경처리
//                View delivery_addinfo_area = (View) mViewHolder.itemView.findViewById(R.id.ll_listitem);
//                delivery_addinfo_area.setBackgroundColor(Color.parseColor("#e60013"));
//                // 주소 글자처리
//                Common.setTextColorInView(mViewHolder.itemView, R.id.delivery_addinfo_area, "#FFFFFF");
                // 맨하단이면 더비영역을 히든시켜준다.
                if (deliveryListItems.size() == (mViewHolder.getAdapterPosition()+1)) {
                    Loggers.d2(this, "-------- getMovementFlags 맨끝입니다....");
                    View foot_dummy = (View) mViewHolder.itemView.findViewById(R.id.foot_dummy);
                    foot_dummy.setVisibility(View.GONE);
                }

                Common.setTextColorInView(mViewHolder.itemView, R.id.area_item_text, "#FFFFFF");
                Common.setTextColorInView(mViewHolder.itemView, R.id.delivery_status, "#FFFFFF");
                Loggers.d2(this, "-------- getMovementFlags[" + retFlags + "] position = " + mViewHolder.getAdapterPosition());
                return retFlags;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 아이템 스와이프 시
                //deliveryListItemAdapter.removeAtPosition(viewHolder.getAdapterPosition());
            }

        }).attachToRecyclerView(recyclerView);


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                    // 계속 누르고 있을때의 이전 액션 아이템뷰의 배경리소스롤 바꿔준다
                    if (mViewHolder != null) {
                        mViewHolder.itemView.setBackgroundColor(Color.parseColor("#e60013"));
                        Common.setTextColorInView(mViewHolder.itemView, R.id.area_item_text, "#FFFFFF");
                        Common.setTextColorInView(mViewHolder.itemView, R.id.delivery_status, "#FFFFFF");

                        setEffectDragStart(mViewHolder.itemView);
                        // 주소 배경처리
//                        View delivery_addinfo_area = (View) mViewHolder.itemView.findViewById(R.id.ll_listitem);
//                        delivery_addinfo_area.setBackgroundColor(Color.parseColor("#e60013"));
//                        // 주소 글자처리
//                        Common.setTextColorInView(mViewHolder.itemView, R.id.delivery_addinfo_area, "#FFFFFF");

                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 누른 액션을 마쳤을때의 이전 액션 아이템뷰의 배경리소스롤 바꿔준다
                    if (mViewHolder != null) {
                        mViewHolder.itemView.setBackgroundResource(R.drawable.item_selector);
                        Common.setTextColorInView(mViewHolder.itemView, R.id.area_item_text, "#645b5c");
                        Common.setTextColorInView(mViewHolder.itemView, R.id.delivery_status, "#645b5c");

                        setEffectDragFinish(mViewHolder.itemView);

                        // 주소 배경처리
//                        View delivery_addinfo_area = (View) mViewHolder.itemView.findViewById(R.id.ll_listitem);
//                        delivery_addinfo_area.setBackgroundColor(Color.parseColor("#F7F7F7"));
//                        // 주소 글자처리
//                        Common.setTextColorInView(mViewHolder.itemView, R.id.delivery_addinfo_area, "#656580");
                        mViewHolder = null;
                    }
                }

                return false;
            }
        });

    }

    /**
     * 배차내역, 위치정보 업데이트
     */
    public void updateLocationInfoSet() {
        try {
            // 배차번호있는, 운행시작이고 관제중일경우 리스트에 위치정보를 업데이트 해주기위함..
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
            if (!CVO_DISPATCH_ID.isEmpty()) {
                if (AppSetting.todayStatus.equals(TodayStatus._출근)) {
                    if (recyclerView != null) {
                        String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
                        // 리스트의 날짜와 운행중인 날짜가 일치할때..
                        if (DDATE.equals(search_date_sch)) {
                            if (!bDeliveryList_Running) {
                                Loggers.d("배송조회 요청작업이 진행중아닐때 업데이트..");
                                // 리스트 갱신
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 배송 리스트뷰 갱신
     */
    public void refreshListData() {

        // 기존 선택아이템이 있다면..
        if (deliveryListItemAdapter.OldView != null) {
            deliveryListItemAdapter.OldView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            // 버튼 감추기.
            LinearLayout prev_item_button_area = (LinearLayout) deliveryListItemAdapter.OldView.findViewById(R.id.add_action_area);
            prev_item_button_area.setVisibility(View.GONE);
            // 하위 텍스트 일반으로 일괄처리..
            Common.setTextTypefaceInView(deliveryListItemAdapter.OldView, R.id.area_item_text, Typeface.NORMAL);
            Common.setTextTypefaceInView(deliveryListItemAdapter.OldView, R.id.delivery_status, Typeface.NORMAL);

            setEffectDragFinish(deliveryListItemAdapter.OldView);
            // 주소배경처리
//            View prev_delivery_addinfo_area = (View) deliveryListItemAdapter.OldView.findViewById(R.id.ll_listitem);
//            prev_delivery_addinfo_area.setBackgroundColor(Color.parseColor("#F9F9F9"));

            curItemIndex = -1;
            deliveryListItemAdapter.OldView = null;
            deliveryListItemAdapter.prePosition = -1;
        }

        recyclerView.setAdapter(deliveryListItemAdapter);

        recyclerView.getAdapter().notifyDataSetChanged();

        // 점포보고 활성화 처리...
        int nextPosition = DeliveryListManager.getInstance().getNextPosWithPositionInfo();
        if (nextPosition == -1) {
            // 점포자동보고 활성화...
            String store_report_yn = SettingPref.with(mContext).loadPref(ConstValue.Login.STORE_REPORT_YN.name(), "N");
            // 점포도착 보고여부 확인
            if ("N".equals(store_report_yn)) {
                SettingPref.with(mContext).savePrefWithString(ConstValue.Login.STORE_REPORT_YN.name(), "Y");
            }
        } else {
            // 점포자동보고 비활성화...
            SettingPref.with(mContext).savePrefWithString(ConstValue.Login.STORE_REPORT_YN.name(), "N");
        }
    }


    /**
     * 배송목록 조회 - ERP
     */
    public void requestErpDispatchSel() {

        // 배송조회 요청작업이 진행중이라면 대기..
        while (bDeliveryList_Running) {
            try {
                Loggers.d("[Running]배송조회 요청작업이 진행중 확인됨...2");
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 배송목록 조회요청작업 진행여부 - 잠그기
        bDeliveryList_Running = true;
        recyclerView.removeAllViews();
        //recyclerView.getAdapter().notifyDataSetChanged();
        mProgress.setVisibility(View.VISIBLE);

        try {

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
//            paraMap.put("@I_DDATE", "2023-08-03");
//            paraMap.put("@I_EPCODE", "0004");
            paraMap.put("@I_DDATE", search_date_sch); // CommonUtil.with().nowYYYY_MM_DD());
            paraMap.put("@I_EPCODE", AppSetting.cvoUserInfo.getEMPNO());
            paraMap.put("I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));
            paraMap.put("@I_INPUT_USER", AppSetting.cvoUserInfo.getGEONUM());

            RequesterSession.with().RestAPIServiceCall(Svc.ERP_DISPATCH_SEL, paraMap, requestErpDispatchSelCallback);

        } catch (Exception ex) {
            ex.printStackTrace();
            bDeliveryList_Running = false;
            mProgress.setVisibility(View.GONE);
        }

    }

    /**
     * 배송목록 조회 콜백처리
     */
    private Callback requestErpDispatchSelCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, "requestErpDispatchSelCallback, ---- onFailure:" + e.toString());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
            // 배송목록 조회요청작업 진행여부 - 풀기
            bDeliveryList_Running = false;
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
                    bDeliveryList_Running = false;
                    handler.obtainMessage(REQ_OK_MESSAGE, "조회된 건수가 없습니다.").sendToTarget();
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
                deliveryListItemAdapter.dataset = deliveryListItems;

                Loggers.d("[callBack]리스트어댑터 건수 = " + deliveryListItems.size());

                handler.obtainMessage(ERP_DISPATCH_SEL, "").sendToTarget();
            } catch (Exception ex) {
                ex.printStackTrace();
                // 배송목록 조회요청작업 진행여부 - 풀기
                bDeliveryList_Running = false;
                handler.obtainMessage(RequesterSession.REQ_NOK, "").sendToTarget();
            }
        }
    };

    /**
     * CVO 배송시작
     */
    public void requestCvoDeliveryStart() {
        try {
            // 배송조회 요청작업이 진행중이라면 대기..
            while (bDeliveryList_Running) {
                try {
                    Loggers.d("[Running]배송조회 요청작업이 진행중 확인됨...3");
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 배송목록 조회요청작업 진행여부 - 잠그기
            bDeliveryList_Running = true;
            recyclerView.removeAllViews();
            mProgress.setVisibility(View.VISIBLE);

            // 자동출근보고 및 관제시작
            ((DriverMainActivity) DriverMainActivity.mContext).workReportSav(WorkStatus._출근보고);
            // CVO 배차정보 확인
            String DDATE = CommonUtil.with().nowYYYY_MM_DD(); // 배송일자는 당일로 설정한다.(이전주문 당일배송가능-아이사랑,준비편)
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");
            // 배송순서정보 추출..
            JSONArray jArray = new JSONArray();
            JSONObject jObj = new JSONObject();
            try {
                for (int i = 0; i < deliveryListItemAdapter.dataset.size(); i++) {
                    DeliveryListInfo item = new DeliveryListInfo();
                    item = deliveryListItemAdapter.dataset.get(i);
                    JSONObject sObject = new JSONObject();//배열 내에 들어갈 json

                    sObject.put("DISPATCH_ID", CVO_DISPATCH_ID);
                    sObject.put("COMPANYCD", Svc.CVO_COMPANYCD);
                    sObject.put("DDATE", DDATE);
                    sObject.put("SHIFT_NO", item.getWSD_NUM());
                    sObject.put("ROUTE_SEQ", String.format("%d", i+1));
                    sObject.put("EPCODE", item.getEPCODE());
                    sObject.put("EPNAME", item.getEPNAME());
                    sObject.put("EPPHONE", item.getEPPHONE());
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
            paraMap.put("@I_DATA_SIZE", String.format("%d", deliveryListItemAdapter.dataset.size()));
            paraMap.put("@I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("@I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));

            //RequesterSession.with().RestAPIServiceCall(Svc.CVO_DELIVERY_START, paraMap, requestCvoDeliveryStartCallback);

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
            // 배송목록 조회요청작업 진행여부 - 풀기
            bDeliveryList_Running = false;
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
            deliveryListItemAdapter.dataset = deliveryListItems;

            handler.obtainMessage(CVO_DELIVERY_START, "").sendToTarget();
        }
    };

    /**
     * CVO - 배송리스트 조회
     * @param dispatchId
     */
    public void requestCvoDeliveryList(String dispatchId) {
        try {
            // 배송조회 요청작업이 진행중이라면 대기..
            while (bDeliveryList_Running) {
                try {
                    Loggers.d("[Running]배송조회 요청작업이 진행중 확인됨...4");
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 배송목록 조회요청작업 진행여부 - 잠그기
            bDeliveryList_Running = true;
            recyclerView.removeAllViews();
            mProgress.setVisibility(View.VISIBLE);

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_DISPATCH_ID", dispatchId);
            paraMap.put("@I_LAT", Double.toString(AppSetting.GPS_MANAGER.getGpsLat()));
            paraMap.put("@I_LON", Double.toString(AppSetting.GPS_MANAGER.getGpsLng()));

            //RequesterSession.with().RestAPIServiceCall(Svc.CVO_DELIVERY_START, paraMap, requestCvoDeliveryStartCallback);

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
            // 배송목록 조회요청작업 진행여부 - 풀기
            bDeliveryList_Running = false;
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
            deliveryListItemAdapter.dataset = deliveryListItems;

            handler.obtainMessage(CVO_DELIVERY_SEL, "").sendToTarget();
        }
    };



    /**
     * 프로그램 종료 처리
     */
    private void FinishWork() {

//        //백그라운드로 넘김
//        moveTaskToBack(true);
//
//        // 서비스 중지
//        StopWorkService();
//
//        // 리시버 해제
//        StopReceiver();

        // 로그인 액티비티 종료
        //((LoginActivity) LoginActivity.getLoginContext()).finish();

        // 현재 액티비티 종료
        finish();

//        // 프로세스 종료
//        android.os.Process.killProcess(android.os.Process.myPid());
//
//        // 시스템 종료
//        System.exit(1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == POPMENU_EXIT) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {

        // 서비스 중지
        StopWorkService();

        // 리시버 해제
        StopReceiver();

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean bRet = super.onKeyDown(keyCode, event);
        Loggers.d2(this, "keyCode = " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            mBackButtonCount++;

            if (mBackButtonCount < 2) {

//                UDialog.withTwoCustom_okCancel(DeliveryListActivity.this, "쿡장-배송관리", getString(R.string.FINISH_PROGRAM_YN), "예", "아니오", View.VISIBLE, true).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//                    @Override
//                    public void onClick_yes() {
//                        // 프로그램 종료
//                        FinishWork();
//                    }
//
//                    @Override
//                    public void onClick_no() {
//
//                    }
//                });
                // 프로그램 종료
                FinishWork();

                mBackButtonCount = 0;
            }
            bRet = true;
        } else if (keyCode == KeyEvent.KEYCODE_HOME ) {
            bRet = true;
        }
        return bRet;
    }

    /**
     * 리프레쉬버튼 반응..
     * @param view
     */
    public void clickRefreshButton(View view) {
        try {
            String DDATE = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
            String CVO_DISPATCH_ID = SettingPref.with(mContext).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");

            if (DDATE.equals(search_date_sch)) {
                // 배차번호 체크..
                if (
                        !CVO_DISPATCH_ID.isEmpty()
                ) {
                    // CVO 배송 목록 요청
                    bDeliveryList_Running = false;
                    requestCvoDeliveryList(CVO_DISPATCH_ID);
                    // 리스트변경차단
                    listEditYn = true;
                } else {
                    // ERP 배송 목록 요청
                    bDeliveryList_Running = false;
                    requestErpDispatchSel();
                }
            } else {
                // ERP 배송 목록 요청
                bDeliveryList_Running = false;
                requestErpDispatchSel();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 날짜 더하기 빼기 클릭시..
     * @param view
     */
    public void moveSearchDate(View view) {

        int nTag = Integer.parseInt(view.getTag().toString());

        // 날짜 텍스트 변경처리
        setSearchDate(nTag);

        recyclerView.removeAllViews();

        // 새로고침처리..
        clickRefreshButton(null);
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
     * 거래처상세 팝업 슬라이드
     */
    public void popClientDetailInfo(String code) {
        try {

            Intent intent = new Intent(mContext, SlidePopClientDetailActivity.class);
            intent.putExtra("CLCODE",  code);
//            intent.putExtra("OPEN_TYPE", PopType._일반상품);
//            intent.putExtra("BOX_QTY", info.getORD_BOXQTY());
//            intent.putExtra("UNIT_QTY", info.getORD_UNITQTY());

//            // 기존 주문건수가 있다면...
//            int boxQty = Integer.parseInt(info.getORD_BOXQTY());
//            int unitQty = Integer.parseInt(info.getORD_UNITQTY());
//            if((boxQty>0) || (unitQty>0)) {
//                intent.putExtra("OPEN_TYPE", PopType._일반상품_수정);
//            }

            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.no_change); // 위로 슬라이드..

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 카카오 내비 실행
     * @param info
     */
    public void launchKakaoNavi(DeliveryListInfo info) {
        try {
            KakaoService kakao = new KakaoService();
            kakao.launchNavi(this, mContext, info.getCLNAME(), info.getLON(), info.getLAT());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * TMap 내비 실행
     * @param info
     */
    public void launchTMapNavi(DeliveryListInfo info) {
        try {
            // 웹을통한 TMAP호출기능
            String url = String.format(
                    "https://apis.openapi.sk.com/tmap/app/routes?appKey=9qVTjoOTzj4HMrrJAiDt44L2gzFvdD5YaLBdx7Bb&name=%s&lon=%s&lat=%s",
                    info.getCLNAME(), info.getLON(), info.getLAT());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);

//            TMapTapi tmaptapi = new TMapTapi(mContext);
//            tmaptapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
//                @Override
//                public void SKTMapApikeySucceed() {
////                    if (tmaptapi.isTmapApplicationInstalled()) {
////                        Log.e("HomeActivity", "INSTALLED");
//                        float fLon = Float.parseFloat(info.getLON());
//                        float fLat = Float.parseFloat(info.getLAT());
//                        tmaptapi.invokeRoute(info.getCLNAME(), fLon, fLat);
//////                tmaptapi.invokeRoute("T타워", 126.984098f, 37.566385f);
////                    } else {
////
////                        Log.e("HomeActivity", "NOT INSTALLED"); // ◀ 설치되어있지 않다고 나옴
////
////                    }
//                }
//
//                @Override
//                public void SKTMapApikeyFailed(String s) {
//                    Log.e("HomeActivity", "SKTMapApikeyFailed");
//                }
//            });
//            tmaptapi.setSKTMapAuthentication("9qVTjoOTzj4HMrrJAiDt44L2gzFvdD5YaLBdx7Bb");




        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //
    /**
     * 브로드캐스트 리시버정의 - GPS위치정보 수집결과 반영
     */
//    BroadcastReceiver traceReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String gpsMsg = intent.getStringExtra(BroadCastDomain.BROADCAST_TRACE_GPS_STATUS);
//
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
//
//            if (gpsMsg.equals("GPS_FIX_TRUE")) {
//
//                Loggers.d2(this, "----- [배송목록,브로드캐스트수신 - GPS] " + gpsMsg);
//
//                // 현재 위치정보 표시
//                if (AppSetting.CUR_LOCATION != null) {
//                    Location location = AppSetting.CUR_LOCATION;
//
//                    // 속도가져오기...
//                    float speed = 0;
//                    if (location.hasSpeed()) {
//                        speed = (float) location.getSpeed();
//                    }
//                    speed = (float) (speed * 3.6);
//
//                    String gpsInfo = ""
//                            //+ strLocTime + "-"
//                            + CommonUtil.with().getAddressString(mContext, location) + "  "
//                            + CommonUtil.with().speedStringWithUnit(speed)
//                            ;
//                    Loggers.d2(this, "----- [DeliveryListActivity - BroadcastReceiver] gpsInfo = " + gpsInfo);
//                    setAddinInfoText(ADDINFO_GPS, gpsInfo);
//
//                    llRouteResult.setBackgroundColor(Color.parseColor("#00D4FF"));
//
//                }
//            } else if (gpsMsg.equals("FINISH_REPORT_OK")) {
//                // 점포도착 보고 메시지 표시
//                UDialog.withTwoCustom_okCancel(mContext, "점포도착", getString(R.string.FINISH_REPORT_OK), "확인", "취소", View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//                    @Override
//                    public void onClick_yes() {
//                        // 화면갱신처리...
//                        if (mProgress.getVisibility() != View.VISIBLE) {
//                            // 최종차수정보 확인 - 목록갱신 포함됨...
//                            searchWorKStatus();
//                        }
//                    }
//
//                    @Override
//                    public void onClick_no() {
//                    }
//                });
//            } else {
//                // 현재 위치정보 표시
//                if (AppSetting.CUR_LOCATION != null) {
//                    // 최근 위치확인시간
//                    long curLocTime = AppSetting.CUR_LOCATION.getTime();
//                    // 현재시간
//                    long curTime = System.currentTimeMillis();
//                    // 최근 위치확인 시간과 현재시간의 차이(초)
//                    long diffSecond = (curTime - curLocTime) / 1000;
//                    // GPS 수집주기 확인
//                    String gps_get_cycle = SettingPref.with(mContext).loadPref(ConstValue.Config.GPS_RECV_CYCLE.name(), "");
//
//                    if (diffSecond > Integer.valueOf(gps_get_cycle)) {
//                        // GPS 수집주기를 초과하였다면...
//                        llRouteResult.setBackgroundColor(Color.parseColor("#e60013"));
//                        setAddinInfoText(ADDINFO_GPS, "GPS상태가 원활하지 않습니다.");
//                    }
//                } else {
//                    llRouteResult.setBackgroundColor(Color.parseColor("#e60013"));
//                    setAddinInfoText(ADDINFO_GPS, "GPS상태가 원활하지 않습니다.");
//                }
//            }
//
//            llRouteResult.setVisibility(View.VISIBLE);
//        }
//    };
}
