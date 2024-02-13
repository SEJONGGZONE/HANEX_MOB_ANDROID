package com.gzonesoft.sg623.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.DriverMainActivity;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.DeliveryImageInfo;
import com.gzonesoft.sg623.data.ItemInfo;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.domain.WorkStatus;
import com.gzonesoft.sg623.model.ItemListManager;
import com.gzonesoft.sg623.pop.PopCommonActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 아이템 이미지정보 Set
 */
class ItemImageInfo {
    public String ITCODE;
    public String TYPE;
    public String SEQ;
    public String URL;

    public String getITCODE() {
        return ITCODE;
    }

    public void setITCODE(String ITCODE) {
        this.ITCODE = ITCODE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}

public class BoardActivity extends AppCompatActivity {

    private String mCurUrl = "", mMoveType = "", mItCode = "";
    private String mDDate = "", mClCode = "", mClName = "";
    public static Context mContext = null;
    private ItemInfo itemInfo = null;
    private ArrayList<ItemImageInfo> mItemInfos = new ArrayList<>();
    private ArrayList<DeliveryImageInfo> mDeliveryImageInfos = new ArrayList<>();

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int ERP_ITEMIMAGE_DEL = 10000;   // 아이템 이미지 삭제하기
    public static final int CVO_DISPATCHIMAGE_SEL = 20000;   // 납품 이미지 조회하기
    public static final int CVO_DISPATCHIMAGE_DEL = 20001;   // 납품 이미지 삭제하기


    public static final int PLAY_TTS = 30001;   // TTS 재생

    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CVO_DISPATCHIMAGE_DEL:
                    // 새로고침후 거래처선택까지...
                    ((CvoMainActivity) CvoMainActivity.mContext).targetRefreshAction();

                    // 웹뷰액티비티 종료..
                    ((BoardActivity) BoardActivity.mContext).FinishWork();
                    break;
                case CVO_DISPATCHIMAGE_SEL:
                    setWebView("");
                    break;
                case ERP_ITEMIMAGE_DEL:
                    // 리스트 재조회하기...
                    ((ItemMngListActivity) ItemMngListActivity.mContext).requestItemList();
                    // 웹뷰액티비티 종료..
                    ((BoardActivity) BoardActivity.mContext).FinishWork();
                    break;

                case RequesterSession.REQ_OK:

                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

            }


        }
    };


    private String mItcode;
    private String mType;
    private String mSeq;
    /**
     * 상품아미지 삭제여부 확인- 팝업호출
     */
    public void popItemImageDelete(String itcode, String type, String seq){
        try {

            // 삭제 키 저장..
            mItcode = itcode;
            mType = type;
            mSeq = seq;

            String title = "이미지 삭제";
            String message01 = "";
            String message02 = "선택하신 상품 이미지를 <big><big><b>삭제</b></big></big> 하시겠습니까?";

            Intent intent = new Intent(mContext, PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName()); //  현재 엑티비티명 확인
            intent.putExtra("REQ_POP_CODE", WorkStatus._상품이미지삭제);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "배차내역조회");
            mContext.startActivity(intent);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 상품아미지 삭제여부 확인- 팝업호출
     */
    public void popDeliveryImageDelete(String ddate, String clCode, String type, String seq){
        try {

            // 삭제 키 저장..
            mDDate = ddate;
            mClCode = clCode;
            mType = type;
            mSeq = seq;

            String title = "이미지 삭제";
            String message01 = "";
            String message02 = "선택하신 납품 이미지를 <big><big><b>삭제</b></big></big> 하시겠습니까?";

            Intent intent = new Intent(mContext, PopCommonActivity.class);
            intent.putExtra("TITLE_COLOR", "#305597");
            intent.putExtra("ACTIVITY_NAME", this.getClass().getSimpleName()); //  현재 엑티비티명 확인
            intent.putExtra("REQ_POP_CODE", WorkStatus._납품이미지삭제);
            intent.putExtra("TITLE", title);
            intent.putExtra("MESSAGE01", message01);
            intent.putExtra("MESSAGE02", message02);
            intent.putExtra("CANCEL_ACTION", "배차내역조회");
            mContext.startActivity(intent);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 공통팝업 콜백
     */
    public void popUpCallback(String p_req_pop_code) {
        try {
            // 1.사운드 재생
            if ( (DriverMainActivity) DriverMainActivity.mContext != null)
                ((DriverMainActivity) DriverMainActivity.mContext).playSound();

            // 2.팝업유형에 따른 작업처리..
            String htmlString = "";
            if(p_req_pop_code.equals(WorkStatus._상품이미지삭제)) {
                // API-요청
                requestItemImageDelete();
            } else if(p_req_pop_code.equals(WorkStatus._납품이미지삭제)) {
                // API-요청(작업중..)
                requestCvoDeliveryImageDelete();
            }

            // 3.팝업닫기
            ((PopCommonActivity) PopCommonActivity.mContext).FinishWork();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 아이템 이미지 삭제요청
     */
    public void requestItemImageDelete() {

        try {
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("@I_ITCODE", mItcode);
            jsonMap.put("@I_TYPE", mType);
            jsonMap.put("@I_SEQ", mSeq);

            RequesterSession.with().RestAPIServiceCall(Svc.ERP_ITEMIMAGE_DEL, jsonMap, requestItemImageDeleteCallback);

        }
        catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }
    }

    /**
     * REST-API Callback - 아이템 요청, 콜백
     */
    private Callback requestItemImageDeleteCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "requestItemImageDeleteCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "requestItemImageDeleteCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "requestItemImageDeleteCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + "").sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestItemImageDeleteCallback, 수신데이타:" + strJsonOutput);

                // 서비스 호출 성공
                handler.obtainMessage(ERP_ITEMIMAGE_DEL, "").sendToTarget();

            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "").sendToTarget();
                e.printStackTrace();
            }
        }
    };


    /**
     * CVO - 납품 이미지 삭제요청
     */
    public void requestCvoDeliveryImageDelete() {

        try {
//            // 삭제 키 저장..
//            mDispatchId = dispatchId;
//            mClCode = clCode;
//            mType = type;
//            mSeq = seq;

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_DDATE", mDDate);
            paraMap.put("@I_CLCODE", mClCode);
            paraMap.put("@I_TYPE", mType);
            paraMap.put("@I_SEQ", mSeq);

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_CLIENTIMAGE_DEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoDeliveryImageDeleteCallback,
                    keyMap,
                    false
            );

        }
        catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }
    }

    /**
     * REST-API Callback - 아이템 삭제요청, 콜백
     */
    private Callback requestCvoDeliveryImageDeleteCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "requestCvoDeliveryImageDeleteCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "requestCvoDeliveryImageDeleteCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "requestItemImageDeleteCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + "").sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestItemImageDeleteCallback, 수신데이타:" + strJsonOutput);

                // 서비스 호출 성공
                handler.obtainMessage(CVO_DISPATCHIMAGE_DEL, "").sendToTarget();

            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "").sendToTarget();
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        // UI 세팅
        initUI();

        // 넘어온 데이타 확인
        Intent intent = getIntent();
        mCurUrl = String.valueOf(intent.getStringExtra("MOVE_URL"));
        mMoveType = String.valueOf(intent.getStringExtra("MOVE_TYPE"));
        mItCode = String.valueOf(intent.getStringExtra("ITCOCE"));

        if (mMoveType.equals(CommType._상품등록이미지)) {
            itemInfo = ItemListManager.getInstance().getItemInfo(mItCode);
            getItemImageInfo(); // 상품이미지 가져오기...

            // 페이지이동
            setWebView(mCurUrl);
        } else if (mMoveType.equals(CommType._납품사진이미지)) { // 2023.09.25 작성..
            mDDate = String.valueOf(intent.getStringExtra("DDATE"));
            mClCode = String.valueOf(intent.getStringExtra("CLCOCE"));
            mClName = String.valueOf(intent.getStringExtra("CLNAME"));
            // Cvo납품이미지 가져오기...
            requestCvoDeliveryImage(mDDate, mClCode, mClName);
        }

//        if (moveType.equals(WebMoveType._결제)) {
//            FloatingActionButton fab = findViewById(R.id.floatingBtn);
//            fab.setVisibility(View.GONE);
//        }


    }

    /**
     * 아이템, 이미지정보 JSON 추출...
     */
    private void getItemImageInfo() {
        try {
            String jsonItemData = "{ \"RecordSet\" : " + itemInfo.getJSON_IMAGE_URL() +  "}";
            JsonElement jelement = new JsonParser().parse(jsonItemData);
            JsonObject jObject = jelement.getAsJsonObject();
            JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();

            mItemInfos.clear();
            for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
                JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
                Gson gson = new Gson();
                ItemImageInfo info = gson.fromJson(jobjectStore, ItemImageInfo.class);
                mItemInfos.add(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_board);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // 떠다니는 버튼 세팅..
        FloatingActionButton fab = findViewById(R.id.floatingBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinishWork();
            }
        });

        Button btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카메라 호출
                ((DriverMainActivity) DriverMainActivity.mContext).takePicture(mDDate, mClCode);
                // 닫기..
                finish();
            }
        });
    }

    /**
     * 웹뷰 URL 이동하기
     * @param url
     */
    private void setWebView(String url) {
        try {
            WebView webView = (WebView) findViewById(R.id.webView);

            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.setNetworkAvailable(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            webView.addJavascriptInterface(new WebViewInterface(this, webView), "NativeApp");
            //// Sets whether the DOM storage API is enabled.
            webView.getSettings().setDomStorageEnabled(true);


            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();

            String data = "";
            if (mMoveType.equals(CommType._상품등록이미지)) {
                data = "" +
                        "<html>" +
                        "<head><title>cookzzang</title>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />" +
                        "<style>" +
                        ".centered {position: fixed;top: 50%;left: 50%;transform: translate(-50%, -50%); width:100%; height:auto;}" +
                        ".centered2 {width:100%; height:auto;}" +
                        "button {" +
                        "padding:10px 20px; font-size:20px; margin:10px 0px 20px 0px; background-color:rgb(225, 77, 0); color:white; border-width:0px;" +
                        "}" +
                        "</style>" +
                        "</head>";
                // 센터관리자-아이템이미지 업로드작업중...
                int nIdx = 0, moreImageCnt = 0;
                String htmlString = "<body>";
                for (ItemImageInfo info : mItemInfos) {
                    htmlString += "<center>";
                    if (info.getTYPE().equals(CommType._대표이미지)) {
                        htmlString += "<BR><h1>대표이미지</h1><BR>" +
                                "<img src=\"" + info.getURL() + "\" class=\"centered2\"/><BR>";
                    } else {
                        moreImageCnt++;
                        htmlString += "<h1>추가이미지 " + String.format("%d", moreImageCnt) + "</h1><BR>" +
                                "<img src=\"" + info.getURL() + "\" class=\"centered2\"/><BR>";
                    }
                    htmlString += "<button onclick=\"window.NativeApp.deleteImage(" +
                            "'" + info.getITCODE() + "'," +
                            "'" + info.getTYPE() + "'," +
                            "'" + info.getSEQ() + "');\">삭 제</button>" +
                            "</center><hr><BR><BR>";
                    nIdx++;
                }
                htmlString += "</body></html>";
                data = data + htmlString;

                webView.loadData(data, "text/html", null);

            } else if (mMoveType.equals(CommType._납품사진이미지)) {
                data = "" +
                        "<html>" +
                        "<head><title>아이사랑</title>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />" +
                        "<style>" +
                        ".clientName {font-size:2rem; font-weight:600; color:white; background-color:rgba(34, 13, 125, 0.7); padding:0.2rem;}" +
                        ".ddate {font-size:1.5rem; font-weight:600; color:white; background-color:rgba(125, 19, 230, 0.739); padding:0.2rem; margin-top:-1.8rem;}" +
                        ".centered {position: fixed;top: 50%;left: 50%;transform: translate(-50%, -50%); width:100%; height:auto;}" +
                        ".centered2 {width:100%; height:auto;}" +
                        "button {" +
                        "padding:10px 20px; font-size:20px; margin:10px 0px 20px 0px; background-color:rgb(225, 77, 0); color:white; border-width:0px;" +
                        "}" +
                        "</style>" +
                        "</head>";
                // 센터관리자-아이템이미지 업로드작업중...
                int nIdx = 0, moreImageCnt = 0;

                String ddateString = mDeliveryImageInfos.get(0).getDDATE();
                ddateString = ddateString.substring(0,4) + "/" + ddateString.substring(4,6) + "/" + ddateString.substring(6,8);

                String htmlString = "<body>" +
                        "<div style=\"width:100%; text-align: center; margin-top:4rem;\">" +
                        "<p class=\"clientName\">" + mDeliveryImageInfos.get(0).getCLNAME() + "</p>" +
                        "<p class=\"ddate\">" + ddateString + "</p>" +
                        "</div>";
                for (DeliveryImageInfo info : mDeliveryImageInfos) {
                    htmlString += "<center>";
                    if (info.getTYPE().equals(CommType._서명이미지)) {
                        htmlString += "<BR><h1>서명이미지</h1><BR>" +
                                "<img src=\"" + info.getURL() + "\" class=\"centered2\"/><BR>";
                    } else {
                        moreImageCnt++;
                        htmlString += "" +
                                "<h1>납품이미지 " + String.format("%d", moreImageCnt) + "</h1>" +
                                "<h3 style=\"color:gray;\">- 촬영일시 : " + info.getWS_NEWDATE() + " -</h3>" +
                                "" +
                                "<img src=\"" + info.getURL() + "\" class=\"centered2\"/><BR>";
                    }
                    if (info.getWS_NEWUSER().equals(AppSetting.cvoUserInfo.getEMPNO())) {
                        // 내가올린 사진은 삭제할수 있다.
                        htmlString += "<button onclick=\"window.NativeApp.deleteDeliveryImage(" +
                                "'" + info.getDDATE() + "'," +
                                "'" + info.getCLCODE() + "'," +
                                "'" + info.getTYPE() + "'," +
                                "'" + info.getSEQ() + "');\">삭 제</button>" +
                                "</center><BR><BR>";
                    } else {
                        // 다른유저가 올린사진은 삭제할수 없다.
                        htmlString += "<BR><BR>";
                    }
                    nIdx++;
                }
                htmlString += "</body></html>";
                data = data + htmlString;

                webView.loadData(data, "text/html", null);

            } else if (mMoveType.equals(CommType._일반호출)) {
                webView.loadUrl(url);
            } else {
                data = "" +
                        "<html>" +
                        "<head><title>cookzzang</title>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />" +
                        "<style>" +
                        ".centered {position: fixed;top: 50%;left: 50%;transform: translate(-50%, -50%); width:100%; height:auto;}" +
                        "}" +
                        "</style>" +
                        "</head>";
                data = data + "<body><center><img src=\"" + url + "\" class=\"centered\"/></center></body></html>";
                webView.loadData(data, "text/html", null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    /**
     * Back뒤로가기 버튼 처리..
     */
    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }


    /**
     * CVO - 납품이미지 조회
     * @param dispatchId
     */
    public void requestCvoDeliveryImage(String ddate, String clCode, String clName) {
        try {


            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("@I_COMPANYCD", Svc.CVO_COMPANYCD);
            paraMap.put("@I_DDATE", ddate);
            paraMap.put("@I_CLCODE", clCode);
            paraMap.put("@I_CLNAME", clName);

            // CVO 호출 -------------------------------------------------------------------------------------------
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY());
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getCVO_API_SERVER() + Svc.CVO_CLIENTIMAGE_SEL),
                    "POST",
                    "",
                    paraMap,
                    requestCvoDeliveryImageCallback,
                    keyMap,
                    false
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 콜백처리 - CVO 배송리스트 조회
     */
    private Callback requestCvoDeliveryImageCallback = new Callback() {
        String tag = "requestCvoDeliveryImageCallback";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d2(this, tag + "---- onFailure:" + e.toString());
            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "접속에 실패했습니다.").sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]접속에 실패했습니다.").sendToTarget();
                return;
            }
            Loggers.d2(this, tag + ", ---- onResponse");

            String strJsonOutput = response.body().string();

            Loggers.d2(this, tag +",수신데이타:" + strJsonOutput);

            JsonElement jelement = new JsonParser().parse(strJsonOutput);
            JsonObject recvObj = jelement.getAsJsonObject();

            JsonArray jarrayShif = recvObj.getAsJsonArray("RecordSet");

            // 기존 배송정보 삭제
            mDeliveryImageInfos.clear();

            for (int idx = 0; idx < jarrayShif.size(); ++idx) {
                JsonObject jobject = jarrayShif.get(idx).getAsJsonObject();

                Gson gson = new Gson();
                DeliveryImageInfo info = gson.fromJson(jobject, DeliveryImageInfo.class);
                // 정보 추가
                mDeliveryImageInfos.add(info);
            }

            handler.obtainMessage(CVO_DISPATCHIMAGE_SEL, "").sendToTarget();

        }
    };


}
