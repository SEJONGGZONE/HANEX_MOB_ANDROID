package com.gzonesoft.sg623.Layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.data.ItemInfo;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.ui.ItemMngListActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;


public class ItemLayout extends LinearLayout {

    public static Context mContext = null;

    private ItemInfo mCurInfo = null;

    private View viewBackground;
    private ImageView ivItemImage, ivStatusMark;
    private TextView tvOrderMin, tvItName, tvAmount, tvItstan, tvMaker, tvCategoryName, tvAddFavIcon;
    private View viewDivide;
    private FrameLayout flBtnArea1, flBtnArea2;
    private Button btnItem, btnMainImage, btnMoreImage, btnAdmin;

    public int nAdmCount = 0;

    public ItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public ItemLayout(Context context, ItemInfo info) {
        super(context);

        init(context);

        mContext = context;
        mCurInfo = info;

        setData();

        slideRight(ivItemImage);

        slideRight(tvItName);
        if (tvOrderMin.getVisibility() == VISIBLE) slideRight(tvOrderMin);
        slideRight(tvAmount);
        slideLeft(tvItstan);
        slideLeft(tvMaker);

        slideUp(flBtnArea1);
        slideUp(flBtnArea2);
        slideUp(viewDivide);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_item, this, true);

        ivItemImage = findViewById(R.id.ivItemImage);
        ivStatusMark = findViewById(R.id.ivStatusMark);
        tvOrderMin = findViewById(R.id.tvOrderMin);
        tvItName = findViewById(R.id.tvItName);
        tvAmount = findViewById(R.id.tvAmount);
        tvItstan = findViewById(R.id.tvItstan);
        tvMaker = findViewById(R.id.tvMaker);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        tvAddFavIcon = findViewById(R.id.tvAddFavIcon);

        viewDivide = findViewById(R.id.viewDivide);
        flBtnArea1 = findViewById(R.id.flBtnArea1);
        flBtnArea2 = findViewById(R.id.flBtnArea2);
        btnItem = findViewById(R.id.btnItem);
        btnItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지보기..
                if(mCurInfo.getJSON_IMAGE_URL().length() >0) {
                    ((ItemMngListActivity) ItemMngListActivity.mContext).viewItemImage(
                            mCurInfo.getIMG_ADM_UPLOAD(), mCurInfo.getITCODE()
                            );
                }
            }
        });

        btnMainImage = findViewById(R.id.btnMainImage);
        btnMainImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPopButtons(v);
            }
        });

        btnMoreImage = findViewById(R.id.btnOrder);
        btnMoreImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPopButtons(v);
            }
        });
//        // 결제필수여부에 따른 버튼명변경(2023.05.02)
//        if (AppSetting.mobileUserInfo.getPAY_YN().equals("Y")) {
//            // 필수 라면..
//            btnOrder.setText("장바구니  ");
//        } else {
//            // 필수가 아니라면..
//            btnOrder.setText("주문하기  ");
//        }

        // 관리자기능
        btnAdmin = findViewById(R.id.btnAdmin);
        btnAdmin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (nAdmCount <= 10) {
//                    nAdmCount++;
//                    if (nAdmCount == 5) {
//                        if (((ItemListActivity) ItemListActivity.mContext) != null) {
//                            ((ItemListActivity) ItemListActivity.mContext).handler.obtainMessage(RequesterSession.REQ_OK, "5번남았습니다.").sendToTarget();
//                        }
//                    } else if (nAdmCount == 7) {
//                        if (((ItemListActivity) ItemListActivity.mContext) != null) {
//                            ((ItemListActivity) ItemListActivity.mContext).handler.obtainMessage(RequesterSession.REQ_OK, "3번남았습니다.").sendToTarget();
//                        }
//                    } else if (nAdmCount == 10) {
//                        if (((ItemListActivity) ItemListActivity.mContext) != null) {
//                            if (((ItemListActivity) ItemListActivity.mContext).bAdminModeYN == false) {
//                                ((ItemListActivity) ItemListActivity.mContext).handler.obtainMessage(RequesterSession.REQ_OK, "관리자모드로 전환되었습니다.").sendToTarget();
//                                ((ItemListActivity) ItemListActivity.mContext).bAdminModeYN = true;
//                            } else {
//                                ((ItemListActivity) ItemListActivity.mContext).handler.obtainMessage(RequesterSession.REQ_OK, "일반모드로 전환되었습니다.").sendToTarget();
//                                ((ItemListActivity) ItemListActivity.mContext).bAdminModeYN = false;
//                            }
//                            nAdmCount = 0;
//                            // 리스트 새로고침 요청..
//                            ((ItemListActivity) ItemListActivity.mContext).pageNum = 1;
//                            ((ItemListActivity) ItemListActivity.mContext).requestItemList(((ItemListActivity) ItemListActivity.mContext).mSubCode);
//                        }
//                    }
//                }
            }
        });
        //if (AppSetting.mobileUserInfo.getCLCODE().equals("0000000204") || AppSetting.mobileUserInfo.getCLCODE().equals("0000000427")) {
//        if (AppSetting.mobileUserInfo.getCLCODE().equals("0000000204")) {
//            btnAdmin.setVisibility(VISIBLE);
//        } else {
            btnAdmin.setVisibility(GONE);
//        }


    }

    /**
     * 단가세팅(관리자모드 여부)
     * @param bAdminModeYn
     */
    public void setPrice(boolean bAdminModeYn) {
        String htmlString = "";
        if (bAdminModeYn) {
            // 관리자 모드인경우... 앞에 매입단가 표시..
            if (!mCurInfo.getITEASDAN().isEmpty()) {
                htmlString =  "<small><small><small>(" + mCurInfo.getITEA_IPDAN() +  ")</small></small></small>" +
                        " <B>" + mCurInfo.getITEASDAN() + "</B><small><small>원</small></small>";
            } else {
                htmlString =  "</small></small></small>(" + mCurInfo.getITEA_IPDAN() +  ")</small></small></small>" +
                        " 0<small><small>원</small></small>";
            }
        } else {
//            if (AppSetting.clientInfo.getUSER_MODE().equals(JoinStatus._데모)) {
//                htmlString = "**,**0<small><small>원</small></small>";
//            } else {
                // 일반모드
                if (!mCurInfo.getITEASDAN().isEmpty()) {
                    htmlString = "<B>" + mCurInfo.getITEASDAN() + "</B><small><small>원</small></small>";
                } else {
                    htmlString = "0<small><small>원</small></small>";
                }
//            }
        }
        // 단가세팅...
        CommonUtil.with().setHtmlMarqueeText(tvAmount, htmlString);
    }
    /**
     * 데이타 세팅
     */
    private void setData() {

        try {
            if (mCurInfo != null) {
                String htmlString = "";

                try {
                    int nOrderDay = Integer.parseInt(mCurInfo.getITORDERMIN());
                    if (nOrderDay>1) {
                        //nOrderDay++;
                        tvOrderMin.setText(String.format("D-%d", nOrderDay));
                        tvOrderMin.setVisibility(VISIBLE);
                    } else {
                        tvOrderMin.setVisibility(GONE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    tvOrderMin.setVisibility(GONE);
                }

                htmlString = mCurInfo.getITNAME();
                CommonUtil.with().setHtmlMarqueeText(tvItName, htmlString);

                // 관리자의경우에만 표시..
                //if (AppSetting.mobileUserInfo.getCLCODE().equals("0000000204") || AppSetting.mobileUserInfo.getCLCODE().equals("0000000427")) {
//                if (AppSetting.mobileUserInfo.getCLCODE().equals("0000000204")) {
//
//                    if (((ItemListActivity) ItemListActivity.mContext) != null) {
//                        if (((ItemListActivity) ItemListActivity.mContext).bAdminModeYN) {
//                            setPrice(true);
//                        } else {
//                            setPrice(false);
//                        }
//                    } else {
//                        setPrice(false);
//                    }
//                } else {
                    setPrice(false);
//                }



                htmlString = mCurInfo.getITSTAN();
                CommonUtil.with().setHtmlMarqueeText(tvItstan, htmlString);

                htmlString = mCurInfo.getITMAKER();
                CommonUtil.with().setHtmlMarqueeText(tvMaker, htmlString);

//                htmlString = mCurInfo.getCATEGORY_NAME();
//                CommonUtil.with().setHtmlMarqueeText(tvInfoText5, htmlString);

                // 이미지 세팅
                if(mCurInfo.getIMG_ADM_UPLOAD().length() >0) {
                    // 관리자 업로드 이미지가있다면...
                    ivItemImage.setScaleType(ImageView.ScaleType.CENTER);
                    final String imagePath = mCurInfo.getIMG_ADM_UPLOAD();
                    setAdmUplodadImage(ivItemImage, imagePath);
                    ivItemImage.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ((ItemListActivity) ItemListActivity.mContext).showImageViewer(imagePath);
                        }
                    });
                } else {
                    // 없다면..일반이미지 표시..
                    ivItemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ivItemImage.setImageResource(R.drawable.no_image_05);
                    ivItemImage.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                ((ItemListActivity) ItemListActivity.mContext).showImageViewer(Svc.IMG_SERVER_PATH + imagePath);
                        }
                    });
                }

                btnMainImage.setBackgroundResource(R.drawable.bg_listitem_btn_01);
                btnMainImage.setTextColor(Color.parseColor("#FFFFFF"));

//                // 관심상품여부 확인하기
//                if (mCurInfo.getCHK_FAVORITE().equals("Y")) {
//                    tvAddFavIcon.setText("-");
//                    btnMainImage.setBackgroundResource(R.drawable.bg_listitem_btn_02);
//                    btnMainImage.setTextColor(Color.parseColor("#7800AE"));
////                    btnAddFavorite.setText("       관심제거");
//                    tvAddFavIcon.setTextColor(Color.parseColor("#7800AE"));
//                } else {
//                    tvAddFavIcon.setText("+");
//                    btnMainImage.setBackgroundResource(R.drawable.bg_listitem_btn_01);
//                    btnMainImage.setTextColor(Color.parseColor("#FFFFFF"));
////                    btnAddFavorite.setText("       관심추가");
//                    tvAddFavIcon.setTextColor(Color.parseColor("#FFFFFF"));
//                }

                // 주문상태여부 확인하기
                int boxQty = Integer.parseInt(mCurInfo.getORD_BOXQTY());
                int unitQty = Integer.parseInt(mCurInfo.getORD_UNITQTY());
                if ((boxQty>0) || (unitQty>0)) {
                    // 주문이 있다면..리본을 달아주자..
                    ivStatusMark.setVisibility(VISIBLE);
                    ivStatusMark.setImageResource(R.drawable.status_mark_01);
                } else {
                    ivStatusMark.setVisibility(GONE);
                }
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
            case 10: // 메인이미지

                // 카메라 호출
                ((ItemMngListActivity) ItemMngListActivity.mContext).takePicture(mCurInfo.getITCODE(), CommType._대표이미지);
                break;
            case 20: // 추가이미지
                ((ItemMngListActivity) ItemMngListActivity.mContext).takePicture(mCurInfo.getITCODE(), CommType._추가이미지);
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
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_middle);
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
