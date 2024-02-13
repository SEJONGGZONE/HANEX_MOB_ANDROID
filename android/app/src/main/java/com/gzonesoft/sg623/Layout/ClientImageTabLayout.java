package com.gzonesoft.sg623.Layout;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.data.DeliveryImageInfo;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;


public class ClientImageTabLayout extends LinearLayout {

    public static Context mContext = null;

    public DeliveryImageInfo mCurInfo = null;

    public boolean mDateShowYn = false;

    public LinearLayout llDDate, llImage;
    public TextView tvDDate, tvImageCnt;

    private ImageView ivDelImage;

    private View viewCameraRound;

    // 애니메이션 효과
    Animation animationStart = null;
    Animation animationStop = null;

    /**
     * 생성자1
     * @param context
     * @param attrs
     */
    public ClientImageTabLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        init(context);
    }

    /**
     * 생성자2
     * @param context
     * @param info
     */
    public ClientImageTabLayout(Context context, DeliveryImageInfo info, boolean dateShowYn) {
        super(context);

        mContext = context;
        mCurInfo = info;
        mDateShowYn = dateShowYn;

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
        inflater.inflate(R.layout.item_clientimage_tab,this,true);

        llDDate = findViewById(R.id.llDDate);
        llImage = findViewById(R.id.llImage);
        tvDDate = findViewById(R.id.tvDDate);
        tvImageCnt = findViewById(R.id.tvImageCnt);
        ivDelImage = findViewById(R.id.ivDelImage);

        if (mDateShowYn) {
            llDDate.setVisibility(VISIBLE);
            llImage.setVisibility(GONE);
        } else {
            llDDate.setVisibility(GONE);
            llImage.setVisibility(VISIBLE);
        }


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
            String ddateString = mCurInfo.getDDATE();
            htmlString = ddateString.substring(4,6) + "/" + ddateString.substring(6,8);
            CommonUtil.with().setHtmlMarqueeText(tvDDate, htmlString);

            htmlString = mCurInfo.getD_IMG_CNT() + "<small>장</small>";
            CommonUtil.with().setHtmlMarqueeText(tvImageCnt, htmlString);

            ivDelImage.setScaleType(ImageView.ScaleType.CENTER);
            final String imagePath = mCurInfo.getURL();
            setAdmUplodadImage(ivDelImage, imagePath);
            ivDelImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 이미지보기..
                    if(mCurInfo.getURL().length() >0) {
                        ((CvoMainActivity) CvoMainActivity.mContext).viewItemImage(
                                mCurInfo.getDDATE(), mCurInfo.getCLCODE(), mCurInfo.getCLNAME()
                        );
                    }
                }
            });


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
