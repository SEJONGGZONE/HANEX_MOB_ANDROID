package com.gzonesoft.sg623.pop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gzonesoft.sg623.IntroActivity;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.SettingPref;

public class SlidePopAuthInfoActivity extends AppCompatActivity {
    public Context mContext;

    private String curAreaType = "";

    private TextView tvTitle, tvSummary01, tvSummary02;
    private Button btn02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // 넘어온 데이타 확인
        Intent intent = getIntent();
        curAreaType = String.valueOf(intent.getStringExtra("AREA_TYPE"));

        // UI 초기화
        initUI();

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
        setContentView(R.layout.activity_pop_auth);

        // 상태바 색상 지정(검은색으로)
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().hide();

        // 뷰 매핑
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSummary01 = (TextView) findViewById(R.id.tvSummary01);
        tvSummary02 = (TextView) findViewById(R.id.tvSummary02);
        btn02 = (Button) findViewById(R.id.btn02);


//        String htmlString = "";
//
//        //htmlString = "최근 공차등록 - <small><small><small>01시30분 경과</small></small></small>";
//        htmlString = "최근 공차정보";
//        setHtmlMarqueeText(tvTitle, htmlString);
//        htmlString = "<small>대기시간 : </small><big><b>09시30분</b></big> <small>이후</small>";
//        setHtmlMarqueeText(tvSummary01, htmlString);
//        htmlString = "대구 달성군 → 경기 고양시 일산동구";
//        setHtmlMarqueeText(tvSummary02, htmlString);
//        htmlString = "수작업 / 당착 / 혼적가능";
//        setHtmlMarqueeText(tvSummary03, htmlString);
//        htmlString = "위 정보를 <big><b>수정</b></big> 하시겠습니까?";
//        setHtmlMarqueeText(tvSummary04, htmlString);

    }

    /**
     * HTML 텍스트로 세팅(마퀴즈효과는 덤으로..)
     * @param tvTarget
     * @param htmlString
     */
    private void setHtmlMarqueeText(TextView tvTarget, String htmlString) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                tvTarget.setText(Html.fromHtml(htmlString));
            } else {
                tvTarget.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
            }
            // 글자가 흐르기위해서...
            tvTarget.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvTarget.setSingleLine(true);
            tvTarget.setMarqueeRepeatLimit(-1);
            tvTarget.setSelected(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 배경영역을 터치시.(View XML에서 기술함..)
     * @param view
     */
    public void hideQuickMenu(View view) {
        SettingPref.with(mContext).savePrefWithString(ConstValue.LastWorkStat.AUTH_POP_YN.name(), "Y");
        ((IntroActivity) IntroActivity.mContext).permissionCheck();
        FinishWork();
    }

    /**
     * 종료작업 - 팝업 닫기
     */
    private void FinishWork() {

        // 닫기
        finish();
        overridePendingTransition(R.anim.slide_down, R.anim.no_change);
    }
}
