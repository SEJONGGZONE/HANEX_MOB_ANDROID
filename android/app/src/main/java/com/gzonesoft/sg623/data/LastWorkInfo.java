package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

/**
 * 최종작업상태 조회
 */
public class LastWorkInfo {

    String NOTICE_TITLE = "";
    String NOTICE_CONTENT = "";
    String NOTICE_DT = "";
    String NOTICE_POP_YN = "";
    String LAST_POS_X = "";
    String LAST_POS_Y = "";
    String VEHICLE_STATUS = "";
    String VEHICLE_STATUS_NM = "";
    String TODAY_DISTANCE = "";
    String TODAY_DURATION = "";
    String ACC_COUNT_TODAY = "";
    String ACC_COUNT_WEEK = "";
    String GPS_YN = "";
    String GPS_GET_CYCLE = "";
    String GPS_SEND_TIME = "";
    String TODAY_START_DTM = "";
    String TODAY_END_DTM = "";
    String LAST_REPORT_CD = "";
    String LAST_REPORT_NM = "";
    String LAST_REPORT_DTM = "";
    String LAST_TODAY_CD = "";
    String LAST_TODAY_NM = "";
    String LAST_TODAY_DTM = "";
    String LAST_LOGIN_DTM = "";

    // TMS 정보
    String TMS_INFO_01 = "";
    String TMS_INFO_02 = "";
    String TMS_INFO_03 = "";
    String TMS_INFO_04 = "";
    String TMS_INFO_05 = "";
    String TMS_INFO_06 = "";
    String TMS_INFO_07 = "";

    // 메뉴 서브타이틀
    String MENU_SUBTITLE_01 = "";
    String MENU_SUBTITLE_02 = "";
    String MENU_SUBTITLE_03 = "";
    String MENU_SUBTITLE_04 = "";

    // 관제정보
    String CVO_INFO_01 = "";
    String CVO_INFO_02 = "";
    String CVO_INFO_03 = "";
    String CVO_INFO_04 = "";
    String CVO_INFO_05 = "";

    // 휴차일수
    String DAYOFF_CNT = "";

    // 배차통계(건수)-배차내역상단 표시
    String STAT_CNT_01 = "";
    String STAT_CNT_02 = "";
    String STAT_CNT_03 = "";
    String STAT_CNT_04 = "";
    String STAT_CNT_05 = "";

    // 위치서비스 리셋하기
    String RESET_LOCATION_SERVICE_YN = "";

    public String getNOTICE_TITLE() {
        return StringUtil.getNvlStr(NOTICE_TITLE);
    }

    public void setNOTICE_TITLE(String NOTICE_TITLE) {
        this.NOTICE_TITLE = NOTICE_TITLE;
    }

    public String getNOTICE_CONTENT() {
        return StringUtil.getNvlStr(NOTICE_CONTENT);
    }

    public void setNOTICE_CONTENT(String NOTICE_CONTENT) {
        this.NOTICE_CONTENT = NOTICE_CONTENT;
    }

    public String getNOTICE_DT() {
        return StringUtil.getNvlStr(NOTICE_DT);
    }

    public void setNOTICE_DT(String NOTICE_DT) {
        this.NOTICE_DT = NOTICE_DT;
    }

    public String getNOTICE_POP_YN() {
        return StringUtil.getNvlStr(NOTICE_POP_YN);
    }

    public void setNOTICE_POP_YN(String NOTICE_POP_YN) {
        this.NOTICE_POP_YN = NOTICE_POP_YN;
    }

    public String getLAST_POS_X() {
        return StringUtil.getNvlStr(LAST_POS_X);
    }

    public void setLAST_POS_X(String LAST_POS_X) {
        this.LAST_POS_X = LAST_POS_X;
    }

    public String getLAST_POS_Y() {
        return StringUtil.getNvlStr(LAST_POS_Y);
    }

    public void setLAST_POS_Y(String LAST_POS_Y) {
        this.LAST_POS_Y = LAST_POS_Y;
    }

    public String getVEHICLE_STATUS() {
        return StringUtil.getNvlStr(VEHICLE_STATUS);
    }

    public void setVEHICLE_STATUS(String VEHICLE_STATUS) {
        this.VEHICLE_STATUS = VEHICLE_STATUS;
    }

    public String getVEHICLE_STATUS_NM() {
        return StringUtil.getNvlStr(VEHICLE_STATUS_NM);
    }

    public void setVEHICLE_STATUS_NM(String VEHICLE_STATUS_NM) {
        this.VEHICLE_STATUS_NM = VEHICLE_STATUS_NM;
    }

    public String getTODAY_DISTANCE() {
        return StringUtil.getNvlStr(TODAY_DISTANCE);
    }

    public void setTODAY_DISTANCE(String TODAY_DISTANCE) {
        this.TODAY_DISTANCE = TODAY_DISTANCE;
    }

    public String getTODAY_DURATION() {
        return StringUtil.getNvlStr(TODAY_DURATION);
    }

    public void setTODAY_DURATION(String TODAY_DURATION) {
        this.TODAY_DURATION = TODAY_DURATION;
    }

    public String getACC_COUNT_TODAY() {
        return StringUtil.getNvlStr(ACC_COUNT_TODAY);
    }

    public void setACC_COUNT_TODAY(String ACC_COUNT_TODAY) {
        this.ACC_COUNT_TODAY = ACC_COUNT_TODAY;
    }

    public String getACC_COUNT_WEEK() {
        return StringUtil.getNvlStr(ACC_COUNT_WEEK);
    }

    public void setACC_COUNT_WEEK(String ACC_COUNT_WEEK) {
        this.ACC_COUNT_WEEK = ACC_COUNT_WEEK;
    }

    public String getGPS_YN() {
        return StringUtil.getNvlStr(GPS_YN);
    }

    public void setGPS_YN(String GPS_YN) {
        this.GPS_YN = GPS_YN;
    }

    public String getGPS_GET_CYCLE() {
        return StringUtil.getNvlStr(GPS_GET_CYCLE);
    }

    public void setGPS_GET_CYCLE(String GPS_GET_CYCLE) {
        this.GPS_GET_CYCLE = GPS_GET_CYCLE;
    }

    public String getGPS_SEND_TIME() {
        return StringUtil.getNvlStr(GPS_SEND_TIME);
    }

    public void setGPS_SEND_TIME(String GPS_SEND_TIME) {
        this.GPS_SEND_TIME = GPS_SEND_TIME;
    }

    public String getTODAY_START_DTM() {
        return StringUtil.getNvlStr(TODAY_START_DTM);
    }

    public void setTODAY_START_DTM(String TODAY_START_DTM) {
        this.TODAY_START_DTM = TODAY_START_DTM;
    }

    public String getTODAY_END_DTM() {
        return StringUtil.getNvlStr(TODAY_END_DTM);
    }

    public void setTODAY_END_DTM(String TODAY_END_DTM) {
        this.TODAY_END_DTM = TODAY_END_DTM;
    }

    public String getLAST_TODAY_CD() {
        return StringUtil.getNvlStr(LAST_TODAY_CD);
    }

    public void setLAST_TODAY_CD(String LAST_TODAY_CD) {
        this.LAST_TODAY_CD = LAST_TODAY_CD;
    }

    public String getLAST_TODAY_NM() {
        return StringUtil.getNvlStr(LAST_TODAY_NM);
    }

    public void setLAST_TODAY_NM(String LAST_TODAY_NM) {
        this.LAST_TODAY_NM = LAST_TODAY_NM;
    }

    public String getLAST_TODAY_DTM() {
        return StringUtil.getNvlStr(LAST_TODAY_DTM);
    }

    public void setLAST_TODAY_DTM(String LAST_TODAY_DTM) {
        this.LAST_TODAY_DTM = LAST_TODAY_DTM;
    }

    public String getLAST_LOGIN_DTM() {
        return StringUtil.getNvlStr(LAST_LOGIN_DTM);
    }

    public void setLAST_LOGIN_DTM(String LAST_LOGIN_DTM) {
        this.LAST_LOGIN_DTM = LAST_LOGIN_DTM;
    }

    public String getLAST_REPORT_NM() {
        return StringUtil.getNvlStr(LAST_REPORT_NM);
    }

    public void setLAST_REPORT_NM(String LAST_REPORT_NM) {
        this.LAST_REPORT_NM = LAST_REPORT_NM;
    }

    public String getLAST_REPORT_DTM() {
        return StringUtil.getNvlStr(LAST_REPORT_DTM);
    }

    public void setLAST_REPORT_DTM(String LAST_REPORT_DTM) {
        this.LAST_REPORT_DTM = LAST_REPORT_DTM;
    }

    public String getLAST_REPORT_CD() {
        return StringUtil.getNvlStr(LAST_REPORT_CD);
    }

    public void setLAST_REPORT_CD(String LAST_REPORT_CD) {
        this.LAST_REPORT_CD = LAST_REPORT_CD;
    }

    public String getTMS_INFO_01() {
        return StringUtil.getNvlStr(TMS_INFO_01);
    }

    public void setTMS_INFO_01(String TMS_INFO_01) {
        this.TMS_INFO_01 = TMS_INFO_01;
    }

    public String getTMS_INFO_02() {
        return StringUtil.getNvlStr(TMS_INFO_02);
    }

    public void setTMS_INFO_02(String TMS_INFO_02) {
        this.TMS_INFO_02 = TMS_INFO_02;
    }

    public String getTMS_INFO_03() {
        return StringUtil.getNvlStr(TMS_INFO_03);
    }

    public void setTMS_INFO_03(String TMS_INFO_03) {
        this.TMS_INFO_03 = TMS_INFO_03;
    }

    public String getTMS_INFO_04() {
        return StringUtil.getNvlStr(TMS_INFO_04);
    }

    public void setTMS_INFO_04(String TMS_INFO_04) {
        this.TMS_INFO_04 = TMS_INFO_04;
    }

    public String getTMS_INFO_05() {
        return StringUtil.getNvlStr(TMS_INFO_05);
    }

    public void setTMS_INFO_05(String TMS_INFO_05) {
        this.TMS_INFO_05 = TMS_INFO_05;
    }

    public String getTMS_INFO_06() {
        return StringUtil.getNvlStr(TMS_INFO_06);
    }

    public void setTMS_INFO_06(String TMS_INFO_06) {
        this.TMS_INFO_06 = TMS_INFO_06;
    }

    public String getMENU_SUBTITLE_01() {
        return StringUtil.getNvlStr(MENU_SUBTITLE_01);
    }

    public void setMENU_SUBTITLE_01(String MENU_SUBTITLE_01) {
        this.MENU_SUBTITLE_01 = MENU_SUBTITLE_01;
    }

    public String getMENU_SUBTITLE_02() {
        return StringUtil.getNvlStr(MENU_SUBTITLE_02);
    }

    public void setMENU_SUBTITLE_02(String MENU_SUBTITLE_02) {
        this.MENU_SUBTITLE_02 = MENU_SUBTITLE_02;
    }

    public String getMENU_SUBTITLE_03() {
        return StringUtil.getNvlStr(MENU_SUBTITLE_03);
    }

    public void setMENU_SUBTITLE_03(String MENU_SUBTITLE_03) {
        this.MENU_SUBTITLE_03 = MENU_SUBTITLE_03;
    }

    public String getMENU_SUBTITLE_04() {
        return StringUtil.getNvlStr(MENU_SUBTITLE_04);
    }

    public void setMENU_SUBTITLE_04(String MENU_SUBTITLE_04) {
        this.MENU_SUBTITLE_04 = MENU_SUBTITLE_04;
    }

    public String getCVO_INFO_01() {
        return StringUtil.getNvlStr(CVO_INFO_01);
    }

    public void setCVO_INFO_01(String CVO_INFO_01) {
        this.CVO_INFO_01 = CVO_INFO_01;
    }

    public String getCVO_INFO_02() {
        return StringUtil.getNvlStr(CVO_INFO_02);
    }

    public void setCVO_INFO_02(String CVO_INFO_02) {
        this.CVO_INFO_02 = CVO_INFO_02;
    }

    public String getCVO_INFO_03() {
        return StringUtil.getNvlStr(CVO_INFO_03);
    }

    public void setCVO_INFO_03(String CVO_INFO_03) {
        this.CVO_INFO_03 = CVO_INFO_03;
    }

    public String getCVO_INFO_04() {
        return StringUtil.getNvlStr(CVO_INFO_04);
    }

    public void setCVO_INFO_04(String CVO_INFO_04) {
        this.CVO_INFO_04 = CVO_INFO_04;
    }

    public String getCVO_INFO_05() {
        return StringUtil.getNvlStr(CVO_INFO_05);
    }

    public void setCVO_INFO_05(String CVO_INFO_05) {
        this.CVO_INFO_05 = CVO_INFO_05;
    }

    public String getTMS_INFO_07() {
        return StringUtil.getNvlStr(TMS_INFO_07);
    }

    public void setTMS_INFO_07(String TMS_INFO_07) {
        this.TMS_INFO_07 = TMS_INFO_07;
    }

    public String getDAYOFF_CNT() {
        return StringUtil.getNvlStr(DAYOFF_CNT);
    }

    public void setDAYOFF_CNT(String DAYOFF_CNT) {
        this.DAYOFF_CNT = DAYOFF_CNT;
    }

    public String getSTAT_CNT_01() {
        return STAT_CNT_01;
    }

    public void setSTAT_CNT_01(String STAT_CNT_01) {
        this.STAT_CNT_01 = STAT_CNT_01;
    }

    public String getSTAT_CNT_02() {
        return STAT_CNT_02;
    }

    public void setSTAT_CNT_02(String STAT_CNT_02) {
        this.STAT_CNT_02 = STAT_CNT_02;
    }

    public String getSTAT_CNT_03() {
        return STAT_CNT_03;
    }

    public void setSTAT_CNT_03(String STAT_CNT_03) {
        this.STAT_CNT_03 = STAT_CNT_03;
    }

    public String getSTAT_CNT_04() {
        return STAT_CNT_04;
    }

    public void setSTAT_CNT_04(String STAT_CNT_04) {
        this.STAT_CNT_04 = STAT_CNT_04;
    }

    public String getSTAT_CNT_05() {
        return STAT_CNT_05;
    }

    public void setSTAT_CNT_05(String STAT_CNT_05) {
        this.STAT_CNT_05 = STAT_CNT_05;
    }

    public String getRESET_LOCATION_SERVICE_YN() {
        return StringUtil.getNvlStr(RESET_LOCATION_SERVICE_YN);
    }

    public void setRESET_LOCATION_SERVICE_YN(String RESET_LOCATION_SERVICE_YN) {
        this.RESET_LOCATION_SERVICE_YN = RESET_LOCATION_SERVICE_YN;
    }
}
