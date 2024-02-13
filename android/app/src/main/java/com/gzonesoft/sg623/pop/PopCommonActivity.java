package com.gzonesoft.sg623.pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.gzonesoft.cookzzang.DriverDispatchListActivity;
import com.gzonesoft.sg623.DriverMainActivity;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.ui.BoardActivity;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.ui.DeliveryListActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.ssomai.android.scalablelayout.ScalableLayout;

public class PopCommonActivity extends AppCompatActivity {

    public static Context mContext = null;

    private String title_color;
    private String activity_name;
    private String req_pop_code;
    private String title = "", btnTitle01 = "", btnTitle02 = "";
    private String message01 = "";
    private String message02 = "";
    private String cancelAction = "";


    View viewBlet;

    ScalableLayout scalableLayout_101;
    ScalableLayout scalableLayout_102;

    TextView tvPopTitle;
    TextView tvMessage1;
    TextView tvMessage2;

    TextView tvOK, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_common);

        mContext = this;

        // 넘어온 데이타 확인
        Intent intent = getIntent();

        title_color = String.valueOf(intent.getStringExtra("TITLE_COLOR"));
        activity_name = String.valueOf(intent.getStringExtra("ACTIVITY_NAME"));
        req_pop_code = String.valueOf(intent.getStringExtra("REQ_POP_CODE"));
        title = String.valueOf(intent.getStringExtra("TITLE"));
        btnTitle01 = String.valueOf(intent.getStringExtra("BUTTON_TITLE_01"));
        btnTitle02 = String.valueOf(intent.getStringExtra("BUTTON_TITLE_02"));
        message01 = String.valueOf(intent.getStringExtra("MESSAGE01"));
        message02 = String.valueOf(intent.getStringExtra("MESSAGE02"));
        cancelAction = String.valueOf(intent.getStringExtra("CANCEL_ACTION"));

        initMainUI();


        setMessage();


    }


    /**
     * 메인UI 초기화..
     */
    private void initMainUI() {
        viewBlet = (View) findViewById(R.id.viewBlet);
        scalableLayout_101 = (ScalableLayout) findViewById(R.id.scalableLayout_101);
        scalableLayout_102 = (ScalableLayout) findViewById(R.id.scalableLayout_102);
        scalableLayout_101.setVisibility(View.GONE);
        scalableLayout_102.setVisibility(View.GONE);

        tvPopTitle = (TextView) findViewById(R.id.tvPopTitle);
        tvMessage1 = (TextView) findViewById(R.id.tvMessage1);
        tvMessage2 = (TextView) findViewById(R.id.tvMessage2);

        tvOK = (TextView) findViewById(R.id.tvOK);
        tvCancel = (TextView) findViewById(R.id.tvCancel);

        try {
            if (!btnTitle01.equals("null") && btnTitle01.length() > 0) {
                tvOK.setText(btnTitle01);
            }
            if (!btnTitle02.equals("null") && btnTitle02.length() > 0) {
                tvCancel.setText(btnTitle02);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 메세지 세팅
     */
    public void setMessage(){


        if(title != null) {
            CommonUtil.with().setHtmlText(tvPopTitle, title);
        }

        if(title_color != null && !title_color.equals("")) {
            viewBlet.setBackgroundColor(Color.parseColor(title_color));
            tvPopTitle.setTextColor(Color.parseColor(title_color));
            tvOK.setBackgroundColor(Color.parseColor(title_color));
        }

        if(message01 != null && !message01.equals("")) {
            scalableLayout_101.setVisibility(View.VISIBLE);
            CommonUtil.with().setHtmlText(tvMessage1, message01);
        }

        if(message02 != null && !message02.equals("")) {
            scalableLayout_102.setVisibility(View.VISIBLE);
            CommonUtil.with().setHtmlText(tvMessage2, message02);
        }

    }


    /**
     * 버튼클릭 공용 메서드(태크로 구분)
     * @param view
     */
    public void clickEvent(View view) {

        int nTag = Integer.parseInt(view.getTag().toString());
        Loggers.d("nTag : " + nTag);

        switch (nTag) { 
            case 201:
                // 확인 - 보고..
                reportProcess();
                break;
            case 202:
                // 취소
                if (cancelAction.equals("납품이미지촬영")) {
                    // 메인화면에서 호출된경우..
                    ((DriverMainActivity) DriverMainActivity.mContext).runGallery();
                } else {
                    finish();
                }
                break;
            case 900: // 배경영역 터치시...
                finish();
            default:
                break;
        }
    }

    /**
     * 확인버튼 터치시 - 보고처리..
     */
    public void reportProcess(){

        // 1.MOB 상태보고..
        switch (activity_name) {
            case "DriverMainActivity":
                // 메인화면에서 호출된경우..
                ((DriverMainActivity) DriverMainActivity.mContext).popUpCallback(req_pop_code);
                break;
            case "DeliveryListActivity":
                ((DeliveryListActivity) DeliveryListActivity.mContext).popUpCallback(req_pop_code);
                break;
            case "BoardActivity":
                ((BoardActivity) BoardActivity.mContext).popUpCallback(req_pop_code);
                break;
            case "CvoMainActivity":
                ((CvoMainActivity) CvoMainActivity.mContext).popUpCallback(req_pop_code);
                break;
            default:
                break;
        }

        // 2.CVO 상태보고..
        if (((DriverMainActivity) DriverMainActivity.mContext) != null) {
            // CVO 업무보고(이벤트) - R:휴차(Relax)
//            if(req_pop_code.equals(WorkStatus._출근보고)) {
//                ((DriverMainActivity) DriverMainActivity.mContext).reportCvoEvent("S", "", "", "");
//            } else {
//                ((DriverMainActivity) DriverMainActivity.mContext).reportCvoEvent("R", "", "", "");
//            }
        }

    }


    /**
     * 종료작업 - 팝업 닫기
     */
    public void FinishWork() {

        // 닫기
        finish();
        overridePendingTransition(R.anim.slide_down, R.anim.no_change);
    }


}