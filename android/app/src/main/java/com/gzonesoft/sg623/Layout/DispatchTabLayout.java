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
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;


// 배차리스트, 배차수 탭 표현
public class DispatchTabLayout extends LinearLayout {

    public static Context mContext = null;

    public DeliveryListInfo mCurInfo = null;

    public Button btnCamera;

    public TextView tvRouteSeq; // 배차순번
    public TextView tvClname; // 배송처명
    public TextView tvImageCnt; // 이미지건수
    public TextView tvDistance; // 예상거리


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
    public DispatchTabLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        init(context);
    }

    /**
     * 생성자2
     * @param context
     * @param info
     */
    public DispatchTabLayout(Context context, DeliveryListInfo info) {
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
        inflater.inflate(R.layout.item_dispatchlist_01,this,true);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Loggers.d("카메라 버튼 클릭.." + mCurInfo.getCLNAME());
                // 카메라이미지가 보일때, 호출시도.(버튼자체가 테두리를 표현하기때문에 보이지만 클릭하면 차단해야한다.)
                if(ivCamera.getVisibility() == VISIBLE) {
                    // 카메라 호출
                    ((DriverMainActivity) DriverMainActivity.mContext).takePicture(
                            mCurInfo.getDISPATCH_ID(),
                            mCurInfo.getCLCODE());
                }
            }
        });

        tvRouteSeq = (TextView) findViewById(R.id.tvRouteSeq);
        tvClname = (TextView) findViewById(R.id.tvClname);
        tvImageCnt = (TextView) findViewById(R.id.tvImageCnt);
        tvDistance = (TextView) findViewById(R.id.tvDistance);

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
            double distance = 0;
            try {
                double curLat = AppSetting.GPS_MANAGER.getGpsLat();
                double curLon = AppSetting.GPS_MANAGER.getGpsLng();
                double targetLat = Double.parseDouble(mCurInfo.getLAT());
                double targetLon = Double.parseDouble(mCurInfo.getLON());
                distance = CommonUtil.with().distance(curLat, curLon, targetLat, targetLon);
            } catch (Exception ex) {
                distance = -1;
                ex.printStackTrace();
            }

            // 배차순번
            CommonUtil.with().setHtmlText(tvRouteSeq, mCurInfo.getROUTE_SEQ());
            // 배송처명
            CommonUtil.with().setHtmlMarqueeText(tvClname, mCurInfo.getCLNAME());



            if (distance <= 0) {
                htmlString = "- <B>위치 미등록</B> -";
            } else {
                htmlString = "<B>" + CommonUtil.with().distanceStringOnlyValue(distance / 1000) + "</B>km <small>주변</small>";
            }
            CommonUtil.with().setHtmlMarqueeText(tvDistance, htmlString);
            // 200미터이내의 경우...
            if (distance < 3000) {
                tvDistance.setBackgroundResource(R.drawable.round_bottom_01_distance_inbound);
                tvDistance.setTextColor(Color.parseColor("#FFFFFF"));
                tvDistance.startAnimation(animationStart);
            } else {
                tvDistance.setBackgroundResource(R.drawable.round_bottom_01_distance_normal);
                tvDistance.setTextColor(Color.parseColor("#0072C3"));
                tvDistance.startAnimation(animationStop);
            }
//
            // 이미지 세팅
            View viewMorImgEffect1 = findViewById(R.id.viewMorImgEffect1);
            View viewMorImgEffect2 = findViewById(R.id.viewMorImgEffect2);
            if(mCurInfo.getRECENT_IMG_URL().length() >0) {
                // 업로드 이미지가있다면...
                cardViewDelImage.setVisibility(VISIBLE);
                ivCamera.setVisibility(GONE);
                viewCameraRound.setVisibility(GONE);

                ivDelImage.setScaleType(ImageView.ScaleType.CENTER);
                final String imagePath = mCurInfo.getRECENT_IMG_URL();
                setAdmUplodadImage(ivDelImage, imagePath);
                ivDelImage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 이미지보기..
                        if(mCurInfo.getRECENT_IMG_URL().length() >0) {
                            ((DriverMainActivity) DriverMainActivity.mContext).viewItemImage(
                                    mCurInfo.getDISPATCH_ID(), mCurInfo.getCLCODE()
                            );
                        }
                    }
                });

                // 1개이상이라면 뒤에 추가이미지 효과...
                int nCnt = Integer.parseInt(mCurInfo.getIMAGE_CNT());
                if (nCnt>1) {
                    viewMorImgEffect1.setVisibility(VISIBLE);
                    viewMorImgEffect2.setVisibility(VISIBLE);
                } else {
                    viewMorImgEffect1.setVisibility(GONE);
                    viewMorImgEffect2.setVisibility(GONE);
                }

                // 이미지건수 세팅
                tvImageCnt.setText(mCurInfo.getIMAGE_CNT());
                tvImageCnt.setVisibility(VISIBLE);

            } else {
                // 없다면..일반이미지 표시..
                cardViewDelImage.setVisibility(GONE);
                ivCamera.setVisibility(VISIBLE);
                viewCameraRound.setVisibility(VISIBLE);

                viewMorImgEffect1.setVisibility(GONE);
                viewMorImgEffect2.setVisibility(GONE);

                tvImageCnt.setVisibility(GONE);
            }


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
