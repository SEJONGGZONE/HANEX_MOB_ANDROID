package com.gzonesoft.sg623.util;

/**
 * Created by brian on 2017-03-23.
 */

public class ConstValue {

    // REPORT_TYPE 유형 및 정의..(현업 협의후 확정 대기 - 2017.08.08)
    public static final String _점포출발 = "SS";      // Store Start
    public static final String _배송지도착 = "DA";    // Delivery Arrive
    public static final String _배송완료 = "DE";     // Delivery End
    public static final String _다음배송지출발 = "DD"; // Delivery Departure
    public static final String _점포도착 = "SA";    // Store Arrive
    // ----------------------------------------------------------------
    // 로그인 화면관련(로그인전)
    // ----------------------------------------------------------------
    public static enum Login {
        SERVICE_AGREEMENT_YN,
        SERVICE_AGREEMENT_TIME,
        GPS_AGREEMENT_TIME,
        STORE_ID,
        STORE_NM,
        STORE_LAT,
        STORE_LON,
        STORE_REPORT_YN,
        SHIFT_ID,
        SHIFT_NM,
        NOTI_CHK,
        SDS_SESSION;

        public int toOrdianl() {
            return ordinal();
        }

        public String toString() {
            return name();
        }

        public static Login toValue(String value) {
            try {
                return valueOf(value);
            } catch (Exception e) {
            }
            return null;
        }
    }


    // ----------------------------------------------------------------
    // 사용자정보
    // ----------------------------------------------------------------
    public static enum UserSettingInfos {
        FCM_TOKEN,
        AUTH_PHONE_NO,
        // --
        LOGIN_ID,
        PASSWORD,
        NOTICE_READ_DATE,
        USER_TYPE,
        DPID,
        TRECODE,
        ALERT_FINISH_YN,
        AUTH_POP_YN,
        AGREE_YN,
        TTS_YN,
        TREINFO_AMOUNT,
        // 2020-09-28 추가
        CLCODE,  // 1000008
        JOIN_STATUS,    // 0,1,2,3
        START_TYPE,  // 10,20,30,40,99
        EFFECT_YN,
        AGREEDATE,
        JUSUN_TELNO,
        PHONE_NO
    }

    // ----------------------------------------------------------------
    // 최종 상태정보
    // ----------------------------------------------------------------
    public static enum LastWorkStat {
        AUTH_POP_YN,
        AUTH_PHONE_NO,
        DDATE,
        CVO_DISPATCH_ID,
        LOGIN_ID,
        PASSWORD,
        LAST_REPORT_POP_YN,
        LAST_DISPATCH_ID,
        LAST_DELI_NO,
        LAST_ROUTE_SEQ,
        LAST_STOP_SEQ,
        LAST_DEST_CD,
        LAST_INVOICE_RESULT_URL,
        LAST_REASON_TYPE,
        GPS_REPORT_START,
        WORK_FINISH,
        VAN_START_TIME
    }

    // ----------------------------------------------------------------
    // 개발관련
    // ----------------------------------------------------------------
    public static enum DebugStat {
        VEHICLE_CD
    }


//    public final static int DEFAULT_MAP_ZOOM = 12;
//
//    public final static int VEHICLE_PATH_COLOR = 0x990665fb;
//    public final static float VEHICLE_PATH_WIDTH = 10f;
//
//    public final static int RADIUS_STROKE = 0x990665fb;
//    public final static int RADIUS_SHADE = 0x990665fb;
//    public final static float RADIUS_WIDTH = 5f;
//
//    public final static int RANGE_DEFAULT = 10000;
//    private static String[] RANGES;
//
//    public final static int COUNT_DEFAULT = 10;
//    private static int[] COUNTS;
//
//    // REPORT_TYPE 유형 및 정의..(현업 협의후 확정 대기 - 2017.08.08)
//    public static final String _점포출발 = "SS";      // Store Start
//    public static final String _배송지도착 = "DA";    // Delivery Arrive
//    public static final String _배송완료 = "DE";     // Delivery End
//    public static final String _다음배송지출발 = "DD"; // Delivery Departure
//    public static final String _점포도착 = "SA";    // Store Arrive
//
////    public static void setRanges(Context ctx) {
////        RANGES = ctx.getResources().getStringArray(R.array.pref_range_values);
////
////        Loggers.d("range size: " + RANGES.length);
////    }
////
////    public static void setCounts(Context ctx) {
////        COUNTS = ctx.getResources().getIntArray(R.array.pref_count_values);
////        Loggers.d("count size: " + COUNTS.length);
////
////    }
//
//    public static int getRangeValue(int idx) {
//        if (idx >= 0 && RANGES.length > idx) {
//            try {
//                return Integer.parseInt(RANGES[idx]);
//            } catch (Exception e) {
//
//            }
//        }
//        return RANGE_DEFAULT;
//    }
//
//    public static int getRangeIndex(int range) {
//        if (RANGES != null) {
//            for (int idx = 0; idx < RANGES.length; ++idx) {
//                try {
//                    if (Integer.parseInt(RANGES[idx]) == range)
//                        return idx;
//                } catch (Exception e) {
//                }
//            }
//        }
//        return 0;
//    }
//
//    public static int getCountValue(int idx) {
//        if (idx >= 0 && COUNTS != null && COUNTS.length > idx)
//            return COUNTS[idx];
//
//        return COUNT_DEFAULT;
//    }
//
//    public static int getCountIndex(int count) {
//        if (COUNTS != null) {
//            for (int idx = 0; idx < COUNTS.length; ++idx) {
//                if (COUNTS[idx] == count)
//                    return idx;
//            }
//        }
//        return 0;
//    }
//
//    public static enum SERVER_CODE {
//        SUCCESS(00),
//        NEXT_SUCCESS(01),
//        FAIL(1),
//        ERROR(9998),
//        RESULT(0),
//        CHANGEDATE(3333);
//
//
//        private final int value;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static SERVER_CODE toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        public int getValue() {
//            return value;
//        }
//
//        private SERVER_CODE(int value) {
//            this.value = value;
//        }
//    }
//
//
//    // ----------------------------------------------------------------
//    // Device 정보
//    // ----------------------------------------------------------------
//
//    public static enum DEVICE {
//        DEVICE_MODEL,
//        OS_VERSION,
//        PHONE_NUMBER;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static DEVICE toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//    }
//
//    // ----------------------------------------------------------------
//    // App 정보
//    // ----------------------------------------------------------------
//    public static enum App {
//        PACKAGE_NAME,
//        VERSION;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static App toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//    }
//
    // ----------------------------------------------------------------
    // App APP환경정보
    // ----------------------------------------------------------------
    public static enum Config {
        GPS_RECV_CYCLE,      // GPS 수집주기
        GPS_SEND_CYCLE,     //GPS 전송주기
        GEO_FENCE_DELIVERY, // GPS 배송지반경
        GEO_FENCE_STORE,    // GPS 점포반경
        LAST_VERSION,       // APP 최종버전
        DOWNLOAD_URL;       // APP 업데이트 경로


        public int toOrdianl() {
            return ordinal();
        }

        public String toString() {
            return name();
        }

        public static Config toValue(String value) {
            try {
                return valueOf(value);
            } catch (Exception e) {
            }
            return null;
        }
    }
//
//    // ----------------------------------------------------------------
//    // 로그인 화면관련(로그인전)
//    // ----------------------------------------------------------------
//    public static enum Login {
//        SERVICE_AGREEMENT_YN,
//        SERVICE_AGREEMENT_TIME,
//        GPS_AGREEMENT_TIME,
//        ORG_URL,
//        ORG_NAME,
//        STORE_ID,
//        STORE_NM,
//        STORE_LAT,
//        STORE_LON,
//        STORE_REPORT_YN,
//        SHIFT_ID,
//        SHIFT_NM,
//        NOTI_CHK,
//        SDS_SESSION;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static Login toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//    }
//
//
//    // ----------------------------------------------------------------
//    // 배송지 목록 요청정보
//    // ----------------------------------------------------------------
//    public static enum DeliveryListInfo {
//        DELIVERY_DT,
//        SHIFT_ID,
//        STORE_ID,
//        VAN_ID;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static DeliveryListInfo toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//    }
//
//    // ----------------------------------------------------------------
//    // GPS 정보(현위치)
//    // ----------------------------------------------------------------
//    public static enum GPS_MANAGER {
//        GPS_LAT,
//        GPS_LNG,
//        GPS_UPDATE_TIME,
//        GPS_STATUS;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static GPS_MANAGER toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//    }
//
//
//    // ----------------------------------------------------------------
//    // GPS 정보(현위치)
//    // ----------------------------------------------------------------
//    public static enum LastReport {
//        ROUTE_IDX,
//        ORDER_NO,
//        REPORT_TYPE,
//        LAT,
//        LNG;
//
//        public int toOrdianl() {
//            return ordinal();
//        }
//
//        public String toString() {
//            return name();
//        }
//
//        public static LastReport toValue(String value) {
//            try {
//                return valueOf(value);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//    }


}
