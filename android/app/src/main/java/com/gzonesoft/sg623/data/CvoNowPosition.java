package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

public class CvoNowPosition {
    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    String RANK;
    String NAME;
    String COMPANYCD;
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
    String ADDRESS;
    String DISPATCH_KEY;
    String INTERVALDISTANCE;
    String BATTERYLEVEL;
    String TIMESTAMP;
    String MARKER_CAPTION;
    String MARKER_SUBCAPTION;
    String MARKER_SUBTITLE;
    String STATUS_NM;

    public String getRANK() {
        return StringUtil.getNvlStr(RANK);
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getNAME() {
        return StringUtil.getNvlStr(NAME);
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCOMPANYCD() {
        return StringUtil.getNvlStr(COMPANYCD);
    }

    public void setCOMPANYCD(String COMPANYCD) {
        this.COMPANYCD = COMPANYCD;
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

    public String getMARKER_SUBTITLE() {
        return StringUtil.getNvlStr(MARKER_SUBTITLE);
    }

    public void setMARKER_SUBTITLE(String MARKER_SUBTITLE) {
        this.MARKER_SUBTITLE = MARKER_SUBTITLE;
    }

    public String getSTATUS_NM() {
        return StringUtil.getNvlStr(STATUS_NM);
    }

    public void setSTATUS_NM(String STATUS_NM) {
        this.STATUS_NM = STATUS_NM;
    }
}
