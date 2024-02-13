package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.StringUtil;

/**
 * 모바일인증 관리
 */
public class MobileAuth {

    // StringUtil.getNvlStr(msg_cd).toString();

    String GEONUM;
    String PHONE_NO;
    String AUTH_NO;
    String LIMIT_DATE;
    String WS_NEWDATE;
    String WS_NEWUSER;
    String WS_EDTDATE;
    String WS_EDTUSER;


    // StringUtil.getNvlStr(msg_cd).toString();


    public String getGEONUM() {
        return StringUtil.getNvlStr(GEONUM).toString();
    }

    public void setGEONUM(String GEONUM) {
        this.GEONUM = GEONUM;
    }

    public String getPHONE_NO() {
        return StringUtil.getNvlStr(PHONE_NO).toString();
    }

    public void setPHONE_NO(String PHONE_NO) {
        this.PHONE_NO = PHONE_NO;
    }

    public String getAUTH_NO() {
        return StringUtil.getNvlStr(AUTH_NO).toString();
    }

    public void setAUTH_NO(String AUTH_NO) {
        this.AUTH_NO = AUTH_NO;
    }

    public String getLIMIT_DATE() {
        return StringUtil.getNvlStr(LIMIT_DATE).toString();
    }

    public void setLIMIT_DATE(String LIMIT_DATE) {
        this.LIMIT_DATE = LIMIT_DATE;
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

    @Override
    public String toString() {
        return "MobileAuth{" +
                "GEONUM='" + GEONUM + '\'' +
                ", PHONE_NO='" + PHONE_NO + '\'' +
                ", AUTH_NO='" + AUTH_NO + '\'' +
                ", LIMIT_DATE='" + LIMIT_DATE + '\'' +
                ", WS_NEWDATE='" + WS_NEWDATE + '\'' +
                ", WS_NEWUSER='" + WS_NEWUSER + '\'' +
                ", WS_EDTDATE='" + WS_EDTDATE + '\'' +
                ", WS_EDTUSER='" + WS_EDTUSER + '\'' +
                '}';
    }
}
