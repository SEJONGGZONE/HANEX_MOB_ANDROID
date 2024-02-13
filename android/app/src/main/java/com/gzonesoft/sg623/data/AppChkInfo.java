package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

public class AppChkInfo {

    String UPDATE_YN = "";
    String UPDATE_URL = "";
    String GPS_SND_URL = "";
    String CVO_EVENT_URL = "";
    String TMS_API_SERVER = "";
    String TMS_API_KEY_NAME = "";
    String TMS_API_KEY = "";
    String CVO_API_SERVER = "";
    String CVO_API_KEY_NAME = "";
    String CVO_API_KEY = "";
    String OS_TYPE = "";
    String APP_NAME = "";
    String LAST_VERSION = "";


    public String getAPP_NAME() {
        return StringUtil.getNvlStr(APP_NAME);
    }

    public void setAPP_NAME(String APP_NAME) {
        this.APP_NAME = APP_NAME;
    }

    public String getOS_TYPE() {
        return StringUtil.getNvlStr(OS_TYPE);
    }

    public void setOS_TYPE(String OS_TYPE) {
        this.OS_TYPE = OS_TYPE;
    }

    public String getUPDATE_URL() {
        return StringUtil.getNvlStr(UPDATE_URL);
    }

    public void setUPDATE_URL(String UPDATE_URL) {
        this.UPDATE_URL = UPDATE_URL;
    }

    public String getGPS_SND_URL() {
        return StringUtil.getNvlStr(GPS_SND_URL);
    }

    public void setGPS_SND_URL(String GPS_SND_URL) {
        this.GPS_SND_URL = GPS_SND_URL;
    }

    public String getCVO_EVENT_URL() {
        return StringUtil.getNvlStr(CVO_EVENT_URL);
    }

    public void setCVO_EVENT_URL(String CVO_EVENT_URL) {
        this.CVO_EVENT_URL = CVO_EVENT_URL;
    }

    public String getTMS_API_SERVER() {
        return StringUtil.getNvlStr(TMS_API_SERVER);
    }

    public void setTMS_API_SERVER(String TMS_API_SERVER) {
        this.TMS_API_SERVER = TMS_API_SERVER;
    }

    public String getTMS_API_KEY_NAME() {
        return StringUtil.getNvlStr(TMS_API_KEY_NAME);
    }

    public void setTMS_API_KEY_NAME(String TMS_API_KEY_NAME) {
        this.TMS_API_KEY_NAME = TMS_API_KEY_NAME;
    }

    public String getTMS_API_KEY() {
        return StringUtil.getNvlStr(TMS_API_KEY);
    }

    public void setTMS_API_KEY(String TMS_API_KEY) {
        this.TMS_API_KEY = TMS_API_KEY;
    }

    public String getCVO_API_SERVER() {
        return StringUtil.getNvlStr(CVO_API_SERVER);
    }

    public void setCVO_API_SERVER(String CVO_API_SERVER) {
        this.CVO_API_SERVER = CVO_API_SERVER;
    }

    public String getCVO_API_KEY_NAME() {
        return StringUtil.getNvlStr(CVO_API_KEY_NAME);
    }

    public void setCVO_API_KEY_NAME(String CVO_API_KEY_NAME) {
        this.CVO_API_KEY_NAME = CVO_API_KEY_NAME;
    }

    public String getCVO_API_KEY() {
        return StringUtil.getNvlStr(CVO_API_KEY);
    }

    public void setCVO_API_KEY(String CVO_API_KEY) {
        this.CVO_API_KEY = CVO_API_KEY;
    }

    public String getUPDATE_YN() {
        return StringUtil.getNvlStr(UPDATE_YN);
    }

    public void setUPDATE_YN(String UPDATE_YN) {
        this.UPDATE_YN = UPDATE_YN;
    }

    public String getLAST_VERSION() {
        return StringUtil.getNvlStr(LAST_VERSION);
    }

    public void setLAST_VERSION(String LAST_VERSION) {
        this.LAST_VERSION = LAST_VERSION;
    }

    @Override
    public String toString() {
        return "AppChkInfo{" +
                "UPDATE_YN='" + UPDATE_YN + '\'' +
                ", UPDATE_URL='" + UPDATE_URL + '\'' +
                ", GPS_SND_URL='" + GPS_SND_URL + '\'' +
                ", CVO_EVENT_URL='" + CVO_EVENT_URL + '\'' +
                ", TMS_API_SERVER='" + TMS_API_SERVER + '\'' +
                ", TMS_API_KEY_NAME='" + TMS_API_KEY_NAME + '\'' +
                ", TMS_API_KEY='" + TMS_API_KEY + '\'' +
                ", CVO_API_SERVER='" + CVO_API_SERVER + '\'' +
                ", CVO_API_KEY_NAME='" + CVO_API_KEY_NAME + '\'' +
                ", CVO_API_KEY='" + CVO_API_KEY + '\'' +
                ", OS_TYPE='" + OS_TYPE + '\'' +
                ", APP_NAME='" + APP_NAME + '\'' +
                ", LAST_VERSION='" + LAST_VERSION + '\'' +
                '}';
    }
}
