package com.gzonesoft.sg623.pop;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//import com.gzonesoft.cookzzang.DispatchMainActivity;
//import com.gzonesoft.cookzzang.ManagerMainActivity;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.R;
//import com.gzonesoft.cookzzang.SalesMainActivity;
import com.gzonesoft.sg623.comm.RequesterSession;
//import com.gzonesoft.cookzzang.data.OrderInfo;
//import com.gzonesoft.cookzzang.data.VehicleInfo;
//import com.gzonesoft.cookzzang.data.model.OrderListManager;
//import com.gzonesoft.cookzzang.data.model.VehicleListManager;
//import com.gzonesoft.cookzzang.domain.CVOEventCode;
import com.gzonesoft.sg623.data.CvoNowPosition;
import com.gzonesoft.sg623.data.DeliveryListInfo;
//import com.gzonesoft.cookzzang.layout.NearOrderListItemLayout;
import com.gzonesoft.sg623.model.CvoNowPositionListManager;
import com.gzonesoft.sg623.ui.DispatchSummaryActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.UDialog;
import com.naver.maps.geometry.LatLng;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PopDetailVehicleActivity extends AppCompatActivity {

    public static Context mContext = null;

    private LinearLayout ll_area_01, ll_area_02, ll_area_03, ll_area_04;
    private TextView tvInfo01, tvInfo02, tvInfo03, tvInfo04, tvInfo05;
    private TextView tvInfo06, tvInfo07, tvInfo08, tvInfo09, tvInfo10;
    private TextView tvInfo11, tvInfo12, tvInfo13, tvInfo14, tvInfo15;

    private Button btnMethod01, btnMethod02;
    private Button btnMoreAction01, btnMoreAction02, btnMoreAction03, btnMoreAction04, btnMoreAction05;

    private TextView tvTitle;
    private LinearLayout llNearByInfo, llHScrollItemArea;
    private ScalableLayout scTitle;

    private View viewBtn, viewActionButtonBG;
    private Button btnSms, btnTel, btnAction1, btnAction2, btnAction3, btnOK, btnCancel;
    private ImageView ivSms, ivTel;
    private View viewVLine01, viewVLine02;

    private boolean bShowOrder = true;

    private CvoNowPosition curInfo = new CvoNowPosition();

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int RETRY_PROCESS = 19999;
    public static final int LIST_MESSAGE = 20000;
    public static final int NO_DATA_FOUND = 20001;
    public static final int ERR_EXCEPTION = 20002;
    public static final int REQ_NEAR_CUST_SEARCH_OK  = 30000;   // 현위치 주변 거래처 가져오기
    public static final int REQ_NEAR_ORDER_SEARCH_OK  = 30001;   // 현위치 주변 주문 가져오기
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            double lat, lon;

            switch (msg.what) {

                case REQ_NEAR_CUST_SEARCH_OK:
                    // 대기화면 닫기..
                    showProgress(false);
                    break;

                case REQ_NEAR_ORDER_SEARCH_OK:
//                    if (OrderListManager.getInstance().values().size()>0) {
//                        // 가로스크롤뷰 지우기
//                        llHScrollItemArea.removeAllViews();
//
//                        for (OrderInfo info : OrderListManager.getInstance().values()) {
//                            addLayoutItem(info);
//                        }
//                        llNearByInfo.setVisibility(View.VISIBLE);
//                        if (((ManagerMainActivity) ManagerMainActivity.mContext) != null) {
//                            // 메인지도에 마커 추가 - 현재기준 검색된 차량으로...
//                            ((ManagerMainActivity) ManagerMainActivity.mContext).setOrderMarkers(OrderListManager.getInstance().values());
//                        }
//                    } else {
//                        CommonUtil.with().ToastMsg(mContext, "검색된 주문이 없습니다.", Toast.LENGTH_SHORT).show();
//                    }

                    // 대기화면 닫기..
                    showProgress(false);
                    btnOK.setEnabled(true);
                    break;

                case NO_DATA_FOUND:
                    // 대기화면 닫기..
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, "데이타가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case ERR_EXCEPTION:
                    // 대기화면 닫기..
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, "예외사항이 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case RETRY_PROCESS:
                    // 대기화면 닫기..
                    showProgress(false);
                    break;

                case LIST_MESSAGE:
                    // 대기화면 닫기..
                    showProgress(false);
                    break;

                case RequesterSession.REQ_OK:
                    // 대기화면 닫기..
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    // 대기화면 닫기..
                    showProgress(false);
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;
            }

            return true;
        }
    });


    // 현재 차량 정보
    private CvoNowPosition cvoNowPosition = new CvoNowPosition();
    // 현재 차량의 주문정보
    private DeliveryListInfo deliveryListInfo = null;


    // 배차여부
    public boolean mDispatchYn = false;

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
// 팝업에서는 띄우지않는걸로...
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                if (show) {
//                    pDialog.show();
//                }
//                else {
//                    pDialog.dismiss();
//                }
//            } else {
//                if (show) {
//                    pDialog.show();
//                }
//                else {
//                    pDialog.dismiss();
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // 넘어온 데이타 확인
        Intent intent = getIntent();
        String DEVICE_NO = String.valueOf(intent.getStringExtra("DEVICE_NO"));
        cvoNowPosition = CvoNowPositionListManager.getInstance().getCvoNowPositionInfo(DEVICE_NO);

//        if (VehicleListManager.getInstance().values().size() == 0) {
//            // 테스트용 차량 추가..
//            CommonUtil.with().ToastMsg(mContext, "테스트용 차량입니다..", Toast.LENGTH_SHORT).show();
//            VehicleInfo info = new VehicleInfo();
//            info.setDISTANCE("6.3");
//            info.setVEHICLE_NO("경북86아9013");
//            info.setLAST_DTM("05/10 23:05:56");
//            info.setLATITUDE("37.505817");
//            info.setLONGITUDE("126.813072");
//            info.setVELOCITY("36");
//            info.setDIRECTION("116");
//            info.setADDRESS("경기도 부천시 춘의동 길주로 640 계남주유소 (29m)");
//            info.setDISPATCH_YN("Y");
//            info.setMARKER_TITLE("36");
//            info.setMARKER_SUBTITLE("경북86아9013");
//            info.setMARKER_CAPTION("경기도 부천시 춘의동 길주로 640 계남주유소 (29m)");
//            info.setMARKER_SUBCAPTION("05/10 23:05:56");
//
//            VehicleListManager.getInstance().clear();
//            VehicleListManager.getInstance().add(info);
//
//            cvoNowPosition = info;
//        } else {
//            // 넘어온 차량..
//            cvoNowPosition = VehicleListManager.getInstance().getVehicleInfoForNo(VEHICLE_NO);
//        }


        Loggers.d(cvoNowPosition.toString());

        // UI 초기화
        initUI();

        // 프로그래스다이얼로그 초기화
        initProgressDlg();

        // 데이타세팅
        setData();

        // 지도이동
        moveDest();

        // 주변주문조회
//        double lat = Double.parseDouble(mVehicleInfo.getLATITUDE());
//        double lon = Double.parseDouble(mVehicleInfo.getLONGITUDE());
//        getNearOrderSearch(new LatLng(lat, lon));

        slideUp(tvInfo01);

        slideDown(viewBtn);
        slideUp(btnSms);
        slideDown(ivSms);
        slideUp(btnTel);
        slideDown(ivTel);
        slideUp(viewVLine01);

        slideUp(tvInfo04);
        slideUp(tvInfo02);

        slideLeft(btnAction1);
        slideRight(btnAction2);
        slideLeft(viewActionButtonBG);

        slideUp(viewVLine02);


        slideDown(tvTitle);

        //slideUpDelayTime += 800;
        slideUp(btnCancel);

    }


    @Override
    public void setRequestedOrientation(int requestedOrientation){
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            super.setRequestedOrientation(requestedOrientation);
        }
    }

    /**
     * UI 초기화
     */
    private void initUI() {
        setContentView(R.layout.activity_pop_detail_vehicle);

        // 상태바 색상 지정(검은색으로)
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().hide();

        // 뷰 매핑
        tvInfo01 = (TextView) findViewById(R.id.tvInfo01);
        tvInfo02 = (TextView) findViewById(R.id.tvInfo02);
        tvInfo03 = (TextView) findViewById(R.id.tvInfo03);
        tvInfo04 = (TextView) findViewById(R.id.tvInfo04);

        btnMoreAction01 = (Button) findViewById(R.id.btnMoreAction01);
        btnMoreAction02 = (Button) findViewById(R.id.btnMoreAction02);
        btnMoreAction03 = (Button) findViewById(R.id.btnMoreAction03);
        btnMoreAction04 = (Button) findViewById(R.id.btnMoreAction04);
        btnMoreAction05 = (Button) findViewById(R.id.btnMoreAction05);
        String htmlString = "";
        htmlString = "-ALL-<BR>전체"; CommonUtil.with().setHtmlText(btnMoreAction01, htmlString);
        htmlString = "UG<BR>휘발유"; CommonUtil.with().setHtmlText(btnMoreAction02, htmlString);
        htmlString = "ULSD<BR>경질유"; CommonUtil.with().setHtmlText(btnMoreAction03, htmlString);
        htmlString = "LPG<BR><small>액화석유</small>"; CommonUtil.with().setHtmlText(btnMoreAction04, htmlString);
        htmlString = "SLOP<BR>항공유"; CommonUtil.with().setHtmlText(btnMoreAction05, htmlString);

        scTitle = (ScalableLayout) findViewById(R.id.scTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        // 주변정보 조회레이어
        llNearByInfo = (LinearLayout) findViewById(R.id.llNearByInfo);
        llNearByInfo.setVisibility(View.GONE);
        // 좌우스크롤영역 레이어
        llHScrollItemArea = (LinearLayout) findViewById(R.id.llHScrollItemArea);

//        scArea01 = (ScalableLayout) findViewById(R.id.scArea01);
//        scArea02 = (ScalableLayout) findViewById(R.id.scArea02);
//        scArea03 = (ScalableLayout) findViewById(R.id.scArea03);

        //  방법#1
        btnMethod01 = (Button) findViewById(R.id.btnMethod01);
        btnMethod01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(getApplicationContext(), PopSelectOilTypeActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_up, R.anim.no_change);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        // 방법#2
        btnMethod02 = (Button) findViewById(R.id.btnMethod02);
        btnMethod02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(getApplicationContext(), PopSelectAreaActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_up, R.anim.no_change);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        viewBtn = (View) findViewById(R.id.viewBtn);
        viewActionButtonBG = (View) findViewById(R.id.viewActionButtonBG);
        ivSms = (ImageView) findViewById(R.id.ivSms);
        ivTel = (ImageView) findViewById(R.id.ivTel);
        viewVLine01 = (View) findViewById(R.id.viewVLine01);
        viewVLine02 = (View) findViewById(R.id.viewVLine02);

        btnSms = (Button) findViewById(R.id.btnSms);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 한번더 물어본다.
                String htmlString = "" +
                        "<big><B><font color='#3F51B5'>" +
                        "" + cvoNowPosition.getVEHICLENO() + " - " + cvoNowPosition.getNAME() +
                        "</font><B></big>" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "기사님에게" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "<big><B><font color='#ED7D31'>문자</font><B></big> 를 전송 하시겠습니까?";
                UDialog.withHtmlTwoCustom_okCancel(mContext, "문자 전송", htmlString, "보내기", "취소", View.VISIBLE, true).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                    @Override
                    public void onClick_yes() {
                        if (cvoNowPosition.getDEVICENO().length()>0) {
                            Uri smsUri = Uri.parse("sms:"+ cvoNowPosition.getDEVICENO());
//                            Uri smsUri = Uri.parse("sms:01094065736"); // 개발용..
                            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
                            sendIntent.putExtra("sms_body","");
                            startActivity(sendIntent);
                        } else {
                            CommonUtil.with().ToastMsg(mContext, "전화번호가 비었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onClick_no() {
//                        CommonUtil.with().ToastMsg(mContext, "취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnTel = (Button) findViewById(R.id.btnTel);
        btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 한번더 물어본다.
                String htmlString = "" +
                        "<big><B><font color='#3F51B5'>" +
                        "" + cvoNowPosition.getVEHICLENO() + " - " + cvoNowPosition.getNAME() +
                        "</font><B></big>" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "기사님에게" +
                        "<small><small><small><small><BR><BR></small></small></small></small>" +
                        "<big><B><font color='#ED7D31'>전화</font><B></big> 를 연결 하시겠습니까?";
                UDialog.withHtmlTwoCustom_okCancel(mContext, "전화연결", htmlString, "연결하기", "취소", View.VISIBLE, true).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                    @Override
                    public void onClick_yes() {

                        if (cvoNowPosition.getDEVICENO().length()>0) {

                            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                                // 안드로이드 M 이상일 경우
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // 전화걸기 시도...
                                    String mdn = cvoNowPosition.getDEVICENO().replace("-", "");
                                    Loggers.d("[fromMobileMessage] mdn =  " + mdn);
                                    // 전화걸기
                                    final String callString = "tel:" + mdn;
                                    Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse(callString));
                                    startActivity(tt);
                                }
                                else {
                                    //전화 권한 없을 경우
                                    CommonUtil.with().ToastMsg(mContext, "전화걸기 권한을 확인바랍니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else { // 안드로이드 M 이하일 경우
                                // 전화걸기 시도...
                                String mdn = cvoNowPosition.getDEVICENO().replace("-", "");
                                Loggers.d("[fromMobileMessage] mdn =  " + mdn);
                                // 전화걸기
                                final String callString = "tel:" + mdn;
                                Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse(callString));
                                startActivity(tt);
                            }

                        } else {
                            CommonUtil.with().ToastMsg(mContext, "등록된 전화번호를 확인바랍니다.", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onClick_no() {
//                        CommonUtil.with().ToastMsg(mContext, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnAction1 = (Button) findViewById(R.id.btnAction1);
        btnAction1.setText("배차보기");
//        if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._영업)) {
//        }
        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 배차내역조회

                Intent intent = new Intent(getApplicationContext(), DispatchSummaryActivity.class);
                intent.putExtra("SELECT_DEVICE_NO", cvoNowPosition.getDEVICENO());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_change);

////                if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._영업)) {
//                    try {
//                        // 주문보기
//                        //OrderInfo info = OrderListManager.getInstance().getOrderInfoForVehicleCd(mVehicleInfo.getVEHICLE_CD());
//                        if (deliveryListInfo.getDELI_NO().length() > 0) {
//                            if (bShowOrder) {
//                                bShowOrder = false;
//                                btnAction1.setText("차량보기");
//                                // 지도이동
//                                double lat = Double.parseDouble(deliveryListInfo.getDEST_LAT());
//                                double lon = Double.parseDouble(deliveryListInfo.getDEST_LNG());
//                                if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._영업)) {
//                                    if (((SalesMainActivity) SalesMainActivity.mContext) != null) {
//                                        ((SalesMainActivity) SalesMainActivity.mContext).moveNaverMap(new LatLng(lat, lon));
//                                    }
//                                }
//                                if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._배차)) {
//                                    if (((DispatchMainActivity) DispatchMainActivity.mContext) != null) {
//                                        ((DispatchMainActivity) DispatchMainActivity.mContext).moveNaverMap(new LatLng(lat, lon));
//                                    }
//                                }
//                            } else {
//                                bShowOrder = true;
//                                btnAction1.setText("주문보기");
//                                // 지도이동
//                                double lat = Double.parseDouble(cvoNowPosition.getLATITUDE());
//                                double lon = Double.parseDouble(cvoNowPosition.getLONGITUDE());
//                                if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._영업)) {
//                                    if (((SalesMainActivity) SalesMainActivity.mContext) != null) {
//                                        ((SalesMainActivity) SalesMainActivity.mContext).moveNaverMap(new LatLng(lat, lon));
//                                    }
//                                }
//                                if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._배차)) {
//                                    if (((DispatchMainActivity) DispatchMainActivity.mContext) != null) {
//                                        ((DispatchMainActivity) DispatchMainActivity.mContext).moveNaverMap(new LatLng(lat, lon));
//                                    }
//                                }
//                            }
//                        } else {
//                            // 주문(배차)정보가 없는경우..
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
////                } else {
////                    // 배차내역
////                    CommonUtil.with().ToastMsg(mContext, "개발중 입니다. 잠시만, 기다려 주세요.", Toast.LENGTH_SHORT).show();
////                }
            }
        });

        //
        btnAction2 = (Button) findViewById(R.id.btnAction2);
//        if (AppSetting.loginInfo.getUSER_TYPE().equals(UserType._영업)) btnAction2.setText("이동경로");
        btnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 경로조회
                ((CvoMainActivity) CvoMainActivity.mContext).openRouteInfo(cvoNowPosition.getDEVICENO());

//                // 경로조회
//                if (((ManagerMainActivity) ManagerMainActivity.mContext) != null) {
//                    ((ManagerMainActivity) ManagerMainActivity.mContext).openRouteInfo(mVehicleInfo.getMIN_NO());
//                } else if (((SalesMainActivity) SalesMainActivity.mContext) != null) {
//                    ((SalesMainActivity) SalesMainActivity.mContext).openRouteInfo(mVehicleInfo.getMIN_NO());
//                }

//                // 주문보기
//                //OrderInfo info = OrderListManager.getInstance().getOrderInfoForVehicleCd(mVehicleInfo.getVEHICLE_CD());
//                if (deliveryListInfo.getDELI_NO().length() > 0) {
//
//                    // CVO 관제조회화면으로 이동..
//                    String encodeURL = "";
//                    // 주문찾기
//
//                    try {
//                        encodeURL = CommonUtil.with().encodeBase64("order_no=" + deliveryListInfo.getORDER_NO());
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    Loggers.d("관제호출URL=https://hdoatoms.oilbank.co.kr/open/orderVehicle?" + encodeURL);
//
//                    if (Integer.parseInt(deliveryListInfo.getDELI_STS_CD()) < 400) { // 출하지출발이전이면..
//                        handler.obtainMessage(RequesterSession.REQ_OK, "죄송합니다.\n아직, 출발전입니다.").sendToTarget();
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), PopWebCVOActivity.class);
//                        intent.putExtra("MOVE_URL", "https://hdoatoms.oilbank.co.kr/open/orderVehicle?" + encodeURL);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
//                    }
//                } else {
//                    // 주문(배차)정보가 없는경우..
//                }
//                // 팝업닫기..
//                //PopFinishWork();
            }
        });

        // 하단 왼쪽버튼 - 주변차량조회
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // 주변주문조회
//                getNearOrderSearch(((ManagerMainActivity) ManagerMainActivity.mContext).mCurrentCoord);
            }
        });

        // 하단 오른쪽버튼
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopFinishWork();
            }
        });

    }

    /**
     * 위치 이동
     */
    public void moveDest() {
        try {
//            // 지도이동
//            double lat = Double.parseDouble(cvoNowPosition.getLATITUDE());
//            double lon = Double.parseDouble(cvoNowPosition.getLONGITUDE());
//            if (((ManagerMainActivity) ManagerMainActivity.mContext) != null) {
//                ((ManagerMainActivity) ManagerMainActivity.mContext).moveNaverMap(new LatLng(lat, lon));
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 배경영역을 터치시.(View XML에서 기술함..)
     * @param view
     */
    public void hideQuickMenu(View view) {
        PopFinishWork();
    }

    /**
     * 종료작업 - 팝업 닫기
     */
    public void PopFinishWork() {
        // 닫기
        overridePendingTransition(R.anim.slide_down, R.anim.no_change);
        finish();
    }

    /**
     * 데이타세팅
     */
    private void setData() {
        try {
            String htmlString = "";

            // 차량번호
            htmlString = "<small><B>" +
                    "" + cvoNowPosition.getVEHICLENO() + "<small> (" + cvoNowPosition.getNAME() + ")</small>" +
                    "</B></small>" +
                    "<small><small> - " + cvoNowPosition.getMARKER_SUBCAPTION() + "</small></small>";
            CommonUtil.with().setHtmlText(tvTitle, htmlString);

            // 주소
            htmlString = cvoNowPosition.getADDRESS();
            CommonUtil.with().setHtmlText(tvInfo02, htmlString);

            // 일시
            htmlString = "Update. " + cvoNowPosition.getMARKER_SUBCAPTION();
            CommonUtil.with().setHtmlMarqueeText(tvInfo03, htmlString);

            // 운행중 확인
            boolean bRunningYn = false;
            try {
                if (Integer.parseInt(cvoNowPosition.getSPEED())>0) bRunningYn = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (bRunningYn) {
                scTitle.setBackgroundResource(R.drawable.pop_bg_corners_round_10_blue_01);
                tvInfo01.setTextColor(Color.parseColor("#4CAF50"));
                // 속도
                htmlString = cvoNowPosition.getSPEED();
                CommonUtil.with().setHtmlText(tvInfo04, htmlString);
                tvInfo04.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else {
                scTitle.setBackgroundResource(R.drawable.pop_bg_corners_round_10_gray_01);
                tvInfo01.setTextColor(Color.parseColor("#4CAF50"));
                // 속도
                htmlString = cvoNowPosition.getSPEED();
                CommonUtil.with().setHtmlText(tvInfo04, htmlString);
                tvInfo04.setBackgroundColor(Color.parseColor("#767876"));
            }
//            // 배차여부에 따른 구분표시..
//            if (cvoNowPosition.getDISPATCH_YN().equals("Y")) {
//                // 배차된 경우..
//                scTitle.setBackgroundResource(R.drawable.pop_bg_corners_round_10_blue_01);
//                tvInfo01.setTextColor(Color.parseColor("#4CAF50"));
//                // 속도
//                htmlString = cvoNowPosition.getSPEED();
//                CommonUtil.with().setHtmlText(tvInfo04, htmlString);
//                tvInfo04.setBackgroundColor(Color.parseColor("#4CAF50"));
//            } else {
//                // 배차되지 않은 경우..
//                scTitle.setBackgroundResource(R.drawable.pop_bg_corners_round_10_gray_01);
//                tvInfo01.setTextColor(Color.parseColor("#767876"));
//                // 속도
//                htmlString = cvoNowPosition.getVELOCITY();
//                CommonUtil.with().setHtmlText(tvInfo04, htmlString);
//                tvInfo04.setBackgroundColor(Color.parseColor("#767876"));
//            }

            // 상태 세팅
            tvInfo01.setText(cvoNowPosition.getSTATUS_NM());
//            if (cvoNowPosition.getEVENT_CODE().equals(CVOEventCode._업무시작))
//                tvInfo01.setText("업무시작");
//            else if (cvoNowPosition.getEVENT_CODE().equals(CVOEventCode._출하중))
//                tvInfo01.setText("출하중");
//            else if (cvoNowPosition.getEVENT_CODE().equals(CVOEventCode._하화중))
//                tvInfo01.setText("하화중");
//            else if (cvoNowPosition.getEVENT_CODE().equals(CVOEventCode._출하지이동))
//                tvInfo01.setText("출하지이동");
//            else if (cvoNowPosition.getEVENT_CODE().equals(CVOEventCode._차고지이동))
//                tvInfo01.setText("차고지이동");
//            else if (cvoNowPosition.getEVENT_CODE().equals(CVOEventCode._휴차))
//                tvInfo01.setText("휴차");

            // 현재 차량의 주문정보 확인
//            try {
//                String[] arrDispInfo = cvoNowPosition.getLAST_DISP_INFO().split("/");
//                if (arrDispInfo[0].length()>0) { // 최종 배차정보가 있다면...
//                    String DISPATCH_NO = arrDispInfo[0];
//                    String DELI_NO = arrDispInfo[1];
//                    String ROUTE_SEQ = arrDispInfo[2];
//                    String DEST_CD = arrDispInfo[3];
////                    String EVENT_TYPE = arrDispInfo[4];
////                    String EVENT_NAME = arrDispInfo[5];
//                    deliveryListInfo = OrderListManager.getInstance().getOrderInfoForDeliNo(DELI_NO);
//                    if (deliveryListInfo.getDELI_STS_CD().isEmpty()) {
//                        deliveryListInfo = OrderListManager.getInstance().getOrderInfoForVehicleCd(cvoNowPosition.getVEHICLE_CD());
//                    }
//                } else {
//                    deliveryListInfo = OrderListManager.getInstance().getOrderInfoForVehicleCd(cvoNowPosition.getVEHICLE_CD());
//                }
//                // 주문 확인
//                if (deliveryListInfo.getDELI_STS_CD().isEmpty()) {
//                    // 주문/경로보기 버튼 비활성화...
//                    btnAction1.setEnabled(false);
//                    btnAction1.setTextColor(Color.parseColor("#507F7F7F"));
//                    btnAction2.setEnabled(false);
//                    btnAction2.setTextColor(Color.parseColor("#507F7F7F"));
//                } else {
//                    if (Integer.parseInt(deliveryListInfo.getDELI_STS_CD()) < 400) { // 출하지출발이전이면..
//                        // 주문/경로보기 버튼 비활성화...
//                        btnAction1.setEnabled(false);
//                        btnAction1.setTextColor(Color.parseColor("#507F7F7F"));
//                        btnAction2.setEnabled(false);
//                        btnAction2.setTextColor(Color.parseColor("#507F7F7F"));
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 동적 레이아웃 추가 메소드
     */
//    private void addLayoutItem(OrderInfo info) {
//
//        NearOrderListItemLayout itemLayout = new NearOrderListItemLayout(getApplicationContext(), info);
//        llHScrollItemArea.addView(itemLayout);
//    }


    // clickVehicle

    /**
     * 위치기반 주문조회
     */
    public void getNearOrderSearch(LatLng coord){

//        llNearByInfo.setVisibility(View.GONE);
//
//        if(OrderListManager.getInstance().values().size()>0)
//            OrderListManager.getInstance().clear();
//
//        showProgress(true);
//
//        Map<String, String> paraMap = new LinkedHashMap<String, String>();
//
//        paraMap.put("I_DELI_REQ_DT", AppSetting.dispatchDate);
//        paraMap.put("I_FILTER_01", "");
//        paraMap.put("I_FILTER_02", "");
//        paraMap.put("I_FILTER_03", "");
//        paraMap.put("I_PARAM_01", String.format("%.06f", coord.latitude));
//        paraMap.put("I_PARAM_02", String.format("%.06f", coord.longitude));
//        paraMap.put("I_PARAM_03", "100"); // ?km 이내
//        paraMap.put("I_PARAM_04", "");
//        paraMap.put("I_PARAM_05", "");
//
//        RequesterSession.with().RestAPIServiceCall(Svc.POST, Svc.NEAR_ORDER_SEARCH, paraMap, getNearOrderSearchCallback);

    }


    /**
     * REST-API Callback - 주문조회 콜백..
     *
     */
    private Callback getNearOrderSearchCallback = new Callback() {

        String mTag = "getNearOrderSearchCallback()";

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

//            if( response.code() == 200 ){
//
//                try {
//                    String strJsonOutput = response.body().string();
//
//                    Loggers.d2(this, mTag + " ---- 수신데이타 : " + strJsonOutput);
//
//                    JsonElement jelement = new JsonParser().parse(strJsonOutput);
//                    JsonObject jObject = jelement.getAsJsonObject();
//
//                    String resultCode = jObject.get("resultCode").getAsString();
//                    String resultMsg = jObject.get("resultMsg").getAsString();
//
//                    JsonArray jarrayRecordSet = jObject.get("resultData").getAsJsonArray();
//
//
//                    if (!resultCode.equals("00")) {
//                        handler.obtainMessage(NO_DATA_FOUND, "").sendToTarget();
//                    } else {
//
//                        if (jarrayRecordSet.size() == 0) {
//                            // 응답은 정상 - 서비스 결과 없음
//                            OrderListManager.getInstance().clear();
//                        } else {
//                            OrderListManager.getInstance().clear();
//
//                            for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
//                                JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
//                                // 데이타 추가..
//                                Gson gson = new Gson();
//                                OrderInfo info = gson.fromJson(jobjectStore, OrderInfo.class);
//                                OrderListManager.getInstance().add(info);
//                            }
//
//                        }
//
//                        handler.obtainMessage(REQ_NEAR_ORDER_SEARCH_OK, getString(R.string.complete)).sendToTarget();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Loggers.d("Exception : " + e.getMessage());
//                    handler.obtainMessage(ERR_EXCEPTION, "").sendToTarget();
//                }
//
//            }else if (response.code() == 404) {
//                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
//                return;
//
//            }else{
//                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[" + response.code() + "]" + getString(R.string.msg_conn_failure)).sendToTarget();
//                return;
//            }
        }

    };


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

}
