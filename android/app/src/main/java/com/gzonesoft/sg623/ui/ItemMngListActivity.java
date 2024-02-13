package com.gzonesoft.sg623.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.Layout.ItemLayout;
import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.ItemInfo;
import com.gzonesoft.sg623.data.UploadFileInfo;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.model.ClientCustManager;
import com.gzonesoft.sg623.model.ItemListManager;
import com.gzonesoft.sg623.pop.SlidePopClientDetailActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ImageResizeUtils;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;
import com.gzonesoft.sg623.util.UDialog;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItemMngListActivity extends AppCompatActivity {

    private ScrollView scrollItemList;
    private LinearLayout llItemList; // 아이템 리스트

    private ScalableLayout scSearchArea, scInfoArea;
    private ImageView iv_back;
    private TextView toolbar_title;

    private AutoCompleteTextView actvKeyword; // 검색어 입력
    private Button btnDeleteKeyword;

    public static Context mContext = null;

    private String mKeyword;
    ProgressDialog pDialog;

    // 업로드 파일처리 관련
    private boolean bUploadYn = false;
    private ArrayList<File> mUploadWorkFiles = new ArrayList<>();   // 업로드 작업정보 저장 - 파일정보
    private ArrayList<String> mUploadDocType = new ArrayList<>();   // 업로드 작업정보 저장 - 서류종류
    private int mLastWorkIndex = -1;    // 최종 업로드 작업 인덱스
    private ArrayList<UploadFileInfo> uploadFileResultInfos = new ArrayList<UploadFileInfo>();    // 업로드 성공 리턴정보 저장

    // 업로드구분
    public String mImageType = "";

    // 카메라 촬영 관련
    private File mTempFile;
    private int nCurrentCameraButtonTag = 0;
    private Uri mImageCaptureUri;
    // 이미지 처리관련..
    private Boolean isCamera = false;
    private final int ACT_TAKE_PIC = 20000;
    private final int PICK_FROM_GALLERY = 20001;
    private final int REQ_CODE_SELECT_IMAGE = 20002;

    private ItemInfo mItemInfo = new ItemInfo();

    public int pageSize = 10, pageNum = 1, curResultCount = 0;

    /**
     * 서버요청 처리결과를 위한 핸들러
     */
    public static final int ERP_ITEM_MNG_SEL = 10000;   // 아이템 정보 가져오기
    public static final int NEXT_UPLOAD_WORK = 10001;   //


    public static final int PLAY_TTS = 30001;   // TTS 재생

    public static final int RETRY_PROCESS = 19999;   // 재처리 프로세스

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NEXT_UPLOAD_WORK:
                    // 대기화면 닫기..
                    showProgress(false);
                    try {
                        // 업로드 요청..
                        mLastWorkIndex++;
                        if (mUploadWorkFiles.size() > mLastWorkIndex) {
                            // 다음 업로드 파일 시도..
                            uploadImage(mUploadWorkFiles.get(mLastWorkIndex).getPath(), mUploadDocType.get(mLastWorkIndex));
                        } else {
                            // 업로드 작업 끝
                            requestItemList();

//                            // 상품재조회..
//                            String subCode = ((ItemListActivity) ItemListActivity.mContext).mSubCode;
//                            ((ItemListActivity) ItemListActivity.mContext).requestItemList(subCode);
//
//                            // 슬라이드 팝업닫기
//                            FinishWork();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case ERP_ITEM_MNG_SEL:

                    // 대기화면 보이기/감추기..
                    showProgress(false);

                    // 아이템 세팅
                    setItemInfo();

                    break;
                case RETRY_PROCESS:

                    // 대기화면 닫기..
                    showProgress(false);

                    // 재처리 프로세스 호출
                    retryProcess(msg.obj.toString());

                    break;
                case RequesterSession.REQ_OK:

                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    // 대기화면 보이기/감추기..
                    showProgress(false);

                    break;

                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    // 대기화면 보이기/감추기..
                    showProgress(false);
                    break;

            }


        }
    };

    /**
     * 재처리 프로세스
     *
     * @param jsonBodyString
     */
    void retryProcess(final String jsonBodyString) {
//        // 재처리여부 팝업다이얼로그 호출
//        UDialog.withTwoCustom_okCancel(mContext, Svc.APP_NAME, getString(R.string.msg_conn_retry), getString(R.string.title_confirm), getString(R.string.title_cancel), View.GONE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
//            @Override
//            public void onClick_yes() {
//
//                showProgress(true);
//
//                // 사용자 로그인, 재처리 시도..
//                RequesterSession.with().RestAPIServiceCall(Svc.CHECK_LOGIN, jsonBodyString, requestLoginCallback);
//            }
//
//            @Override
//            public void onClick_no() {
//                CommonUtil.with().ToastMsg(mContext, getString(R.string.msg_cancel), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 컨텍스트 저장
        mContext = this;

        // 프로그래스 다이얼로그 초기화
        initProgressDlg();

        // 메인 UI 초기화
        initMainUI();

        scSearchArea.setVisibility(View.VISIBLE);

        // 아이템 요청
        pageNum = 1;


        // 포커스 주기
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 사용하고자 하는 코드
                actvKeyword.requestFocus();
                // 키보드 보이기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(actvKeyword, 0);
            }
        }, 1000);


    }

    // 팝업여부 확인
    public boolean popOpenYN = true;
    @Override
    protected void onResume() {
        super.onResume();
        Loggers.d("MainActivity, 화면 복귀시..");

        popOpenYN = false;
    }

    /**
     * 메인 UI 초기화
     */
    private void initMainUI() {
        // 화면을 portrait(세로) 화면으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_item_mng);


        llItemList = (LinearLayout) findViewById(R.id.llItemList); // 아이템 리스트

        toolbar_title = (TextView) findViewById(R.id.toolbar_title); // 툴바타이틀
        CommonUtil.with().setHtmlMarqueeText(toolbar_title, "상품 관리");

        scSearchArea = (ScalableLayout) findViewById(R.id.scSearchArea); // 검색 영역
        scInfoArea = (ScalableLayout) findViewById(R.id.scInfoArea); // 안내문구 영역


        scrollItemList = (ScrollView) findViewById(R.id.scrollItemList);
        // 스크롤뷰 마지막 위치 이벤트 감지하기
        scrollItemList.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

//                int scrollX = rootScrollView.getScrollX(); //for horizontalScrollView
//                int scrollY = rootScrollView.getScrollY(); //for verticalScrollView
//                //DO SOMETHING WITH THE SCROLL COORDINATES

                View view = scrollItemList.getChildAt(scrollItemList.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollItemList.getHeight() + scrollItemList.getScrollY()));

                if (diff == 0) { // 스크롤 bottom
                    Loggers.d("스크롤바닥..");
                    //CommonUtil.with().ToastMsg(mContext, "데이타 요청중입니다.", Toast.LENGTH_SHORT).show();
                    pageNum = pageNum + 1;
                    if (curResultCount < pageSize) {
                        Loggers.d("추가요청필요없음..");
                        CommonUtil.with().ToastMsg(mContext, "더이상 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        requestItemList();
                    }
                }

            }
        });

        // 뒤로가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mKeyword = "";
        // 검색어 입력
        actvKeyword = (AutoCompleteTextView) findViewById(R.id.actvKeyword);
        // actvKeyword.setVisibility(View.GONE);

        actvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClientCustManager.getInstance() != null) {
                    int inputLength = actvKeyword.length();
                    if (inputLength == 0) {
                        scInfoArea.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // 검색어 입력 버튼 - 엔터키 눌렀을때 세팅..
        actvKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    // 키보드내리기
                    hideKeyboard();

                    int inputLength = actvKeyword.length();
                    if (inputLength<2) {
                        CommonUtil.with().ToastMsg(mContext, "최소 2글자 이상입력바랍니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        mKeyword = actvKeyword.getText().toString();
                        pageNum = 1;
                        requestItemList();
                    }
                    return true;
                }
                return false;
            }
        });
        actvKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Loggers.d("입력값은 = " + s.toString() + ", 폰트크기는 = " + actvKeyword.getTextSize()
                        + ", 폰트크기는 = " + actvKeyword.getTextScaleX());
//                int inputLength = actvKeyword.length();
//                if (inputLength>=0) {
//
//                }
            }
        });
        // 검색어 삭제 버튼 세팅
        btnDeleteKeyword = (Button) findViewById(R.id.btnDeleteKeyword);
        btnDeleteKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 검색어 삭제
                    actvKeyword.setText("");
                    mKeyword = ""; // 검색어 지우기
                    ClientCustManager.getInstance().clear();  // 데이타 초기화
                    llItemList.removeAllViews(); // 뷰지우기..

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }




    /**
     * 키보드 내리기..
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(actvKeyword.getWindowToken(), 0);
    }

    /**
     * 프로그래스 다이얼로그 초기화
     */
    private void initProgressDlg() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setTitle("잠시만 기다려주세요.");
        pDialog.setMessage("서버 요청중입니다.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
    }

    /**
     * 프로그레스 컨트롤 제어
     *
     * @param show 보이기 여부
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            if (show) {
                pDialog.show();
            }
            else {
                pDialog.dismiss();
            }
        } else {
            if (show) {
                pDialog.show();
            }
            else {
                pDialog.dismiss();
            }
        }
    }

    /**
     * 아이템 세팅
     */
    private void setItemInfo() {
        int nIdx = 0;
        for(ItemInfo info: ItemListManager.getInstance().values()) {
            if (pageNum == 1) {
                // 1페이지의 경우 무조건 추가..
                addItem(info);
            } else {
                // 기존 페이지 이후에만 추가..
                if ((pageSize * (pageNum - 1)) <= nIdx) {
                    addItem(info);
                }
            }
            nIdx++;
        }
    }

    /**
     * 아이템 추가
     * @param info
     */
    private void addItem(ItemInfo info) {
        ItemLayout itemLayout1 = new ItemLayout(getApplicationContext(), info);
        llItemList.addView(itemLayout1);
    }


    /**
     * 이미지 상세보기(웹뷰활용)
     * @param urlString
     * @param itCode
     */
    public void viewItemImage(String urlString, String itCode) {
        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
        intent.putExtra("MOVE_URL", urlString);
        intent.putExtra("MOVE_TYPE", CommType._상품등록이미지);
        intent.putExtra("ITCOCE", itCode);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    /**
     * 거래처상세 팝업 슬라이드
     */
    public void popClientDetailInfo(String code) {
        try {

            Intent intent = new Intent(mContext, SlidePopClientDetailActivity.class);
            intent.putExtra("CLCODE",  code);
//            intent.putExtra("OPEN_TYPE", PopType._일반상품);
//            intent.putExtra("BOX_QTY", info.getORD_BOXQTY());
//            intent.putExtra("UNIT_QTY", info.getORD_UNITQTY());

//            // 기존 주문건수가 있다면...
//            int boxQty = Integer.parseInt(info.getORD_BOXQTY());
//            int unitQty = Integer.parseInt(info.getORD_UNITQTY());
//            if((boxQty>0) || (unitQty>0)) {
//                intent.putExtra("OPEN_TYPE", PopType._일반상품_수정);
//            }

            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.no_change); // 위로 슬라이드..

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 아이템 요청
     */
    public void requestItemList() {

        try {

            showProgress(true);

            if (pageNum == 1) {
                ItemListManager.getInstance().clear();  // 데이타 초기화
                llItemList.removeAllViews(); // 뷰지우기..
            }


            Map<String, String> jsonMap = new HashMap<String, String>();
            // 운영용..
            jsonMap.put("@I_KEYWORD", mKeyword);
            jsonMap.put("@I_PAGE_SIZE", String.format("%d", pageSize));
            jsonMap.put("@I_PAGE_NUM", String.format("%d", pageNum));
            jsonMap.put("@I_INPUT_USER", "");

            RequesterSession.with().RestAPIServiceCall(Svc.ERP_ITEM_MNG_SEL, jsonMap, requestItemListCallback);

        }
        catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }
    }

    /**
     * REST-API Callback - 아이템 요청, 콜백
     */
    private Callback requestItemListCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "requestItemListCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "requestItemListCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "requestItemListCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "requestItemListCallback, 수신데이타:" + strJsonOutput);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject jObject = jelement.getAsJsonObject();

                String ResultCode = jObject.get("ResultCode").getAsString();
                String ResultMsg = jObject.get("ResultMsg").getAsString();
                String RecordCount = jObject.get("RecordCount").getAsString();
                JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();

                if (!ResultCode.equals("00")) {
                    // 요청 실패
                } else {
                    // 요청 성공
                    if (jarrayRecordSet.size() == 0) {
                        // 데이타가 없음
                        handler.obtainMessage(RequesterSession.REQ_OK, "데이타가 없습니다.").sendToTarget();
                    } else {
                        // 데이타 추가..
                        curResultCount = 0;
                        for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
                            JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
                            Gson gson = new Gson();

                            ItemInfo info = gson.fromJson(jobjectStore, ItemInfo.class);
                            ItemListManager.getInstance().add(info);
                            curResultCount++; // 결과수 저장..(스크롤바닥까지 갔을때 다시요청할필요없는 상황을 파악하기 위함)
                        }

                        // 서비스 호출 성공
                        handler.obtainMessage(ERP_ITEM_MNG_SEL, "").sendToTarget();
                    }
                }

            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }
        }
    };


    /**
     * 카메라 사진 찍기
     */
    public void takePicture(final String itCode, final String imageType) {
        try {

            // 아이템 코드 저장..
            mItemInfo.setITCODE(itCode);
            // 사진유형 저장
            mImageType = imageType;

            UDialog.withTwoCustom_okCancel(mContext, Svc.APP_NAME, "사진촬영 또는 앨범을 선택해주세요.", "사진촬영", "앨범선택", View.VISIBLE).forShow_all(new UDialog.WDialogBtnTwoCustom.CallbackAll() {
                @Override
                public void onClick_yes() {
                    // 사진촬영
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // 1.디렉토리 생성..
                        String savePath = Environment.getExternalStorageDirectory() + Svc.CAMERA_IMG_PATH;
                        CommonUtil.with().makeDir(savePath);
                        // 2.촬영파일 경로 생성..
                        mTempFile = new File(savePath, "/" + imageType + "_" + CommonUtil.getSystemTime() + ".jpg");
                        Loggers.d("FilePath=" + mTempFile.getPath());
                        // 3.카메라 준비..
                        mImageCaptureUri = FileProvider.getUriForFile(mContext, "com.gzonesoft.sg415", mTempFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        // 4.카메라 호출..
                        startActivityForResult(takePictureIntent, ACT_TAKE_PIC);
                    }
                }

                @Override
                public void onClick_no() {
                    // 앨범불러오기
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_GALLERY);
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 카메라 촬영 또는 앨범이미지 선택후 결과이벤트 수신부..
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ACT_TAKE_PIC) {
                if (mTempFile != null) {
                    Loggers.d("사진추가1....");
                    isCamera = true;
                    ImageResizeUtils.resizeFile(mTempFile, mTempFile, 1280, isCamera);

                }
            } else if (requestCode == PICK_FROM_GALLERY) {

                Uri photoUri = data.getData();

                Cursor cursor = null;
                try {
                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = { MediaStore.Images.Media.DATA };
                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);
                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    mTempFile = new File(cursor.getString(column_index));
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (mTempFile != null) {
                    isCamera = true;
                    ImageResizeUtils.resizeFile(mTempFile, mTempFile, 1280, isCamera);
                }
            }

            // 파일업로드 시도...
            saveFileWork();
        }
    }

    /**
     * 첨부서류들을 저장하는 작업
     */
    public void saveFileWork() {

        mUploadWorkFiles.clear();
        mUploadDocType.clear();
        try {
            // 업로드 대상 확인..
            mUploadWorkFiles.add(mTempFile);
            if (mImageType.equals(CommType._대표이미지)) {
                mUploadDocType.add("103"); // 대표이미지
            } else {
                mUploadDocType.add("10499"); // 추가이미지
            }


            if (mUploadWorkFiles.size() > 0) {

                // 업로드 파일정보 관리변수 초기화
                uploadFileResultInfos.clear();
                // 업로드 요청..
                mLastWorkIndex = 0;
                // 업로드이후 콜백에서 다음을 확인후 반복 호출된다. 그래서 시작(초기)인덱스만 주어서 호출한다.
                uploadImage(mUploadWorkFiles.get(mLastWorkIndex).getPath(), mUploadDocType.get(mLastWorkIndex));
            } else {
                // 업로드할 파일이 없다면..
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 서버에 최종적으로 저장될 파일명 생성
     * @param GEONUM
     * @param docType
     * @return
     */
    private String getSaveFileName(String GEONUM, String docType) {
        String nowHHMMSS = CommonUtil.with().nowHHMMSS();

        return "I_" + GEONUM + "_" + docType + "_" + nowHHMMSS;
    }

    /**
     * 파일 업로드 시도요청
     *
     * @param FileUrl
     */
    private void uploadImage(String FileUrl, String docType) {
        try {
//            Loggers.d("파일 업로드 시도요청[0], FileUrl = " + FileUrl);
//            Loggers.d("파일 업로드 시도요청[1], FileUrl = " + Environment.getExternalStorageDirectory().getAbsolutePath() + FileUrl);

            //CommonUtil.with().ToastMsg(mContext, "전송작업중입니다. 통신/네트워크 상황에 따라 다소 지연시간이 생길수 있습니다.\n잠시만 기다려주세요...", Toast.LENGTH_SHORT).show();
            showProgress(true);

            // 서명이미지 파일업로드관련.
            File sourceFile = new File(FileUrl);

            // 서버요청 준비..
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", FileUrl, RequestBody.create(MultipartBody.FORM, sourceFile))
                    .build();

            // 서버 서비스 호출
            String uploadURL = Svc.IMG_UPLOAD_URL
                    + Svc.FILE_TYPE_GOODS + "/"
                    + Svc.FILE_FORMAT_JPG + "/false/"
                    + getSaveFileName(mItemInfo.getITCODE(), docType);

            RequesterSession.with().commServiceCall(
                    requestBody,
                    uploadURL,
                    uploadImageCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * REST-API Callback - 파일 업로드 시도요청, 콜백
     */
    private Callback uploadImageCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

            Loggers.d2(this, "uploadImageCallback, ---- body:" + RequesterSession.with().bodyToString(call.request()));
            Loggers.d2(this, "uploadImageCallback, ---- onFailure:" + e.toString());
//            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();

            String bodyString = RequesterSession.with().bodyToString(call.request());
            handler.obtainMessage(RETRY_PROCESS, bodyString).sendToTarget();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d2(this, "uploadImageCallback, ---- onResponse, " + response.code());
            if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }

            try {
                String strJsonOutput = response.body().string();

                Loggers.d2(this, "uploadImageCallback, 수신데이타:" + strJsonOutput);

                showProgress(false);

                JsonElement jelement = new JsonParser().parse(strJsonOutput);
                JsonObject jObject = jelement.getAsJsonObject();

                String ResultCode = jObject.get("ResultCode").getAsString();
                String ResultMsg = jObject.get("ResultMsg").getAsString();
                String RecordCount = jObject.get("FileCount").getAsString();
                JsonArray jarrayRecordSet = jObject.get("fileDetails").getAsJsonArray();

                if (!ResultCode.equals("00")) {
                    // 요청 실패
                } else {
                    // 요청 성공
                    if (jarrayRecordSet.size() < 0) {
                        Loggers.d("[uploadImageCallback] -- " + getString(R.string.api_has_nodata));
                    } else {
                        // 결과 있음
                        JsonObject jobjectStore = jarrayRecordSet.get(0).getAsJsonObject();
                        Gson gson = new Gson();
                        UploadFileInfo info = gson.fromJson(jobjectStore, UploadFileInfo.class);
                        // 원본데이타 추가..
                        uploadFileResultInfos.add(info);

                        handler.obtainMessage(NEXT_UPLOAD_WORK, "").sendToTarget();

                    }
                }


            } catch (Exception e) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_error_return)).sendToTarget();
                e.printStackTrace();
            }
        }
    };








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
