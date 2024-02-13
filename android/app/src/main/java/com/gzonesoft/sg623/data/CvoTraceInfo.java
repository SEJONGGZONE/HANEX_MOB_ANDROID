package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

public class CvoTraceInfo {
    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

/*

      "GEONUM": 1470945,
      "DEVICENO": "01094065736",
      "VEHICLECD": "86-1563",
      "VEHICLENO": "86루1563",
      "TRACEDATE": "20230817",
      "TRACETIME": "164951",
      "EVENTCODE": "05",
      "GPSYN": "Y",
      "CHARGEYN": "Y",
      "LATITUDE": "36.5980856",
      "LONGITUDE": "127.3018561",
      "DIRECTION": "49",
      "SPEED": 0,
      "REMARK": "",
      "INTERVALDISTANCE": "2",
      "BATTERYLEVEL": 100,
      "TIMESTAMP": "2023-08-17 16:49:52",
      "ADDRESS": "세종특별자치시 조치원읍 남리 239",
      "DISPATCH_KEY": ""
      "MARKER_SUBTITLE": "86루1563",
      "MARKER_CAPTION": "세종특별자치시 조치원읍 남리 239",
      "MARKER_SUBCAPTION": "08/17 16:49",
      "STATUS_NM": "정차중"

*/
    String RANK;
    String DEVICENO;
    String VEHICLECD;
    String VEHICLENO;
    String TRACEDATE;
    String TRACETIME;
    String EVENTCODE;
    String GPSYN;
    String CHARGEYN;
    String LATITUDE;
    String LONGITUDE;
    String DIRECTION;
    String SPEED;
    String REMARK;
    String INTERVALDISTANCE;
    String BATTERYLEVEL;
    String TIMESTAMP;
    String ADDRESS;
    String DISPATCH_KEY;
    String ROUTE_TITLE;
    String MARKER_SUBTITLE;
    String MARKER_CAPTION;
    String MARKER_SUBCAPTION;
    String STATUS_NM;
    String MAX_SPEED;
    String AVG_SPEED;
    String DISTANCE_01;
    String DISTANCE_02;
    String DISTANCE_03;
    String DURATION_INFO;



    public String getRANK() {
        return StringUtil.getNvlStr(RANK);
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getDEVICENO() {
        return StringUtil.getNvlStr(DEVICENO);
    }

    public void setDEVICENO(String DEVICENO) {
        this.DEVICENO = DEVICENO;
    }

    public String getVEHICLECD() {
        return StringUtil.getNvlStr(VEHICLECD);
    }

    public void setVEHICLECD(String VEHICLECD) {
        this.VEHICLECD = VEHICLECD;
    }

    public String getVEHICLENO() {
        return StringUtil.getNvlStr(VEHICLENO);
    }

    public void setVEHICLENO(String VEHICLENO) {
        this.VEHICLENO = VEHICLENO;
    }

    public String getTRACEDATE() {
        return StringUtil.getNvlStr(TRACEDATE);
    }

    public void setTRACEDATE(String TRACEDATE) {
        this.TRACEDATE = TRACEDATE;
    }

    public String getTRACETIME() {
        return StringUtil.getNvlStr(TRACETIME);
    }

    public void setTRACETIME(String TRACETIME) {
        this.TRACETIME = TRACETIME;
    }

    public String getEVENTCODE() {
        return StringUtil.getNvlStr(EVENTCODE);
    }

    public void setEVENTCODE(String EVENTCODE) {
        this.EVENTCODE = EVENTCODE;
    }

    public String getGPSYN() {
        return StringUtil.getNvlStr(GPSYN);
    }

    public void setGPSYN(String GPSYN) {
        this.GPSYN = GPSYN;
    }

    public String getCHARGEYN() {
        return StringUtil.getNvlStr(CHARGEYN);
    }

    public void setCHARGEYN(String CHARGEYN) {
        this.CHARGEYN = CHARGEYN;
    }

    public String getLATITUDE() {
        return StringUtil.getNvlStr(LATITUDE);
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return StringUtil.getNvlStr(LONGITUDE);
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getDIRECTION() {
        return StringUtil.getNvlStr(DIRECTION);
    }

    public void setDIRECTION(String DIRECTION) {
        this.DIRECTION = DIRECTION;
    }

    public String getSPEED() {
        return StringUtil.getNvlStr(SPEED);
    }

    public void setSPEED(String SPEED) {
        this.SPEED = SPEED;
    }

    public String getREMARK() {
        return StringUtil.getNvlStr(REMARK);
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getINTERVALDISTANCE() {
        return StringUtil.getNvlStr(INTERVALDISTANCE);
    }

    public void setINTERVALDISTANCE(String INTERVALDISTANCE) {
        this.INTERVALDISTANCE = INTERVALDISTANCE;
    }

    public String getBATTERYLEVEL() {
        return StringUtil.getNvlStr(BATTERYLEVEL);
    }

    public void setBATTERYLEVEL(String BATTERYLEVEL) {
        this.BATTERYLEVEL = BATTERYLEVEL;
    }

    public String getTIMESTAMP() {
        return StringUtil.getNvlStr(TIMESTAMP);
    }

    public void setTIMESTAMP(String TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public String getADDRESS() {
        return StringUtil.getNvlStr(ADDRESS);
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getDISPATCH_KEY() {
        return StringUtil.getNvlStr(DISPATCH_KEY);
    }

    public void setDISPATCH_KEY(String DISPATCH_KEY) {
        this.DISPATCH_KEY = DISPATCH_KEY;
    }

    public String getROUTE_TITLE() {
        return StringUtil.getNvlStr(ROUTE_TITLE);
    }

    public void setROUTE_TITLE(String ROUTE_TITLE) {
        this.ROUTE_TITLE = ROUTE_TITLE;
    }

    public String getMARKER_SUBTITLE() {
        return StringUtil.getNvlStr(MARKER_SUBTITLE);
    }

    public void setMARKER_SUBTITLE(String MARKER_SUBTITLE) {
        this.MARKER_SUBTITLE = MARKER_SUBTITLE;
    }

    public String getMARKER_CAPTION() {
        return StringUtil.getNvlStr(MARKER_CAPTION);
    }

    public void setMARKER_CAPTION(String MARKER_CAPTION) {
        this.MARKER_CAPTION = MARKER_CAPTION;
    }

    public String getMARKER_SUBCAPTION() {
        return StringUtil.getNvlStr(MARKER_SUBCAPTION);
    }

    public void setMARKER_SUBCAPTION(String MARKER_SUBCAPTION) {
        this.MARKER_SUBCAPTION = MARKER_SUBCAPTION;
    }

    public String getSTATUS_NM() {
        return StringUtil.getNvlStr(STATUS_NM);
    }

    public void setSTATUS_NM(String STATUS_NM) {
        this.STATUS_NM = STATUS_NM;
    }

    public String getMAX_SPEED() {
        return StringUtil.getNvlStr(MAX_SPEED);
    }

    public void setMAX_SPEED(String MAX_SPEED) {
        this.MAX_SPEED = MAX_SPEED;
    }

    public String getAVG_SPEED() {
        return StringUtil.getNvlStr(AVG_SPEED);
    }

    public void setAVG_SPEED(String AVG_SPEED) {
        this.AVG_SPEED = AVG_SPEED;
    }

    public String getDISTANCE_01() {
        return StringUtil.getNvlStr(DISTANCE_01);
    }

    public void setDISTANCE_01(String DISTANCE_01) {
        this.DISTANCE_01 = DISTANCE_01;
    }

    public String getDISTANCE_02() {
        return StringUtil.getNvlStr(DISTANCE_02);
    }

    public void setDISTANCE_02(String DISTANCE_02) {
        this.DISTANCE_02 = DISTANCE_02;
    }

    public String getDISTANCE_03() {
        return StringUtil.getNvlStr(DISTANCE_03);
    }

    public void setDISTANCE_03(String DISTANCE_03) {
        this.DISTANCE_03 = DISTANCE_03;
    }

    public String getDURATION_INFO() {
        return StringUtil.getNvlStr(DURATION_INFO);
    }

    public void setDURATION_INFO(String DURATION_INFO) {
        this.DURATION_INFO = DURATION_INFO;
    }
}
