package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.StringUtil;

/**
 * 납품 이미지정보 Set
 */
public class DeliveryImageInfo {

    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }


    String msg_cd;
    String msg_dsc;

    public String COMPANYCD;
    public String COMPANY_NAME;
    public String DISPATCH_ID;
    public String DDATE;
    public String CLCODE;
    public String CLNAME;
    public String TYPE;
    public String SEQ;
    public String URL;
    public String D_IMG_CNT;
    public String WS_NEWDATE;
    public String WS_NEWUSER;

    public String getCOMPANYCD() {
        return COMPANYCD;
    }

    public void setCOMPANYCD(String COMPANYCD) {
        this.COMPANYCD = COMPANYCD;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getDISPATCH_ID() {
        return DISPATCH_ID;
    }

    public void setDISPATCH_ID(String DISPATCH_ID) {
        this.DISPATCH_ID = DISPATCH_ID;
    }

    public String getDDATE() {
        return DDATE;
    }

    public void setDDATE(String DDATE) {
        this.DDATE = DDATE;
    }

    public String getCLCODE() {
        return CLCODE;
    }

    public String getCLNAME() {
        return CLNAME;
    }

    public void setCLNAME(String CLNAME) {
        this.CLNAME = CLNAME;
    }

    public void setCLCODE(String CLCODE) {
        this.CLCODE = CLCODE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getD_IMG_CNT() {
        return D_IMG_CNT;
    }

    public void setD_IMG_CNT(String d_IMG_CNT) {
        D_IMG_CNT = d_IMG_CNT;
    }

    public String getWS_NEWDATE() {
        return WS_NEWDATE;
    }

    public void setWS_NEWDATE(String WS_NEWDATE) {
        this.WS_NEWDATE = WS_NEWDATE;
    }

    public String getWS_NEWUSER() {
        return WS_NEWUSER;
    }

    public void setWS_NEWUSER(String WS_NEWUSER) {
        this.WS_NEWUSER = WS_NEWUSER;
    }
}
