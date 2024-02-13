package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

public class CvoSpotInfo {
//    "GEONUM": 1,
//    "COMPANYCD": "00002",
//    "COMPANY_NAME": "아이사랑 정푸드",
//    "SPOT_CD": "0000000001",
//    "SPOT_NAME": "곤지암",
//    "ADDRESS": "경기도 광주시 곤지암읍 경충대로 731",
//    "PIC_NAME": "박경수 과장",
//    "PIC_TEL": "010-9000-5344",
//    "LATITUDE": "37.3535564",
//    "LONGITUDE": "127.3273437",
//    "RADIUS": 200,
//    "WS_NEWDATE": "2023-10-24 16:11:37",
//    "WS_NEWUSER": "9999999999",
//    "WS_EDTDATE": "2023-10-24 17:10:26",
//    "WS_EDTUSER": null,
//    "LAST_DATE": "2023-10-24 17:10:26",
//    "LAST_UPDATE_DATE": "23/10/24 17:10"

    String GEONUM;
    String COMPANYCD;
    String COMPANY_NAME;
    String SPOT_CD;
    String SPOT_NAME;
    String ADDRESS;
    String PIC_NAME;
    String PIC_TEL;
    String LATITUDE;
    String LONGITUDE;
    String RADIUS;
    String WS_NEWDATE;
    String WS_NEWUSER;
    String WS_EDTDATE;
    String WS_EDTUSER;
    String LAST_DATE;
    String LAST_UPDATE_DATE;

    public String getGEONUM() {
        return StringUtil.getNvlStr(GEONUM).toString();
    }

    public void setGEONUM(String GEONUM) {
        this.GEONUM = GEONUM;
    }

    public String getCOMPANYCD() {
        return StringUtil.getNvlStr(COMPANYCD).toString();
    }

    public void setCOMPANYCD(String COMPANYCD) {
        this.COMPANYCD = COMPANYCD;
    }

    public String getCOMPANY_NAME() {
        return StringUtil.getNvlStr(COMPANY_NAME).toString();
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getSPOT_CD() {
        return StringUtil.getNvlStr(SPOT_CD).toString();
    }

    public void setSPOT_CD(String SPOT_CD) {
        this.SPOT_CD = SPOT_CD;
    }

    public String getSPOT_NAME() {
        return StringUtil.getNvlStr(SPOT_NAME).toString();
    }

    public void setSPOT_NAME(String SPOT_NAME) {
        this.SPOT_NAME = SPOT_NAME;
    }

    public String getADDRESS() {
        return StringUtil.getNvlStr(ADDRESS).toString();
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getPIC_NAME() {
        return StringUtil.getNvlStr(PIC_NAME).toString();
    }

    public void setPIC_NAME(String PIC_NAME) {
        this.PIC_NAME = PIC_NAME;
    }

    public String getPIC_TEL() {
        return StringUtil.getNvlStr(PIC_TEL).toString();
    }

    public void setPIC_TEL(String PIC_TEL) {
        this.PIC_TEL = PIC_TEL;
    }

    public String getLATITUDE() {
        return StringUtil.getNvlStr(LATITUDE).toString();
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return StringUtil.getNvlStr(LONGITUDE).toString();
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getRADIUS() {
        return StringUtil.getNvlStr(RADIUS).toString();
    }

    public void setRADIUS(String RADIUS) {
        this.RADIUS = RADIUS;
    }

    public String getWS_NEWDATE() {
        return StringUtil.getNvlStr(WS_NEWDATE).toString();
    }

    public void setWS_NEWDATE(String WS_NEWDATE) {
        this.WS_NEWDATE = WS_NEWDATE;
    }

    public String getWS_NEWUSER() {
        return StringUtil.getNvlStr(WS_NEWUSER).toString();
    }

    public void setWS_NEWUSER(String WS_NEWUSER) {
        this.WS_NEWUSER = WS_NEWUSER;
    }

    public String getWS_EDTDATE() {
        return StringUtil.getNvlStr(WS_EDTDATE).toString();
    }

    public void setWS_EDTDATE(String WS_EDTDATE) {
        this.WS_EDTDATE = WS_EDTDATE;
    }

    public String getWS_EDTUSER() {
        return StringUtil.getNvlStr(WS_EDTUSER).toString();
    }

    public void setWS_EDTUSER(String WS_EDTUSER) {
        this.WS_EDTUSER = WS_EDTUSER;
    }

    public String getLAST_DATE() {
        return StringUtil.getNvlStr(LAST_DATE).toString();
    }

    public void setLAST_DATE(String LAST_DATE) {
        this.LAST_DATE = LAST_DATE;
    }

    public String getLAST_UPDATE_DATE() {
        return StringUtil.getNvlStr(LAST_UPDATE_DATE).toString();
    }

    public void setLAST_UPDATE_DATE(String LAST_UPDATE_DATE) {
        this.LAST_UPDATE_DATE = LAST_UPDATE_DATE;
    }

    @Override
    public String toString() {
        return "CvoSpotInfo{" +
                "GEONUM='" + GEONUM + '\'' +
                ", COMPANYCD='" + COMPANYCD + '\'' +
                ", COMPANY_NAME='" + COMPANY_NAME + '\'' +
                ", SPOT_CD='" + SPOT_CD + '\'' +
                ", SPOT_NAME='" + SPOT_NAME + '\'' +
                ", ADDRESS='" + ADDRESS + '\'' +
                ", PIC_NAME='" + PIC_NAME + '\'' +
                ", PIC_TEL='" + PIC_TEL + '\'' +
                ", LATITUDE='" + LATITUDE + '\'' +
                ", LONGITUDE='" + LONGITUDE + '\'' +
                ", RADIUS='" + RADIUS + '\'' +
                ", WS_NEWDATE='" + WS_NEWDATE + '\'' +
                ", WS_NEWUSER='" + WS_NEWUSER + '\'' +
                ", WS_EDTDATE='" + WS_EDTDATE + '\'' +
                ", WS_EDTUSER='" + WS_EDTUSER + '\'' +
                ", LAST_DATE='" + LAST_DATE + '\'' +
                ", LAST_UPDATE_DATE='" + LAST_UPDATE_DATE + '\'' +
                '}';
    }
}
