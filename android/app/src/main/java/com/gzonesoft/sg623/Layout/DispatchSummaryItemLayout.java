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

import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.model.DeliveryListManager;
import com.gzonesoft.sg623.ui.DispatchSummaryActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.Random;


public class DispatchSummaryItemLayout extends LinearLayout {

    public static Context mContext = null;

    // 시작영역
    private ScalableLayout scHeaderArea, scStartArea;
    private View vLine0, vLine1, vLine2, vLine3;
    private TextView tvEpName, tvTopInfo1, tvTopInfo2, tvTopInfo3, tvTopInfo4;
    private ImageView ivRandom;

    // 거점영역
    private DeliveryListInfo mCurInfo = null;
    private ImageView ivDotLine;
    private TextView tvSeq, tvClname, tvOrderAmount, tvDistance, tvDuration, tvArrowRight, tvArrTime;
    private View viewPoint;
    private Button btnInvoice;

    // 새로고침 버튼 영역..
    private ScalableLayout scRefreshArea;
    private TextView tvUpdateTime;
    private ImageView ivRefresh;

    private Boolean bShowDispNo = false;


    public DispatchSummaryItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public DispatchSummaryItemLayout(Context context, DeliveryListInfo info) {
        super(context);
        // 초기화
        mContext = context;
        mCurInfo = info;
        init(context);

        // 데이타세팅
        setData();
        // 애니메이션
        slideRight(tvSeq);
        slideLeft(btnInvoice);
        slideRight(tvClname);
        slideLeft(tvArrTime);
        slideRight(tvOrderAmount);
//        slideUp(tvDuration);
        slideRight(tvDistance);

        slideRight(tvArrowRight);
        slideRight(ivDotLine);


        slideRight(vLine0);
        slideLeft(vLine1);
        slideRight(vLine2);
        slideLeft(vLine3);
        slideDown(tvTopInfo1);
        slideUp(tvTopInfo3);
        slideUp(tvTopInfo4);
        slideRight(tvTopInfo2);

        ivRandom.setVisibility(GONE);

        // 효과이후 한번더 세팅하여 마퀴즈효과를 살려놓는다..
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slideUp(ivRandom);
                setData();
            }
        }, 500);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_disp_summary, this, true);
        // 시작영역
        scHeaderArea = (ScalableLayout) findViewById(R.id.scHeaderArea);
        scStartArea = (ScalableLayout) findViewById(R.id.scStartArea);
        vLine0 = (View) findViewById(R.id.vLine0);
        vLine1 = (View) findViewById(R.id.vLine1);
        vLine2 = (View) findViewById(R.id.vLine2);
        vLine3 = (View) findViewById(R.id.vLine3);
        tvEpName = (TextView) findViewById(R.id.tvEpName);
        tvTopInfo1 = (TextView) findViewById(R.id.tvTopInfo1);
        tvTopInfo1 = (TextView) findViewById(R.id.tvTopInfo1);
        tvTopInfo2 = (TextView) findViewById(R.id.tvTopInfo2);
        tvTopInfo3 = (TextView) findViewById(R.id.tvTopInfo3);
        tvTopInfo4 = (TextView) findViewById(R.id.tvTopInfo4);
        ivRandom = (ImageView) findViewById(R.id.ivRandom);

        int nSeq = Integer.parseInt(mCurInfo.getROUTE_SEQ());
        // 제일상단영역 세팅..
        if (nSeq == 1) {
            scHeaderArea.setVisibility(VISIBLE);
            scStartArea.setVisibility(VISIBLE);
            Random random = new Random();
            int nRandIdx = random.nextInt(13);
            if (nRandIdx == 0) nRandIdx = 1;
            String resId = String.format("random_%02d", nRandIdx);
            Loggers.d("**************************************************************** resId = " + resId);
            ivRandom.setImageResource(
                    mContext.getResources().getIdentifier(resId, "drawable", "com.gzonesoft.sg415")
            );
        }

        // 거점영역
        viewPoint = (View) findViewById(R.id.viewPoint);
        tvSeq = (TextView) findViewById(R.id.tvSeq);
        tvClname = (TextView) findViewById(R.id.tvClname);
        tvOrderAmount = (TextView) findViewById(R.id.tvOrderAmount);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvArrTime = (TextView) findViewById(R.id.tvArrTime);

        btnInvoice = (Button) findViewById(R.id.btnInvoice);
        btnInvoice.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 거래명세서 요청하기..
                ((DispatchSummaryActivity) DispatchSummaryActivity.mContext).requestDeliveryItemList(mCurInfo);
            }
        });

        ivDotLine = (ImageView) findViewById(R.id.ivDotLine);
        tvArrowRight = (TextView) findViewById(R.id.tvArrowRight);


        // 새로고침 영역
        tvUpdateTime = (TextView) findViewById(R.id.tvUpdateTime);
        scRefreshArea = (ScalableLayout) findViewById(R.id.scRefreshArea);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        ivRefresh.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로고침 요청하기..
                ((DispatchSummaryActivity) DispatchSummaryActivity.mContext).requestCvoDispatchSummaryList();
            }
        });

        tvEpName.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bShowDispNo) {
                    bShowDispNo = false;
                } else {
                    bShowDispNo = true;
                }
                setData();
            }
        });

//        slideRight(ivItemImage);


//        slideRight(tvInfoText2);
//        slideLeft(tvInfoText3);
//        slideLeft(tvInfoText4);


    }

    private void setData() {

        try {
            if (mCurInfo != null) {
                String htmlString = "";

                htmlString = mCurInfo.getROUTE_SEQ();
                CommonUtil.with().setHtmlMarqueeText(tvSeq, htmlString);

                htmlString = mCurInfo.getCLNAME();
                CommonUtil.with().setHtmlMarqueeText(tvClname, htmlString);

                htmlString = mCurInfo.getORDER_AMT() + " <small>원</small>";
                CommonUtil.with().setHtmlMarqueeText(tvOrderAmount, htmlString);

                String sDuration = "", sDistance = "";
                if (mCurInfo.getDURATION().length()>0) {
                    sDuration = CommonUtil.with().secondToTimestring(Integer.valueOf(mCurInfo.getDURATION()));
                    // 소요시간은 아직 미정이다.. 2023.08.24 18.42 ...
                }
                if (mCurInfo.getDISTANCE().length()>0) {
                    sDistance = CommonUtil.with().distanceStringWithUnit(Double.valueOf(mCurInfo.getDISTANCE()) / 1000);
                    htmlString = "<b>" + sDistance + "</b><small><small> km</small></small>";
                }
                CommonUtil.with().setHtmlMarqueeText(tvDistance, htmlString);

                if (mCurInfo.getINBOUND_DTM().length()>0) { // 도착시간이 있다면...
                    // 순번배경색 세팅
                    viewPoint.setBackgroundResource(R.drawable.bg_blue_circle_10_fill);
                    // 거리정보 배경색 세팅
                    tvDistance.setBackgroundColor(Color.parseColor("#7E57C2"));
                    // 화살표 색상 세팅
                    ivDotLine.setImageResource(R.drawable.dash_line_01_blue);
                    tvArrowRight.setTextColor(Color.parseColor("#7E57C2"));
                    // 주문금액 글자색 세팅
                    tvOrderAmount.setTextColor(Color.parseColor("#7E57C2"));
                    // 도착시간정보 세팅
                    htmlString = mCurInfo.getINBOUND_DTM().substring(11, 16) + " 도착";
                    tvArrTime.setTextColor(Color.parseColor("#FFFFFF"));
                    tvArrTime.setBackgroundColor(Color.parseColor("#7E57C2"));
                } else { // 도착시간이 없다...
                    // 순번배경색 세팅
                    viewPoint.setBackgroundResource(R.drawable.bg_gray_circle_10_fill);
                    // 거리정보 배경색 세팅
                    tvDistance.setBackgroundColor(Color.parseColor("#AAAAAA"));
                    // 화살표 색상 세팅
                    ivDotLine.setImageResource(R.drawable.dash_line_01_gray);
                    tvArrowRight.setTextColor(Color.parseColor("#AAAAAA"));
                    // 거래처명 글자색 세팅
                    tvClname.setTextColor(Color.parseColor("#AAAAAA"));
                    // 주문금액 글자색 세팅
                    tvOrderAmount.setTextColor(Color.parseColor("#AAAAAA"));

                    // 도착시간정보 세팅
                    htmlString = " - 대기 - ";
                    tvArrTime.setTextColor(Color.parseColor("#AAAAAA"));
                    tvArrTime.setBackgroundColor(Color.parseColor("#F1F1F1"));
                }
                CommonUtil.with().setHtmlMarqueeText(tvArrTime, htmlString);

                Loggers.d("mCurInfo.getROUTE_SEQ()=" + mCurInfo.getROUTE_SEQ());

                int nCnt = DeliveryListManager.getInstance().values().size();
                int nSeq = Integer.parseInt(mCurInfo.getROUTE_SEQ());
                // 제일상단영역 세팅..
                if (nSeq == 1) {
                    scHeaderArea.setVisibility(VISIBLE);
                    scStartArea.setVisibility(VISIBLE);

                    if (bShowDispNo) {
                        htmlString = mCurInfo.getDISPATCH_ID();
                    } else {
                        htmlString = "- " + mCurInfo.getEPNAME() + "" +
                                " / " + mCurInfo.getEPPHONE() +
                                " -";
                    }
                    CommonUtil.with().setHtmlMarqueeText(tvEpName, htmlString);

                    htmlString = "" + mCurInfo.getROUTE_CNT() +
                            "<small>건</small> / " +
                            "" + CommonUtil.with().toNumFormat(
                                    Integer.parseInt(mCurInfo.getTOT_ORDER_AMT())
                            ) + "<small>원</small>";
                    CommonUtil.with().setHtmlMarqueeText(tvTopInfo1, htmlString);
                    htmlString = "" + mCurInfo.getSTART_TIME() +
                            " <small>출발</small>";
                    CommonUtil.with().setHtmlMarqueeText(tvTopInfo2, htmlString);
                    htmlString = "<small><small>최종보고시간</small></small><BR>" +
                            "" + mCurInfo.getLAST_REP_TIME();
                    CommonUtil.with().setHtmlMarqueeText(tvTopInfo3, htmlString);
                    htmlString = "<small><small>이동거리:</small></small>" +
                            "" + mCurInfo.getTOT_DISTANCE() + "<small><small>km</small></small>" +
                            " <small><small>최고:</small></small>" +
                            "" + mCurInfo.getMAX_SPEED() + "<small><small>km/h</small></small>";
                    CommonUtil.with().setHtmlMarqueeText(tvTopInfo4, htmlString);
                } else {
                    scHeaderArea.setVisibility(GONE);
                    scStartArea.setVisibility(GONE);
                }
                // 제일하단영역 세팅..
                if (nCnt == nSeq) {
                    tvUpdateTime.setText(
                            String.format("- Last Update. %s -", CommonUtil.with().nowMM_DD_HH_MM_SS())
                    );
                    scRefreshArea.setVisibility(VISIBLE);
                    scRefreshArea.setVisibility(VISIBLE);
                } else {
                    scRefreshArea.setVisibility(GONE);
                }

//                htmlString =  mCurInfo.getWSD_TOT() + "<small><small>원</small></small>";
//                CommonUtil.with().setHtmlMarqueeText(tvInfoText2, htmlString);
//
//                htmlString = mCurInfo.getITSTAN();
//                CommonUtil.with().setHtmlMarqueeText(tvInfoText3, htmlString);
//
//                htmlString = mCurInfo.getWSD_DAN() + "*" + mCurInfo.getWSD_QTY() + "개";
//                CommonUtil.with().setHtmlMarqueeText(tvInfoText4, htmlString);
//
//                if (mCurInfo.getIMAGE_URL().length()>0) { // 이미지정보가 있다면...
//                    // 이미지클릭시...
//                    btnItem.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ((SlidePopClientDetailActivity) SlidePopClientDetailActivity.mContext).viewItemImage(mCurInfo.getIMAGE_URL());
//                        }
//                    });
//                    settingImage(ivItemImage, mCurInfo.getIMAGE_URL());
//                } else {
//                    ivItemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    ivItemImage.setImageResource(R.drawable.no_image_05);
//                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_middle);
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
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_middle);
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
