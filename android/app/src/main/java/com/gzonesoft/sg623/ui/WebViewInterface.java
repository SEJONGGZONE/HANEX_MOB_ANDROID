package com.gzonesoft.sg623.ui;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.gzonesoft.sg623.util.Loggers;

public class WebViewInterface {

    private WebView mAppView;
    private Activity mContext;





    /**
     * 생성자.
     * @param activity : context
     * @param view : 적용될 웹뷰
     */
    public WebViewInterface(Activity activity, WebView view) {
        mAppView = view;
        mContext = activity;
    }


    /**
     * 안드로이드 토스트를 출력한다. Time Long.
     * @param message : 메시지
     */
    @JavascriptInterface
    public void toastLong (String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
    /**
     * 안드로이드 토스트를 출력한다. Time Short.
     * @param message : 메시지
     */
    @JavascriptInterface
    public void toastShort (String message) { // Show toast for a short time
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

//    @JavascriptInterface
//    public void setMessage(final String arg) {
//        handler.post(new Runnable() {
//            public void run() {
//                Loggers.d("[JavascriptInterface] 받은 메시지 : " + arg);
//                try {
//                    String sSendMsg = "";
//                    if (arg.equals("운행시작")) {
//                        sSendMsg = ((MainActivity) MainActivity.mContext).gpsOnByWeb();
//                        sSendMsg = "javascript:ReceiveData(1,'" + sSendMsg + "');";
//                    } else if (arg.equals("운행종료")) {
//                        sSendMsg = ((MainActivity) MainActivity.mContext).gpsOffByWeb();
//                        sSendMsg = "javascript:ReceiveData(0,'" + sSendMsg + "');";
//                    } else if (arg.equals("requestOtpNo")) {
//
//                    }
//                    // 위치서비스가 켜진/꺼진 시간정보 리턴, 없으면 오류.
//                    if (!sSendMsg.equals("")) {
//                        // 웹페이지에 반영
//                        Loggers.d("[JavascriptInterface] 보내는 메시지 : " + sSendMsg);
//                        mAppView.loadUrl(sSendMsg);
//                    } else {
//                        Toast.makeText(mContext, "위치서비스 활성화에 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

//    public TossPayResultInfo mPaymentResult = null;

    /**
     * 상품이미지 삭제요청
     * @param itcode
     * @param type
     * @param seq
     */
    @JavascriptInterface
    public void deleteImage(final String itcode, final String type, final String seq) {
        try {
            Loggers.d("itcode=\" + itcode + \", type=\" + type + \", seq=" + seq);
            // 삭제여부 팝업 호축
            ((BoardActivity) BoardActivity.mContext).popItemImageDelete(itcode, type, seq);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @JavascriptInterface
    public void deleteDeliveryImage(final String ddate, final String clcode, final String type, final String seq) {
        try {
            Loggers.d("ddate=\" + ddate + \", clcode=\" + clcode + \", type=\" + type + \", seq=" + seq);
            // 삭제여부 팝업 호축
            ((BoardActivity) BoardActivity.mContext).popDeliveryImageDelete(ddate, clcode, type, seq);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }




    @JavascriptInterface
    public void fromMobileMessage(final String jsonData) {

        Loggers.d("[fromMobileMessage] 받은 데이타 : " + jsonData);



//        try {
//            JsonElement jelement = new JsonParser().parse(jsonData);
//            JsonObject jObject = jelement.getAsJsonObject();
//
//
//
//            if (jObject.has("status")) {
////                // 성공시..
////                String status = jObject.get("status").getAsString();
////                String paymentKey = jObject.get("paymentKey").getAsString();
////                String orderId = jObject.get("orderId").getAsString();
////                String orderName = jObject.get("orderName").getAsString();
////                String card = jObject.get("card").getAsString(); // 하위자료있음..
////                String secret = jObject.get("secret").getAsString();
////                String receipt = jObject.get("receipt").getAsString(); // 영수증
////                String totalAmount = jObject.get("totalAmount").getAsString(); // 결제금액
////                String suppliedAmount = jObject.get("suppliedAmount").getAsString(); // 공급가
////                String vat = jObject.get("vat").getAsString(); // 부가세
////                String taxFreeAmount = jObject.get("taxFreeAmount").getAsString(); // 면세금액
////                String method = jObject.get("method").getAsString(); // 결제방법
////                String version = jObject.get("version").getAsString(); // 버전
////                String orgData = jsonData; // 원본데이타..
//
//                // 데이타 추가..
//                Gson gson = new Gson();
//                mPaymentResult = gson.fromJson(jObject, TossPayResultInfo.class);
//                mPaymentResult.setOrgData(jsonData); // 원본데이타 저장..
//
//                Loggers.d("[fromMobileMessage] 결제키 = "+ mPaymentResult.getPaymentKey());
//                Loggers.d("[fromMobileMessage] 결제카드번호 = "+ mPaymentResult.getCard().getNumber());
//
//                // 결제성공, 결제정보 저장..
//                ((WizSujuListActivity) WizSujuListActivity.mContext).popOpenYN = true; // 중복조회 하지않기위해서..결제저장후 자동재조회로직과 onResume()이벤트에서의 자동조회가 중복되었다..
//                ((WizSujuListActivity) WizSujuListActivity.mContext).paymentSave(mPaymentResult);
//
//            } else {
//                // 실패시..
//                String code = jObject.get("code").getAsString();
//                String message = jObject.get("message").getAsString();
//                Loggers.d("[fromMobileMessage] 실패 데이타-code="+code);
//                Loggers.d("[fromMobileMessage] 실패 데이타-message="+message);
//
//                CommonUtil.with().ToastMsg(((WizSujuListActivity) WizSujuListActivity.mContext), message , Toast.LENGTH_SHORT).show();
//                // 결제WEB 화면 닫기
//                ((BoardActivity) BoardActivity.mContext).FinishWork();
//            }
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        handler.post(new Runnable() {
//            public void run() {
//
//                try {
//                    JsonElement jelement = new JsonParser().parse(jsonData);
//                    JsonObject obj = jelement.getAsJsonObject();
//                    String WORK_ID = StringUtil.getRemoveQuota(obj.get("WORK_ID").toString());
//                    Loggers.d("[fromMobileMessage] WORK_ID =  " + WORK_ID);
//
//
//                    if (WORK_ID.equals(MobileWebWorkDomain.CALL_PHONE)) {
//                        // 전화걸기
//                        callPhone(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.SET_VERIFY_USERINFO)) {
//                        // 사용자 인증정보 전달 + 메뉴구성
//                        setVerifyUserInfo(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.WAITING_START)) {
//                        // 브로드캐스트 - 대기화면 표시
//                        Intent intent =  new Intent(BroadCastDomain.BROADCAST_SDC2CHROOT);
//                        intent.putExtra(BroadCastDomain.WEB_ACTION, BroadCastDomain.START_PAGE);
//                        mContext.sendBroadcast(intent);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.WAITING_STOP)) {
//                        // 브로드캐스트 - 대기화면 표시
//                        Intent intent =  new Intent(BroadCastDomain.BROADCAST_SDC2CHROOT);
//                        intent.putExtra(BroadCastDomain.WEB_ACTION, BroadCastDomain.END_PAGE);
//                        mContext.sendBroadcast(intent);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.REQUEST_OTP)) {
//                        // OTP 인증 요청
//                        requestOtp(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.SUCCESS_OTP)) {
//                        // OTP 성공 처리
//                        successOtp(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.DRIVE_START)) {
//                        // 운행시작 처리
//                        driveStart(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.DRIVE_STOP)) {
//                        // 운행종료 처리
//                        driveStop(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.RELOAD_PAGE)) {
//                        // 브로드캐스트 - 페이지 리로드
//                        Intent intent =  new Intent(BroadCastDomain.BROADCAST_SDC2CHROOT);
//                        intent.putExtra(BroadCastDomain.WEB_ACTION, BroadCastDomain.RELOAD_PAGE);
//                        mContext.sendBroadcast(intent);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.LOAD_WORK_START_PAGE)) {
//                        // 브로드캐스트 - 초기 페이지로드
//                        Intent intent =  new Intent(BroadCastDomain.BROADCAST_SDC2CHROOT);
//                        intent.putExtra(BroadCastDomain.WEB_ACTION, BroadCastDomain.LOAD_WORK_START_PAGE);
//                        mContext.sendBroadcast(intent);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.REQUEST_MESSAGELIST)) {
//                        String CALLBACK_FUNC = StringUtil.getRemoveQuota(obj.get("CALLBACK_FUNC").toString());
//                        // 메시지 리스트 요청
//                        sendMessageList(CALLBACK_FUNC);
//                        // 메시지 읽음 처리 요청
//                        MessageDBManager.getInstance(mContext).updateReadMessageAll();
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.DELETE_MESSAGE)) {
//                        try {
//                            String dataJsonString = obj.get("DATA").toString();
//                            JsonElement dataJelement = new JsonParser().parse(dataJsonString);
//                            JsonObject dataObj = dataJelement.getAsJsonObject();
//                            int deleteId = Integer.valueOf(dataObj.get("deleteId").toString());
//
//                            String CALLBACK_FUNC = StringUtil.getRemoveQuota(obj.get("CALLBACK_FUNC").toString());
//
//                            // 메시지 삭제 요청
//                            deleteMessageList(deleteId, CALLBACK_FUNC);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            deleteMessageList(-1,"삭제 요청데이타 추출중 오류발생");
//                        }
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.DELETEALL_MESSAGE)) {
//                        String CALLBACK_FUNC = StringUtil.getRemoveQuota(obj.get("CALLBACK_FUNC").toString());
//                        // 전체 메시지 삭제 요청
//                        deleteAllMessageList(CALLBACK_FUNC);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.EXECUTE_TMAP)) {
//
//                        String dataJsonString = obj.get("DATA").toString();
//                        JsonElement dataJelement = new JsonParser().parse(dataJsonString);
//                        JsonObject dataObj = dataJelement.getAsJsonObject();
//                        String name = StringUtil.getRemoveQuota(String.valueOf(dataObj.get("name").toString()));
//                        String lat = String.valueOf(dataObj.get("lat").toString());
//                        String lng = String.valueOf(dataObj.get("lng").toString());
//
//
//                        // 외부앱 실행
//                        RunTmap(name, lat, lng);
////                        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage("com.skt.tmap.ku");
//////                        launchIntent.putExtra("invokeRoute", "rGoName=ABC&rGoY=37.50861147&rGoX=126.8911457");
////                        launchIntent.putExtra("rGoName", "경기도 고양시 일산동구 백석2동");
////                        launchIntent.putExtra("rGoY", "37.50861147");
////                        launchIntent.putExtra("rGoX", "126.8911457");
////                        mContext.startActivity(launchIntent);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.LAST_ALLOCATEDGROUPID)) {
//                        // 최종보고된 아이디 전달
//                        retLastRepAllocId(obj);
//                    } else if (WORK_ID.equals(MobileWebWorkDomain.UPDATE_LAST_ALLOCATEDGROUPID)) {
//                        String dataJsonString = obj.get("DATA").toString();
//                        JsonElement dataJelement = new JsonParser().parse(dataJsonString);
//                        JsonObject dataObj = dataJelement.getAsJsonObject();
//                        String allocatedGroupId = StringUtil.getRemoveQuota(String.valueOf(dataObj.get("allocatedGroupId").toString()));
//
//                        // 최종보고된 배차번호 업데이트
//                        SettingPref.with(mContext).savePrefWithString("last_allocatedGroupId", allocatedGroupId);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    /**
     * 웹에 전화번호를 전달한다.
     */
    public void sendPhoneNo(String phoneNo) {

//        String sSendMsg = "javascript:PAGE_USER_VERIFY.setPhoneNo('" + Svc.DEV_PHONE_NO + "');";
//        mAppView.loadUrl(sSendMsg);
    }
//
//    /**
//     * 웹에 전화번호를 전달한다.
//     */
//    public void sendAuthNo(String authNo) {
//        try {
//            Loggers.d("sendAuthNo = " + authNo);
//            String sSendMsg = "javascript:PAGE_USER_VERIFY.setSmsAuthNo('" + authNo + "');";
//            mAppView.loadUrl(sSendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 웹에 약관 타이틀과 본문을 전달한다.
//     */
//    public void setVerifyInfo() {
//        String privateTitle = TosInfoManager.getInstance().getPrivateTitle();
//        String privateContents = TosInfoManager.getInstance().getPrivateContents();
//        String locationTitle = TosInfoManager.getInstance().getLocationTitle();
//        String locationContents = TosInfoManager.getInstance().getLocationContents();
//
//        String info = "";
//        info = "{";
//        info += "\"privateTitle\" : \"" + privateTitle + "\",";
//        info += "\"privateContents\" : \"" + StringUtil.getRemoveQuota(privateContents.replace("\"", "'")) + "\",";
//        info += "\"locationTitle\" : \"" + locationTitle + "\",";
//        info += "\"locationContents\" : \"" + StringUtil.getRemoveQuota(locationContents.replace("\"", "'")) + "\"";
//        info += "}";
//        Loggers.d("약관 = " + info);
//        Loggers.d("privateContents = " + privateContents);
//        Loggers.d("locationContents = " + locationContents);
//        String sSendMsg01 = "javascript:PAGE_USER_VERIFY.setVerifyInfo01("+privateContents+");";
//        mAppView.loadUrl(sSendMsg01);
//
//        String sSendMsg02 = "javascript:PAGE_USER_VERIFY.setVerifyInfo02("+locationContents+");";
//        mAppView.loadUrl(sSendMsg02);
//    }
//
//    public void loadUrl(String url) {
//        mAppView.loadUrl(url);
//    }
//    /**
//     * 웹에 OTP저장정보를 전달한다.
//     * @param infoString OTP저장정보
//     */
//    public void sendOtpSaveInfo(String infoString) {
//        String sSendMsg = "javascript:PAGE_USER_VERIFY.setOtpSaveInfo('" + infoString + "');";
//        mAppView.loadUrl(sSendMsg);
//    }
//    /**
//     * [공통] 웹에 사용자 정보를 전달한다.
//     */
//    public void maiv_user_sendUserInfo() {
//
//        // APP 정보 전달
//        String phoneNo = Svc.DEV_PHONE_NO; //ActivityUtils.getPhoneNumber(mContext);
////        String phoneNo = ActivityUtils.getPhoneNumber(mContext);
//
//        String token = "";
//        if (Svc.OTP_TOKEN.isEmpty()) {
//            token = TokenVerifyManager.getInstance().getSvcToken();
//        } else {
//            token = Svc.OTP_TOKEN;
//        }
//
//        String refreshToken = "";
//        if (Svc.OTP_REFRESH_TOKEN.isEmpty()) {
//            refreshToken = TokenVerifyManager.getInstance().getSvcRefreshToken();
//        } else {
//            refreshToken = Svc.OTP_REFRESH_TOKEN;
//        }
//
//        String verify_link_auth_token = TokenVerifyManager.getInstance().getLINK_AUTH_TOKEN();
//        String verify_link_app_token = TokenVerifyManager.getInstance().getLINK_APP_TOKEN();
//        String verify_link_self = TokenVerifyManager.getInstance().getLINK_self();
//        String verify_link_tos = TokenVerifyManager.getInstance().getLINK_TOS();
//        String tos_link_otp = TosInfoManager.getInstance().getLink_otp();
//        String tos_link_self = TosInfoManager.getInstance().getLink_self();
//        String msgCnt = String.valueOf(MessageDBManager.getInstance(mContext).getAllNewMessage());
//
//        String infoString = "{";
//        infoString += " \"phoneNo\":\"" + phoneNo + "\"";
//        infoString += ",\"token\":\"" + token + "\"";
//        infoString += ",\"refreshToken\":\"" + refreshToken + "\"";
//        infoString += ",\"verify_link_auth_token\":\"" + verify_link_auth_token + "\"";
//        infoString += ",\"verify_link_app_token\":\"" + verify_link_app_token + "\"";
//        infoString += ",\"verify_link_self\":\"" + verify_link_self + "\"";
//        infoString += ",\"verify_link_tos\":\"" + verify_link_tos + "\"";
//        infoString += ",\"tos_link_otp\":\"" + tos_link_otp + "\"";
//        infoString += ",\"tos_link_self\":\"" + tos_link_self + "\"";
//        infoString += ",\"msgCnt\":\"" + msgCnt + "\"";
//        infoString += "}";
//
//        Loggers.d("[WebViewInterface] maiv_user_sendUserInfo = " + infoString);
//
//        String sSendUrl = "javascript:MAIV_USER.setVerifyUserInfo('" + infoString + "');";
//        mAppView.loadUrl(sSendUrl);
//    }
//
//
//
//    /**
//     * 앱 및 기기정보 취득
//     */
//    private TosSaveInfo getTosSaveInfo() {
//        TosSaveInfo info = new TosSaveInfo();
//        try {
//            String packageName = mContext.getPackageName();
//            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
//
//
//            Loggers.d("==== packageName =" + packageName);  // kr.co.aivision.talk_lbs
//            Loggers.d("==== versionName =" + packageInfo.versionName);  // 1.0.1
//            Loggers.d("==== versionCode =" + packageInfo.versionCode);  // 101
//
//            String mdn = Svc.DEV_PHONE_NO; //ServiceUtils.getPhoneNumber(mContext);
////            String mdn = ServiceUtils.getPhoneNumber(mContext);
//            String versionName = String.valueOf(packageInfo.versionName);
//            String[] versionInfo = versionName.split("\\.");
//            String majorVer = versionInfo[0];
//            String minorVer = versionInfo[1];
//
//            info.setAppBuildVer(Build.VERSION.SDK_INT + "");
//            info.setAppMajorVer(majorVer);
//            info.setAppMinorVer(minorVer);
////            info.setAppTkn(TokenVerifyManager.getInstance().getSvcToken());
//
//            // 다시 받아온다.
//            Svc.HASH_KEY = MyFirebaseInstanceIDService.refreshToken();
//
//            // FCM토큰을 못바아 온 경우...
//            if (Svc.HASH_KEY == null) {
//                // 다시 받아온다.
//                Svc.HASH_KEY = MyFirebaseInstanceIDService.refreshToken();
//            }
//            info.setAppTkn(Svc.HASH_KEY);
//
//            info.setModel(Build.MODEL);
//            info.setOsNm("Android");
//            info.setOsVer(Build.VERSION.RELEASE);
//            info.setOtp(TosInfoManager.getInstance().getOtp());
//            info.setPhoneNo(mdn);
//            info.setTelCo(ServiceUtils.getTelecomName(mContext));
//            info.setTosIds(TosInfoManager.getInstance().getStringTosIds());
//
//        } catch (PackageManager.NameNotFoundException e) {
//            Loggers.d("[getTosSaveInfo-Exception]" + e.getMessage());
//        } finally {
//            Loggers.d("[getTosSaveInfo] finally");
//        }
//        return info;
//    }
//
//
//    /**
//     * [fromMobileMessage]전화걸기
//     * @param obj JSON 객체
//     */
//    private void callPhone(JsonObject obj) {
//        // 콜백정보를 추출..
//        String DATA = StringUtil.getRemoveQuota(obj.get("DATA").toString());
//        Loggers.d("[fromMobileMessage] DATA =  " + DATA);
//        JsonElement jelement_data = new JsonParser().parse(DATA);
//        JsonObject jobject_data = jelement_data.getAsJsonObject();
//        String mdn = StringUtil.getRemoveQuota(jobject_data.get("mdn").toString());
//
//        Loggers.d("[fromMobileMessage] mdn =  " + mdn);
//        // 전화걸기 시도...
//        mContext.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + mdn)));
//    }
//
//    /**
//     * [fromMobileMessage]사용자 인증정보 전달 + 메뉴구성
//     * @param obj JSON 객체
//     */
//    private void setVerifyUserInfo(JsonObject obj) {
//        // 사용자 인증정보 전달
//        maiv_user_sendUserInfo();
//
//        // 콜백정보를 추출하여 관련 메뉴구성..
//        String DATA = StringUtil.getRemoveQuota(obj.get("DATA").toString());
//        Loggers.d("[fromMobileMessage] DATA =  " + DATA);
//        // callBackFunc 정보 저장
//        JsonElement jelement_data = new JsonParser().parse(DATA);
//        JsonObject jobject_data = jelement_data.getAsJsonObject();
//        String callBackFunc = StringUtil.getRemoveQuota(jobject_data.get("callBackFunc").toString());
//        Loggers.d("[fromMobileMessage] callBackFunc =  " + callBackFunc);
//        String sSendUrl = "javascript:" + callBackFunc;
//        mAppView.loadUrl(sSendUrl);
//    }
//
//    /**
//     * [fromMobileMessage]OTP 인증 요청
//     * @param obj JSON 객체
//     */
//    private void requestOtp(JsonObject obj) {
//        String DATA = StringUtil.getRemoveQuota(obj.get("DATA").toString());
//        Loggers.d("[fromMobileMessage] DATA =  " + DATA);
//
//        Loggers.d("[fromMobileMessage] OTP 번호 요청");
//        // OTP 번호 저장
//        JsonElement jelement_data = new JsonParser().parse(DATA);
//        JsonObject jobject_data = jelement_data.getAsJsonObject();
//        Loggers.d("[fromMobileMessage] otp =  " + jobject_data.get("otp").toString());
//        TosInfoManager.getInstance().setOtp(StringUtil.getRemoveQuota(jobject_data.get("otp").toString()));
//
//        // TOS 저장정보 생성
//        TosSaveInfo info = getTosSaveInfo();
//        Loggers.d("[fromMobileMessage] TosSaveInfo = " + info.toJsonString());
//        sendOtpSaveInfo(info.toJsonString());
//    }
//
//    /**
//     * [fromMobileMessage]OTP 성공 처리
//     * @param obj JSON 객체
//     */
//    private void successOtp(JsonObject obj) {
//        String DATA = obj.get("DATA").toString();
//        Loggers.d("[fromMobileMessage] DATA =  " + DATA);
//        Loggers.d("[fromMobileMessage] 약관동의 완료");
//
//        // 브로드캐스트 - 대기화면 표시
//        Intent intent =  new Intent(BroadCastDomain.BROADCAST_SDC2CHROOT);
//        intent.putExtra(BroadCastDomain.WEB_ACTION, BroadCastDomain.START_PAGE);
//        mContext.sendBroadcast(intent);
//
//        // {"self":{"href":"http://210.126.128.181:8080/m/user/tos/save"}}
//
//        // self 추출..
//        JsonElement jelement_self = new JsonParser().parse(DATA);
//        JsonObject jobject_self = jelement_self.getAsJsonObject();
//        Loggers.d("[fromMobileMessage] token =  " + jobject_self.get("token").toString());
//        Loggers.d("[fromMobileMessage] refreshToken =  " + jobject_self.get("refreshToken").toString());
//        Loggers.d("[fromMobileMessage] _links =  " + jobject_self.get("_links").toString());
//
//        String token = StringUtil.getRemoveQuota(jobject_self.get("token").toString());
//        String refreshToken = StringUtil.getRemoveQuota(jobject_self.get("refreshToken").toString());
//        String _links = StringUtil.getRemoveQuota(jobject_self.get("_links").toString());
//
//        Loggers.d("[fromMobileMessage] 로컬영역에 저장함"); // 앱을 종료해도 남아있고, 지우기전까지 유지됨.
//        SettingPref.with(mContext).savePrefWithString("OTP_TOKEN", token);
//        SettingPref.with(mContext).savePrefWithString("OTP_REFRESH_TOKEN", refreshToken);
//        SettingPref.with(mContext).savePrefWithString("OTP_LINKS", _links);
//        //
//        // OTP인증 정보 획득 - 기존인증정보 확인
//        Svc.OTP_TOKEN = SettingPref.with(mContext).loadPref("OTP_TOKEN", "");
//        Svc.OTP_REFRESH_TOKEN = SettingPref.with(mContext).loadPref("OTP_REFRESH_TOKEN", "");
//        Svc.OTP_LINKS = SettingPref.with(mContext).loadPref("OTP_LINKS", "");
//        Svc.HASH_KEY = MyFirebaseInstanceIDService.refreshToken();
//
//        Loggers.d("[fromMobileMessage] 토큰매니저 반영");
//        TokenVerifyManager.getInstance().setSvcToken(token);
//        TokenVerifyManager.getInstance().setSvcRefreshToken(Svc.OTP_REFRESH_TOKEN);
////                        TokenVerifyManager.getInstance().setLinkInfos(_links);
//
//        Loggers.d("[fromMobil] 사용자 인증 재확인");
//        TokenManager.with(mContext).requestUserVerify(Svc.URL_USER_VERIFY, TokenVerifyManager.getInstance().getSvcToken());
//    }
//
//    /**
//     * [fromMobileMessage] 운행시작 처리
//     * @param obj JSON 객체
//     */
//    private void driveStart(JsonObject obj) {
//        String sSendMsg = ((MainActivity) MainActivity.mContext).gpsOnByWeb();  // 성공시 시간정보가 리턴됨.
//        sSendMsg = "javascript:INDEX_PAGE.ReceiveData('" + MobileWebWorkDomain.DRIVE_START + "','" + sSendMsg + "');";
//        mAppView.loadUrl(sSendMsg);
//    }
//
//    /**
//     * [fromMobileMessage] 운행종료 처리
//     * @param obj JSON 객체
//     */
//    private void driveStop(JsonObject obj) {
//        String sSendMsg = ((MainActivity) MainActivity.mContext).gpsOffByWeb(); // 성공시 시간정보가 리턴됨.
//        sSendMsg = "javascript:INDEX_PAGE.ReceiveData('" + MobileWebWorkDomain.DRIVE_STOP + "','" + sSendMsg + "');";
//        mAppView.loadUrl(sSendMsg);
//    }
//
//    /**
//     * 메시지 정보 업데이트
//     */
//    public  void setMessageInfo() {
//        // 메시지 디비 가져오기
//        int nCount = MessageDBManager.getInstance(mContext).getAllNewMessage();
//        String sSendMsg = "javascript:sessionStorage.messageCnt=" + String.valueOf(nCount) + ";";
//        mAppView.loadUrl(sSendMsg);
//    }
//
//    public void requestCurrent() {
//        String requestUrl = "javascript:MAIV_MENU.requestCurrentNative();";
//        mAppView.loadUrl(requestUrl);
//
//
//    }
//
//    /**
//     * 메시지 리스트 디비를 가져와서 웹의 콜백함수로 전달해준다.
//     * @param callbackFunc 콜백함수명
//     */
//    public  void sendMessageList(String callbackFunc) {
//        // 메시지 디비 가져오기
//        int nMessageCount = MessageDBManager.getInstance(mContext).getAllMessage();
//        String jsonStr = "[";
//        for (int i=0; i<nMessageCount; i++) {
//            RecvMessage info = MessageDBManager.getInstance(mContext).get(i);
//            try {
//                int id = info.getId();
//                String message = info.getMessage();
//                String recvDate = info.getRecvDate();
//                // 메시지 데이타 작성
//                String infoString = "{";
//                infoString += " \"id\":" + id + "";
//                infoString += ",\"message\":\"" + message + "\"";
//                infoString += ",\"recvDate\":\"" + recvDate + "\"";
//                infoString += "}";
//                if (i == 0) {
//                    jsonStr += infoString;
//                } else {
//                    jsonStr += "," + infoString;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } // end for loop
//        jsonStr += "]";
//
//        // 메시지건수 전달(세션값반영)
//        setMessageInfo();
//
//        // WEB에 전달..
//        String sSendMsg = "javascript:" + callbackFunc + "('" + jsonStr + "');";
//        mAppView.loadUrl(sSendMsg);
//    }
//
//    /**
//     * 메시지 삭제(단일건)
//     * @param callbackFunc 콜백함수
//     */
//    public void deleteMessageList(int id, String callbackFunc) {
//        if (id == -1) {
//            String sSendMsg = "javascript:alert(" + callbackFunc + ");";
//            mAppView.loadUrl(sSendMsg);
//            return;
//        } else {
//            try {
//                // 메시지 삭제
//                MessageDBManager.getInstance(mContext).removeMessageData(id);
//                // WEB에 전달..
//                String sSendMsg = "javascript:" + callbackFunc + "();";
//                mAppView.loadUrl(sSendMsg);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 전체 메시지 삭제(테이블삭제)
//     * @param callbackFunc
//     */
//    public void deleteAllMessageList(String callbackFunc) {
//        try {
//            // 테이블삭제
//            MessageDBManager.getInstance(mContext).deleteMessageTable();
//            // WEB에 전달..
//            String sSendMsg = "javascript:" + callbackFunc + "();";
//            mAppView.loadUrl(sSendMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 티맵 실행
//     * @param name 목적지 이름
//     * @param fX 좌표X
//     * @param fY 좌표Y
//     */
//    public void RunTmap(String name, String lat, String lng) {
//        try {
//            TMapTapi tmaptapi = new TMapTapi(mContext);
//            // TMAP 설치여부 확인...
////            boolean bInstalled = tmaptapi.isTmapApplicationInstalled();
////            if (bInstalled) {
//                // 설치됨..(설치여부를 확인하는
//                tmaptapi.setSKTMapAuthentication (Svc.TMAP_KEY);
//                tmaptapi.invokeRoute(name, Float.valueOf(lat), Float.valueOf(lng));
////            } else {
////                // 미설치됨...
////                Loggers.d("설치유도...");
////                Intent i = new Intent(Intent.ACTION_VIEW);
////                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.skt.tmap.ku"));
////                mContext.startActivity(i);
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 최종 보고된 배차번호를 확인후, 배차확정보고 요청
//     * @param obj
//     */
//    private void retLastRepAllocId(JsonObject obj) {
//
//        // 기존 보고된 배차키값 읽기
//        String last_allocatedGroupId = SettingPref.with(mContext).loadPref("last_allocatedGroupId", "");
//
//        // 사용자 인증정보 전달
//        maiv_user_sendUserInfo();
//
//        // 콜백정보를 추출하여 관련 메뉴구성..
//        String DATA = StringUtil.getRemoveQuota(obj.get("DATA").toString());
//        Loggers.d("[fromMobileMessage] DATA =  " + DATA);
//        // callBackFunc 정보 저장
//        JsonElement jelement_data = new JsonParser().parse(DATA);
//        JsonObject jobject_data = jelement_data.getAsJsonObject();
//        String callBackFunc = StringUtil.getRemoveQuota(jobject_data.get("callBackFunc").toString());
//        Loggers.d("[fromMobileMessage] callBackFunc =  " + callBackFunc);
//        String sSendUrl = "javascript:" + callBackFunc + "(" + last_allocatedGroupId + ")";
//        mAppView.loadUrl(sSendUrl);
//    }


}