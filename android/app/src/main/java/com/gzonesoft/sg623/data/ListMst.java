package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

/**
 * 배송지목록 - 최종 차수정보 관리
 */
public class ListMst {

    public String DELIVERY_DT;
    public String DELIVERY_TOT_CNT;
    public String DRIVER_NM;
    public String DRIVERTEL_TXT;
    public String END_TM;
    public String LAST_SHIFT_ID;
    public String ROUTE_CONFIRM_YN;
    public String ROUTE_OPTI_YN;
    public String SHIFT_ID;
    public String SHIFT_NM;
    public String START_TM;
    public String STORE_ID;
    public String STORE_NM;
    public String VAN_END_FLAG;
    public String VAN_END_TIME;
    public String VAN_ID;
    public String VAN_START_FLAG;
    public String VAN_START_TIME;


    public String getDELIVERY_DT() {
        return StringUtil.getNvlStr(DELIVERY_DT).toString();
    }
    public void setDELIVERY_DT(String DELIVERY_DT) {
        this.DELIVERY_DT = DELIVERY_DT;
    }

    public String getDELIVERY_TOT_CNT() {
        return StringUtil.getNvlStr(DELIVERY_TOT_CNT).toString();
    }
    public void setDELIVERY_TOT_CNT(String DELIVERY_TOT_CNT) {
        this.DELIVERY_TOT_CNT = DELIVERY_TOT_CNT;
    }

    public String getDRIVER_NM() {
        return StringUtil.getNvlStr(DRIVER_NM).toString();
    }
    public void setDRIVER_NM(String DRIVER_NM) {
        this.DRIVER_NM = DRIVER_NM;
    }

    public String getDRIVERTEL_TXT() {
        return StringUtil.getNvlStr(DRIVERTEL_TXT).toString();
    }
    public void setDRIVERTEL_TXT(String DRIVERTEL_TXT) {
        this.DRIVERTEL_TXT = DRIVERTEL_TXT;
    }

    public String getEND_TM() {
        return StringUtil.getNvlStr(END_TM).toString();
    }
    public void setEND_TM(String END_TM) {
        this.END_TM = END_TM;
    }

    public String getLAST_SHIFT_ID() {
        return StringUtil.getNvlStr(LAST_SHIFT_ID).toString();
    }
    public void setLAST_SHIFT_ID(String LAST_SHIFT_ID) {
        this.LAST_SHIFT_ID = LAST_SHIFT_ID;
    }

    public String getROUTE_CONFIRM_YN() {
        return StringUtil.getNvlStr(ROUTE_CONFIRM_YN).toString();
    }
    public void setROUTE_CONFIRM_YN(String ROUTE_CONFIRM_YN) {
        this.ROUTE_CONFIRM_YN = ROUTE_CONFIRM_YN;
    }

    public String getROUTE_OPTI_YN() {
        return StringUtil.getNvlStr(ROUTE_OPTI_YN).toString();
    }
    public void setROUTE_OPTI_YN(String ROUTE_OPTI_YN) {
        this.ROUTE_OPTI_YN = ROUTE_OPTI_YN;
    }

    public String getSHIFT_ID() {
        return StringUtil.getNvlStr(SHIFT_ID).toString();
    }
    public void setSHIFT_ID(String SHIFT_ID) {
        this.SHIFT_ID = SHIFT_ID;
    }

    public String getSHIFT_NM() {
        return StringUtil.getNvlStr(SHIFT_NM).toString();
    }
    public void setSHIFT_NM(String SHIFT_NM) {
        this.SHIFT_NM = SHIFT_NM;
    }

    public String getSTART_TM() {
        return StringUtil.getNvlStr(START_TM).toString();
    }
    public void setSTART_TM(String START_TM) {
        this.START_TM = START_TM;
    }

    public String getSTORE_ID() {
        return StringUtil.getNvlStr(STORE_ID).toString();
    }
    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public String getSTORE_NM() {
        return StringUtil.getNvlStr(STORE_NM).toString();
    }
    public void setSTORE_NM(String STORE_NM) {
        this.STORE_NM = STORE_NM;
    }

    public String getVAN_END_FLAG() {
        return StringUtil.getNvlStr(VAN_END_FLAG).toString();
    }
    public void setVAN_END_FLAG(String VAN_END_FLAG) {
        this.VAN_END_FLAG = VAN_END_FLAG;
    }

    public String getVAN_END_TIME() {
        return StringUtil.getNvlStr(VAN_END_TIME).toString();
    }
    public void setVAN_END_TIME(String VAN_END_TIME) {
        this.VAN_END_TIME = VAN_END_TIME;
    }

    public String getVAN_ID() {
        return StringUtil.getNvlStr(VAN_ID).toString();
    }
    public void setVAN_ID(String VAN_ID) {
        this.VAN_ID = VAN_ID;
    }

    public String getVAN_START_FLAG() {
        return StringUtil.getNvlStr(VAN_START_FLAG).toString();
    }
    public void setVAN_START_FLAG(String VAN_START_FLAG) {
        this.VAN_START_FLAG = VAN_START_FLAG;
    }

    public String getVAN_START_TIME() {
        return StringUtil.getNvlStr(VAN_START_TIME).toString();
    }
    public void setVAN_START_TIME(String VAN_START_TIME) {
        this.VAN_START_TIME = VAN_START_TIME;
    }


    @Override
    public String toString() {
        return "ListMst{" + "\n" +
                "DELIVERY_DT='" + DELIVERY_DT + '\'' + "\n" +
                ", DELIVERY_TOT_CNT='" + DELIVERY_TOT_CNT + '\'' + "\n" +
                ", DRIVER_NM='" + DRIVER_NM + '\'' + "\n" +
                ", DRIVERTEL_TXT='" + DRIVERTEL_TXT + '\'' + "\n" +
                ", END_TM='" + END_TM + '\'' + "\n" +
                ", LAST_SHIFT_ID='" + LAST_SHIFT_ID + '\'' + "\n" +
                ", ROUTE_CONFIRM_YN='" + ROUTE_CONFIRM_YN + '\'' + "\n" +
                ", ROUTE_OPTI_YN='" + ROUTE_OPTI_YN + '\'' + "\n" +
                ", SHIFT_ID='" + SHIFT_ID + '\'' + "\n" +
                ", SHIFT_NM='" + SHIFT_NM + '\'' + "\n" +
                ", START_TM='" + START_TM + '\'' + "\n" +
                ", STORE_ID='" + STORE_ID + '\'' + "\n" +
                ", STORE_NM='" + STORE_NM + '\'' + "\n" +
                ", VAN_END_FLAG='" + VAN_END_FLAG + '\'' + "\n" +
                ", VAN_END_TIME='" + VAN_END_TIME + '\'' + "\n" +
                ", VAN_ID='" + VAN_ID + '\'' + "\n" +
                ", VAN_START_FLAG='" + VAN_START_FLAG + '\'' + "\n" +
                ", VAN_START_TIME='" + VAN_START_TIME + '\'' + "\n" +
                '}';
    }
}
