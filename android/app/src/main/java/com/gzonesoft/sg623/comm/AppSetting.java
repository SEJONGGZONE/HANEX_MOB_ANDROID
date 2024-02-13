package com.gzonesoft.sg623.comm;


import android.location.Location;
import android.os.Build;

import com.gzonesoft.sg623.data.AppChkInfo;
import com.gzonesoft.sg623.data.CvoUserInfo;
import com.gzonesoft.sg623.data.FcmMessageInfo;
import com.gzonesoft.sg623.data.LastWorkInfo;
import com.gzonesoft.sg623.data.LocationInfo;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.util.StringUtil;

import java.util.UUID;

/**
 * 전역 변수 관리
 */
public class AppSetting {
    /**
     * 사용자 Config
     */
    public static class userConfig{

        /**
         * 위치정보 수집대상 여부
         */
        public static String GPS_USE_YN;

        /**
         * 위치정보 수집주기
         */
        public static int GPS_GET_CYCLE;

        /**
         * 위치정보 전송주기
         */
        public static int GPS_SEND_TIME;

    }

    /**
     * 로그인 정보
     */
    public static CvoUserInfo cvoUserInfo;

    public static String PLAY_MODE = CommType._기사;

    /**
     * 앱 정보(버전,업데이트URL 및 GPS전송URL 등..)
     */
    public static AppChkInfo appChkInfo = new AppChkInfo();

    /**
     * Device 정보
     */
    public static class Device {
        public static String DEVICE_ID;
        public static String DEVICE_MODEL = Build.MODEL;
        public static String OS_VERSION = String.format("%d",Build.VERSION.SDK_INT);
        public static String PHONE_NUMBER;      // 01022223333
        //public static String PHONE_NUMBER_MIN;  //  1022223333
        public static String MAC_ADDRESS;
        public static String UUID_STRING = UUID.randomUUID().toString();

    }


    // ----------------------------------------------------------------
    // GPS 정보(현위치)
    // ----------------------------------------------------------------
    public static class GPS_MANAGER {

        public static LocationInfo locationInfo = new LocationInfo();

        public static double GPS_LAT = 0.0;
        public static double GPS_LNG = 0.0;
        public static String GPS_TIME;

        public static double getGpsLat(){
            return GPS_LAT;
        }

        public static double getGpsLng(){
            return GPS_LNG;
        }

        public static String getGpsTime(){
            return StringUtil.getNvlStr(GPS_TIME);
        }
    }

    /**
     * 출근/퇴근 구분 상태
     */
    public static String todayStatus = "";

    /**
     * App 정보
     */
    public static class App {
        // PACKAGE 명
        public static String PACKAGE_NAME;
        // Version Code
        public static int VERSION_CODE;
        // Version Name
        public static String VERSION_NAME;

    }

    /**
     * LastWorkInfo
     */
    public static LastWorkInfo LAST_WORK_INFO;

    /**
     * 현재상태
     */
    public static String workStatus;
    public static String dispatchDate = "";
    public static String dispatchMinNo = "";
    public static String drvCd = "";
    public static String password = "";
    public static String vhclPclCd = "";



















//
//    /**
//     * 코드정보
//     */
//    public static ArrayList<CodeInfo> codeInfos = new ArrayList<CodeInfo>();
//
//    /**
//     * 사용자 정보 - 홈페이지 가입정보
//     */
//    public static MobileUserInfo mobileUserInfo;
//
//
//    /**
//     * 사용자 정보 - 폰번호 기준으로 가입유무/승인/미승인/사용자구분 통합으로 가져옴.(2020-08-14)
//     */
//    public static UserInfoByPhoneNo userInfoByPhoneNo;
//
//    /**
//     * FCM 관련
//     */
//    public static String fcmToken;
//
//    /**
//     * Device 정보
//     */
//    public static class Device {
//        public static String DEVICE_ID;
//        public static String DEVICE_MODEL = Build.MODEL;
//        public static String OS_VERSION;
//        public static String PHONE_NUMBER;
//        public static String MAC_ADDRESS = getDeviceNo();
//
//    }
//
//    /**
//     * App 정보
//     */
//    public static class App {
//        // PACKAGE 명
//        public static String PACKAGE_NAME;
//        // Version
//        public static String VERSION;
//    }
//
//    /**
//     * APP환경정보
//     */
//    public static class Config {
//        // APP 최종버전
//        public static String LAST_VERSION;
//        // APP 업데이트 경로
//        public static String DOWNLOAD_URL;
//    }
//
    /**
     * FCM 수신정보
     */
    public static FcmMessageInfo fcmRecvMessage;
//
//    /**
//     * 화면정보 얻기
//     */
//    public static int actionBarHeight;
//
//    // ----------------------------------------------------------------
//    // MAC address, Model
//    // ----------------------------------------------------------------
//    public static String getDeviceNo() {
//        try {
//            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface nif : all) {
//                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
//
//                byte[] macBytes = nif.getHardwareAddress();
//                if (macBytes == null) {
//                    return "";
//                }
//
//                StringBuilder res1 = new StringBuilder();
//                for (byte b : macBytes) {
//                    res1.append(String.format("%02X",b));
//                }
//
//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length() - 1);
//                }
//                return res1.toString();
//            }
//        } catch (Exception ex) {
//            //handle exception
//        }
//        return "";
//    }
//
//
    // ----------------------------------------------------------------
    // 로그인 화면관련(로그인전)
    // ----------------------------------------------------------------
    public static class Login {
        // 매장ID
        public static String STORE_ID;
        // 매장이름
        public static String STORE_NM;
        // 매장위치-lat
        public static Double STORE_LAT;
        // 매장위치-lon
        public static Double STORE_LON;
        // 차수ID
        public static String SHIFT_ID;
        // 차수명
        public static String SHIFT_NM;
        //
        public static String SDS_SESSION;
    }
//
//    // ----------------------------------------------------------------
//    // 최종업무 상태정보 New -
//    // ----------------------------------------------------------------
//    public static ListMst UserSettingInfos;
//
//    public static class ListMst {
//        public String DELIVERY_DT;
//        public String DELIVERY_TOT_CNT;
//        public String DRIVER_NM;
//        public String DRIVERTEL_TXT;
//        public String END_TM;
//        public String LAST_SHIFT_ID;
//        public String ROUTE_CONFIRM_YN;
//        public String ROUTE_OPTI_YN;
//        public String SHIFT_ID;
//        public String SHIFT_NM;
//        public String START_TM;
//        public String STORE_ID;
//        public String STORE_NM;
//        public String VAN_END_FLAG;
//        public String VAN_END_TIME;
//        public String VAN_ID;
//        public String VAN_START_FLAG;
//        public String VAN_START_TIME;
//    }
//
//    // ----------------------------------------------------------------
//    // 배송지 목록 요청정보
//    // ----------------------------------------------------------------
//    public static class DeliveryListInfo {
//        public static String DELIVERY_DT;
//        public static String SHIFT_ID;
//        public static String STORE_ID;
//        public static String VAN_ID;
//    }
//
//    // ----------------------------------------------------------------
//    // GPS 정보(현위치)
//    // ----------------------------------------------------------------
//    public static class GPS_MANAGER {
////        public static double GPS_LAT;
////        public static double GPS_LNG;
////        public static String GPS_TIME;
//
//        public static LocationInfo locationInfo = new LocationInfo();
//
//        public static double GPS_LAT = 0.0;
//        public static double GPS_LNG = 0.0;
//        public static String GPS_TIME;
//
//        public static double getGpsLat(){
//            return GPS_LAT;
//        }
//
//        public static double getGpsLng(){
//            return GPS_LNG;
//        }
//
//        public static String getGpsTime(){
//            return StringUtil.getNvlStr(GPS_TIME).toString();
//        }
//
//    }
//
    public static Location CUR_LOCATION;
    public static int WORKING_DELIVERY_POSITION;
//
//    // ----------------------------------------------------------------
//    // 최종 보고정보
//    // ----------------------------------------------------------------
//    public static class LastReport {
//
//        public static String ROUTE_IDX;
//        public static String ORDER_NO;
//        public static String REPORT_TYPE;
//        public static String LAT;
//        public static String LNG;
//
//        // REPORT_TYPE 유형 및 정의..(현업 협의후 확정 대기 - 2017.08.08)
//        public static final String _점포출발 = "SS";      // Store Start
//        public static final String _배송지도착 = "DA";    // Delivery Arrive
//        public static final String _배송완료 = "DE";     // Delivery End
//        public static final String _SKIP보고 = "DS";     // Delivery Skip
//        public static final String _배송지에가지않음 = "DN";     // Delivery Not
//        public static final String _다음배송지출발 = "DD"; // Delivery Departure
//        public static final String _점포도착 = "SA";    // Store Arrive
//    }

}
