package com.gzonesoft.sg623.Layout;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.data.FavItemInfo;
//import com.gzonesoft.cookzzang.ui.FavItemListActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;


public class ClientPopFavItemLayout extends LinearLayout {

    public static Context mContext = null;

    private FavItemInfo mCurInfo = null;

    private View viewBackground;
    private ImageView ivItemImage;
    private TextView tvInfoText1, tvInfoText2, tvInfoText3, tvInfoText4, tvInfoText5,tvFavMark;
    private View viewDivide;
    private FrameLayout flBtnArea1, flBtnArea2;
//    private Button btnAddFavorite, btnOrder;



    public ClientPopFavItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public ClientPopFavItemLayout(Context context, FavItemInfo info) {
        super(context);

        init(context);

        mContext = context;
        mCurInfo = info;

        setData();
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_client_pop_favitem, this, true);

        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        tvInfoText1 = (TextView) findViewById(R.id.tvWSD_DATE);
        tvInfoText2 = (TextView) findViewById(R.id.tvITNAME);
        tvInfoText3 = (TextView) findViewById(R.id.tvInfoText3);
        tvInfoText4 = (TextView) findViewById(R.id.tvITSTAN);
        tvInfoText5 = (TextView) findViewById(R.id.tvInfoText5);
//        tvFavMark = (TextView) findViewById(R.id.tvFavMark);

//        viewDivide = (View) findViewById(R.id.viewDivide);
//        flBtnArea1 = findViewById(R.id.flBtnArea1);
//        flBtnArea2 = findViewById(R.id.flBtnArea2);
//        btnAddFavorite = (Button) findViewById(R.id.btnAddFavorite);
//        btnAddFavorite.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickPopButtons(v);
//            }
//        });
//
//        btnOrder = (Button) findViewById(R.id.btnOrder);
//        btnOrder.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickPopButtons(v);
//            }
//        });


        slideRight(ivItemImage);

        slideRight(tvInfoText1);
        slideRight(tvInfoText2);
        slideLeft(tvInfoText3);
        slideLeft(tvInfoText4);

//        slideUp(flBtnArea1);
//        slideUp(flBtnArea2);
//        slideUp(viewDivide);
    }

    private void setData() {

        try {
            if (mCurInfo != null) {
                String htmlString = "";

                htmlString = mCurInfo.getItname();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText1, htmlString);

                if (!mCurInfo.getIteasdan().isEmpty()) {
                    htmlString =  "<B>" + mCurInfo.getIteasdan() + "</B><small><small>원</small></small>";
                } else {
                    htmlString =  "0<small><small>원</small></small>";
                }
                CommonUtil.with().setHtmlMarqueeText(tvInfoText2, htmlString);

                htmlString = mCurInfo.getItstan();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText3, htmlString);

                htmlString = mCurInfo.getItmaker();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText4, htmlString);

//                htmlString = mCurInfo.getCATEGORY_NAME();
//                CommonUtil.with().setHtmlMarqueeText(tvInfoText5, htmlString);

                // 이미지 세팅
                if ((mCurInfo.getImg_file_jm().equals("") || mCurInfo.getImg_file_jm().equals("readyimag.jpg") || mCurInfo.getImg_file_jm().equals("NULL"))) {
                    ivItemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ivItemImage.setImageResource(R.drawable.no_image_05);
                } else {
                    ivItemImage.setScaleType(ImageView.ScaleType.CENTER);
                    final String imagePath = String.format("%s/%s", mCurInfo.getImg_path_jm(), mCurInfo.getImg_file_jm());
                    setUplodadImage(ivItemImage, imagePath);

//                    ivItemImage.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ((SlidePopClientDetailActivity) SlidePopClientDetailActivity.mContext).showImageViewer(Svc.IMG_SERVER_PATH + imagePath);
//                        }
//                    });
                }

                // 관심상품여부 확인하기
//                tvFavMark.setText("-");
//                btnAddFavorite.setBackgroundResource(R.drawable.bg_listitem_btn_02);
//                btnAddFavorite.setTextColor(Color.parseColor("#7800AE"));
//                btnAddFavorite.setText("       관심제거");
//                tvFavMark.setTextColor(Color.parseColor("#7800AE"));

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
    private void setUplodadImage(ImageView ivTarget, String fileURL) {
        if (!fileURL.isEmpty()) {
            try {
                String imgUrl = Svc.IMG_SERVER_PATH + fileURL;
                Glide.with(this).load(imgUrl).into(ivTarget);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * 슬라이드 팝업 버튼 처리..
     * @param view
     */
    public void clickPopButtons(View view) {
        Loggers.d("[clickSignButtons]클릭한 버튼 태그 : " + view.getTag());

        Intent intent = null;

        int nTag = Integer.parseInt(view.getTag().toString());

        switch (nTag) {
            case 10: // 관심품목 제거
//                ((FavItemListActivity) FavItemListActivity.mContext).removeFavorite(mCurInfo.getItcode());
                break;
            case 20: // 주문하기
//                ((FavItemListActivity) FavItemListActivity.mContext).popItemInfo(mCurInfo.getItcode());
                break;
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
