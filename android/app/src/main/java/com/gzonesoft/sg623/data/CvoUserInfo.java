package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

public class CvoUserInfo {

    String GPS_GET_CYCLE;
    String GPS_SEND_TIME;
    String GPS_USE_YN;
    String CHANGE_PWD_YN;
    String GEONUM;
    String COMPANYCD;
    String COMPANY_NAME;
    String EMPNO;
    String NAME;
    String DEVICENO;
    String PHONENO;
    String VEHICLECD;
    String VEHICLENO;
    String JOBTITLE;
    String USERTYPE;
    String USEYN;
    String WS_NEWDATE;
    String WS_NEWUSER;
    String WS_EDTDATE;
    String WS_EDTUSER;

    // StringUtil.getNvlStr(GPS_GET_CYCLE);


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

    public String getGPS_USE_YN() {
        return StringUtil.getNvlStr(GPS_USE_YN);
    }

    public void setGPS_USE_YN(String GPS_USE_YN) {
        this.GPS_USE_YN = GPS_USE_YN;
    }

    public String getCHANGE_PWD_YN() {
        return StringUtil.getNvlStr(CHANGE_PWD_YN);
    }

    public void setCHANGE_PWD_YN(String CHANGE_PWD_YN) {
        this.CHANGE_PWD_YN = CHANGE_PWD_YN;
    }

    public String getGEONUM() {
        return StringUtil.getNvlStr(GEONUM);
    }

    public void setGEONUM(String GEONUM) {
        this.GEONUM = GEONUM;
    }

    public String getCOMPANYCD() {
        return StringUtil.getNvlStr(COMPANYCD);
    }

    public void setCOMPANYCD(String COMPANYCD) {
        this.COMPANYCD = COMPANYCD;
    }

    public String getCOMPANY_NAME() {
        return StringUtil.getNvlStr(COMPANY_NAME);
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getEMPNO() {
        return StringUtil.getNvlStr(EMPNO);
    }

    public void setEMPNO(String EMPNO) {
        this.EMPNO = EMPNO;
    }

    public String getNAME() {
        return StringUtil.getNvlStr(NAME);
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDEVICENO() {
        return StringUtil.getNvlStr(DEVICENO);
    }

    public void setDEVICENO(String DEVICENO) {
        this.DEVICENO = DEVICENO;
    }

    public String getPHONENO() {
        return StringUtil.getNvlStr(PHONENO);
    }

    public void setPHONENO(String PHONENO) {
        this.PHONENO = PHONENO;
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

    public String getJOBTITLE() {
        return StringUtil.getNvlStr(JOBTITLE);
    }

    public void setJOBTITLE(String JOBTITLE) {
        this.JOBTITLE = JOBTITLE;
    }

    public String getUSERTYPE() {
        return StringUtil.getNvlStr(USERTYPE);
    }

    public void setUSERTYPE(String USERTYPE) {
        this.USERTYPE = USERTYPE;
    }

    public String getUSEYN() {
        return StringUtil.getNvlStr(USEYN);
    }

    public void setUSEYN(String USEYN) {
        this.USEYN = USEYN;
    }

    public String getWS_NEWDATE() {
        return StringUtil.getNvlStr(WS_NEWDATE);
    }

    public void setWS_NEWDATE(String WS_NEWDATE) {
        this.WS_NEWDATE = WS_NEWDATE;
    }

    public String getWS_NEWUSER() {
        return StringUtil.getNvlStr(WS_NEWUSER);
    }

    public void setWS_NEWUSER(String WS_NEWUSER) {
        this.WS_NEWUSER = WS_NEWUSER;
    }

    public String getWS_EDTDATE() {
        return StringUtil.getNvlStr(WS_EDTDATE);
    }

    public void setWS_EDTDATE(String WS_EDTDATE) {
        this.WS_EDTDATE = WS_EDTDATE;
    }

    public String getWS_EDTUSER() {
        return StringUtil.getNvlStr(WS_EDTUSER);
    }

    public void setWS_EDTUSER(String WS_EDTUSER) {
        this.WS_EDTUSER = WS_EDTUSER;
    }

    @Override
    public String toString() {
        return "CvoUserInfo{" +
                "GPS_GET_CYCLE='" + GPS_GET_CYCLE + '\'' +
                ", GPS_SEND_TIME='" + GPS_SEND_TIME + '\'' +
                ", GPS_USE_YN='" + GPS_USE_YN + '\'' +
                ", CHANGE_PWD_YN='" + CHANGE_PWD_YN + '\'' +
                ", GEONUM='" + GEONUM + '\'' +
                ", COMPANYCD='" + COMPANYCD + '\'' +
                ", EMPNO='" + EMPNO + '\'' +
                ", NAME='" + NAME + '\'' +
                ", DEVICENO='" + DEVICENO + '\'' +
                ", PHONENO='" + PHONENO + '\'' +
                ", VEHICLECD='" + VEHICLECD + '\'' +
                ", VEHICLENO='" + VEHICLENO + '\'' +
                ", JOBTITLE='" + JOBTITLE + '\'' +
                ", USERTYPE='" + USERTYPE + '\'' +
                ", USEYN='" + USEYN + '\'' +
                ", WS_NEWDATE='" + WS_NEWDATE + '\'' +
                ", WS_NEWUSER='" + WS_NEWUSER + '\'' +
                ", WS_EDTDATE='" + WS_EDTDATE + '\'' +
                ", WS_EDTUSER='" + WS_EDTUSER + '\'' +
                '}';
    }
}
