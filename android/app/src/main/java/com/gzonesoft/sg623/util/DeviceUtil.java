package com.gzonesoft.sg623.util;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.gzonesoft.sg623.domain.CommType;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class DeviceUtil {

    private String TAG = "DeviceUtil";

    private static DeviceUtil mDeviceUtil;
    private static ProgressDialog pd;

    private DeviceUtil() {
    }

    public static DeviceUtil with() {
        if (mDeviceUtil == null)
            mDeviceUtil = new DeviceUtil();
        return mDeviceUtil;
    }


    /**
     * 디바이스 조회
     * @param ctx, String
     * @return String
     */
    public String getDeviceInfo(Context ctx, String info_type) {

        String sTag = "getDeviceInfo()";

        String deviceInfo = "";

        Loggers.d("권한 READ_SMS : " + ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_SMS));
        Loggers.d("권한 READ_PHONE_NUMBERS : " + ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_NUMBERS));
        Loggers.d("권한 READ_PHONE_STATE : " + ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE));
//        Loggers.d("권한 CommonUtil.with().readPhonePermission() : " + ActivityCompat.checkSelfPermission(ctx, CommonUtil.with().readPhonePermission()));

        if ( ActivityCompat.checkSelfPermission(ctx, CommonUtil.with().readPhonePermission()) == PackageManager.PERMISSION_GRANTED ) {

            try {
                TelephonyManager telephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

                // 디바이스 아이디 (DEVICE ID)
                if(info_type.equals("DEVICE_ID")) {
                    String device_id = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.d(sTag, "device_id : " + device_id);
                    deviceInfo = device_id;
                }

                // 폰번호 (실제번호 ex: 01022223333)
                if(info_type.equals("PHONE_NO")) {
                    deviceInfo = telephonyMgr.getLine1Number();

                    if (deviceInfo == null || deviceInfo.trim().equals("")) {
                        deviceInfo = "00000000000";
                    }else{
                        deviceInfo = deviceInfo.replace("+82", "0");
                    }

                    Log.d(sTag, "전화번호 : " + deviceInfo);

                }

                // 폰번호 (MIN_NO ex: 1022223333)
                if(info_type.equals("PHONE_NO_MIN")) {
                    deviceInfo = telephonyMgr.getLine1Number();

                    if (deviceInfo == null || deviceInfo.trim().equals("")) {
                        deviceInfo = "0000000000";
                    }else{
                        deviceInfo = deviceInfo.replace("+82", "");
                    }

                    Log.d(sTag, "전화번호(MIN_NO) : " + deviceInfo);
                }

                // 모델명
                if(info_type.equals("MODEL")) {
                    Log.d(sTag, "Build.MODEL : " + Build.MODEL);
                    deviceInfo = Build.MODEL;
                }

                /*
                Log.d(TAG, "음성통화 상태 : [ getCallState ] >>> " + telephonyMgr.getCallState());
                Log.d(TAG, "데이터통신 상태 : [ getDataState ] >>> " + telephonyMgr.getDataState());
                Log.d(TAG, "통신사 ISO 국가코드 : [ getNetworkCountryIso ] >>> " + telephonyMgr.getNetworkCountryIso());
                Log.d(TAG, "통신사 ISO 국가코드 : [ getSimCountryIso ] >>> " + telephonyMgr.getSimCountryIso());
                Log.d(TAG, "망사업자 MCC+MNC : [ getNetworkOperator ] >>> " + telephonyMgr.getNetworkOperator());
                Log.d(TAG, "망사업자 MCC+MNC : [ getSimOperator ] >>> " + telephonyMgr.getSimOperator());
                Log.d(TAG, "망사업자명 : [ getNetworkOperatorName ] >>> " + telephonyMgr.getNetworkOperatorName());
                Log.d(TAG, "망사업자명 : [ getSimOperatorName ] >>> " + telephonyMgr.getSimOperatorName());
                Log.d(TAG, "SIM 카드 상태 : [ getSimState ] >>> " + telephonyMgr.getSimState());

                Log.d(TAG, "제조사 : [ Build.MANUFACTURER ] >>> " + Build.MANUFACTURER);
                Log.d(TAG, "브랜드 : [ Build.MANUFACTURER ] >>> " + Build.BRAND);
                Log.d(TAG, "모델명 : [ Build.MANUFACTURER ] >>> " + Build.MODEL);
                Log.d(TAG, "OS 버전 : [ Build.MANUFACTURER ] >>> " + Build.VERSION.RELEASE);
                Log.d(TAG, "SDK 버전 : [ Build.MANUFACTURER ] >>> " + Build.VERSION.SDK_INT);
                */

            }catch(Exception e){
                Loggers.d2(sTag, "Exception : " + e.getMessage());
            }
        }

        return deviceInfo;
    }


    /**
     * 충전여부
     * @param ctx
     * @return boolean
     */
    public String getBatteryStatus(Context ctx){
        // 배터리 상태 확인
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = ctx.registerReceiver(null, ifilter);

        // 충전여부
        int statusBattery = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        String isCharging = "N";
        if( statusBattery == BatteryManager.BATTERY_STATUS_CHARGING || statusBattery == BatteryManager.BATTERY_STATUS_FULL ){
            isCharging = "Y";
        }
        //Log.d(TAG, "isCharging : " + isCharging);

        return isCharging;
    }


    /**
     * 충전방법
     * @param ctx
     * @return String
     */
    public String getBatteryChargeType(Context ctx){
        String howCharge = "";

        // 배터리 상태 확인
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = ctx.registerReceiver(null, ifilter);

        // 충전방법
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if(usbCharge){
           howCharge = CommType._USB;
        }else if(acCharge){
            howCharge = CommType._AC;
        }else{
            howCharge = "";
        }

        return howCharge;
    }


    /**
     * 배터리잔량
     * @param ctx
     * @return int
     */
    public int getBatteryPercent(Context ctx){
        // 배터리 상태 확인
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = ctx.registerReceiver(null, ifilter);

        // 배터리잔량
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPercent = level * 100 / scale;

        return batteryPercent;
    }



    /**
     * device 제조사 가져오기
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * device 브랜드 가져오기
     * @return
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * device 모델명 가져오기
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * device Android OS 버전 가져오기
     * @return
     */
    public static String getDeviceOs() {
        return Build.VERSION.RELEASE;
    }

    /**
     * device SDK 버전 가져오기
     * @return
     */
    public static int getDeviceSdk() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 화면정보 얻기
     */
    public static int actionBarHeight;


    /**
     * MAC address
     * @return
     */
    public static String getHardwareAddress() {
        String sTag = "getHardwareAddress()";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            Loggers.d(sTag + "all : " + all.size());

            for (NetworkInterface nif : all) {
                Loggers.d(sTag + "nif.getName() : " + nif.getName());

                /*
                check point - nif.getName() : v4-rmnet0
                check point - nif.getName() : rmnet0
                check point - nif.getName() : lo
                check point - nif.getName() : rmnet1
                */

                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

                return res1.toString();
            }

        } catch (Exception ex) {
            Loggers.d("getHardwareAddress() - Exception : " + ex.toString());

        }
        return "";
    }


    /**
     * getLocalIpAddress()
     * @return
     */
    public String getLocalIpAddress() {

        Loggers.d("getLocalIpAddress()");
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }






}