package com.gzonesoft.sg623.domain;


/**
 * 브로드 캐스트 도메인 관리
 */
public class BroadCastDomain {
    // FCM 관련..
    public static final String BROADCAST_FCM_SVC = "BROADCAST_FCM_SVC";
    public static final String BROADCAST_FCM_RELOAD = "BROADCAST_FCM_RELOAD";
    // GPS 관련
    public static final String BROADCAST_TRACE_GPS_SVC = "BROADCAST_TRACE_GPS_SVC";
    public static final String BROADCAST_TRACE_GPS_STATUS = "BROADCAST_TRACE_GPS_STATUS";
    // 휴대폰인증, SMS 수신관련
    public static final String BROADCAST_PHONEAUTH = "BROADCAST_PHONEAUTH";
    public static final String RECV_SMS = "RECV_SMS";


    // 보고관련
    public static final String REPORT_CURRENT_LOCATION = "REPORT_CURRENT_LOCATION";

}