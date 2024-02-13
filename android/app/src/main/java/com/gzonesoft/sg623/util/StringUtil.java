package com.gzonesoft.sg623.util;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author dkpark
 *
 */
public class StringUtil {
    public static final String STR_EMPTY_STRING = "";

    /**
     * 주어진 문자열의 값이 널인 경우 blank로 변환한다.
     * @param str
     * @return
     */
    public static String getNvlStr(String str) {
        if ( str == null ) {
            return STR_EMPTY_STRING;
        }
        return str;
    }

    /**
     * 주어진 문자열의 값이 널인 경우 blank로 변환한다.
     * @param str
     * @return
     */
//    public static String getNvlStr(String str) {
//        if ( str == null ) {
//            return STR_EMPTY_STRING;
//        }
//        return str;
//    }


    /**
     * 원 문자열의 값이 null인경우 해당 문자열로 치환한다.
     * @param strSrcData
     * @param strReplaceData
     * @return
     */
    public static String getNvlStr(String strSrcData, String strReplaceData) {
        if ( (strSrcData == null) || (strSrcData.equals(STR_EMPTY_STRING)) ) {
            return strReplaceData;
        }
        return strSrcData;
    }
    public static int getNvlInt(String strOrg) {
        if ( strOrg == null || strOrg.trim().length() == 0 ) {
            return 0;
        }
        return Integer.parseInt(strOrg);
    }

    public static String getNvlIntString(String strOrg) {
        if ( strOrg == null || strOrg.trim().length() == 0 ) {
            return "0";
        }
        return  String.valueOf(Integer.parseInt(strOrg));
    }

    public static int getNvlInt(String strOrg, int intInt) {
        if ( strOrg == null || strOrg.trim().length() == 0 ) {
            return intInt;
        }
        return Integer.parseInt(strOrg);
    }

    public static long getNvlLong(String strOrg) {
        if ( strOrg == null || strOrg.trim().length() == 0 ) {
            return 0;
        }
        return Long.parseLong(strOrg);
    }

    public static long getNvlLong(String strOrg, long lngLng) {
        if ( strOrg == null || strOrg.trim().length() == 0 ) {
            return lngLng;
        }
        return Long.parseLong(strOrg);
    }

    public static String readFromAssets(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    // 따옴표를 제거하고 리턴..
    public static String getRemoveQuota(String strOrg) {
        if ( strOrg == null || strOrg.trim().length() == 0 ) {
            return strOrg;
        }
        return strOrg.replace("\"", "").toString();
    }


    // JoDa 타임을 스트링으로...
    public static String getStringJodaDateFormat(String preDt, String preformat, String newFormat){

        try {
            if (preDt.isEmpty()) return "";
            preDt = getStringJodaDateFormat(preDt, newFormat);

//            if (preDt.indexOf(".")>0) preformat += ".SSS";
//            DateTimeFormatter transPreFormat = DateTimeFormat.forPattern(preformat);
//            DateTimeFormatter transNewFormat = DateTimeFormat.forPattern(newFormat);
//
//            DateTime dateTime = transPreFormat.parseDateTime(preDt);
//
//            //DateTimeZone.UTC을 사용하지 않으면 한국 시간으로 변경된다. UTC타임을 그대로 사용
//            //transNewFormat = transNewFormat.withZone(DateTimeZone.UTC); //UTC타임 시간으로 추출
//
//            //한국 시간으로 변환된다.
//            preDt = dateTime.toString(transNewFormat);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return preDt;
    }

    /*
  System.out.println(getStringJodaDateFormat2("2017-11-09T20:12:00.000",  "HH:mm:ss"));
  System.out.println(getStringJodaDateFormat2("2017-11-09T20:12:00",  "HH:mm:ss"));
*/
    public static String getStringJodaDateFormat(String preDt, String newFormat){

        try {
            DateTime jodaTime = DateTime.parse(preDt);

            DateTimeFormatter transNewFormat = DateTimeFormat.forPattern(newFormat);

            //한국 시간으로 변환된다.
            preDt = jodaTime.toString(transNewFormat);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return preDt;
    }

    /**
     * Right 함수
     */
    public static String Right(String Str, int Num) {
        if (Num <= 0)
            return "";
        else if (Num > Str.length())
            return Str;
        else {
            return Str.substring(Str.length() - Num, Str.length());
        }
    }

    /**
     * 고정길이 문자열 리턴
     * @param string
     * @param length
     * @return
     */
    public static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }

}
