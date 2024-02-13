package com.gzonesoft.sg623.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzonesoft.sg623.R;

import java.util.ArrayList;

public class UDialog {
    private UDialog() {
    }

    public static WDialogBtnOne withOne(Context pCon, String message, String btnTextOk) {
        return new WDialogBtnOne(pCon, message, btnTextOk);
    }

    public static WDialogBtnOne withOne_yes(Context pCon, String message) {
        return new WDialogBtnOne(pCon, message, "예");
    }

    public static WDialogBtnOne withOne_ok(Context pCon, String message) {
        return new WDialogBtnOne(pCon, message, "확인");
    }


    public static class WDialogBtnOne {
        private Callback mCallback;
        private Context mCon;
        private String mTitle;
        private String mMessage;
        private String mBtnTextOk;

        private WDialogBtnOne(Context pCon, String message, String btnTextOk) {
            mCon = pCon;
            mTitle = "알림";
            mMessage = message;
            mBtnTextOk = btnTextOk;
        }

        public WDialogBtnOne forShow(Callback WDialogBtnOne_Callback) {
            mCallback = WDialogBtnOne_Callback;
            try {
                new AlertDialog.Builder(mCon).setTitle(mTitle).setMessage(mMessage)
                        .setPositiveButton(mBtnTextOk, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCallback != null) {
                                    mCallback.onClick();
                                }
                            }
                        }).show().setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public static interface Callback {
            void onClick();
        }
    }

    public static WDialogBtnTwo withTwo(Context pCon, String message, String btnTextOne, String btnTextTwo) {
        return new WDialogBtnTwo(pCon, message, btnTextOne, btnTextTwo);
    }

    public static WDialogBtnTwo withTwo_yesNo(Context pCon, String message) {
        return new WDialogBtnTwo(pCon, message, "예", "아니오");
    }

    public static WDialogBtnTwo withTwo_okCancel(Context pCon, String message) {
        return new WDialogBtnTwo(pCon, message, "확인", "취소");
    }

    public static WDialogBtnTwoCustom withTwoCustom_okCancel(Context pCon, String title, String message, String btn1, String btn2, int btn2vis) {
        return new WDialogBtnTwoCustom(pCon, title, message, btn1, btn2, btn2vis);
    }

    // HTML텍스트를 받는...
    public static WDialogBtnTwoCustom withHtmlTwoCustom_okCancel(Context pCon, String title, String message, String btn1, String btn2, int btn2vis) {
        return new WDialogBtnTwoCustom(pCon, title, true, message, btn1, btn2, btn2vis);
    }

    // HTML텍스트를 받는...(작은높이유형)
    public static WDialogBtnTwoCustom withHtmlTwoCustom_okCancel(Context pCon, String title, String message, String btn1, String btn2, int btn2vis, boolean smallHeightYn) {
        return new WDialogBtnTwoCustom(pCon, title, true, message, btn1, btn2, btn2vis, smallHeightYn);
    }

    // HTML텍스트를 받고, 정렬기준도 받는...
    public static WDialogBtnTwoCustom withHtmlTwoCustom_okCancel(Context pCon, String title, String message, String btn1, String btn2, int btn2vis, int alignType) {
        return new WDialogBtnTwoCustom(pCon, title, true, alignType, message, btn1, btn2, btn2vis);
    }

    // 다이얼로그의 모달 혹은 모달리스의 형태로 띄울지를 위한 옵션추가..맨뒤에 boolean 형태로 받는다.
    public static WDialogBtnTwoCustom withTwoCustom_okCancel(Context pCon, String title, String message, String btn1, String btn2, int btn2vis, boolean modalYN) {
        return new WDialogBtnTwoCustom(pCon, title, message, btn1, btn2, btn2vis, modalYN);
    }
    // 다이얼로그의 모달 혹은 모달리스의 형태로 띄울지를 위한 옵션추가..맨뒤에 boolean 형태로 받는다. + 자동팝업종료기능 추가..
    public static WDialogBtnTwoCustom withTwoCustom_okCancel(Context pCon, String title, String message, String btn1, String btn2, int btn2vis, boolean modalYN, int autoCloseSecond) {
        return new WDialogBtnTwoCustom(pCon, title, message, btn1, btn2, btn2vis, modalYN, autoCloseSecond);
    }

    public static class WDialogBtnTwoCustom extends Dialog {

        private CallbackAll mCallbackAll;
        private Context pCon;
        private String mTitle;
        private String mMessage;
        private String mBtnTextOne;
        private String mBtnTextTwo;
        private int btn2vis;
        private boolean htmlYn = false;
        private int alignType = -1; // 기본값 왼쪽 정렬

        private TextView dialog_title_tv;
        private TextView dialog_message_tv;
        private TextView dialog_cancel_tv;
        private TextView dialog_ok_tv;

        private boolean bBackButtonEnable = true;

        // 애니메이션 효과
        private Animation animationStart = null;
        private Animation animationStartSpeed = null;
        private Animation animationStop = null;

        public WDialogBtnTwoCustom forShow_all(CallbackAll WDialogBtnTwoCustom_CallbackAll) {
            mCallbackAll = WDialogBtnTwoCustom_CallbackAll;
            return this;
        }

        public interface CallbackAll {
            void onClick_yes();

            void onClick_no();
        }

        public WDialogBtnTwoCustom(Context pCon, String title, String message, String btn1, String btn2, int btn2vis) {
            super(pCon);
            this.pCon = pCon;
            this.mMessage = message;
            this.mBtnTextOne = btn1;
            this.mBtnTextTwo = btn2;
            this.mTitle = title;
            this.btn2vis = btn2vis;
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two);
            init();
            show();
        }

        public WDialogBtnTwoCustom(Context pCon, String title, boolean htmlYn, String message, String btn1, String btn2, int btn2vis) {
            super(pCon);
            this.pCon = pCon;
            this.mMessage = message;
            this.mBtnTextOne = btn1;
            this.mBtnTextTwo = btn2;
            this.mTitle = title;
            this.btn2vis = btn2vis;
            this.htmlYn = htmlYn;   // <---- Html 표시여부 세팅
            this.alignType = 0; // 가운데
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two);
            init();
            show();
        }

        public WDialogBtnTwoCustom(Context pCon, String title, boolean htmlYn, String message, String btn1, String btn2, int btn2vis, boolean smallHeightYn) {
            super(pCon);
            this.pCon = pCon;
            this.mMessage = message;
            this.mBtnTextOne = btn1;
            this.mBtnTextTwo = btn2;
            this.mTitle = title;
            this.btn2vis = btn2vis;
            this.htmlYn = htmlYn;   // <---- Html 표시여부 세팅
            this.alignType = 0; // 가운데
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (smallHeightYn)
                setContentView(R.layout.dialog_two_small_height);
            else
                setContentView(R.layout.dialog_two);
            init();
            show();
        }

        public WDialogBtnTwoCustom(Context pCon, String title, boolean htmlYn, int alignType, String message, String btn1, String btn2, int btn2vis) {
            super(pCon);
            this.pCon = pCon;
            this.mMessage = message;
            this.mBtnTextOne = btn1;
            this.mBtnTextTwo = btn2;
            this.mTitle = title;
            this.btn2vis = btn2vis;
            this.htmlYn = htmlYn;   // <---- Html 표시여부 세팅
            this.alignType = alignType; // 정렬기준..
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two);
            init();
            show();
        }

        // 모달 여부 포함 초기화
        public WDialogBtnTwoCustom(Context pCon, String title, String message, String btn1, String btn2, int btn2vis, boolean modalYN) {
            super(pCon);
            this.pCon = pCon;
            this.mMessage = message;
            this.mBtnTextOne = btn1;
            this.mBtnTextTwo = btn2;
            this.mTitle = title;
            this.btn2vis = btn2vis;


            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two);
            init();
            show();
            // 다이얼로그의 모달여부를 세팅한다
            this.bBackButtonEnable = modalYN;
            setCanceledOnTouchOutside(modalYN);

        }

        // 자동 버튼 눌림기능 설정 포함 초기화
        public WDialogBtnTwoCustom(Context pCon, String title, String message, String btn1, String btn2, int btn2vis, boolean modalYN, int autoCloseSecond) {
            super(pCon);
            this.pCon = pCon;
            this.mMessage = message;
            this.mBtnTextOne = btn1;
            this.mBtnTextTwo = btn2;
            this.mTitle = title;
            this.btn2vis = btn2vis;


            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_two);
            init();
            show();
            // 다이얼로그의 모달여부를 세팅한다
            this.bBackButtonEnable = modalYN;
            setCanceledOnTouchOutside(modalYN);

            // 자동버튼 눌림기능 세팅
            if (autoCloseSecond>0) {
                this.orgMsg = btn1; // 버튼 메시지 저장
                this.bAutoOK = true; // 자동닫힘(OK)처리 활성화..
                // 애니메이션리소스 매핑
                animationStart = AnimationUtils.loadAnimation(this.pCon, R.anim.animation_start);
                animationStartSpeed = AnimationUtils.loadAnimation(this.pCon, R.anim.animation_start_speed);
                animationStop = AnimationUtils.loadAnimation(this.pCon, R.anim.animation_stop);

                //dialog_ok_tv.startAnimation(animationStart);

                showDelayTime(autoCloseSecond + 1);
            }

        }

        // 자동버튼 눌림처리 실행 메소드 및 환경 세팅...
        public int setSecond = 0;
        public String orgMsg = "";
        public boolean bAutoOK = true;
        public void showDelayTime(int sec) {
            setSecond = sec;
            // 1초후 실행...
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int remainSecond = setSecond-1;
                    dialog_ok_tv.setText(orgMsg + "(" + remainSecond + "초)");
                    if (remainSecond>0) {
                        showDelayTime(remainSecond);
                    } else {
                        if (bAutoOK) {
                            // 자동처리
                            mCallbackAll.onClick_yes();
                            dismiss();
                        }
                    }
                }
            }, 1100);
        }

        private void init() {
            dialog_title_tv = (TextView) findViewById(R.id.dialog_title_tv);
            dialog_message_tv = (TextView) findViewById(R.id.dialog_message_tv);
            dialog_cancel_tv = (TextView) findViewById(R.id.dialog_cancel_tv);
            dialog_ok_tv = (TextView) findViewById(R.id.dialog_ok_tv);

            if (htmlYn) {
                CommonUtil.with().setHtmlMarqueeText(dialog_title_tv, mTitle);
            } else {
                dialog_title_tv.setText(mTitle);
            }

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                dialog_message_tv.setText( htmlYn ? Html.fromHtml(mMessage) : mMessage);
            } else {
                dialog_message_tv.setText( htmlYn ? Html.fromHtml(mMessage, Html.FROM_HTML_MODE_LEGACY) : mMessage);
            }


            //dialog_message_tv.setTextAlignment( htmlYn ? View.TEXT_ALIGNMENT_CENTER: View.TEXT_ALIGNMENT_VIEW_START);
            if (this.alignType == -1) {
                dialog_message_tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            } else if (this.alignType == 0) {
                dialog_message_tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else if (this.alignType == 1) {
                dialog_message_tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            dialog_ok_tv.setText(htmlYn ? Html.fromHtml(mBtnTextOne) : mBtnTextOne);
            dialog_cancel_tv.setText(htmlYn ? Html.fromHtml(mBtnTextTwo) : mBtnTextTwo);

            dialog_cancel_tv.setVisibility(btn2vis);

            dialog_cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bAutoOK = false; // 자동닫힘(OK)처리 취소
                    mCallbackAll.onClick_no();
                    dismiss();
                }
            });
            dialog_ok_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bAutoOK = false; // 자동닫힘(OK)처리 취소
                    mCallbackAll.onClick_yes();
                    dismiss();
                }
            });
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                Toast.makeText(this.pCon, "백버튼눌림 - 팝업다이얼로그에서 잡힘..." + this.bBackButtonEnable, Toast.LENGTH_SHORT).show();
                if (!this.bBackButtonEnable) return false;
            }
            return super.onKeyDown(keyCode, event);
        }
    }


    public static class WDialogBtnTwo {
        private CallbackYes mCallbackYes;
        private CallbackAll mCallbackAll;
        private Context mCon;
        private String mTitle;
        private String mMessage;
        private String mBtnTextOne;
        private String mBtnTextTwo;

        public WDialogBtnTwo(Context context, String message, String btnTextOne, String btnTextTwo) {
            mCon = context;
            mTitle = "알림";
            mMessage = message;
            mBtnTextOne = btnTextOne;
            mBtnTextTwo = btnTextTwo;
        }


        public WDialogBtnTwo forShow_yes(CallbackYes WDialogBtnTwo_CallbackYes) {
            mCallbackYes = WDialogBtnTwo_CallbackYes;
            show();
            return this;
        }

        public WDialogBtnTwo forShow_all(CallbackAll WDialogBtnTwo_CallbackAll) {
            mCallbackAll = WDialogBtnTwo_CallbackAll;
            show();
            return this;
        }

        private void show() {
            try {
                new AlertDialog.Builder(mCon).setTitle(mTitle).setMessage(mMessage)
                        .setPositiveButton(mBtnTextOne, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCallbackYes != null) {
                                    mCallbackYes.onClick();
                                }
                                if (mCallbackAll != null) {
                                    mCallbackAll.onClick_yes();
                                }
                            }
                        }).setNegativeButton(mBtnTextTwo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallbackAll != null) {
                            mCallbackAll.onClick_no();
                        }
                    }
                }).show().getWindow();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public interface CallbackYes {
            void onClick();
        }

        public interface CallbackAll {
            void onClick_yes();

            void onClick_no();
        }
    }


    public static WDialogList withList_cancel(Context pCon, String[] textContArray) {
        return new WDialogList(pCon, textContArray, "취소");
    }

    public static class WDialogList {
        private Callback mCallback;
        private Context mCon;
        private String mTitle;
        private String[] mTextContArray;
        private String mBtnText;

        private WDialogList(Context pCon, String[] textContArray, String btnText) {
            mCon = pCon;
            mTitle = "알림";
            mTextContArray = textContArray;
            mBtnText = btnText;
        }

        public WDialogList forShow(Callback WDialogList_Callback) {
            mCallback = WDialogList_Callback;
            try {
                new AlertDialog.Builder(mCon).setTitle(mTitle).setItems(mTextContArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            mCallback.onClick(which);
                        }
                    }
                }).setNegativeButton(mBtnText, null).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public static interface Callback {
            void onClick(int index);
        }
    }

    public static WCheckBoxDialog with_ChecBoxList(Context pCon, String title, String[] textContArray, String[] boardSeq) {
        return new WCheckBoxDialog(pCon, title, textContArray, boardSeq);
    }


    public static class WCheckBoxDialog {
        private Callback mCallback;
        private Context mCon;
        private String mTitle;
        private String[] mTextContArray;
        private boolean[] mTextChk;
        private String[] mBoardSeq;

        private ArrayList<String> mReturnSeq = new ArrayList<>();
        private ArrayList<String> mReturnBandName = new ArrayList<>();

        private WCheckBoxDialog(Context pCon, String title, String[] textContArray, String[] boardSeq) {
            mCon = pCon;
            mTitle = title;
            mTextContArray = textContArray;
            mBoardSeq = boardSeq;
        }

        public WCheckBoxDialog forShow(Callback WDialogRadio_Callback) {
            mCallback = WDialogRadio_Callback;
            mTextChk = new boolean[mTextContArray.length];
            for (int i = 0; i < mTextChk.length; i++) {
                mTextChk[i] = false;
            }
            try {
                new AlertDialog.Builder(mCon).setTitle(mTitle).setMultiChoiceItems(mTextContArray, mTextChk, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mTextChk[which] = isChecked;
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            for (int i = 0; i < mTextChk.length; i++) {
                                if (mTextChk[i] == true) {
                                    mReturnSeq.add(mBoardSeq[i]);
                                    mReturnBandName.add(mTextContArray[i]);
                                }
                            }
                            mCallback.onClick(mReturnSeq, mReturnBandName);
                        }
                    }
                }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public static interface Callback {
            void onClick(ArrayList<String> returnSeq, ArrayList<String> returnBandName);
        }
    }


    public static WDialogRadio withRadio_ok(Context pCon, String[] textContArray, int indexSelected) {
        return new WDialogRadio(pCon, textContArray, indexSelected, "확인");
    }

    public static class WDialogRadio {
        private Callback mCallback;
        private Context mCon;
        private String mTitle;
        private String[] mTextContArray;
        private String mBtnText;
        private int mIndexSelected;

        private WDialogRadio(Context pCon, String[] textContArray, int indexSelected, String btnText) {
            mCon = pCon;
            mTitle = "알림";
            mTextContArray = textContArray;
            mBtnText = btnText;
            mIndexSelected = indexSelected;
        }

        public WDialogRadio forShow(Callback WDialogRadio_Callback) {
            mCallback = WDialogRadio_Callback;
            try {
                new AlertDialog.Builder(mCon).setTitle(mTitle)
                        .setSingleChoiceItems(mTextContArray, mIndexSelected, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mIndexSelected = which;
                            }
                        }).setNegativeButton(mBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            if (mTextContArray != null) {
                                mCallback.onClick(mIndexSelected, mTextContArray[mIndexSelected]);
                            }
                        }
                    }
                }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public static interface Callback {
            void onClick(int index, String value);
        }
    }

    public static ImageDialog withImg(Context pCon, Bitmap bitmap) {
        return new ImageDialog(pCon, bitmap);
    }

    public static class ImageDialog extends Dialog {

        private Callback mCallback;

        public static interface Callback {
            void onClick();
        }


        public ImageDialog forShow(Callback imageDialog_Callback) {
            mCallback = imageDialog_Callback;
            return this;
        }

        public ImageDialog(Context context, Bitmap img) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_img);

            ImageView iv = (ImageView) findViewById(R.id.dialog_iv);
            iv.setImageBitmap(img);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDialog.this.dismiss();
                    mCallback.onClick();
                }
            });
            show();
        }
    }
}
