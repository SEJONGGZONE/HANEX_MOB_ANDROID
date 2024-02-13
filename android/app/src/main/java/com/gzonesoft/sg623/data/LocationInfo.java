package com.gzonesoft.sg623.data;

import com.gzonesoft.sg623.util.StringUtil;

public class LocationInfo {

    String I_DEVICE_ID = "";
    String I_TERMINAL_NO = "";
    String I_POS_X = "";
    String I_POS_Y = "";
    String I_GPS_DTM = "";
    String I_SPEED = "";
    String I_ANGEL = "";
    String I_BATTERY_LEVEL = "";
    String I_ADDRESS_INFO = "";
    String I_ETC_DATA = "";
    String I_REPORT_DTM = "";
    String I_DISTANCE_TO = "";
    String I_SEQ = "";
    String I_CHECK_DISTANCE = "";
    String I_CHECK_TIME = "";
    String I_CHECK_SPEED = "";


    public String getI_DEVICE_ID() {
        return StringUtil.getNvlStr(I_DEVICE_ID);
    }

    public void setI_DEVICE_ID(String i_DEVICE_ID) {
        I_DEVICE_ID = i_DEVICE_ID;
    }

    public String getI_TERMINAL_NO() {
        return StringUtil.getNvlStr(I_TERMINAL_NO);
    }

    public void setI_TERMINAL_NO(String i_TERMINAL_NO) {
        I_TERMINAL_NO = i_TERMINAL_NO;
    }

    public String getI_POS_X() {
        return StringUtil.getNvlStr(I_POS_X);
    }

    public void setI_POS_X(String i_POS_X) {
        I_POS_X = i_POS_X;
    }

    public String getI_POS_Y() {
        return StringUtil.getNvlStr(I_POS_Y);
    }

    public void setI_POS_Y(String i_POS_Y) {
        I_POS_Y = i_POS_Y;
    }

    public String getI_GPS_DTM() {
        return StringUtil.getNvlStr(I_GPS_DTM);
    }

    public void setI_GPS_DTM(String i_GPS_DTM) {
        I_GPS_DTM = i_GPS_DTM;
    }

    public String getI_SPEED() {
        return StringUtil.getNvlStr(I_SPEED);
    }


    public void setI_SPEED(String i_SPEED) {
        I_SPEED = i_SPEED;
    }

    public String getI_ANGEL() {
        return StringUtil.getNvlStr(I_ANGEL);
    }

    public void setI_ANGEL(String i_ANGEL) {
        I_ANGEL = i_ANGEL;
    }

    public String getI_BATTERY_LEVEL() {
        return StringUtil.getNvlStr(I_BATTERY_LEVEL);
    }

    public void setI_BATTERY_LEVEL(String i_BATTERY_LEVEL) {
        I_BATTERY_LEVEL = i_BATTERY_LEVEL;
    }

    public String getI_ADDRESS_INFO() {
        return StringUtil.getNvlStr(I_ADDRESS_INFO);
    }

    public void setI_ADDRESS_INFO(String i_ADDRESS_INFO) {
        I_ADDRESS_INFO = i_ADDRESS_INFO;
    }

    public String getI_ETC_DATA() {
        return StringUtil.getNvlStr(I_ETC_DATA);
    }

    public void setI_ETC_DATA(String i_ETC_DATA) {
        I_ETC_DATA = i_ETC_DATA;
    }

    public String getI_REPORT_DTM() {
        return StringUtil.getNvlStr(I_REPORT_DTM);
    }

    public void setI_REPORT_DTM(String i_REPORT_DTM) {
        I_REPORT_DTM = i_REPORT_DTM;
    }

    public String getI_DISTANCE_TO() {
        return StringUtil.getNvlStr(I_DISTANCE_TO);
    }

    public void setI_DISTANCE_TO(String i_DISTANCE_TO) {
        I_DISTANCE_TO = i_DISTANCE_TO;
    }

    public String getI_SEQ() {
        return StringUtil.getNvlStr(I_SEQ);
    }

    public void setI_SEQ(String i_SEQ) {
        I_SEQ = i_SEQ;
    }

    public String getI_CHECK_DISTANCE() {
        return StringUtil.getNvlStr(I_CHECK_DISTANCE);
    }

    public void setI_CHECK_DISTANCE(String i_CHECK_DISTANCE) {
        I_CHECK_DISTANCE = i_CHECK_DISTANCE;
    }

    public String getI_CHECK_TIME() {
        return StringUtil.getNvlStr(I_CHECK_TIME);
    }

    public void setI_CHECK_TIME(String i_CHECK_TIME) {
        I_CHECK_TIME = i_CHECK_TIME;
    }

    public String getI_CHECK_SPEED() {
        return StringUtil.getNvlStr(I_CHECK_SPEED);
    }

    public void setI_CHECK_SPEED(String i_CHECK_SPEED) {
        I_CHECK_SPEED = i_CHECK_SPEED;
    }


    @Override
    public String toString() {
        return "LocationInfo{" +
                "I_DEVICE_ID='" + I_DEVICE_ID + '\'' +
                ", I_TERMINAL_NO='" + I_TERMINAL_NO + '\'' +
                ", I_POS_X='" + I_POS_X + '\'' +
                ", I_POS_Y='" + I_POS_Y + '\'' +
                ", I_GPS_DTM='" + I_GPS_DTM + '\'' +
                ", I_SPEED='" + I_SPEED + '\'' +
                ", I_ANGEL='" + I_ANGEL + '\'' +
                ", I_BATTERY_LEVEL='" + I_BATTERY_LEVEL + '\'' +
                ", I_ADDRESS_INFO='" + I_ADDRESS_INFO + '\'' +
                ", I_ETC_DATA='" + I_ETC_DATA + '\'' +
                ", I_REPORT_DTM='" + I_REPORT_DTM + '\'' +
                ", I_DISTANCE_TO='" + I_DISTANCE_TO + '\'' +
                ", I_SEQ='" + I_SEQ + '\'' +
                ", I_CHECK_DISTANCE='" + I_CHECK_DISTANCE + '\'' +
                ", I_CHECK_TIME='" + I_CHECK_TIME + '\'' +
                ", I_CHECK_SPEED='" + I_CHECK_SPEED + '\'' +
                '}';
    }
}
