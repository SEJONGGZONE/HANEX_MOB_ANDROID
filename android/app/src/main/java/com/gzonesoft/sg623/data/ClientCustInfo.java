package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

import java.io.Serializable;

/**
 * 거래처 정보 관리
 */
public class ClientCustInfo implements Serializable {

    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
    
    String GEONUM;
    String CLCODE;
    String CLUSER;
    String CLNAME;
    String CLGUBUN;
    String CLVISIBLE;
    String CLSANGHO;
    String CLSAUPNO;
    String CLCEO;
    String CLPOST;
    String CLJUSO1;
    String CLJUSO2;
    String CLUPTAE;
    String CLJONG;
    String CLTEL;
    String CLPHONE;
    String CLFAX;
    String CLBIGO;
    String CLBANKNO1;
    String CLBANKNAME1;
    String CLSENDNAME1;
    String CLBANKNO2;
    String CLBANKNAME2;
    String CLSENDNAME2;
    String CLEMPNAME;
    String CLDANSEL;
    String CLSALECLASS;
    String CLBASESTORE;
    String CLCASHDATE;
    String CLTAXCREATE_YN;
    String CLCREDIT_AMT;
    String CLTAXSALE_YN;
    String CLMISUYN;
    String CLSAWON1;
    String CLSATEL1;
    String CLSAWON2;
    String CLSATEL2;
    String CLS_NAME1;
    String CLS_NAME2;
    String CLS_NAME3;
    String CLS_NAME4;
    String CLS_NAME5;
    String CLS_NAME6;
    String CLS_NAME7;
    String CLS_NAME8;
    String CLS_NAME9;
    String CLS_NAME10;
    String WS_NEWDATE;
    String WS_NEWUSER;
    String WS_EDTDATE;
    String WS_EDTUSER;
    String CLJUMIN;
    String CLBANKNO;
    String CLEMP_MEIP;
    String CLLAST_DATE;
    String CLID;
    String CLEMAIL;
    String CLBANKNAME;
    String CLCLASS;
    String CLOPEN_DATE;
    String CLSAUP_JUSO;
    String CLMEMO;
    String CLFIRST_DANSEL;
    String CLPW;
    String TaxMail;
    String ComMail;
    String CLEMPNAME2;
    String CLSHOTNAME;
    String CLCHOSEONG;
    String CLPOST2;
    String CLJUSO3;
    String CLJUSO4;
    String SUCODE;
    String CLBARCODEYN;
    String CLEDAN;
    String CLEDATE;
    String CL_BMCODE;
    String CLPUB_SUCODE;
    String CLETAXCLASS;
    String CLETAXREGCHK;
    String CLETAXSAWON;
    String CLETAXTEL;
    String CLLIMIT_IPAMT;
    String CLCORPNO;
    String CLITPOINT;
    String CLPOINT;
    String CLLOGINYN;
    String CLSJDATE;
    String CLIPDATE;
    String CLSALE_STOP;
    String CLSALE_MONTH1;
    String CLSALE_DAY1;
    String CLSALE_MONTH2;
    String CLSALE_DAY2;
    String CLPAYMENT_MONTH1;
    String CLPAYMENT_DAY1;
    String CLPAYMENT_MONTH2;
    String CLPAYMENT_DAY2;
    String CLDCYUL;
    String CLEVENTYN;
    String CLPOINTYN;
    String CLEQUIPYN;
    String CLTAXZERO;
    String CLSUBCLASS;
    String CLSUBCODE;
    String CLLEDGER_CHK;
    String CLCODE_TARGET;
    String CLTAXDCYUL;
    String CLSAUP_IMG_URL;

    String MOBILE_USER_NAME;
    String MOBILE_USER_PHONE;
    String MOBILE_COMPANY_NAME;
    String MOBILE_COMPANY_CORPNO;
    String MOBILE_PASSWORD;

    String USER_NAME;
    String USER_PHONE;
    String COMPANY_NAME;
    String COMPANY_CORPNO;
    String PASSWORD;
    String FILE_NO1;

    String USER_MODE; // 2021-11-08 추가...
    String 외상한도설정금액;
    String 관심품목갯수;
    String 최종업데이트일시;

    String LAT;
    String LON;
    String DISTANCE_ME;
    String IMAGE_CNT_TOT;
    String IMAGE_CNT_TODAY;

    public String getGEONUM() {
        return StringUtil.getNvlStr(GEONUM).toString();
    }

    public void setGEONUM(String GEONUM) {
        this.GEONUM = GEONUM;
    }

    public String getCLCODE() {
        return StringUtil.getNvlStr(CLCODE).toString();
    }

    public void setCLCODE(String CLCODE) {
        this.CLCODE = CLCODE;
    }

    public String getCLUSER() {
        return StringUtil.getNvlStr(CLUSER).toString();
    }

    public void setCLUSER(String CLUSER) {
        this.CLUSER = CLUSER;
    }

    public String getCLNAME() {
        return StringUtil.getNvlStr(CLNAME).toString();
    }

    public void setCLNAME(String CLNAME) {
        this.CLNAME = CLNAME;
    }

    public String getCLGUBUN() {
        return StringUtil.getNvlStr(CLGUBUN).toString();
    }

    public void setCLGUBUN(String CLGUBUN) {
        this.CLGUBUN = CLGUBUN;
    }

    public String getCLVISIBLE() {
        return StringUtil.getNvlStr(CLVISIBLE).toString();
    }

    public void setCLVISIBLE(String CLVISIBLE) {
        this.CLVISIBLE = CLVISIBLE;
    }

    public String getCLSANGHO() {
        return StringUtil.getNvlStr(CLSANGHO).toString();
    }

    public void setCLSANGHO(String CLSANGHO) {
        this.CLSANGHO = CLSANGHO;
    }

    public String getCLSAUPNO() {
        return StringUtil.getNvlStr(CLSAUPNO).toString();
    }

    public void setCLSAUPNO(String CLSAUPNO) {
        this.CLSAUPNO = CLSAUPNO;
    }

    public String getCLCEO() {
        return StringUtil.getNvlStr(CLCEO).toString();
    }

    public void setCLCEO(String CLCEO) {
        this.CLCEO = CLCEO;
    }

    public String getCLPOST() {
        return StringUtil.getNvlStr(CLPOST).toString();
    }

    public void setCLPOST(String CLPOST) {
        this.CLPOST = CLPOST;
    }

    public String getCLJUSO1() {
        return StringUtil.getNvlStr(CLJUSO1).toString();
    }

    public void setCLJUSO1(String CLJUSO1) {
        this.CLJUSO1 = CLJUSO1;
    }

    public String getCLJUSO2() {
        return StringUtil.getNvlStr(CLJUSO2).toString();
    }

    public void setCLJUSO2(String CLJUSO2) {
        this.CLJUSO2 = CLJUSO2;
    }

    public String getCLUPTAE() {
        return StringUtil.getNvlStr(CLUPTAE).toString();
    }

    public void setCLUPTAE(String CLUPTAE) {
        this.CLUPTAE = CLUPTAE;
    }

    public String getCLJONG() {
        return StringUtil.getNvlStr(CLJONG).toString();
    }

    public void setCLJONG(String CLJONG) {
        this.CLJONG = CLJONG;
    }

    public String getCLTEL() {
        return StringUtil.getNvlStr(CLTEL).toString();
    }

    public void setCLTEL(String CLTEL) {
        this.CLTEL = CLTEL;
    }

    public String getCLPHONE() {
        return StringUtil.getNvlStr(CLPHONE).toString();
    }

    public void setCLPHONE(String CLPHONE) {
        this.CLPHONE = CLPHONE;
    }

    public String getCLFAX() {
        return StringUtil.getNvlStr(CLFAX).toString();
    }

    public void setCLFAX(String CLFAX) {
        this.CLFAX = CLFAX;
    }

    public String getCLBIGO() {
        return StringUtil.getNvlStr(CLBIGO).toString();
    }

    public void setCLBIGO(String CLBIGO) {
        this.CLBIGO = CLBIGO;
    }

    public String getCLBANKNO1() {
        return StringUtil.getNvlStr(CLBANKNO1).toString();
    }

    public void setCLBANKNO1(String CLBANKNO1) {
        this.CLBANKNO1 = CLBANKNO1;
    }

    public String getCLBANKNAME1() {
        return StringUtil.getNvlStr(CLBANKNAME1).toString();
    }

    public void setCLBANKNAME1(String CLBANKNAME1) {
        this.CLBANKNAME1 = CLBANKNAME1;
    }

    public String getCLSENDNAME1() {
        return StringUtil.getNvlStr(CLSENDNAME1).toString();
    }

    public void setCLSENDNAME1(String CLSENDNAME1) {
        this.CLSENDNAME1 = CLSENDNAME1;
    }

    public String getCLBANKNO2() {
        return StringUtil.getNvlStr(CLBANKNO2).toString();
    }

    public void setCLBANKNO2(String CLBANKNO2) {
        this.CLBANKNO2 = CLBANKNO2;
    }

    public String getCLBANKNAME2() {
        return StringUtil.getNvlStr(CLBANKNAME2).toString();
    }

    public void setCLBANKNAME2(String CLBANKNAME2) {
        this.CLBANKNAME2 = CLBANKNAME2;
    }

    public String getCLSENDNAME2() {
        return StringUtil.getNvlStr(CLSENDNAME2).toString();
    }

    public void setCLSENDNAME2(String CLSENDNAME2) {
        this.CLSENDNAME2 = CLSENDNAME2;
    }

    public String getCLEMPNAME() {
        return StringUtil.getNvlStr(CLEMPNAME).toString();
    }

    public void setCLEMPNAME(String CLEMPNAME) {
        this.CLEMPNAME = CLEMPNAME;
    }

    public String getCLDANSEL() {
        return StringUtil.getNvlStr(CLDANSEL).toString();
    }

    public void setCLDANSEL(String CLDANSEL) {
        this.CLDANSEL = CLDANSEL;
    }

    public String getCLSALECLASS() {
        return StringUtil.getNvlStr(CLSALECLASS).toString();
    }

    public void setCLSALECLASS(String CLSALECLASS) {
        this.CLSALECLASS = CLSALECLASS;
    }

    public String getCLBASESTORE() {
        return StringUtil.getNvlStr(CLBASESTORE).toString();
    }

    public void setCLBASESTORE(String CLBASESTORE) {
        this.CLBASESTORE = CLBASESTORE;
    }

    public String getCLCASHDATE() {
        return StringUtil.getNvlStr(CLCASHDATE).toString();
    }

    public void setCLCASHDATE(String CLCASHDATE) {
        this.CLCASHDATE = CLCASHDATE;
    }

    public String getCLTAXCREATE_YN() {
        return StringUtil.getNvlStr(CLTAXCREATE_YN).toString();
    }

    public void setCLTAXCREATE_YN(String CLTAXCREATE_YN) {
        this.CLTAXCREATE_YN = CLTAXCREATE_YN;
    }

    public String getCLCREDIT_AMT() {
        return StringUtil.getNvlStr(CLCREDIT_AMT).toString();
    }

    public void setCLCREDIT_AMT(String CLCREDIT_AMT) {
        this.CLCREDIT_AMT = CLCREDIT_AMT;
    }

    public String getCLTAXSALE_YN() {
        return StringUtil.getNvlStr(CLTAXSALE_YN).toString();
    }

    public void setCLTAXSALE_YN(String CLTAXSALE_YN) {
        this.CLTAXSALE_YN = CLTAXSALE_YN;
    }

    public String getCLMISUYN() {
        return StringUtil.getNvlStr(CLMISUYN).toString();
    }

    public void setCLMISUYN(String CLMISUYN) {
        this.CLMISUYN = CLMISUYN;
    }

    public String getCLSAWON1() {
        return StringUtil.getNvlStr(CLSAWON1).toString();
    }

    public void setCLSAWON1(String CLSAWON1) {
        this.CLSAWON1 = CLSAWON1;
    }

    public String getCLSATEL1() {
        return StringUtil.getNvlStr(CLSATEL1).toString();
    }

    public void setCLSATEL1(String CLSATEL1) {
        this.CLSATEL1 = CLSATEL1;
    }

    public String getCLSAWON2() {
        return StringUtil.getNvlStr(CLSAWON2).toString();
    }

    public void setCLSAWON2(String CLSAWON2) {
        this.CLSAWON2 = CLSAWON2;
    }

    public String getCLSATEL2() {
        return StringUtil.getNvlStr(CLSATEL2).toString();
    }

    public void setCLSATEL2(String CLSATEL2) {
        this.CLSATEL2 = CLSATEL2;
    }

    public String getCLS_NAME1() {
        return StringUtil.getNvlStr(CLS_NAME1).toString();
    }

    public void setCLS_NAME1(String CLS_NAME1) {
        this.CLS_NAME1 = CLS_NAME1;
    }

    public String getCLS_NAME2() {
        return StringUtil.getNvlStr(CLS_NAME2).toString();
    }

    public void setCLS_NAME2(String CLS_NAME2) {
        this.CLS_NAME2 = CLS_NAME2;
    }

    public String getCLS_NAME3() {
        return StringUtil.getNvlStr(CLS_NAME3).toString();
    }

    public void setCLS_NAME3(String CLS_NAME3) {
        this.CLS_NAME3 = CLS_NAME3;
    }

    public String getCLS_NAME4() {
        return StringUtil.getNvlStr(CLS_NAME4).toString();
    }

    public void setCLS_NAME4(String CLS_NAME4) {
        this.CLS_NAME4 = CLS_NAME4;
    }

    public String getCLS_NAME5() {
        return StringUtil.getNvlStr(CLS_NAME5).toString();
    }

    public void setCLS_NAME5(String CLS_NAME5) {
        this.CLS_NAME5 = CLS_NAME5;
    }

    public String getCLS_NAME6() {
        return StringUtil.getNvlStr(CLS_NAME6).toString();
    }

    public void setCLS_NAME6(String CLS_NAME6) {
        this.CLS_NAME6 = CLS_NAME6;
    }

    public String getCLS_NAME7() {
        return StringUtil.getNvlStr(CLS_NAME7).toString();
    }

    public void setCLS_NAME7(String CLS_NAME7) {
        this.CLS_NAME7 = CLS_NAME7;
    }

    public String getCLS_NAME8() {
        return StringUtil.getNvlStr(CLS_NAME8).toString();
    }

    public void setCLS_NAME8(String CLS_NAME8) {
        this.CLS_NAME8 = CLS_NAME8;
    }

    public String getCLS_NAME9() {
        return StringUtil.getNvlStr(CLS_NAME9).toString();
    }

    public void setCLS_NAME9(String CLS_NAME9) {
        this.CLS_NAME9 = CLS_NAME9;
    }

    public String getCLS_NAME10() {
        return StringUtil.getNvlStr(CLS_NAME10).toString();
    }

    public void setCLS_NAME10(String CLS_NAME10) {
        this.CLS_NAME10 = CLS_NAME10;
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

    public String getCLJUMIN() {
        return StringUtil.getNvlStr(CLJUMIN).toString();
    }

    public void setCLJUMIN(String CLJUMIN) {
        this.CLJUMIN = CLJUMIN;
    }

    public String getCLBANKNO() {
        return StringUtil.getNvlStr(CLBANKNO).toString();
    }

    public void setCLBANKNO(String CLBANKNO) {
        this.CLBANKNO = CLBANKNO;
    }

    public String getCLEMP_MEIP() {
        return StringUtil.getNvlStr(CLEMP_MEIP).toString();
    }

    public void setCLEMP_MEIP(String CLEMP_MEIP) {
        this.CLEMP_MEIP = CLEMP_MEIP;
    }

    public String getCLLAST_DATE() {
        return StringUtil.getNvlStr(CLLAST_DATE).toString();
    }

    public void setCLLAST_DATE(String CLLAST_DATE) {
        this.CLLAST_DATE = CLLAST_DATE;
    }

    public String getCLID() {
        return StringUtil.getNvlStr(CLID).toString();
    }

    public void setCLID(String CLID) {
        this.CLID = CLID;
    }

    public String getCLEMAIL() {
        return StringUtil.getNvlStr(CLEMAIL).toString();
    }

    public void setCLEMAIL(String CLEMAIL) {
        this.CLEMAIL = CLEMAIL;
    }

    public String getCLBANKNAME() {
        return StringUtil.getNvlStr(CLBANKNAME).toString();
    }

    public void setCLBANKNAME(String CLBANKNAME) {
        this.CLBANKNAME = CLBANKNAME;
    }

    public String getCLCLASS() {
        return StringUtil.getNvlStr(CLCLASS).toString();
    }

    public void setCLCLASS(String CLCLASS) {
        this.CLCLASS = CLCLASS;
    }

    public String getCLOPEN_DATE() {
        return StringUtil.getNvlStr(CLOPEN_DATE).toString();
    }

    public void setCLOPEN_DATE(String CLOPEN_DATE) {
        this.CLOPEN_DATE = CLOPEN_DATE;
    }

    public String getCLSAUP_JUSO() {
        return StringUtil.getNvlStr(CLSAUP_JUSO).toString();
    }

    public void setCLSAUP_JUSO(String CLSAUP_JUSO) {
        this.CLSAUP_JUSO = CLSAUP_JUSO;
    }

    public String getCLMEMO() {
        return StringUtil.getNvlStr(CLMEMO).toString();
    }

    public void setCLMEMO(String CLMEMO) {
        this.CLMEMO = CLMEMO;
    }

    public String getCLFIRST_DANSEL() {
        return StringUtil.getNvlStr(CLFIRST_DANSEL).toString();
    }

    public void setCLFIRST_DANSEL(String CLFIRST_DANSEL) {
        this.CLFIRST_DANSEL = CLFIRST_DANSEL;
    }

    public String getCLPW() {
        return StringUtil.getNvlStr(CLPW).toString();
    }

    public void setCLPW(String CLPW) {
        this.CLPW = CLPW;
    }

    public String getTaxMail() {
        return StringUtil.getNvlStr(TaxMail).toString();
    }

    public void setTaxMail(String taxMail) {
        TaxMail = taxMail;
    }

    public String getComMail() {
        return StringUtil.getNvlStr(ComMail).toString();
    }

    public void setComMail(String comMail) {
        ComMail = comMail;
    }

    public String getCLEMPNAME2() {
        return StringUtil.getNvlStr(CLEMPNAME2).toString();
    }

    public void setCLEMPNAME2(String CLEMPNAME2) {
        this.CLEMPNAME2 = CLEMPNAME2;
    }

    public String getCLSHOTNAME() {
        return StringUtil.getNvlStr(CLSHOTNAME).toString();
    }

    public void setCLSHOTNAME(String CLSHOTNAME) {
        this.CLSHOTNAME = CLSHOTNAME;
    }

    public String getCLCHOSEONG() {
        return StringUtil.getNvlStr(CLCHOSEONG).toString();
    }

    public void setCLCHOSEONG(String CLCHOSEONG) {
        this.CLCHOSEONG = CLCHOSEONG;
    }

    public String getCLPOST2() {
        return StringUtil.getNvlStr(CLPOST2).toString();
    }

    public void setCLPOST2(String CLPOST2) {
        this.CLPOST2 = CLPOST2;
    }

    public String getCLJUSO3() {
        return StringUtil.getNvlStr(CLJUSO3).toString();
    }

    public void setCLJUSO3(String CLJUSO3) {
        this.CLJUSO3 = CLJUSO3;
    }

    public String getCLJUSO4() {
        return StringUtil.getNvlStr(CLJUSO4).toString();
    }

    public void setCLJUSO4(String CLJUSO4) {
        this.CLJUSO4 = CLJUSO4;
    }

    public String getSUCODE() {
        return StringUtil.getNvlStr(SUCODE).toString();
    }

    public void setSUCODE(String SUCODE) {
        this.SUCODE = SUCODE;
    }

    public String getCLBARCODEYN() {
        return StringUtil.getNvlStr(CLBARCODEYN).toString();
    }

    public void setCLBARCODEYN(String CLBARCODEYN) {
        this.CLBARCODEYN = CLBARCODEYN;
    }

    public String getCLEDAN() {
        return StringUtil.getNvlStr(CLEDAN).toString();
    }

    public void setCLEDAN(String CLEDAN) {
        this.CLEDAN = CLEDAN;
    }

    public String getCLEDATE() {
        return StringUtil.getNvlStr(CLEDATE).toString();
    }

    public void setCLEDATE(String CLEDATE) {
        this.CLEDATE = CLEDATE;
    }

    public String getCL_BMCODE() {
        return StringUtil.getNvlStr(CL_BMCODE).toString();
    }

    public void setCL_BMCODE(String CL_BMCODE) {
        this.CL_BMCODE = CL_BMCODE;
    }

    public String getCLPUB_SUCODE() {
        return StringUtil.getNvlStr(CLPUB_SUCODE).toString();
    }

    public void setCLPUB_SUCODE(String CLPUB_SUCODE) {
        this.CLPUB_SUCODE = CLPUB_SUCODE;
    }

    public String getCLETAXCLASS() {
        return StringUtil.getNvlStr(CLETAXCLASS).toString();
    }

    public void setCLETAXCLASS(String CLETAXCLASS) {
        this.CLETAXCLASS = CLETAXCLASS;
    }

    public String getCLETAXREGCHK() {
        return StringUtil.getNvlStr(CLETAXREGCHK).toString();
    }

    public void setCLETAXREGCHK(String CLETAXREGCHK) {
        this.CLETAXREGCHK = CLETAXREGCHK;
    }

    public String getCLETAXSAWON() {
        return StringUtil.getNvlStr(CLETAXSAWON).toString();
    }

    public void setCLETAXSAWON(String CLETAXSAWON) {
        this.CLETAXSAWON = CLETAXSAWON;
    }

    public String getCLETAXTEL() {
        return StringUtil.getNvlStr(CLETAXTEL).toString();
    }

    public void setCLETAXTEL(String CLETAXTEL) {
        this.CLETAXTEL = CLETAXTEL;
    }

    public String getCLLIMIT_IPAMT() {
        return StringUtil.getNvlStr(CLLIMIT_IPAMT).toString();
    }

    public void setCLLIMIT_IPAMT(String CLLIMIT_IPAMT) {
        this.CLLIMIT_IPAMT = CLLIMIT_IPAMT;
    }

    public String getCLCORPNO() {
        return StringUtil.getNvlStr(CLCORPNO).toString();
    }

    public void setCLCORPNO(String CLCORPNO) {
        this.CLCORPNO = CLCORPNO;
    }

    public String getCLITPOINT() {
        return StringUtil.getNvlStr(CLITPOINT).toString();
    }

    public void setCLITPOINT(String CLITPOINT) {
        this.CLITPOINT = CLITPOINT;
    }

    public String getCLPOINT() {
        return StringUtil.getNvlStr(CLPOINT).toString();
    }

    public void setCLPOINT(String CLPOINT) {
        this.CLPOINT = CLPOINT;
    }

    public String getCLLOGINYN() {
        return StringUtil.getNvlStr(CLLOGINYN).toString();
    }

    public void setCLLOGINYN(String CLLOGINYN) {
        this.CLLOGINYN = CLLOGINYN;
    }

    public String getCLSJDATE() {
        return StringUtil.getNvlStr(CLSJDATE).toString();
    }

    public void setCLSJDATE(String CLSJDATE) {
        this.CLSJDATE = CLSJDATE;
    }

    public String getCLIPDATE() {
        return StringUtil.getNvlStr(CLIPDATE).toString();
    }

    public void setCLIPDATE(String CLIPDATE) {
        this.CLIPDATE = CLIPDATE;
    }

    public String getCLSALE_STOP() {
        return StringUtil.getNvlStr(CLSALE_STOP).toString();
    }

    public void setCLSALE_STOP(String CLSALE_STOP) {
        this.CLSALE_STOP = CLSALE_STOP;
    }

    public String getCLSALE_MONTH1() {
        return StringUtil.getNvlStr(CLSALE_MONTH1).toString();
    }

    public void setCLSALE_MONTH1(String CLSALE_MONTH1) {
        this.CLSALE_MONTH1 = CLSALE_MONTH1;
    }

    public String getCLSALE_DAY1() {
        return StringUtil.getNvlStr(CLSALE_DAY1).toString();
    }

    public void setCLSALE_DAY1(String CLSALE_DAY1) {
        this.CLSALE_DAY1 = CLSALE_DAY1;
    }

    public String getCLSALE_MONTH2() {
        return StringUtil.getNvlStr(CLSALE_MONTH2).toString();
    }

    public void setCLSALE_MONTH2(String CLSALE_MONTH2) {
        this.CLSALE_MONTH2 = CLSALE_MONTH2;
    }

    public String getCLSALE_DAY2() {
        return StringUtil.getNvlStr(CLSALE_DAY2).toString();
    }

    public void setCLSALE_DAY2(String CLSALE_DAY2) {
        this.CLSALE_DAY2 = CLSALE_DAY2;
    }

    public String getCLPAYMENT_MONTH1() {
        return StringUtil.getNvlStr(CLPAYMENT_MONTH1).toString();
    }

    public void setCLPAYMENT_MONTH1(String CLPAYMENT_MONTH1) {
        this.CLPAYMENT_MONTH1 = CLPAYMENT_MONTH1;
    }

    public String getCLPAYMENT_DAY1() {
        return StringUtil.getNvlStr(CLPAYMENT_DAY1).toString();
    }

    public void setCLPAYMENT_DAY1(String CLPAYMENT_DAY1) {
        this.CLPAYMENT_DAY1 = CLPAYMENT_DAY1;
    }

    public String getCLPAYMENT_MONTH2() {
        return StringUtil.getNvlStr(CLPAYMENT_MONTH2).toString();
    }

    public void setCLPAYMENT_MONTH2(String CLPAYMENT_MONTH2) {
        this.CLPAYMENT_MONTH2 = CLPAYMENT_MONTH2;
    }

    public String getCLPAYMENT_DAY2() {
        return StringUtil.getNvlStr(CLPAYMENT_DAY2).toString();
    }

    public void setCLPAYMENT_DAY2(String CLPAYMENT_DAY2) {
        this.CLPAYMENT_DAY2 = CLPAYMENT_DAY2;
    }

    public String getCLDCYUL() {
        return StringUtil.getNvlStr(CLDCYUL).toString();
    }

    public void setCLDCYUL(String CLDCYUL) {
        this.CLDCYUL = CLDCYUL;
    }

    public String getCLEVENTYN() {
        return StringUtil.getNvlStr(CLEVENTYN).toString();
    }

    public void setCLEVENTYN(String CLEVENTYN) {
        this.CLEVENTYN = CLEVENTYN;
    }

    public String getCLPOINTYN() {
        return StringUtil.getNvlStr(CLPOINTYN).toString();
    }

    public void setCLPOINTYN(String CLPOINTYN) {
        this.CLPOINTYN = CLPOINTYN;
    }

    public String getCLEQUIPYN() {
        return StringUtil.getNvlStr(CLEQUIPYN).toString();
    }

    public void setCLEQUIPYN(String CLEQUIPYN) {
        this.CLEQUIPYN = CLEQUIPYN;
    }

    public String getCLTAXZERO() {
        return StringUtil.getNvlStr(CLTAXZERO).toString();
    }

    public void setCLTAXZERO(String CLTAXZERO) {
        this.CLTAXZERO = CLTAXZERO;
    }

    public String getCLSUBCLASS() {
        return StringUtil.getNvlStr(CLSUBCLASS).toString();
    }

    public void setCLSUBCLASS(String CLSUBCLASS) {
        this.CLSUBCLASS = CLSUBCLASS;
    }

    public String getCLSUBCODE() {
        return StringUtil.getNvlStr(CLSUBCODE).toString();
    }

    public void setCLSUBCODE(String CLSUBCODE) {
        this.CLSUBCODE = CLSUBCODE;
    }

    public String getCLLEDGER_CHK() {
        return StringUtil.getNvlStr(CLLEDGER_CHK).toString();
    }

    public void setCLLEDGER_CHK(String CLLEDGER_CHK) {
        this.CLLEDGER_CHK = CLLEDGER_CHK;
    }

    public String getCLCODE_TARGET() {
        return StringUtil.getNvlStr(CLCODE_TARGET).toString();
    }

    public void setCLCODE_TARGET(String CLCODE_TARGET) {
        this.CLCODE_TARGET = CLCODE_TARGET;
    }

    public String getCLTAXDCYUL() {
        return StringUtil.getNvlStr(CLTAXDCYUL).toString();
    }

    public void setCLTAXDCYUL(String CLTAXDCYUL) {
        this.CLTAXDCYUL = CLTAXDCYUL;
    }

    public String getCLSAUP_IMG_URL() {
        return StringUtil.getNvlStr(CLSAUP_IMG_URL).toString();
    }

    public void setCLSAUP_IMG_URL(String CLSAUP_IMG_URL) {
        this.CLSAUP_IMG_URL = CLSAUP_IMG_URL;
    }

    public String getMOBILE_USER_NAME() {
        return StringUtil.getNvlStr(MOBILE_USER_NAME).toString();
    }

    public void setMOBILE_USER_NAME(String MOBILE_USER_NAME) {
        this.MOBILE_USER_NAME = MOBILE_USER_NAME;
    }

    public String getMOBILE_USER_PHONE() {
        return StringUtil.getNvlStr(MOBILE_USER_PHONE).toString();
    }

    public void setMOBILE_USER_PHONE(String MOBILE_USER_PHONE) {
        this.MOBILE_USER_PHONE = MOBILE_USER_PHONE;
    }

    public String getMOBILE_COMPANY_NAME() {
        return StringUtil.getNvlStr(MOBILE_COMPANY_NAME).toString();
    }

    public void setMOBILE_COMPANY_NAME(String MOBILE_COMPANY_NAME) {
        this.MOBILE_COMPANY_NAME = MOBILE_COMPANY_NAME;
    }

    public String getMOBILE_COMPANY_CORPNO() {
        return StringUtil.getNvlStr(MOBILE_COMPANY_CORPNO).toString();
    }

    public void setMOBILE_COMPANY_CORPNO(String MOBILE_COMPANY_CORPNO) {
        this.MOBILE_COMPANY_CORPNO = MOBILE_COMPANY_CORPNO;
    }

    public String getMOBILE_PASSWORD() {
        return StringUtil.getNvlStr(MOBILE_PASSWORD).toString();
    }

    public void setMOBILE_PASSWORD(String MOBILE_PASSWORD) {
        this.MOBILE_PASSWORD = MOBILE_PASSWORD;
    }


    public String getUSER_MODE() {
        return StringUtil.getNvlStr(USER_MODE).toString();
    }

    public void setUSER_MODE(String USER_MODE) {
        this.USER_MODE = USER_MODE;
    }


    public String getUSER_NAME() {
        return StringUtil.getNvlStr(USER_NAME).toString();
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_PHONE() {
        return StringUtil.getNvlStr(USER_PHONE).toString();
    }

    public void setUSER_PHONE(String USER_PHONE) {
        this.USER_PHONE = USER_PHONE;
    }

    public String getCOMPANY_NAME() {
        return StringUtil.getNvlStr(COMPANY_NAME).toString();
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getCOMPANY_CORPNO() {
        return StringUtil.getNvlStr(COMPANY_CORPNO).toString();
    }

    public void setCOMPANY_CORPNO(String COMPANY_CORPNO) {
        this.COMPANY_CORPNO = COMPANY_CORPNO;
    }

    public String getPASSWORD() {
        return StringUtil.getNvlStr(PASSWORD).toString();
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getFILE_NO1() {
        return StringUtil.getNvlStr(FILE_NO1).toString();
    }

    public void setFILE_NO1(String FILE_NO1) {
        this.FILE_NO1 = FILE_NO1;
    }

    public String get외상한도설정금액() {
        return StringUtil.getNvlStr(외상한도설정금액).toString();
    }

    public void set외상한도설정금액(String 외상한도설정금액) {
        this.외상한도설정금액 = 외상한도설정금액;
    }

    public String get관심품목갯수() {
        return StringUtil.getNvlStr(관심품목갯수).toString();
    }

    public void set관심품목갯수(String 관심품목갯수) {
        this.관심품목갯수 = 관심품목갯수;
    }

    public String get최종업데이트일시() {
        return StringUtil.getNvlStr(최종업데이트일시).toString();
    }

    public void set최종업데이트일시(String 최종업데이트일시) {
        this.최종업데이트일시 = 최종업데이트일시;
    }

    public String getLAT() {
        return StringUtil.getNvlStr(LAT).toString();
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLON() {
        return StringUtil.getNvlStr(LON).toString();
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getDISTANCE_ME() {
        return StringUtil.getNvlStr(DISTANCE_ME).toString();
    }

    public void setDISTANCE_ME(String DISTANCE_ME) {
        this.DISTANCE_ME = DISTANCE_ME;
    }

    public String getIMAGE_CNT_TOT() {
        return StringUtil.getNvlStr(IMAGE_CNT_TOT).toString();
    }

    public void setIMAGE_CNT_TOT(String IMAGE_CNT_TOT) {
        this.IMAGE_CNT_TOT = IMAGE_CNT_TOT;
    }

    public String getIMAGE_CNT_TODAY() {
        return StringUtil.getNvlStr(IMAGE_CNT_TODAY).toString();
    }

    public void setIMAGE_CNT_TODAY(String IMAGE_CNT_TODAY) {
        this.IMAGE_CNT_TODAY = IMAGE_CNT_TODAY;
    }
}
