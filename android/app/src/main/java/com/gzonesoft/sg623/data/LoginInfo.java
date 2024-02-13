package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    String DEVICE_CD;
    String VHCL_TYPE_CD;
    String VHCL_TYPE_NM;
    String DRV_CD;
    String DRV_NM;
    String HP_NO;
    String SAP_DRV_CD;
    String VHCL_PCL_CD;
    String VHCL_NO;
    String TRANS_CD;
    String TRANS_NM;
    String PARENT_TRANS_CD;
    String MAIN_SOURCE_CD;
    String VHCL_CARD_NO;
    String MOBILE_NO;
    String MIN_NO;
    String PDA_ID;
    String LAST_DISPATCH_INFO;
    String GPS_GET_CYCLE;
    String GPS_SEND_TIME;
    String GPS_USE_YN;
    String CHANGE_PWD_YN;


    public String getDEVICE_CD() {
        return StringUtil.getNvlStr(DEVICE_CD);
    }

    public void setDEVICE_CD(String DEVICE_CD) {
        this.DEVICE_CD = DEVICE_CD;
    }

    public String getVHCL_TYPE_CD() {
        return StringUtil.getNvlStr(VHCL_TYPE_CD);
    }

    public void setVHCL_TYPE_CD(String VHCL_TYPE_CD) {
        this.VHCL_TYPE_CD = VHCL_TYPE_CD;
    }

    public String getVHCL_TYPE_NM() {
        return StringUtil.getNvlStr(VHCL_TYPE_NM);
    }

    public void setVHCL_TYPE_NM(String VHCL_TYPE_NM) {
        this.VHCL_TYPE_NM = VHCL_TYPE_NM;
    }

    public String getDRV_CD() {
        return StringUtil.getNvlStr(DRV_CD);
    }

    public void setDRV_CD(String DRV_CD) {
        this.DRV_CD = DRV_CD;
    }

    public String getDRV_NM() {
        return StringUtil.getNvlStr(DRV_NM);
    }

    public void setDRV_NM(String DRV_NM) {
        this.DRV_NM = DRV_NM;
    }

    public String getHP_NO() {
        return StringUtil.getNvlStr(HP_NO);
    }

    public void setHP_NO(String HP_NO) {
        this.HP_NO = HP_NO;
    }

    public String getSAP_DRV_CD() {
        return StringUtil.getNvlStr(SAP_DRV_CD);
    }

    public void setSAP_DRV_CD(String SAP_DRV_CD) {
        this.SAP_DRV_CD = SAP_DRV_CD;
    }

    public String getVHCL_PCL_CD() {
        return StringUtil.getNvlStr(VHCL_PCL_CD);
    }

    public void setVHCL_PCL_CD(String VHCL_PCL_CD) {
        this.VHCL_PCL_CD = VHCL_PCL_CD;
    }

    public String getVHCL_NO() {
        return StringUtil.getNvlStr(VHCL_NO);
    }

    public void setVHCL_NO(String VHCL_NO) {
        this.VHCL_NO = VHCL_NO;
    }

    public String getTRANS_CD() {
        return StringUtil.getNvlStr(TRANS_CD);
    }

    public void setTRANS_CD(String TRANS_CD) {
        this.TRANS_CD = TRANS_CD;
    }

    public String getTRANS_NM() {
        return StringUtil.getNvlStr(TRANS_NM);
    }

    public void setTRANS_NM(String TRANS_NM) {
        this.TRANS_NM = TRANS_NM;
    }

    public String getPARENT_TRANS_CD() {
        return StringUtil.getNvlStr(PARENT_TRANS_CD);
    }

    public void setPARENT_TRANS_CD(String PARENT_TRANS_CD) {
        this.PARENT_TRANS_CD = PARENT_TRANS_CD;
    }

    public String getMAIN_SOURCE_CD() {
        return StringUtil.getNvlStr(MAIN_SOURCE_CD);
    }

    public void setMAIN_SOURCE_CD(String MAIN_SOURCE_CD) {
        this.MAIN_SOURCE_CD = MAIN_SOURCE_CD;
    }

    public String getVHCL_CARD_NO() {
        return StringUtil.getNvlStr(VHCL_CARD_NO);
    }

    public void setVHCL_CARD_NO(String VHCL_CARD_NO) {
        this.VHCL_CARD_NO = VHCL_CARD_NO;
    }

    public String getMOBILE_NO() {
        return StringUtil.getNvlStr(MOBILE_NO);
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getMIN_NO() {
        return StringUtil.getNvlStr(MIN_NO);
    }

    public void setMIN_NO(String MIN_NO) {
        this.MIN_NO = MIN_NO;
    }

    public String getPDA_ID() {
        return StringUtil.getNvlStr(PDA_ID);
    }

    public void setPDA_ID(String PDA_ID) {
        this.PDA_ID = PDA_ID;
    }

    public String getLAST_DISPATCH_INFO() {
        return StringUtil.getNvlStr(LAST_DISPATCH_INFO);
    }

    public void setLAST_DISPATCH_INFO(String LAST_DISPATCH_INFO) {
        this.LAST_DISPATCH_INFO = LAST_DISPATCH_INFO;
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

    @Override
    public String toString() {
        return "LoginInfo{" +
                "DEVICE_CD='" + DEVICE_CD + '\'' +
                ", VHCL_TYPE_CD='" + VHCL_TYPE_CD + '\'' +
                ", VHCL_TYPE_NM='" + VHCL_TYPE_NM + '\'' +
                ", DRV_CD='" + DRV_CD + '\'' +
                ", DRV_NM='" + DRV_NM + '\'' +
                ", HP_NO='" + HP_NO + '\'' +
                ", SAP_DRV_CD='" + SAP_DRV_CD + '\'' +
                ", VHCL_PCL_CD='" + VHCL_PCL_CD + '\'' +
                ", VHCL_NO='" + VHCL_NO + '\'' +
                ", TRANS_CD='" + TRANS_CD + '\'' +
                ", TRANS_NM='" + TRANS_NM + '\'' +
                ", PARENT_TRANS_CD='" + PARENT_TRANS_CD + '\'' +
                ", MAIN_SOURCE_CD='" + MAIN_SOURCE_CD + '\'' +
                ", VHCL_CARD_NO='" + VHCL_CARD_NO + '\'' +
                ", MOBILE_NO='" + MOBILE_NO + '\'' +
                ", MIN_NO='" + MIN_NO + '\'' +
                ", PDA_ID='" + PDA_ID + '\'' +
                ", LAST_DISPATCH_INFO='" + LAST_DISPATCH_INFO + '\'' +
                ", GPS_GET_CYCLE='" + GPS_GET_CYCLE + '\'' +
                ", GPS_SEND_TIME='" + GPS_SEND_TIME + '\'' +
                ", GPS_USE_YN='" + GPS_USE_YN + '\'' +
                ", CHANGE_PWD_YN='" + CHANGE_PWD_YN + '\'' +
                '}';
    }
}
