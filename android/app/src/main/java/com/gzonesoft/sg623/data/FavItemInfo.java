package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.StringUtil;

/**
 * 관심품목 관리
 */
public class FavItemInfo {

    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }


    String msg_cd;
    String msg_dsc;

    String geonum;
    String itcode;
    String ituser;
    String itname;
    String itstan;
    String itunit;
    String itmaker;
    String itorigin;
    String itplace;
    String ittax_gubun;
    String itvat_yn;
    String itbox_ipqty;
    String iteacdan;
    String itbox_ipdan;
    String itea_ipdan;
    String itordermin;
    String img_path_jm;
    String img_file_jm;
    String ls_lastdate;
    String ls_dan;
    String ls_boxdan;
    String itboxsdan;
    String itsdan1;
    String itboxsdan2;
    String iteasdan;
    String amount;
    String vat;
    String G1;
    String G2;
    String G3;
    String ORD_BOXQTY;
    String ORD_UNITQTY;

    String IMG_ADM_UPLOAD;


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

    public String getGeonum() {
        return StringUtil.getNvlStr(geonum).toString();
    }

    public void setGeonum(String geonum) {
        this.geonum = geonum;
    }

    public String getItcode() {
        return StringUtil.getNvlStr(itcode).toString();
    }

    public void setItcode(String itcode) {
        this.itcode = itcode;
    }

    public String getItuser() {
        return StringUtil.getNvlStr(ituser).toString();
    }

    public void setItuser(String ituser) {
        this.ituser = ituser;
    }

    public String getItname() {
        return StringUtil.getNvlStr(itname).toString();
    }

    public void setItname(String itname) {
        this.itname = itname;
    }

    public String getItstan() {
        return StringUtil.getNvlStr(itstan).toString();
    }

    public void setItstan(String itstan) {
        this.itstan = itstan;
    }

    public String getItunit() {
        return StringUtil.getNvlStr(itunit).toString();
    }

    public void setItunit(String itunit) {
        this.itunit = itunit;
    }

    public String getItmaker() {
        return StringUtil.getNvlStr(itmaker).toString();
    }

    public void setItmaker(String itmaker) {
        this.itmaker = itmaker;
    }

    public String getItorigin() {
        return StringUtil.getNvlStr(itorigin).toString();
    }

    public void setItorigin(String itorigin) {
        this.itorigin = itorigin;
    }

    public String getItplace() {
        return StringUtil.getNvlStr(itplace).toString();
    }

    public void setItplace(String itplace) {
        this.itplace = itplace;
    }

    public String getIttax_gubun() {
        return StringUtil.getNvlStr(ittax_gubun).toString();
    }

    public void setIttax_gubun(String ittax_gubun) {
        this.ittax_gubun = ittax_gubun;
    }

    public String getItvat_yn() {
        return StringUtil.getNvlStr(itvat_yn).toString();
    }

    public void setItvat_yn(String itvat_yn) {
        this.itvat_yn = itvat_yn;
    }

    public String getItbox_ipqty() {
        return StringUtil.getNvlStr(itbox_ipqty).toString();
    }

    public void setItbox_ipqty(String itbox_ipqty) {
        this.itbox_ipqty = itbox_ipqty;
    }

    public String getIteacdan() {
        return StringUtil.getNvlStr(iteacdan).toString();
    }

    public void setIteacdan(String iteacdan) {
        this.iteacdan = iteacdan;
    }

    public String getItbox_ipdan() {
        return StringUtil.getNvlStr(itbox_ipdan).toString();
    }

    public void setItbox_ipdan(String itbox_ipdan) {
        this.itbox_ipdan = itbox_ipdan;
    }

    public String getItea_ipdan() {
        return StringUtil.getNvlStr(itea_ipdan).toString();
    }

    public void setItea_ipdan(String itea_ipdan) {
        this.itea_ipdan = itea_ipdan;
    }

    public String getItordermin() {
        return StringUtil.getNvlStr(itordermin).toString();
    }

    public void setItordermin(String itordermin) {
        this.itordermin = itordermin;
    }

    public String getImg_path_jm() {
        return StringUtil.getNvlStr(img_path_jm).toString();
    }

    public void setImg_path_jm(String img_path_jm) {
        this.img_path_jm = img_path_jm;
    }

    public String getImg_file_jm() {
        return StringUtil.getNvlStr(img_file_jm).toString();
    }

    public void setImg_file_jm(String img_file_jm) {
        this.img_file_jm = img_file_jm;
    }

    public String getLs_lastdate() {
        return StringUtil.getNvlStr(ls_lastdate).toString();
    }

    public void setLs_lastdate(String ls_lastdate) {
        this.ls_lastdate = ls_lastdate;
    }

    public String getLs_dan() {
        return StringUtil.getNvlStr(ls_dan).toString();
    }

    public void setLs_dan(String ls_dan) {
        this.ls_dan = ls_dan;
    }

    public String getLs_boxdan() {
        return StringUtil.getNvlStr(ls_boxdan).toString();
    }

    public void setLs_boxdan(String ls_boxdan) {
        this.ls_boxdan = ls_boxdan;
    }

    public String getItboxsdan() {
        return StringUtil.getNvlStr(itboxsdan).toString();
    }

    public void setItboxsdan(String itboxsdan) {
        this.itboxsdan = itboxsdan;
    }

    public String getItsdan1() {
        return StringUtil.getNvlStr(itsdan1).toString();
    }

    public void setItsdan1(String itsdan1) {
        this.itsdan1 = itsdan1;
    }

    public String getItboxsdan2() {
        return StringUtil.getNvlStr(itboxsdan2).toString();
    }

    public void setItboxsdan2(String itboxsdan2) {
        this.itboxsdan2 = itboxsdan2;
    }

    public String getIteasdan() {
        return StringUtil.getNvlStr(iteasdan).toString();
    }

    public void setIteasdan(String iteasdan) {
        this.iteasdan = iteasdan;
    }

    public String getAmount() {
        return StringUtil.getNvlStr(amount).toString();
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVat() {
        return StringUtil.getNvlStr(vat).toString();
    }

    public void setVat(String vat) {
        this.vat = vat;
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

    @Override
    public String toString() {
        return "FavItemInfo{" +
                "idx=" + idx +
                ", msg_cd='" + msg_cd + '\'' +
                ", msg_dsc='" + msg_dsc + '\'' +
                ", geonum='" + geonum + '\'' +
                ", itcode='" + itcode + '\'' +
                ", ituser='" + ituser + '\'' +
                ", itname='" + itname + '\'' +
                ", itstan='" + itstan + '\'' +
                ", itunit='" + itunit + '\'' +
                ", itmaker='" + itmaker + '\'' +
                ", itorigin='" + itorigin + '\'' +
                ", itplace='" + itplace + '\'' +
                ", ittax_gubun='" + ittax_gubun + '\'' +
                ", itvat_yn='" + itvat_yn + '\'' +
                ", itbox_ipqty='" + itbox_ipqty + '\'' +
                ", iteacdan='" + iteacdan + '\'' +
                ", itbox_ipdan='" + itbox_ipdan + '\'' +
                ", itea_ipdan='" + itea_ipdan + '\'' +
                ", itordermin='" + itordermin + '\'' +
                ", img_path_jm='" + img_path_jm + '\'' +
                ", img_file_jm='" + img_file_jm + '\'' +
                ", ls_lastdate='" + ls_lastdate + '\'' +
                ", ls_dan='" + ls_dan + '\'' +
                ", ls_boxdan='" + ls_boxdan + '\'' +
                ", itboxsdan='" + itboxsdan + '\'' +
                ", itsdan1='" + itsdan1 + '\'' +
                ", itboxsdan2='" + itboxsdan2 + '\'' +
                ", iteasdan='" + iteasdan + '\'' +
                ", amount='" + amount + '\'' +
                ", vat='" + vat + '\'' +
                ", G1='" + G1 + '\'' +
                ", G2='" + G2 + '\'' +
                ", G3='" + G3 + '\'' +
                ", ORD_BOXQTY='" + ORD_BOXQTY + '\'' +
                ", ORD_UNITQTY='" + ORD_UNITQTY + '\'' +
                ", IMG_ADM_UPLOAD='" + IMG_ADM_UPLOAD + '\'' +
                '}';
    }
}
