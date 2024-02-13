package com.gzonesoft.sg623.Layout;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.gzonesoft.sg623.DriverMainActivity;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.data.ClientCustInfo;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.naver.maps.geometry.LatLng;


// 배차리스트, 배차수 탭 표현
public class ClientCustTabLayout extends LinearLayout {

    public static Context mContext = null;

    public ClientCustInfo mCurInfo = null;

    public Button btnCamera, btnNavi, btnSelectClient;

    public TextView tvRouteSeq; // 배차순번
    public TextView tvClname; // 배송처명
    public TextView tvImageCnt; // 이미지건수
    public TextView tvDistance; // 예상거리
    public TextView tvAddress; // 주소


    private CardView cardViewDelImage;
    private ImageView ivDelImage,ivCamera;

    private View viewCameraRound;

    // 애니메이션 효과
    Animation animationStart = null;
    Animation animationStop = null;

    /**
     * 생성자1
     * @param context
     * @param attrs
     */
    public ClientCustTabLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        init(context);
    }

    /**
     * 생성자2
     * @param context
     * @param info
     */
    public ClientCustTabLayout(Context context, ClientCustInfo info) {
        super(context);

        mContext = context;
        mCurInfo = info;

        init(context);
        // 데이타 세팅
        setData();

//        slideLeft(tvRouteSeq);
//        slideUp(tvDistance);
//        slideRight(tvDistance);
//        slideLeft(tvDistance);
//        slideDown(tvDistance);

        // 애니메이션 효과로 마퀴즈가 사라져, 잠시뒤에 재호출...
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 1500);

    }

    /**
     * 카메라 버튼 클릭..
     * @param v
     */
    public void clickCamera(View v) {
        Loggers.d("카메라 버튼 클릭.." + mCurInfo.getCLNAME());
    }

    /**
     * 화면 초기화
     * @param context
     */
    private void init(Context context){

        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_clientcust_tab_01,this,true);

        // 버튼 - 카메라촬영
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CvoMainActivity) CvoMainActivity.mContext).takePicture4Client(
                        CommonUtil.with().nowYYYY_MM_DD(),
                        mCurInfo.getCLCODE());
            }
        });

        // 버튼 - 네비게이션안내
        btnNavi = (Button) findViewById(R.id.btnNavi);
        btnNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TMAP내비
                ((CvoMainActivity) CvoMainActivity.mContext).launchTMapNavi(
                        mCurInfo.getCLNAME(),
                        mCurInfo.getLAT(),
                        mCurInfo.getLON()
                );


//                // 카카오내비
//                ((CvoMainActivity) CvoMainActivity.mContext).launchKakaoNavi(
//                        mCurCust.getCLNAME(),
//                        mCurCust.getLAT(),
//                        mCurCust.getLON()
//                );
            }
        });

        // 버튼 - 지도이동하기
        btnSelectClient = (Button) findViewById(R.id.btnSelectClient);
        btnSelectClient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CvoMainActivity) CvoMainActivity.mContext).moveNaverMap(
                        new LatLng(
                                Double.parseDouble(mCurInfo.getLAT()),
                                Double.parseDouble(mCurInfo.getLON())
                        ),
                        mCurInfo
                );
            }
        });

        tvRouteSeq = (TextView) findViewById(R.id.tvRouteSeq);
        tvClname = (TextView) findViewById(R.id.tvClname);
        tvImageCnt = (TextView) findViewById(R.id.tvImageCnt);
        tvImageCnt.setVisibility(GONE);

        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        ivCamera = findViewById(R.id.ivCamera);
        viewCameraRound = findViewById(R.id.viewCameraRound);

        cardViewDelImage = findViewById(R.id.cardViewDelImage);
        ivDelImage = findViewById(R.id.ivDelImage);

        // 애니메이션리소스 매핑
        animationStart = AnimationUtils.loadAnimation(this.mContext, R.anim.animation_start_speed);
        animationStop = AnimationUtils.loadAnimation(this.mContext, R.anim.animation_stop);
    }

    /**
     * 데이타 세팅
     */
    private void setData() {
        try {
            String htmlString = "";
            // --------------------------------------------------------------------------------
            // 거리정보
            // --------------------------------------------------------------------------------
            double distance = Double.parseDouble(mCurInfo.getDISTANCE_ME());
//            try {
//                double curLat = AppSetting.GPS_MANAGER.getGpsLat();
//                double curLon = AppSetting.GPS_MANAGER.getGpsLng();
//                double targetLat = Double.parseDouble(mCurInfo.getLAT());
//                double targetLon = Double.parseDouble(mCurInfo.getLON());
//                distance = CommonUtil.with().distance(curLat, curLon, targetLat, targetLon);
//            } catch (Exception ex) {
//                distance = -1;
//                ex.printStackTrace();
//            }

            // 배차순번
            CommonUtil.with().setHtmlText(tvRouteSeq, String.format("%d", mCurInfo.getIdx()));
            // 거래처명
            CommonUtil.with().setHtmlMarqueeText(tvClname, mCurInfo.getCLNAME());
            // 이미지건수 세팅
            try {
                if (Integer.parseInt(mCurInfo.getIMAGE_CNT_TODAY()) > 0) {
                    tvImageCnt.setText(mCurInfo.getIMAGE_CNT_TODAY());
                    tvImageCnt.setVisibility(VISIBLE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            if (distance <= 0) {
                htmlString = "- <B>위치 미등록</B> -";
            } else {
                htmlString = "<B>" + CommonUtil.with().distanceStringOnlyValue(distance / 1000) + "</B>km <small>주변</small>";
            }
            CommonUtil.with().setHtmlMarqueeText(tvDistance, htmlString);
            // 200미터이내의 경우...
            if (distance < 3000) {
//                tvDistance.setBackgroundResource(R.drawable.round_bottom_01_distance_inbound);
//                tvDistance.setTextColor(Color.parseColor("#FFFFFF"));
                tvDistance.startAnimation(animationStart);
            } else {
//                tvDistance.setBackgroundResource(R.drawable.round_bottom_01_distance_normal);
//                tvDistance.setTextColor(Color.parseColor("#0072C3"));
                tvDistance.startAnimation(animationStop);
            }
            // 주소세팅
            tvAddress.setBackgroundResource(R.drawable.round_bottom_01_distance_normal);
            tvAddress.setTextColor(Color.parseColor("#0072C3"));
            htmlString = mCurInfo.getCLJUSO1();
            CommonUtil.with().setHtmlMarqueeText(tvAddress, htmlString);



        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
