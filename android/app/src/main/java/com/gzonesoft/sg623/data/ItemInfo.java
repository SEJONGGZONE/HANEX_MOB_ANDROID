package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.StringUtil;

/**
 * 아이템 관리
 */
public class ItemInfo {

    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }


    String msg_cd;
    String msg_dsc;

    String GEONUM;
    String ITCODE;
    String ITUSER;
    String ITNAME;
    String ITSTAN;
    String ITUNIT;
    String ITMAKER;
    String ITORIGIN;
    String ITPLACE;
    String ITTAX_GUBUN;
    String ITVAT_YN;
    String ITBOX_IPQTY;
    String ITEACDAN;
    String ITBOX_IPDAN;
    String ITEA_IPDAN;
    String ITORDERMIN;
    String IMG_PATH_JM;
    String IMG_FILE_JM;
    String LS_LASTDATE;
    String LS_DAN;
    String LS_BOXDAN;
    String ITBOXSDAN;
    String ITSDAN1;
    String ITEASDAN;
    String G1;
    String G2;
    String G3;
    String AMOUNT;
    String VAT;
    String CATEGORY_CODE;
    String CATEGORY_NAME;
    String CHK_FAVORITE;
    String ORD_BOXQTY;
    String ORD_UNITQTY;
    String IMG_ADM_UPLOAD;
    String JSON_IMAGE_URL;

    public String getMsg_cd() {
        return StringUtil.getNvlStr(msg_cd).toString();
    }

    public void setMsg_cd(String msg_cd) {
        this.msg_cd = msg_cd;
    }

    public String getMsg_dsc() {
        return StringUtil.getNvlStr(msg_dsc).toString();
    }

    public void setMsg_dsc(String msg_dsc) {
        this.msg_dsc = msg_dsc;
    }

    public String getGEONUM() {
        return StringUtil.getNvlStr(GEONUM).toString();
    }

    public void setGEONUM(String GEONUM) {
        this.GEONUM = GEONUM;
    }

    public String getITCODE() {
        return StringUtil.getNvlStr(ITCODE).toString();
    }

    public void setITCODE(String ITCODE) {
        this.ITCODE = ITCODE;
    }

    public String getITUSER() {
        return StringUtil.getNvlStr(ITUSER).toString();
    }

    public void setITUSER(String ITUSER) {
        this.ITUSER = ITUSER;
    }

    public String getITNAME() {
        return StringUtil.getNvlStr(ITNAME).toString();
    }

    public void setITNAME(String ITNAME) {
        this.ITNAME = ITNAME;
    }

    public String getITSTAN() {
        return StringUtil.getNvlStr(ITSTAN).toString();
    }

    public void setITSTAN(String ITSTAN) {
        this.ITSTAN = ITSTAN;
    }

    public String getITUNIT() {
        return StringUtil.getNvlStr(ITUNIT).toString();
    }

    public void setITUNIT(String ITUNIT) {
        this.ITUNIT = ITUNIT;
    }

    public String getITMAKER() {
        return StringUtil.getNvlStr(ITMAKER).toString();
    }

    public void setITMAKER(String ITMAKER) {
        this.ITMAKER = ITMAKER;
    }

    public String getITORIGIN() {
        return StringUtil.getNvlStr(ITORIGIN).toString();
    }

    public void setITORIGIN(String ITORIGIN) {
        this.ITORIGIN = ITORIGIN;
    }

    public String getITPLACE() {
        return StringUtil.getNvlStr(ITPLACE).toString();
    }

    public void setITPLACE(String ITPLACE) {
        this.ITPLACE = ITPLACE;
    }

    public String getITTAX_GUBUN() {
        return StringUtil.getNvlStr(ITTAX_GUBUN).toString();
    }

    public void setITTAX_GUBUN(String ITTAX_GUBUN) {
        this.ITTAX_GUBUN = ITTAX_GUBUN;
    }

    public String getITVAT_YN() {
        return StringUtil.getNvlStr(ITVAT_YN).toString();
    }

    public void setITVAT_YN(String ITVAT_YN) {
        this.ITVAT_YN = ITVAT_YN;
    }

    public String getITBOX_IPQTY() {
        return StringUtil.getNvlStr(ITBOX_IPQTY).toString();
    }

    public void setITBOX_IPQTY(String ITBOX_IPQTY) {
        this.ITBOX_IPQTY = ITBOX_IPQTY;
    }

    public String getITEACDAN() {
        return StringUtil.getNvlStr(ITEACDAN).toString();
    }

    public void setITEACDAN(String ITEACDAN) {
        this.ITEACDAN = ITEACDAN;
    }

    public String getITBOX_IPDAN() {
        return StringUtil.getNvlStr(ITBOX_IPDAN).toString();
    }

    public void setITBOX_IPDAN(String ITBOX_IPDAN) {
        this.ITBOX_IPDAN = ITBOX_IPDAN;
    }

    public String getITEA_IPDAN() {
        String retString = "";
        try {
            int amount = Integer.parseInt(StringUtil.getNvlStr(ITEA_IPDAN).toString().replace(".0", ""));
            retString = CommonUtil.with().setAmount(amount);
        } catch (Exception ex) {
            ex.printStackTrace();
            retString = "0";
        } finally {
            return retString;
        }

        ///return StringUtil.getNvlStr(ITEA_IPDAN).toString().replace(".0", "");
    }

    public void setITEA_IPDAN(String ITEA_IPDAN) {
        this.ITEA_IPDAN = ITEA_IPDAN;
    }

    public String getITORDERMIN() {
        return StringUtil.getNvlStr(ITORDERMIN).toString();
    }

    public void setITORDERMIN(String ITORDERMIN) {
        this.ITORDERMIN = ITORDERMIN;
    }

    public String getIMG_PATH_JM() {
        return StringUtil.getNvlStr(IMG_PATH_JM).toString();
    }

    public void setIMG_PATH_JM(String IMG_PATH_JM) {
        this.IMG_PATH_JM = IMG_PATH_JM;
    }

    public String getIMG_FILE_JM() {
        return StringUtil.getNvlStr(IMG_FILE_JM).toString();
    }

    public void setIMG_FILE_JM(String IMG_FILE_JM) {
        this.IMG_FILE_JM = IMG_FILE_JM;
    }

    public String getLS_LASTDATE() {
        return StringUtil.getNvlStr(LS_LASTDATE).toString();
    }

    public void setLS_LASTDATE(String LS_LASTDATE) {
        this.LS_LASTDATE = LS_LASTDATE;
    }

    public String getLS_DAN() {
        return StringUtil.getNvlStr(LS_DAN).toString();
    }

    public void setLS_DAN(String LS_DAN) {
        this.LS_DAN = LS_DAN;
    }

    public String getLS_BOXDAN() {
        return StringUtil.getNvlStr(LS_BOXDAN).toString();
    }

    public void setLS_BOXDAN(String LS_BOXDAN) {
        this.LS_BOXDAN = LS_BOXDAN;
    }

    public String getITBOXSDAN() {
        return StringUtil.getNvlStr(ITBOXSDAN).toString();
    }

    public void setITBOXSDAN(String ITBOXSDAN) {
        this.ITBOXSDAN = ITBOXSDAN;
    }

    public String getITSDAN1() {
        return StringUtil.getNvlStr(ITSDAN1).toString();
    }

    public void setITSDAN1(String ITSDAN1) {
        this.ITSDAN1 = ITSDAN1;
    }

    public String getITEASDAN() {
        return StringUtil.getNvlStr(ITEASDAN).toString();
    }

    public void setITEASDAN(String ITEASDAN) {
        this.ITEASDAN = ITEASDAN;
    }

    public String getG1() {
        return StringUtil.getNvlStr(G1).toString();
    }

    public void setG1(String g1) {
        G1 = g1;
    }

    public String getG2() {
        return StringUtil.getNvlStr(G2).toString();
    }

    public void setG2(String g2) {
        G2 = g2;
    }

    public String getG3() {
        return StringUtil.getNvlStr(G3).toString();
    }

    public void setG3(String g3) {
        G3 = g3;
    }

    public String getAMOUNT() {
        return StringUtil.getNvlStr(AMOUNT).toString();
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getVAT() {
        return StringUtil.getNvlStr(VAT).toString();
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public String getCATEGORY_CODE() {
        return StringUtil.getNvlStr(CATEGORY_CODE).toString();
    }

    public void setCATEGORY_CODE(String CATEGORY_CODE) {
        this.CATEGORY_CODE = CATEGORY_CODE;
    }

    public String getCATEGORY_NAME() {
        return StringUtil.getNvlStr(CATEGORY_NAME).toString();
    }

    public void setCATEGORY_NAME(String CATEGORY_NAME) {
        this.CATEGORY_NAME = CATEGORY_NAME;
    }

    public String getCHK_FAVORITE() {
        return StringUtil.getNvlStr(CHK_FAVORITE).toString();
    }

    public void setCHK_FAVORITE(String CHK_FAVORITE) {
        this.CHK_FAVORITE = CHK_FAVORITE;
    }

    public String getORD_BOXQTY() {
        return StringUtil.getNvlStr(ORD_BOXQTY).toString();
    }

    public void setORD_BOXQTY(String ORD_BOXQTY) {
        this.ORD_BOXQTY = ORD_BOXQTY;
    }

    public String getORD_UNITQTY() {
        return StringUtil.getNvlStr(ORD_UNITQTY).toString();
    }

    public void setORD_UNITQTY(String ORD_UNITQTY) {
        this.ORD_UNITQTY = ORD_UNITQTY;
    }

    public String getIMG_ADM_UPLOAD() {
        return StringUtil.getNvlStr(IMG_ADM_UPLOAD).toString();
    }

    public void setIMG_ADM_UPLOAD(String IMG_ADM_UPLOAD) {
        this.IMG_ADM_UPLOAD = IMG_ADM_UPLOAD;
    }

    public String getJSON_IMAGE_URL() {
        return StringUtil.getNvlStr(JSON_IMAGE_URL).toString();
    }

    public void setJSON_IMAGE_URL(String JSON_IMAGE_URL) {
        this.JSON_IMAGE_URL = JSON_IMAGE_URL;
    }
}
