package com.gzonesoft.sg623.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DateUtil {

    public final static int PERIOD = 15;
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DATE = 24 * HOUR;
    public static final int YEAR = 365 * DATE;

    public static int getYear(String strdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            return cal.get(Calendar.YEAR);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return 2000;
    }

    public static int getMonth(String strdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            return cal.get(Calendar.MONTH) + 1;

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return 1;
    }

    public static int getDate(String strdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            return cal.get(Calendar.DATE);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return 1;
    }

    public static int getHour(String strdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            return cal.get(Calendar.HOUR_OF_DAY);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return 1;
    }

    public static int getMinute(String strdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            return cal.get(Calendar.MINUTE);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return 1;
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        String ret = getDateString(cal);
        return ret;
    }

    private static String getDateString(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        String ret = String.format("%4d%02d%02d", year, month, date);
        return ret;
    }


    public static String getDateTime() {
        Calendar cal = Calendar.getInstance();
        String ret = getDateTimeString(cal);
        return ret;
    }

    private static String getDateTimeString(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        //"20170321000000"

        String ret = String.format("%4d%02d%02d%02d%02d%02d", year, month, date, hour, minute, second);
        return ret;
    }



    public static String getDateString(String strdate) {
        String ret = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int hh = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);


            ret = String.format("%4d-%02d-%02d", year, month, date);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return ret;
    }

    public static String getDateString2(String strdate) {
        String ret = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int hh = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);


            ret = String.format("%4d-%02d-%02d", year, month, date);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return ret;
    }

    public static String getDateStringSimple(String strdate) {
        String ret = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);

            ret = String.format("%02d-%02d", month, date);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return ret;
    }

    public static String getHourString(String strdate) {
        String ret = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strdate));//.getTime());

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int hh = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);

            ret = String.format("%02d:%02d", hour, min);

        } catch (ParseException e) {
            Loggers.e("Error", e);
        }
        return ret;
    }

    public static String getDateBeforeOneHour() {

        Calendar cal = Calendar.getInstance();

        long time = cal.getTimeInMillis() - HOUR;

        cal.setTimeInMillis(time);

        String ret = getDateTimeString(cal);
        return ret;
    }

    public static String getDateBefore5Days() {

        Calendar cal = Calendar.getInstance();

        long time = cal.getTimeInMillis() - 5 * DATE;

        cal.setTimeInMillis(time);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

        return ret;
    }

    public static String getDateBefore2Weeks() {

        Calendar cal = Calendar.getInstance();

        long time = cal.getTimeInMillis() - 14 * DATE;

        cal.setTimeInMillis(time);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

        return ret;
    }

    public static String getDateAfter2Weeks() {

        Calendar cal = Calendar.getInstance();

        long time = cal.getTimeInMillis() + 14 * DATE;

        cal.setTimeInMillis(time);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

        return ret;
    }

    public static ArrayList<String> getDateList() {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();

        for (int idx = 0; idx < PERIOD; ++idx) {

            long time = now + idx * DATE;
            cal.setTimeInMillis(time);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);

            String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

            cal.get(Calendar.DAY_OF_WEEK);

            list.add(ret);
        }

        return list;
    }

    public static ArrayList<String> getDateList(String startdate, int end) {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();

        for (int idx = 0; idx < end; ++idx) {

            long time = now + idx * DATE;
            cal.setTimeInMillis(time);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);

            String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

            if (ret.compareTo(startdate) < 0) continue;

            cal.get(Calendar.DAY_OF_WEEK);

            list.add(ret);
        }

        return list;
    }

    public static ArrayList<String> getDateList(int start, String enddate) {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();

        for (int idx = start; idx < PERIOD; ++idx) {

            long time = now + idx * DATE;
            cal.setTimeInMillis(time);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);

            String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

            if (ret.compareTo(enddate) > 0) break;

            cal.get(Calendar.DAY_OF_WEEK);

            list.add(ret);
        }

        return list;
    }

    public static ArrayList<String> getDateList(String startdate, String enddate) {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        long now = cal.getTimeInMillis();

        for (int idx = 0; idx < PERIOD; ++idx) {

            long time = now + idx * DATE;
            cal.setTimeInMillis(time);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);

            String ret = year + "." + (month > 9 ? "" + month : "0" + month) + "." + (date > 9 ? "" + date : "0" + date);

            if (ret.compareTo(startdate) < 0) continue;
            if (ret.compareTo(enddate) > 0) break;

            cal.get(Calendar.DAY_OF_WEEK);

            list.add(ret);
        }

        return list;
    }

    public static String getDateBeforeOneYear() {

        Calendar cal = Calendar.getInstance();

//		long time = cal.getTimeInMillis() - YEAR;
//		
//		cal.setTimeInMillis(time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        String ret = (year - 1) + (month > 9 ? "" + month : "0" + month) + (date > 9 ? "" + date : "0" + date);

        return ret;
    }

    public static String getDateBeforeHalfYear() {

        Calendar cal = Calendar.getInstance();

//		long time = cal.getTimeInMillis() - YEAR;
//		
//		cal.setTimeInMillis(time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        if (month > 6)
            month = month - 6;
        else {
            month = month + 6;
            year = year - 1;
        }
        String ret = (year) + (month > 9 ? "" + month : "0" + month) + (date > 9 ? "" + date : "0" + date);

        return ret;
    }

    // 날짜를 받아서 Calendar로 바꾸는..
    public static Calendar getDateTime(String strDatetime) {
        Calendar cal = Calendar.getInstance();
        String[] strSplitDateTime = strDatetime.split(" ");
        String[] strSplitDate = strSplitDateTime[0].split("-");
        String[] strSplitTime = strSplitDateTime[1].split(":");
        cal.set(Integer.parseInt(strSplitDate[0]), Integer.parseInt(strSplitDate[1]) - 1,
                Integer.parseInt(strSplitDate[2]), Integer.parseInt(strSplitTime[0]), Integer.parseInt(strSplitTime[1]));
        return cal;
    }
    // 범위시간에 이전(1) / 포함(0) / 초과(2)
    public static int getValidDate(String strStart, String strEnd, String strValue) {

        Loggers.d("strValue = " + strValue);

        Calendar calStart = getDateTime(strStart);
        Calendar calEnd = getDateTime(strEnd);
        Calendar calValue = getDateTime(strValue);

        int nRet = -99;
        if (calStart.before (calValue) && calEnd.after(calValue)) {
            // 정시
            nRet = 0;
        } else {
            if (calStart.after (calValue)) {
                // 조기
                nRet = 1;
            } else if (calEnd.before(calValue)) {
                // 지연
                nRet = 2;
            }
        }

        return nRet;
    }
}
