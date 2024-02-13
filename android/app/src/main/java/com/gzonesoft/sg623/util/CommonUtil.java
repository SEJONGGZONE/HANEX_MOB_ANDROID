package com.gzonesoft.sg623.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonUtil {

    private String tag = "CommonUtil";

    private static CommonUtil mCommonUtil;
    private static ProgressDialog pd;

    private CommonUtil() {
    }

    public static CommonUtil with() {
        if (mCommonUtil == null)
            mCommonUtil = new CommonUtil();
        return mCommonUtil;
    }

    public String now() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        return dateformat.format(cal.getTime());
    }
    public String nowYear() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy");
        return dateformat.format(cal.getTime());
    }
    public String nowMonth() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("MM");
        return dateformat.format(cal.getTime());
    }
    public String nowDay() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("dd");
        return dateformat.format(cal.getTime());
    }
    public String nowHour() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("HH");
        return dateformat.format(cal.getTime());
    }
    public String nowMinute() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("mm");
        return dateformat.format(cal.getTime());
    }

    public String nowHHMM() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("HHmm");
        return dateformat.format(cal.getTime());
    }
    public String nowHH_MM() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
        return dateformat.format(cal.getTime());
    }
    public String nowHH_MM_SS() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public long outTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        Date date1 = null;
        try {
            date1 = formatter.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar.getTimeInMillis();
    }

    public String nowYYYYMMDD_HH_MM_SS() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowYYYY_MM_DD_HH_MM_SS() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowMM_DD() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowMM_DD_HH_MM() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowMM_DD_HH_MM_SS() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm:ss");
        String strNow = sdfNow.format(date);
        return strNow;
    }


    public String nowYYYY_MM_DD_HH_MM() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowYYYYMMDDHHMMSS() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowHHMMSS() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HHmmss");
        String strNow = sdfNow.format(date);
        return strNow;
    }

    public String nowYYYY_MM() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM");
        return dateformat.format(cal.getTime());
    }

    public String nowYYYY_MM_DD() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(cal.getTime());
    }

    public String lastMonthOfLastDay() {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, 1);
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfPreviousMonth = aCalendar.getTime();
        String fDate = new SimpleDateFormat("yyyyMMdd").format(lastDateOfPreviousMonth);
        return fDate;
    }

    // 지정시간 이전시간 리턴...
    public String nowDiffHour(int nHour, String sFormat) {
        Date date = new Date();
        // 포맷변경 ( 년월일 시분초)
        SimpleDateFormat sdformat = new SimpleDateFormat(sFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 1시간 전
        cal.add(Calendar.HOUR, nHour);
        String fDate = sdformat.format(cal.getTime());

        return fDate;
    }

    public String yyyyMM() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMM");
        return dateformat.format(cal.getTime());
    }

    public String yyyyMM1MonthBefore() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMM");
        return dateformat.format(cal.getTime());
    }


    public String yesterDayWorkingDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
//        Log.i("DUER", "day of weelk = " + day_of_week);
        if (day_of_week == 1) cal.add(Calendar.DATE, -1); //어제가 일요일이면 하루 더 빼서 전전날을 반환
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        return dateformat.format(cal.getTime());
    }

    public String beForeyesterDayWorkingDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
//        Log.i("DUER", "day of weelk = " + day_of_week);
        if (day_of_week == 1) cal.add(Calendar.DATE, -2); //그저께가 일요일이면 하루 더 빼서 전전날을 반환
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        return dateformat.format(cal.getTime());
    }

    /**
     * 월이름 구하기 - 영어버전
     * @return
     */
    public String getMonthString() {
        String month = "wrong";
        String mm = nowMonth();
        int num = Integer.parseInt(mm);
        num--;

        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("en"));
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month.toUpperCase();
    }

    /**
     * 요일명 구하기 - 영어버전
     * @return
     */
    public String getDayString() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String DayOfWeek = "";
        switch (dayOfWeek) {
            case 1:
                DayOfWeek = "SUNDAY";
                break;
            case 2:
                DayOfWeek = "MONDAY";
                break;
            case 3:
                DayOfWeek = "TUESDAY";
                break;
            case 4:
                DayOfWeek = "WEDNESDAY";
                break;
            case 5:
                DayOfWeek = "THURSDAY";
                break;
            case 6:
                DayOfWeek = "FRIDAY";
                break;
            case 7:
                DayOfWeek = "SATURDAY";
                break;
        }

        return DayOfWeek;
    }

    public String wkDayToday() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
//        Log.i("DUER", "day of weelk = " + day_of_week);
        day_of_week = day_of_week - 1;
        return String.valueOf(day_of_week);
    }

    public String wkDayYesterDay() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
//        Log.i("DUER", "day of weelk = " + day_of_week);
        day_of_week = day_of_week - 2;
        if (day_of_week <= 0) {
            day_of_week = 6;
        }

        return String.valueOf(day_of_week);
    }

    public String nowWider() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy - MM - dd");
        return dateformat.format(cal.getTime());
    }


    //전화번호가져오기 못가져어는경우 종종잇음
    public String getLine1Number(Context pCon) {
        TelephonyManager telephonyMgr = (TelephonyManager) pCon.getSystemService(Context.TELEPHONY_SERVICE);
        String line1Number = telephonyMgr.getLine1Number();
        if (line1Number == null || line1Number.trim().equals("")) {
            line1Number = "0000000";
        }
        //return "01052888604";
        return line1Number.replace("+82", "0");
    }


    //입력된 날자 바로 전의 워킹데이를 반환
    public String getYesterDayFromStr(String yyyyMMdd) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat curFormater = new SimpleDateFormat("yyyyMMdd");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObj);
        cal.add(Calendar.DATE, -1);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
//        Log.i("DUER", "day of weelk = " + day_of_week);
        if (day_of_week == 1) cal.add(Calendar.DATE, -1); //어제가 일요일이면 하루 더 빼서 전전날을 반환
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        return dateformat.format(cal.getTime());
    }

    public String getMM_DD_HH_MM(String yyyyMMddHHmmss) {
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).parse(yyyyMMddHHmmss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MM/dd HH:mm", Locale.ENGLISH).format(tradeDate);
    }
    public String getHHmm(String yyyyMMddHHmm) {
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH).parse(yyyyMMddHHmm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HHmm", Locale.ENGLISH).format(tradeDate);
    }
    public String getHHmm2(String yyyyMMddHHmmss) {
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).parse(yyyyMMddHHmmss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HHmm", Locale.ENGLISH).format(tradeDate);
    }

    public String getyyyyMMddToyyyy_mm_dd(String yyyymmdd) {
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(yyyymmdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(tradeDate);
    }


    public String getyyyyMMddToyyyy_mm(String yyyymmdd) {
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(yyyymmdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM", Locale.ENGLISH).format(tradeDate);
    }


    public String getyyyyMMddTodd(String yyyymmdd) {
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(yyyymmdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd", Locale.ENGLISH).format(tradeDate);
    }

    public String getyyyyMMddTomm_dd(String yyyymmdd) {
        Date tradeDate = null;
        try {
            if (yyyymmdd.isEmpty()) {
                return "";
            } else {
                tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(yyyymmdd);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("MM/dd", Locale.ENGLISH).format(tradeDate);
    }

    public String toNumFormat(double num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public String toNumFormatOne(double num) {
        DecimalFormat df = new DecimalFormat("#,###.#");
        return df.format(num);
    }
    public String toNumFormatTwo(double num) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(num);
    }

    public String toNumFormatOnePoint(double num) {
        Locale locale = Locale.KOREA;
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
//        DecimalFormat df = new DecimalFormat("#,###.#" );
        String number = nf.format(num);
        if (!number.contains(".")) {
            number += ".0";
        }
        return number;

    }

    public String distanceStringWithUnit(double num) {
        DecimalFormatSymbols obj = new DecimalFormatSymbols(Locale.ENGLISH);
        obj.setDecimalSeparator('.');
        obj.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.##", obj);
        String ret = df.format(roundSixNumber(num));
        return ret + "";
    }
    public String distanceStringOnlyValue(double num) {
        DecimalFormatSymbols obj = new DecimalFormatSymbols(Locale.ENGLISH);
        obj.setDecimalSeparator('.');
        obj.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.##", obj);
        String ret = df.format(roundSixNumber(num));
        return ret;
    }

    public String speedStringWithUnit(double num) {
        DecimalFormatSymbols obj = new DecimalFormatSymbols(Locale.ENGLISH);
        obj.setDecimalSeparator('.');
        obj.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#", obj);
        String ret = df.format(roundSixNumber(num));
        return ret + "km/h";
    }

    public String speedStringOnlyValue(double num) {
        DecimalFormatSymbols obj = new DecimalFormatSymbols(Locale.ENGLISH);
        obj.setDecimalSeparator('.');
        obj.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#", obj);
        String ret = df.format(roundSixNumber(num));
        return ret;
    }

    public String secondToTimestring(int num) {

        //시, 분, 초 선언
        int hours, minute, second;
        //시간공식
        hours = num / 3600;//시 공식
        minute = num % 3600 / 60;//분을 구하기위해서 입력되고 남은값에서 또 60을 나눈다.
        second = num % 3600 % 60;//마지막 남은 시간에서 분을 뺀 나머지 시간을 초로 계산함

        String timeString = "";
        if (hours>0) {
            timeString += hours + "<small>시간</small>";
        } else {
            timeString += "";
        }

        if (minute>0) {
            timeString += minute + "<small>분</small>";
        } else {
            timeString += "";
        }

//        if (second>0) {
//            timeString += String.format("%02d", second) + "초";
//        } else {
//            timeString += "";
//        }

        return timeString;
    }

    public String toNumFormatGSP(double num) {

        DecimalFormatSymbols obj = new DecimalFormatSymbols(Locale.ENGLISH);
        obj.setDecimalSeparator('.');
        obj.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.######", obj);
        df.setMaximumFractionDigits(12);
        String ret = df.format(roundSixNumber(num));

        return ret;
    }

    public double toNumFormatGSPtoDouble(double num) {
        DecimalFormatSymbols obj = new DecimalFormatSymbols(Locale.ENGLISH);
        obj.setDecimalSeparator('.');
        obj.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.######", obj);
        df.setMaximumFractionDigits(12);
        Double number = Double.valueOf(df.format(roundSixNumber(num)));

        return number;
    }

    private static double roundOneNumber(double d) {
        return (double) Math.round(d * 10D) / 10D;
    }

    private static double roundFourNumber(double d) {
        return (double) Math.round(d * 1000D) / 1000D;
    }

    private static double roundSixNumber(double d) {
        return (double) Math.round(d * 100000D) / 100000D;
    }

    public int weekOfToDayNo() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getRouteWeekNo() {
        return weekOfToDayNo() - 2;
    }

    // bytes 단위로 리턴.
    public String getFileSize(String filePath) {
        String strFilesize;

        File oFile = new File(filePath);

        if (oFile.exists() )
        {
            long lFileSize = oFile.length();
            strFilesize = Long.toString(lFileSize) ; //+ " bytes";
        }
        else
        {
            strFilesize = "0";
        }

        return strFilesize;
    }

    public String makeSignImageFile(View v) {
        String filename = getSystemTime() + ".jpg";
        return makeViewtoImageFile(v, filename);
    }
    public String makeViewtoImageFile(View v, String filename) {

        // 1.디렉토리 생성..
        String StoragePath = Environment.getExternalStorageDirectory() + Svc.CAMERA_IMG_PATH;
        String savePath = StoragePath;
        CommonUtil.with().makeDir(savePath);
        String retFileURL = savePath + "/" + filename;

        File dir = new File(savePath);
        File output = new File(dir, ".nomedia");
        try {
            boolean fileCreated = output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File f = new File(savePath);
        if (!f.isDirectory()) f.mkdirs();

        //v.setDrawingCacheEnabled(false);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath + "/" + filename);
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            }
            Loggers.d("[파일저장경로 = " + savePath + "/" + filename);
            Loggers.d("[파일크기 = " + getFileSize(savePath + "/" + filename));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                    if (StringUtil.getNvlInt(getFileSize(retFileURL)) <= 0) retFileURL = "";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bitmap != null)
                bitmap.recycle();

        }

        return retFileURL;
    }

    /**
     * 디렉토리 만들기
     * @param folderPath 경로
     */
    public void makeDir(String folderPath){
        File folderFile = new File(folderPath);
        if(folderFile.exists() == false) {
            folderFile.mkdirs();
        }
    }

    //파일 & 폴더 삭제
    public void removeDir(String mRootPath) {

        try {
            File file = new File(mRootPath);
            file.delete();

            // 하위 파일 폴더까지 삭제하는 샘플소스...
            File[] childFileList = file.listFiles();
            if (childFileList != null) {
                for (File childFile : childFileList) {
                    if (childFile.isDirectory()) {
                        removeDir(childFile.getAbsolutePath());    //하위 디렉토리
                    } else {
                        childFile.delete();    //하위 파일
                    }
                }
                file.delete();    //root 삭제
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String getLogFileName() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + now() + "_" + "SMLog.txt";
    }

    public void logRecoder(String contentToBeSaved) {
        File filePath = new File(getLogFileName());
        if (!filePath.exists())
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(getLogFileName(), true));
            bufferedWriter.write(contentToBeSaved + " " + nowYYYY_MM_DD_HH_MM_SS() + "\n");
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 네이버 좌표(위경도) 배열에서 거리얻기
     * @param routePoints
     * @return
     */
    public double getTotDistance(ArrayList<LatLng> routePoints) {
        double totDistance = 0;
        try {
            Location srcLocaiton = new Location("source"), tarLocaiton = new Location("target");
            int nIdx = 0;
            for (LatLng point : routePoints) {
                if (nIdx == 0) {
                    // 초기 시작지점 세팅
                    srcLocaiton.setLatitude(point.latitude);
                    srcLocaiton.setLongitude(point.longitude);
                } else {
                    // 계산 목적지점 세팅
                    tarLocaiton.setLatitude(point.latitude);
                    tarLocaiton.setLongitude(point.longitude);

                    totDistance += getDistance(srcLocaiton, tarLocaiton);

                    // 다음 시작점 세팅
                    srcLocaiton.setLatitude(point.latitude);
                    srcLocaiton.setLongitude(point.longitude);
                }
                nIdx++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return totDistance;
        }
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {

        if (lat2 == 0 || lat1 == 0 || lat2 == 0 || lon2 == 0) {
            return -1;
        }

        // TODO: 아래 계산식을 사용할것인가?
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;
//        if (unit == "kilometer") {
//            dist = dist * 1.609344;
//        } else if (unit == "meter") {
//            dist = dist * 1609.344;
//        }

//        Log.d("#LOC# " + lat1 + ","+ lon1 + ","+ lat2 + ","+ lon2 + "," + dist);

        return (dist);
    }

    // This function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This  converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    //컨텐츠 URL에서 실제 파일패스를 찾아준다.

    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void reduceFile(String filpath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap src = BitmapFactory.decodeFile(filpath, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, src.getWidth(), src.getHeight(), true);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filpath);
            resized.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {

                }

            if (src != null)
                src.recycle();


            if (resized != null)
                resized.recycle();

        }
    }

    public static boolean isNumber(String str) {
        //먼저 유효성 체크 검사부터....
        if (str == null || str.equals(""))
            return false;

        //이제부터 실제 변수의 문자열을 하나하나 확인해보는 식....
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    public static String getSystemTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return formatter.format(date);
    }

    // 위치정보 검사. 1.값이 있는지여부, 2.0보다 큰값인지의 여부
    public static boolean checkPositionInfo(String xx, String yy) {
        boolean bRet = false;
        if ((TextUtils.isEmpty(xx)) || (TextUtils.isEmpty(yy))) {
            bRet = false;
        } else {
            if ((Double.valueOf(xx) <= 0) || ( Double.valueOf(yy) <= 0)) {
                bRet = false;
            } else {
                bRet = true;
            }
        }
        return bRet;
    }

    // 위치정보를가지고 주소정보를 얻는다.
    public String getAddressString(Context ctx, Location location) {
        if (location == null) return "";

        String addressString  = "";
        try {
            double latitude = location.getLatitude(); // ����
            double longitude = location.getLongitude(); // �浵

//            Loggers.d2(this, ">>>>>>>>>>>> [getAddressString] latitude = " + latitude + ", longitude = " + longitude);

            Geocoder gcK = new Geocoder(ctx, Locale.KOREA);
            List<Address> addresses = gcK.getFromLocation(latitude, longitude, 1);
            StringBuilder sb = new StringBuilder();

            if (addresses.size() > 0) {
//                for (Address addr : addresses) {
//                    sb.append(addr.getMaxAddressLineIndex()).append("********\n");
//                    for (int i=0;i < addr.getMaxAddressLineIndex();i++)
//                        sb.append(addr.getAddressLine(i)).append("<< \n\n");
//                }
//                sb.append("===========\n");
//
//                Address address = addresses.get(0);
//                sb.append(address.getCountryName()).append(" ");
//                //sb.append(address.getPostalCode()).append(" ");
//                sb.append(address.getLocality()).append(" ");
//                sb.append(address.getThoroughfare()).append(" ");
//                sb.append(address.getFeatureName()).append("");
                String addr = addresses.get(0).getAddressLine(0).toString();
                addr = addr.replace("대한민국 ","");
                addr = addr.replace("한국 ","");
                addr = addr.replace("남한 ","");
                sb.append(addr);
                //sb.append(tStr).append("\n");
                //sb.append(tStrLocal).append("\n");

                addressString = sb.toString();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Loggers.d2(this, "[ERROR]" + e.getMessage());
            addressString = "";
        }
        return addressString ;
    }

    // ------------------------------------------------------------------------------------
    // 해당거리이내 접근여부 확인.(True : 진입, False : 진입하지않음 또는 파라미터 불명확)
    // ------------------------------------------------------------------------------------
    public float lastCheckDistance = 0;
    public boolean checkGeofence(Location srcLocation, Location targetLocation, int chkDistance) {

        // 거리계산
        float distance = getDistance(srcLocation, targetLocation);

//        Log.d(tag, ">>>>>> ----- [checkGeofence] 거리확인 , " + srcLocation.getLatitude() + "," + srcLocation.getLongitude());
//        Log.d(tag, ">>>>>> ----- [checkGeofence] 거리확인 , " + targetLocation.getLatitude() + "," + targetLocation.getLongitude());
//        Log.d(tag, ">>>>>> ----- [checkGeofence] 거리확인 , " + distance);

        lastCheckDistance = distance;

        //Toast.makeText(mCtx, "checkGeofence = " + String.valueOf(distance) + "m", Toast.LENGTH_SHORT).show();

        // 거리값 오류 - false 리턴
        if (distance < 0) distance = 0;

        // 거리확인
        boolean bRet = false;
        if (distance <= chkDistance) {
            bRet = true;
        } else {
            bRet = false;
        }
        return  bRet;
    }
    // ------------------------------------------------------------------------------------
    // 두 지점간 거리계산.( -1 : 계산오류, 파라미터 정보 부족 혹은 잘못된 위치정보)
    // ------------------------------------------------------------------------------------
    public float getDistance(Location srcLocation, Location targetLocation) {

        float distance = 0;

        try {

            // 파라미터 확인
            if ((srcLocation.getLatitude() <=0) || (srcLocation.getLongitude() <=0)) return -1;
            if ((targetLocation.getLatitude() <=0) || (targetLocation.getLongitude() <=0)) return -2;

//        Log.d(tag, "----- [getDistance]"
//                + srcLocation.getLatitude() + "," + srcLocation.getLongitude() + "/"
//                + targetLocation.getLatitude() + "," + targetLocation.getLongitude()
//        );

            // 거리계산
            distance = srcLocation.distanceTo(targetLocation);

            // 거리값 오류 - false 리턴
            if (distance<=0) return 0;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return distance;
    }

    public static void dismissProgress() {
        if (pd != null) {
            pd.dismiss();
        }
    }


    public Toast ToastMsg(Context ctx, String msg, int duration) {
        Toast tt = null;
        try {
            tt = Toast.makeText(ctx, msg, duration);
            tt.setGravity(Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return tt;
        }
    }


    public int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    public Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    /**
     * HTML 텍스트로 세팅(마퀴즈효과는 덤으로..)
     * @param tvTarget
     * @param htmlString
     */
    public void setHtmlMarqueeText(TextView tvTarget, String htmlString) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                tvTarget.setText(Html.fromHtml(htmlString));
            } else {
                tvTarget.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
            }
            // 글자가 흐르기위해서...
            tvTarget.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvTarget.setSingleLine(true);
            tvTarget.setMarqueeRepeatLimit(-1);
            tvTarget.setSelected(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * HTML 텍스트세팅 (마퀴즈효과는 없어..두줄로 표현해야할때 쓰라~)
     * @param tvTarget
     * @param htmlString
     */
    public void setHtmlText(TextView tvTarget, String htmlString) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                tvTarget.setText(Html.fromHtml(htmlString));
            } else {
                tvTarget.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 3자리마다 컴마(금액표시)
     * @param value
     * @return
     */
    public String setAmount(int value) {
        String retString = "0";
        try {
            DecimalFormat formatter = new DecimalFormat("###,###");
            retString = formatter.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retString;
    }
    public String setAmountL(long value) {
        String retString = "0";
        try {
            DecimalFormat formatter = new DecimalFormat("###,###");
            retString = formatter.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retString;
    }




    /**
     * 안드로이드 버전별 READ_PHONE 권한 선택
     */
    public String readPhonePermission(){

        String READ_PHONE_PERMISSION = Manifest.permission.READ_PHONE_NUMBERS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            READ_PHONE_PERMISSION = Manifest.permission.READ_PHONE_NUMBERS;
        }else{
            READ_PHONE_PERMISSION = Manifest.permission.READ_PHONE_STATE;
        }

        return READ_PHONE_PERMISSION;

    }

    public String dateToFormatString(long time, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        String timeString = sdf.format(date);
        return timeString;
    }

}