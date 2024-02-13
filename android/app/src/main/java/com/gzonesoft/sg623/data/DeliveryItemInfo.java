package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.StringUtil;

/**
 * 배송 상품
 */
public class DeliveryItemInfo {

    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

/*

"ITNAME": "천일) 해물볶음밥",
"ITSTAN": "300g\/30봉",
"WSD_QTY": "10",
"WSD_DAN": "2200",
"WSD_TOT": "22000"

*/
    String ITCODE;
    String ITNAME;
    String ITSTAN;
    String WSD_QTY;
    String WSD_DAN;
    String WSD_TOT;
    String IMAGE_URL;
    String TOT_QTY;
    String TOT_AMOUNT;
    String FOOTER_INFO;
    boolean bLastYn =false;

    public String getITCODE() {
        return StringUtil.getNvlStr(ITCODE);
    }

    public void setITCODE(String ITCODE) {
        this.ITCODE = ITCODE;
    }

    public String getITNAME() {
        return StringUtil.getNvlStr(ITNAME);
    }

    public void setITNAME(String ITNAME) {
        this.ITNAME = ITNAME;
    }

    public String getITSTAN() {
        return StringUtil.getNvlStr(ITSTAN);
    }

    public void setITSTAN(String ITSTAN) {
        this.ITSTAN = ITSTAN;
    }

    public String getWSD_QTY() {
        return StringUtil.getNvlStr(WSD_QTY);
    }

    public void setWSD_QTY(String WSD_QTY) {
        this.WSD_QTY = WSD_QTY;
    }

    public String getWSD_DAN() {
        return StringUtil.getNvlStr(WSD_DAN);
    }

    public void setWSD_DAN(String WSD_DAN) {
        this.WSD_DAN = WSD_DAN;
    }

    public String getWSD_TOT() {
        return StringUtil.getNvlStr(WSD_TOT);
    }

    public void setWSD_TOT(String WSD_TOT) {
        this.WSD_TOT = WSD_TOT;
    }

    public String getIMAGE_URL() {
        return StringUtil.getNvlStr(IMAGE_URL);
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getTOT_QTY() {
        return StringUtil.getNvlStr(TOT_QTY);
    }

    public void setTOT_QTY(String TOT_QTY) {
        this.TOT_QTY = TOT_QTY;
    }

    public String getTOT_AMOUNT() {
        return StringUtil.getNvlStr(TOT_AMOUNT);
    }

    public void setTOT_AMOUNT(String TOT_AMOUNT) {
        this.TOT_AMOUNT = TOT_AMOUNT;
    }

    public String getFOOTER_INFO() {
        return StringUtil.getNvlStr(FOOTER_INFO);
    }

    public void setFOOTER_INFO(String FOOTER_INFO) {
        this.FOOTER_INFO = FOOTER_INFO;
    }

    public boolean isbLastYn() {
        return bLastYn;
    }

    public void setbLastYn(boolean bLastYn) {
        this.bLastYn = bLastYn;
    }
}
