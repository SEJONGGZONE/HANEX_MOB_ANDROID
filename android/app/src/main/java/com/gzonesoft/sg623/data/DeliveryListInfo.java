package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

import java.io.Serializable;

/**
 * 배송리스트(업무메인)
 */
public class DeliveryListInfo implements Serializable {
    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    // ERP에서 가져온..
    String ROUTE_IDX;                               // 목원적용
    String RANK;
    String DDATE;
    String WSD_NUM;
    String EPCODE;
    String EPNAME;
    String EPPHONE;
    String CLCODE;
    String CLNAME;
    String ITEM_INFO;
    String ITEM_CNT;
    String ORDER_AMT;
    String LON;
    String LAT;
    String ADDRESS;
    String CLSAUPNO;
    String CLCORPNO;
    String CLCEO;
    String CLPHONE;
    String CLUPTAE;
    String CLJONG;
    String DISTANCE_ME;
    double DISTANCE_ME_VALUE;
    String ETC_DATA;

    // CVO에서 가져온..
    String ROUTE_CNT;
    String TOT_ORDER_AMT;
    String START_TIME;
    String LAST_REP_TIME;
//    String TOT_DISTANCE;
    String MAX_SPEED;
    String DISPATCH_ID;
    String COMPANYCD;
    //    String DDATE;
    String SHIFT_NO;
    String ROUTE_SEQ;
    //    String EPCODE;
//    String EPNAME;
//    String CLCODE;
//    String CLNAME;
//    String LAT;
//    String LON;
//    String ADDRESS;
    String PIC_NAME;
    String PIC_TEL;
    //    String ITEM_INFO;
//    String ITEM_CNT;
//    String ORDER_AMT;
    String EXPECT_TIME;
    String RESULT_TIME;
    //    String ETC_DATA;
    String DISTANCE;
    String DURATION;
    String TOT_DISTANCE;
    String TOT_DURATION;
    String LAST_UPDATEDATE;
    String INBOUND_CNT;
    String INBOUND_DTM;
    String OUTBOUND_CNT;
    String OUTBOUND_DTM;
    String IMAGE_CNT;
    String RECENT_IMG_URL;


    public String getROUTE_CNT() {
        return StringUtil.getNvlStr(ROUTE_CNT);
    }

    public void setROUTE_CNT(String ROUTE_CNT) {
        this.ROUTE_CNT = ROUTE_CNT;
    }

    public String getTOT_ORDER_AMT() {
        return StringUtil.getNvlStr(TOT_ORDER_AMT);
    }

    public void setTOT_ORDER_AMT(String TOT_ORDER_AMT) {
        this.TOT_ORDER_AMT = TOT_ORDER_AMT;
    }

    public String getSTART_TIME() {
        return StringUtil.getNvlStr(START_TIME);
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getLAST_REP_TIME() {
        return StringUtil.getNvlStr(LAST_REP_TIME);
    }

    public void setLAST_REP_TIME(String LAST_REP_TIME) {
        this.LAST_REP_TIME = LAST_REP_TIME;
    }

    public String getMAX_SPEED() {
        return StringUtil.getNvlStr(MAX_SPEED);
    }

    public void setMAX_SPEED(String MAX_SPEED) {
        this.MAX_SPEED = MAX_SPEED;
    }

    public String getROUTE_IDX() {
        return StringUtil.getNvlStr((ROUTE_IDX == null)?getROUTE_SEQ():ROUTE_IDX);
    }

    public void setROUTE_IDX(String ROUTE_IDX) {
        this.ROUTE_IDX = ROUTE_IDX;
    }

    public String getRANK() {
        return StringUtil.getNvlStr(RANK);
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getDDATE() {
        return StringUtil.getNvlStr(DDATE);
    }

    public void setDDATE(String DDATE) {
        this.DDATE = DDATE;
    }

    public String getWSD_NUM() {
        try {
            if (SHIFT_NO.length()>0)
                return StringUtil.getNvlStr(SHIFT_NO);
            else
                return StringUtil.getNvlStr(WSD_NUM);
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtil.getNvlStr(WSD_NUM);
        }
    }

    public void setWSD_NUM(String WSD_NUM) {
        this.WSD_NUM = WSD_NUM;
    }

    public String getEPCODE() {
        return StringUtil.getNvlStr(EPCODE);
    }

    public void setEPCODE(String EPCODE) {
        this.EPCODE = EPCODE;
    }

    public String getEPNAME() {
        return StringUtil.getNvlStr(EPNAME);
    }

    public void setEPNAME(String EPNAME) {
        this.EPNAME = EPNAME;
    }

    public String getEPPHONE() {
        return EPPHONE;
    }

    public void setEPPHONE(String EPPHONE) {
        this.EPPHONE = EPPHONE;
    }

    public String getCLCODE() {
        return StringUtil.getNvlStr(CLCODE);
    }

    public void setCLCODE(String CLCODE) {
        this.CLCODE = CLCODE;
    }

    public String getCLNAME() {
        return StringUtil.getNvlStr(CLNAME);
    }

    public void setCLNAME(String CLNAME) {
        this.CLNAME = CLNAME;
    }

    public String getITEM_INFO() {
        return StringUtil.getNvlStr(ITEM_INFO);
    }

    public void setITEM_INFO(String ITEM_INFO) {
        this.ITEM_INFO = ITEM_INFO;
    }

    public String getITEM_CNT() {
        return StringUtil.getNvlStr(ITEM_CNT);
    }

    public void setITEM_CNT(String ITEM_CNT) {
        this.ITEM_CNT = ITEM_CNT;
    }

    public String getORDER_AMT() {
        return StringUtil.getNvlStr(ORDER_AMT);
    }

    public void setORDER_AMT(String ORDER_AMT) {
        this.ORDER_AMT = ORDER_AMT;
    }

    public String getLON() {
        return StringUtil.getNvlStr(LON);
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getLAT() {
        return StringUtil.getNvlStr(LAT);
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getADDRESS() {
        return StringUtil.getNvlStr(ADDRESS);
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCLSAUPNO() {
        return StringUtil.getNvlStr(CLSAUPNO);
    }

    public void setCLSAUPNO(String CLSAUPNO) {
        this.CLSAUPNO = CLSAUPNO;
    }

    public String getCLCORPNO() {
        return StringUtil.getNvlStr(CLCORPNO);
    }

    public void setCLCORPNO(String CLCORPNO) {
        this.CLCORPNO = CLCORPNO;
    }

    public String getCLCEO() {
        return StringUtil.getNvlStr((CLCEO == null)?getPIC_NAME():CLCEO);
    }

    public void setCLCEO(String CLCEO) {
        this.CLCEO = CLCEO;
    }

    public String getCLPHONE() {
        return StringUtil.getNvlStr((CLPHONE == null)?getPIC_TEL():CLPHONE);
    }

    public void setCLPHONE(String CLPHONE) {
        this.CLPHONE = CLPHONE;
    }

    public String getCLUPTAE() {
        return CLUPTAE;
    }

    public void setCLUPTAE(String CLUPTAE) {
        this.CLUPTAE = CLUPTAE;
    }

    public String getCLJONG() {
        return CLJONG;
    }

    public void setCLJONG(String CLJONG) {
        this.CLJONG = CLJONG;
    }

    public String getDISTANCE_ME() {
        return StringUtil.getNvlStr(DISTANCE_ME);
    }

    public void setDISTANCE_ME(String DISTANCE_ME) {
        this.DISTANCE_ME = DISTANCE_ME;
    }

    public double getDISTANCE_ME_VALUE() {
        return DISTANCE_ME_VALUE;
    }

    public void setDISTANCE_ME_VALUE(double DISTANCE_ME_VALUE) {
        this.DISTANCE_ME_VALUE = DISTANCE_ME_VALUE;
    }

    public String getETC_DATA() {
        return StringUtil.getNvlStr(ETC_DATA);
    }

    public void setETC_DATA(String ETC_DATA) {
        this.ETC_DATA = ETC_DATA;
    }

    // CVO에서 가져온..

    public String getDISPATCH_ID() {
        return StringUtil.getNvlStr(DISPATCH_ID);
    }

    public void setDISPATCH_ID(String DISPATCH_ID) {
        this.DISPATCH_ID = DISPATCH_ID;
    }

    public String getCOMPANYCD() {
        return StringUtil.getNvlStr(COMPANYCD);
    }

    public void setCOMPANYCD(String COMPANYCD) {
        this.COMPANYCD = COMPANYCD;
    }

    public String getSHIFT_NO() {
        return StringUtil.getNvlStr(SHIFT_NO);
    }

    public void setSHIFT_NO(String SHIFT_NO) {
        this.SHIFT_NO = SHIFT_NO;
    }

    public String getROUTE_SEQ() {
        return StringUtil.getNvlStr(ROUTE_SEQ);
    }

    public void setROUTE_SEQ(String ROUTE_SEQ) {
        this.ROUTE_SEQ = ROUTE_SEQ;
    }

    public String getPIC_NAME() {
        return StringUtil.getNvlStr(PIC_NAME);
    }

    public void setPIC_NAME(String PIC_NAME) {
        this.PIC_NAME = PIC_NAME;
    }

    public String getPIC_TEL() {
        return StringUtil.getNvlStr(PIC_TEL);
    }

    public void setPIC_TEL(String PIC_TEL) {
        this.PIC_TEL = PIC_TEL;
    }

    public String getEXPECT_TIME() {
        return StringUtil.getNvlStr(EXPECT_TIME);
    }

    public void setEXPECT_TIME(String EXPECT_TIME) {
        this.EXPECT_TIME = EXPECT_TIME;
    }

    public String getRESULT_TIME() {
        return StringUtil.getNvlStr(RESULT_TIME);
    }

    public void setRESULT_TIME(String RESULT_TIME) {
        this.RESULT_TIME = RESULT_TIME;
    }

    public String getDISTANCE() {
        return StringUtil.getNvlStr(DISTANCE);
    }

    public void setDISTANCE(String DISTANCE) {
        this.DISTANCE = DISTANCE;
    }

    public String getDURATION() {
        return StringUtil.getNvlStr(DURATION);
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getTOT_DISTANCE() {
        return TOT_DISTANCE;
    }

    public void setTOT_DISTANCE(String TOT_DISTANCE) {
        this.TOT_DISTANCE = StringUtil.getNvlStr(TOT_DISTANCE);
    }

    public String getTOT_DURATION() {
        return TOT_DURATION;
    }

    public void setTOT_DURATION(String TOT_DURATION) {
        this.TOT_DURATION = StringUtil.getNvlStr(TOT_DURATION);
    }

    public String getLAST_UPDATEDATE() {
        return StringUtil.getNvlStr(LAST_UPDATEDATE);
    }

    public void setLAST_UPDATEDATE(String LAST_UPDATEDATE) {
        this.LAST_UPDATEDATE = LAST_UPDATEDATE;
    }

    public String getINBOUND_CNT() {
        return StringUtil.getNvlStr(INBOUND_CNT);
    }

    public void setINBOUND_CNT(String INBOUND_CNT) {
        this.INBOUND_CNT = INBOUND_CNT;
    }

    public String getINBOUND_DTM() {
        return StringUtil.getNvlStr(INBOUND_DTM);
    }

    public void setINBOUND_DTM(String INBOUND_DTM) {
        this.INBOUND_DTM = INBOUND_DTM;
    }

    public String getOUTBOUND_CNT() {
        return StringUtil.getNvlStr(OUTBOUND_CNT);
    }

    public void setOUTBOUND_CNT(String OUTBOUND_CNT) {
        this.OUTBOUND_CNT = OUTBOUND_CNT;
    }

    public String getOUTBOUND_DTM() {
        return StringUtil.getNvlStr(OUTBOUND_DTM);
    }

    public void setOUTBOUND_DTM(String OUTBOUND_DTM) {
        this.OUTBOUND_DTM = OUTBOUND_DTM;
    }

    public String getIMAGE_CNT() {
        return StringUtil.getNvlStr(IMAGE_CNT);
    }

    public void setIMAGE_CNT(String IMAGE_CNT) {
        this.IMAGE_CNT = IMAGE_CNT;
    }

    public String getRECENT_IMG_URL() {
        return StringUtil.getNvlStr(RECENT_IMG_URL);
    }

    public void setRECENT_IMG_URL(String RECENT_IMG_URL) {
        this.RECENT_IMG_URL = RECENT_IMG_URL;
    }
}
