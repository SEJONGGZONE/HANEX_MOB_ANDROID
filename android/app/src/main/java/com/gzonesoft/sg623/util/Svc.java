package com.gzonesoft.sg623.util;


/**
 * 서비스 관리
 * 샘플 텍스트 : ← → ↓ ↑
 */
public final class Svc {
    public final static String TAG = "SVC";
    public final static String APP_NAME = "SG623-아이사랑";
    public final static String CAMERA_IMG_PATH = "/DCIM/" + APP_NAME;

//    public static String USER_TYPE = UserType._차주; // 사용자 유형 (A:주선사, B:차주)


    public final static String FCM_RECEIVER = "FCM_RECEIVER";
//    public final static String MAKEUP_ROOMDETAIL_RECEIVER = "MAKEUP_ROOMDETAIL_RECEIVER";
//    public final static String INSPECTION_ROOMDETAIL_RECEIVER = "INSPECTION_ROOMDETAIL_RECEIVER";
//    public final static String APM_ROOMSTATUS_LIST_RECEIVER = "APM_ROOMSTATUS_LIST_RECEIVER";

//    public static Config.BuildType buildType = Config.BuildType.RELEASE;
    public static Config.BuildType buildType = Config.BuildType.DEBUG;

    public static String SCHEME = "http";
    public static String SCHEME_HTTP = "http";
    public static String SCHEME_HTTPS = "https";
    public static String POST = "POST";
    public static String GET = "GET";
    // TMAP-KEY
    public static String TMAP_KEY = "l7xx960af40f5f8a45ad921a0af6097ac0e4";
    // API 서버 - 아이사랑WAS
    public static String SERVER_ADDR = "sjwas.gzonesoft.co.kr"; // 서버
    public static int SERVICE_PORT = 32205;

    public static String API_KEY_NAME = "CodyApiKey";
    public static String API_KEY = "1Bb6wI6DnVL8S8LQAbvAO+pSnKXCf7HwQ7GEn+FnrJk=";

    public static String SERVICE_HOST_URL = SCHEME_HTTP + "://" + SERVER_ADDR + ":" + SERVICE_PORT;

    //File Download
    public static String FILE_SAVE_PATH = "/GZONE_CVO/";

    // 이미지파일 다운로드 경로
    public static String SERVER_IMG_ADDR = "geoimgserver.cafe24.com"; // IMG서버
    public static String SERVICE_IMAGE_URL = SCHEME + "://" + SERVER_IMG_ADDR;
    public static String IMG_SERVER_PATH = SERVICE_IMAGE_URL + "/WIZSALE/WIZSALE_043_0043/";

    // 이미지파일 업로드 경로
    public static String IMG_UPLOAD_URL = "api/File/upload/";   // {파일종류}/{파일포맷}
    public static String FILE_TYPE_DELIVERY = "delivery";
    public static String FILE_TYPE_GOODS = "goods";
    public static String FILE_FORMAT_JPG = "jpg";






    // -------------------------------------------------------------------------------------------------------
    // 신규 참고소스(2023.07.21)
    // -------------------------------------------------------------------------------------------------------
    private  static String MOBILE_SERVICE = "cvoapi/DatabaseController/runProcedure/";
    // ------------------------------------------------------------------------
    // 앱 업데이트 버전 체크
    // ------------------------------------------------------------------------
    public final static String ERP_APPCHECK = MOBILE_SERVICE + "ERP_APPCHECK";

    // ------------------------------------------------------------------------
    // 거래처 조회
    // ------------------------------------------------------------------------
    public final static String ERP_CLIENT_SEL = MOBILE_SERVICE + "ERP_CLIENT_SEL";




    // ------------------------------------------------------------------------
    // 인증완료 처리(추천관계정보 업데이트)
    // ------------------------------------------------------------------------
    public static String REQ_PHONEAUTH_FINISH = MOBILE_SERVICE + "REQ_PHONEAUTH_FINISH";

    // ------------------------------------------------------------------------
    // ERP 메인화면 데이타 조회
    // ------------------------------------------------------------------------
    public final static String CVO_MAINDATA_SEL = MOBILE_SERVICE + "CVO_MAINDATA_SEL";

    // ------------------------------------------------------------------------
    // ERP 출근보고
    // ------------------------------------------------------------------------
    public final static String CVO_WORKSTAT_SAV = MOBILE_SERVICE + "CVO_WORKSTAT_SAV";

    // ------------------------------------------------------------------------
    // ERP 배차내역조회
    // ------------------------------------------------------------------------
    public static String ERP_DISPATCH_SEL = MOBILE_SERVICE + "ERP_DISPATCH_SEL";
    // ------------------------------------------------------------------------
    // ERP 배차상품 정보조회
    // ------------------------------------------------------------------------
    public static String ERP_DISPATCH_ITEM_SEL = MOBILE_SERVICE + "ERP_DISPATCH_ITEM_SEL";
    // ------------------------------------------------------------------------
    // ERP 센터업무 - 상품조회
    // ------------------------------------------------------------------------
    public static String ERP_ITEM_MNG_SEL = MOBILE_SERVICE + "ERP_ITEM_MNG_SEL";
    // ------------------------------------------------------------------------
    // ERP 센터업무 - 상품이미지 삭제
    // ------------------------------------------------------------------------
    public static String ERP_ITEMIMAGE_DEL = MOBILE_SERVICE + "ERP_ITEMIMAGE_DEL";


    // ------------------------------------------------------------------------
    // 문자보내기
    // ------------------------------------------------------------------------
    public static String SMS_SEND = MOBILE_SERVICE + "SMS_SEND";


    public final static String API_PKG_PRC_DRIVER = "api/DatabaseController/runProcedure/";




    /**
     * DELI_FINISH_SAV (완료보고)
     */
    public final static String DELI_FINISH_SAV = "deliFinishSav";


    // ------------------------------------------------------------------------
    // CVO API
    // ------------------------------------------------------------------------
    /**
     * 업체코드
     */
//    public final static String CVO_COMPANYCD = "00001"; // 목원푸드
    public final static String CVO_COMPANYCD = "00002"; // 아이사랑
    /**
     * 모바일 인증요청하기
     */
    public final static String REQ_PHONEAUTH = "REQ_PHONEAUTH";

    /**
     * 사용자정보 체크
     */
    public final static String CVO_USERINFO_CHK = "CVO_623_USERINFO_CHK";
    /**
     * 사용자정보List 가져오기
     */
    public final static String CVO_USERINFO_SEL = "CVO_415_USERINFO_SEL";
    /**
     * 배송시작(배차내역 저장)
     */
    public static String CVO_DELIVERY_START = "CVO_415_DELIVERY_START";
    /**
     * 배송정보 조회
     */
    public static String CVO_DELIVERY_SEL = "CVO_415_DELIVERY_SEL";
    /**
     * 차량위치 조회
     */
    public static String CVO_NOWPOSITION_SEL = "CVO_623_NOWPOSITION_SEL";
    /**
     * 이동경로 조회
     */
    public static String CVO_TRACE_SEL = "CVO_415_TRACE_SEL";
    /**
     * 배차일보 조회
     */
    public static String CVO_DISPATCH_SUMMARYT_SEL = "CVO_415_DISPATCH_SUMMARYT_SEL";
    /**
     * 주유비 이력정보 조회
     */
    public static String CVO_GAS_HISTORY_SEL = "CVO_415_GAS_HISTORY_SEL";
    /**
     * 배송(납품)이미지 조회
     */
    public static String CVO_CLIENTIMAGE_SEL = "CVO_623_CLIENTIMAGE_SEL";

    /**
     * 배송(납품)이미지 삭제
     */
    public static String CVO_CLIENTIMAGE_DEL = "CVO_623_CLIENTIMAGE_DEL";
    /**
     * 물류센터 조회
     */
    public static String CVO_623_SPOT_SEL = "CVO_623_SPOT_SEL";
    /**
     * 물류센터 출발보고
     */
    public static String CVO_623_SPOT_START = "CVO_623_SPOT_START";






















    /**
     * 퇴근보고
     */
    public final static String LEAVE_THE_OFFICE = "todayFinish";



    // -------------------------------------------------------------------------------------------------------
    // 이전 참고소스(2023.07.21)
    // -------------------------------------------------------------------------------------------------------

    /**
     * 위치정보 서비스구동 구분 (시작)
     */
    public final static String ACTION_START_LOCATION_SERVICE = "startLocationService";

    /**
     * 위치정보 서비스구동 구분 (종료)
     */
    public final static String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";



}