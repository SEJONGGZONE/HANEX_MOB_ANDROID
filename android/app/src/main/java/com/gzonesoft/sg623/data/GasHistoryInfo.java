package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

import java.io.Serializable;

/**
 * 배송리스트(업무메인)
 */
public class GasHistoryInfo implements Serializable {
    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    // ERP에서 가져온..
    String GEONUM;
    String COMPANYCD;
    String COMPANY_NAME;
    String EMPNO;
    String NAME;
    String PHONENO;
    String VEHICLENO;
    String CHARGE_DATE;
    String GAS_TYPE;
    String UNIT_AMOUNT;
    String UNIT_QTY;
    String AMOUNT;
    String LAST_UPDATE_DATE;

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

    public String getPHONENO() {
        return StringUtil.getNvlStr(PHONENO);
    }

    public void setPHONENO(String PHONENO) {
        this.PHONENO = PHONENO;
    }

    public String getVEHICLENO() {
        return StringUtil.getNvlStr(VEHICLENO);
    }

    public void setVEHICLENO(String VEHICLENO) {
        this.VEHICLENO = VEHICLENO;
    }

    public String getCHARGE_DATE() {
        return StringUtil.getNvlStr(CHARGE_DATE);
    }

    public void setCHARGE_DATE(String CHARGE_DATE) {
        this.CHARGE_DATE = CHARGE_DATE;
    }

    public String getGAS_TYPE() {
        return StringUtil.getNvlStr(GAS_TYPE);
    }

    public void setGAS_TYPE(String GAS_TYPE) {
        this.GAS_TYPE = GAS_TYPE;
    }

    public String getUNIT_AMOUNT() {
        return StringUtil.getNvlStr(UNIT_AMOUNT);
    }

    public void setUNIT_AMOUNT(String UNIT_AMOUNT) {
        this.UNIT_AMOUNT = UNIT_AMOUNT;
    }

    public String getUNIT_QTY() {
        return StringUtil.getNvlStr(UNIT_QTY);
    }

    public void setUNIT_QTY(String UNIT_QTY) {
        this.UNIT_QTY = UNIT_QTY;
    }

    public String getAMOUNT() {
        return StringUtil.getNvlStr(AMOUNT);
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getLAST_UPDATE_DATE() {
        return StringUtil.getNvlStr(LAST_UPDATE_DATE);
    }

    public void setLAST_UPDATE_DATE(String LAST_UPDATE_DATE) {
        this.LAST_UPDATE_DATE = LAST_UPDATE_DATE;
    }


}
