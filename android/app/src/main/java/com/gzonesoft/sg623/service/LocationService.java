package com.gzonesoft.sg623.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.comm.AppSetting;

import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.LocationInfo;
import com.gzonesoft.sg623.domain.BroadCastDomain;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.domain.TodayStatus;
import com.gzonesoft.sg623.domain.UserType;
import com.gzonesoft.sg623.ui.CvoMainActivity;
import com.gzonesoft.sg623.ui.DeliveryListActivity;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.ConstValue;
import com.gzonesoft.sg623.util.DeviceUtil;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.SettingPref;
import com.gzonesoft.sg623.util.Svc;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LocationService extends Service {

    String TAG = "LocationService";

    static final int LOCATION_SERVICE_ID = 10001;
    public static final int REPORT_CVO_OK = 20001;
    public static final int REPORT_HDO_OK = 30001;

    private LocationInfo mRepLocationInfo;
//    public ArrayList<LocationInfo> locationInfos = new ArrayList<LocationInfo>();

    private Location mLastLocation = null; // 이전 Location 중 (유효한!) 가장 최근 Location
    private Location mTargetLocation = null; // 보고대상 처리용 Location
    private Location mCurrLocation = null; // 현재 가져온 Location

//    double mDistance_tot = 0; // 누적거리
    int limitSpeed = 200; // 이전 위치와 현재위치 유효속도 체크(km)

    Random random = new Random();
    int nRandMili = 1000 + random.nextInt(999) ;
    int mGPS_수집주기 = nRandMili * AppSetting.userConfig.GPS_GET_CYCLE - 1000; // 주기(초)
//    int mGPS_SEND_TIME = 1000 * AppSetting.userConfig.GPS_SEND_TIME; // 전송(초)

    // 브로드캐스트관련.
    private Intent gpsBroadCastIntent = null;

    /**
     * 위치서비스 시작/종료 콜백..
     */
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if( locationResult != null ){
                for (Location location : locationResult.getLocations()) {

                    long gps_time = locationResult.getLastLocation().getTime();

//                    Loggers.d("<<<###>>> --------------------------------------");
//                    Loggers.d("<<<###>>> HH:mm:ss :::: " + CommonUtil.with().dateToFormatString(gps_time, "HH:mm:ss"));
//                    Loggers.d("<<<###>>> location.getTime() : " + location.getTime());
//                    Loggers.d("<<<###>>> location.toString() : " + location.toString());
//                    Loggers.d("<<<###>>> location.describeContents() : " + location.describeContents());
//                    Loggers.d("<<<###>>> location.getProvider() : " + location.getProvider());
//                    Loggers.d("<<<###>>> location.getBearing() : " + location.getBearing());
//                    Loggers.d("<<<###>>> location.getSpeed() : " + location.getSpeed());
//                    Loggers.d("<<<###>>> location.getAccuracy() : " + location.getAccuracy());
//                    Loggers.d("<<<###>>> location.getLatitude() : " + location.getLatitude());
//                    Loggers.d("<<<###>>> location.getLongitude() : " + location.getLongitude());
//                    Loggers.d("<<<###>>> location.getAltitude() : " + location.getAltitude());
//                    Loggers.d("<<<###>>> --------------------------------------");
                }

            }else{
                Loggers.d("<<<###>>> locationResult == null");
            }

            if (locationResult != null && locationResult.getLastLocation() != null) {
                mCurrLocation = locationResult.getLastLocation(); // 현재 가져온 Location 객체
                mTargetLocation = locationResult.getLastLocation(); // 보고처리할  Location 객체 (기본적으로 지금 가져온 Location을 타켓 대상으로 한다)
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 위치서비스 시작
     */
    private void startLocationService() {
        try {

            String channelId = "location_notification_channel_COOKZZANG";
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //Intent resultIntent = new Intent();
            Intent resultIntent = new Intent(getApplicationContext(), CvoMainActivity.class);
            resultIntent.putExtra("CALL_TYPE", CommType._노티바에서실행);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
            builder.setContentTitle("SG623-아이사랑");
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
            // 사용자 구분에 따른 메시지 구분..
            if (AppSetting.cvoUserInfo.getUSERTYPE().equals(UserType._배송기사)) {
                builder.setContentText("[" + AppSetting.cvoUserInfo.getVEHICLENO() + "] " +
                        AppSetting.cvoUserInfo.getNAME() + "" + AppSetting.cvoUserInfo.getUSERTYPE() +
                        "님, 안녕하세요.^^ 오늘도 안전운전하세요.");
            } else {
                builder.setContentText(AppSetting.cvoUserInfo.getNAME() + " " + AppSetting.cvoUserInfo.getUSERTYPE() +
                        "님, 안녕하세요.^^ 좋은 하루되시기 바립니다.");
            }
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setAutoCancel(false);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, "Location Service Channel", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("이 채널은 위치 서비스에서 사용됩니다.");
                notificationChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(notificationChannel);
                //                }
            }


            //int locInterval = AppSetting.userConfig.GPS_GET_CYCLE;
            //int locFastestInterval = AppSetting.userConfig.GPS_GET_CYCLE;
            int locInterval = 1;
            int locFastestInterval = 3;

            Loggers.d("startLocationService() - locInterval : " + locInterval);
            Loggers.d("startLocationService() - locFastestInterval : " + locFastestInterval);

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(locInterval * 1000);
            locationRequest.setFastestInterval(locFastestInterval * 1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
            startForeground(LOCATION_SERVICE_ID, builder.build());

            // 가져온 Location 담기/보내기 핸들러 시작
            startCycleHandler();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 위치서비스 종료
     */
    private void stopLocationService() {
        try {
            // 가져온 Location 담기/보내기 핸들러 중지
            stopCycleHandler();

            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);
            stopForeground(true);
            stopSelf();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                // 넘어온 액션이 위치서비스 활성화냐? 아니냐?
                if (action.equals(Svc.ACTION_START_LOCATION_SERVICE)) {

                    // 위치 서비스 시작
                    startLocationService();

                    // 브로드캐스트 준비
                    gpsBroadCastIntent = new Intent(BroadCastDomain.BROADCAST_TRACE_GPS_SVC);

                } else if (action.equals(Svc.ACTION_STOP_LOCATION_SERVICE)) {

                    // 위치 서비스 종료
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    // 최종 위치보고정보 저장 객체
    private LocationInfo mLastReportInfo = new LocationInfo();

    /**
     * CVO 위치정보 보고
     */
    public void reportCVO(LocationInfo curInfo){
        try {
            Log.d(TAG, "위치정보 보고정보 : " + curInfo.getI_TERMINAL_NO().substring(0,1));

            // 보고시간
            String traceDate =  CommonUtil.with().nowYYYYMMDDHHMMSS();
            String chargeYn = String.format("%s", DeviceUtil.with().getBatteryChargeType(getApplicationContext()));
            if (chargeYn.length() ==0) {
                chargeYn = "N";
            } else {
                chargeYn = "Y";
            }

            // 파라미터 세팅-------------------1

            Map<String, String> paraMap = new LinkedHashMap<String, String>();
            paraMap.put("companyCd", Svc.CVO_COMPANYCD);
            if (curInfo.getI_TERMINAL_NO().substring(0,1).equals("0")) {
                paraMap.put("deviceNo", curInfo.getI_TERMINAL_NO());
            } else {
                paraMap.put("deviceNo", "0" + curInfo.getI_TERMINAL_NO());
            }
            paraMap.put("vehicleCd", AppSetting.cvoUserInfo.getVEHICLECD());
            paraMap.put("vehicleNo", AppSetting.cvoUserInfo.getVEHICLENO());
            paraMap.put("traceDate", traceDate.substring(0,8));
            paraMap.put("traceTime", traceDate.substring(8,14));
            paraMap.put("eventCode", "05"); // 05:주기보고로 고정
            paraMap.put("gpsYn", "Y"); // Y:GPS상태 내부버림으로 'Y'로 고정..
            paraMap.put("chargeYn", chargeYn); // Y:GPS상태 내부버림으로 'Y'로 고정..
            paraMap.put("latitude", curInfo.getI_POS_Y());
            paraMap.put("longitude", curInfo.getI_POS_X());
            paraMap.put("direction", curInfo.getI_ANGEL());
            paraMap.put("speed", curInfo.getI_SPEED());

            // 파라미터 세팅-------------------2

            // 배송일자 배차번호 확인
            String DDATE = SettingPref.with(getApplicationContext()).loadPref(ConstValue.LastWorkStat.DDATE.toString(), "");
            String CVO_DISPATCH_ID = SettingPref.with(getApplicationContext()).loadPref(ConstValue.LastWorkStat.CVO_DISPATCH_ID.toString(), "");

            String remarkString = "";
            if (mLastReportInfo != null) {
                remarkString = "" +
//                        "" + mLastReportInfo.getI_POS_Y() + "," + mLastReportInfo.getI_POS_X() + "/" +
                        "" + mLastReportInfo.getI_ADDRESS_INFO() + "/" +
                        "" + CVO_DISPATCH_ID +
                        "";
            }
            paraMap.put("remark", remarkString);

            // 파라미터 세팅-------------------3

            // 거리정보 보고로직 주석처리 (2022.06.22-이전)
            // paraMap.put("intervalDistance",  curInfo.getI_CHECK_DISTANCE());

            // 거리정보 보고로직 신규작성 (2022.06.22-이후) - 기존 음영지역의 미보고처리로직에 거리계산로직이 있었음. 그러나, 실제 서버에 보고하는 기준이 현실적임.
            // ---> 최근 보고된 위치화의 거리계산을 구해서 서버에 보고하는로직으로 수정..처리..
            String distanceString = "0";
            try {
                if ((mLastReportInfo.getI_POS_Y().length()>0) && (mLastReportInfo.getI_POS_X().length()>0)) {
                    Location srcLocation = new Location("src_location");
                    srcLocation.setLatitude(Double.parseDouble(mLastReportInfo.getI_POS_Y()));
                    srcLocation.setLongitude(Double.parseDouble(mLastReportInfo.getI_POS_X()));
                    Location targetLocation = new Location("tar_location");
                    targetLocation.setLatitude(Double.parseDouble(curInfo.getI_POS_Y()));
                    targetLocation.setLongitude(Double.parseDouble(curInfo.getI_POS_X()));
                    double reportDist = CommonUtil.with().getDistance(srcLocation, targetLocation);
                    distanceString = String.format("%.0f", reportDist);
                }
                mLastReportInfo = curInfo;
            } catch (Exception ex) {
                ex.printStackTrace();
                distanceString = "0";
            } finally {
                paraMap.put("intervalDistance",  distanceString);
            }

            // 파라미터 세팅-------------------4

            // 배터리 수준..
            paraMap.put("batteryLevel", curInfo.getI_BATTERY_LEVEL());
            // 부가정보 컬럼 추가 -> 했더니, 서버에서 오류로 리턴하여 반영이안됨. 그래서 위치정보 자체가 누락됨. 그래서 주석처리함.(2022.06.22 19시)



            // 헤더키 세팅-------------------5
            Map<String, String> keyMap = new HashMap<String, String>();
//            keyMap.put("X-API-ID", "hdo-gps-mobile");
//            keyMap.put("X-API-SECRET", "ze5e505d1fhj11ea978d5f893a0f7ccd");
            keyMap.put(Svc.API_KEY_NAME, Svc.API_KEY);


            // CVO 호출 -------------------6
            RequesterSession.with().CvoGpsServiceCall(
                    new URL(AppSetting.appChkInfo.getGPS_SND_URL()),
                    "POST",
                    "",
                    paraMap,
                    reportCvoCallback,
                    keyMap,
                    false
            );

            // 최종 위치보고정보 저장..
            mLastReportInfo = curInfo;

//            AppSetting.GPS_MANAGER.locationInfo = curInfo; // 2022.09.27 주석처리. 기존보고위치로 현위치를 갱신하고있음..

//            // GPS 상태정보 브로드캐스트..
//            gpsBroadCastIntent.putExtra(
//                    BroadCastDomain.BROADCAST_TRACE_GPS_STATUS,
//                    BroadCastDomain.REPORT_CURRENT_LOCATION);
//            sendBroadcast(gpsBroadCastIntent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * REST-API Callback
     */
    private Callback reportCvoCallback = new Callback() {

        String mTag = "reportCvoCallback()";

        @Override
        public void onFailure(Call call, IOException e) {
            Loggers.d("---------------------------------------------");
            Loggers.d(mTag + " ---- onFailure() : " + e.getMessage());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Loggers.d( mTag + ", ---- body : " + bodyString);
            Loggers.d( "---------------------------------------------");

            handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, getString(R.string.msg_conn_failure)).sendToTarget();
        }


        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Loggers.d(mTag + " ---- onResponse : " + response.code());

            String bodyString = RequesterSession.with().bodyToString(call.request());
            Loggers.d(mTag + ", ---- body : " + bodyString);

            if( response.code() == 200 ){
                try {
                    String strJsonOutput = response.body().string();
                    Loggers.d( mTag + " ---- 수신데이타 : " + strJsonOutput);

                    JsonElement jelement = new JsonParser().parse(strJsonOutput);
                    JsonObject jObject = jelement.getAsJsonObject();

                    String ResultCode = jObject.get("ResultCode").getAsString();
                    String ResultMsg = jObject.get("ResultMsg").getAsString();
                    JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();
                    if (!ResultCode.equals("00")) {
                        // 응답은 정상이지만 서비스 결과 오류
                    } else {
                        if (jarrayRecordSet.size() == 0) {
                            // 응답은 정상 - 서비스 결과 없음
                        } else {
                            JsonObject jobjectStore = jarrayRecordSet.get(0).getAsJsonObject();
                            String REFRESH_YN = jobjectStore.get("REFRESH_YN").getAsString();
                            handler.obtainMessage(REPORT_CVO_OK, REFRESH_YN).sendToTarget();
                        }
                    }
                    /*
                    JsonArray jarrayRecordSet = jObject.get("RecordSet").getAsJsonArray();

                    if (!resultCode.equals("00")) {
                        // 응답은 정상이지만 서비스 결과 오류
                    } else {
                        if (jarrayRecordSet.size() == 0) {
                            // 응답은 정상 - 서비스 결과 없음
                        } else {
                            Iterator<JsonElement> it = jarrayRecordSet.iterator();

                            while(it.hasNext()) {
                                Loggers.d(mTag + " >>>>>> " + it.next());
                            }

                            locationInfos.clear();
                            for (int idx = 0; idx < jarrayRecordSet.size(); ++idx) {
                                JsonObject jobjectStore = jarrayRecordSet.get(idx).getAsJsonObject();
                                Gson gson = new Gson();
                                locationInfo = gson.fromJson(jobjectStore, LocationInfo.class);
                                Loggers.d("locationInfo.toString() : " + locationInfo.toString());
                                locationInfos.add(locationInfo);
                            }

                        }
                        handler.obtainMessage(REQ_CURRENT_POSITION_SAV_OK, getString(R.string.complete)).sendToTarget();
                    }
                    */



                } catch (Exception e) {
                    e.printStackTrace();
                    Loggers.d("Exception : " + e.getMessage());
                }
            }else if (response.code() == 404) {
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[404]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;

            }else{
                handler.obtainMessage(RequesterSession.REQ_ERR_NOT_RESPONSE, "[" + response.code() + "]" + getString(R.string.msg_conn_failure)).sendToTarget();
                return;
            }
        }
    };








    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REPORT_HDO_OK:
                    Loggers.d("위치정보 보고완료-HDO");
                    break;
                case REPORT_CVO_OK:
                    Loggers.d("위치정보 보고완료");
                    String REFRESH_YN = String.format("%s", msg.obj.toString());
                    if (REFRESH_YN.equals("Y")) {
                        // 배차내역조회 화면도 열려있다면 위치정보업데이트 셋..호출
                        if (((DeliveryListActivity) DeliveryListActivity.mContext) != null) {
                            ((DeliveryListActivity) DeliveryListActivity.mContext).clickRefreshButton(null);
                        }
                    }

                    break;
            }
            return true;

        }
    });


    /**
     * 가져온 Location을 체크해서 보고용 데이터에 저장하는 핸들러 호출
     */
    public void startCycleHandler(){
        gpsCycleHandler.sendEmptyMessage(0);
        gpsSendTimeHandler.sendEmptyMessage(0);
    }

    /**
     * 저장한 Location 데이터 보고처리 핸들러 호출
     */
    public void stopCycleHandler(){
        gpsCycleHandler.removeMessages(0);
        gpsSendTimeHandler.removeMessages(0);
    }

    /**
     * 가져온 Location을 체크해서 보고용 데이터에 저장하는 핸들러
     */
    public Handler gpsCycleHandler = new Handler(Looper.getMainLooper()) {

        // API에 저장 요청할 데이터
        String seq;

        String gps_dtm; // 시간
        int speed; // 속도
        int bearing; // 각도
        String addr = ""; // 주소

        // 속도/거리 계산 (DB 저장용)
        double check_distance = 0.0;
        double check_time = 0.0;
        double check_speed = 0.0;

        String etc_data = "";
        String battery_level = ""; // 배터리 잔량
        @Override
        public void handleMessage(Message msg) {

//            Log.d(TAG, "[gpsCycleHandler] 진입");

            if (mCurrLocation != null) {
//                Log.d(TAG, "[gpsCycleHandler] 현재 가져온 curLocation 있으면");
                // ============================================================================
                if (mLastLocation != null && mCurrLocation != null) {
//                    Log.d(TAG, "[gpsCycleHandler] 유효 Location 판단");

                    try{
                        // 마지막 유효한 lastLocation과 curLocation 간 (거리)
                        float gpsDistanceOrg = CommonUtil.with().getDistance(mLastLocation, mCurrLocation);
//                        Loggers.d(sTag + "gpsDistanceOrg : " + gpsDistanceOrg);
                        // 소수점 제거
                        double gpsDistance = Math.floor(gpsDistanceOrg);
//                        Loggers.d("gpsDistance : " + gpsDistance);


                        long fromGpsTime = mLastLocation.getTime();
                        long toGpsTime = mCurrLocation.getTime();

                        // 마지막 유효한 lastLocation과 curLocation 간 (시간)
                        double fromToGpsTime = ( toGpsTime - fromGpsTime ) / 1000.0;
//                        Loggers.d(sTag + "fromToGpsTime : " + fromToGpsTime);

                        // 속력 = 거리 / 시간
                        // 24.7km / (33분/60 = 0.55시간) = 44.9km
                        double distance = 0.0;
                        double bun = 0.0; // 분
                        double si = 0.0; // 시

                        if(gpsDistance > 0){
                            distance = gpsDistance / 1000.0; // Km로 변환
                        }
                        if( fromToGpsTime > 0 ){
                            bun = ( fromToGpsTime / 60 ); // 분
                            si = ( bun / 60 ); // 시
                        }

//                        Loggers.d(sTag + "distance : " + distance);
//                        Loggers.d(sTag + "bun : " + bun);
//                        Loggers.d(sTag + "si : " + si);

                        double curSpeed = 0.0; // 속력
                        if( distance > 0 ){
                            curSpeed = distance / si; // 속력
                        }
//                        Loggers.d(sTag + "checkSpeed : " + curSpeed);

                        // 유효체크 - 현재 위치와 마지막(유효한) 위치간 거리의 속도가 (몇)Km 이하면 유효하다고 판단
                        if(curSpeed < limitSpeed){
                            mTargetLocation = mCurrLocation;
                        }else{
                            mTargetLocation = mLastLocation;
                        }

                        check_distance = gpsDistance; // 거리 (m)
                        check_time = fromToGpsTime; // 시간 (초)
                        check_speed = curSpeed; // 속력 (시속)

                    } catch(Exception e){
                        Loggers.d("Exception : " + e.getMessage());
                    }

                }
                // ============================================================================

            } else if (mLastLocation != null) {
                Log.d(TAG, "[gpsCycleHandler] curLocation가 없고, mLastLocation 있고..");
                // 마지막 유효 Location 있으면 lastLocation을 보고대상으로 지정
                mTargetLocation = mLastLocation;

            }else{
                Log.d(TAG, "[gpsCycleHandler] 둘다 없으면 패스..");
            }


            if (mTargetLocation != null) {
//                Log.d(TAG, "[gpsCycleHandler] 보고용 위치정보가 있으면..");

                double latitude; // 경도
                double longitude; // 위도


                try {

                    mRepLocationInfo = new LocationInfo();

                    // SEQ ID
                    seq = new SimpleDateFormat("yyyyMMddHHmmss.SSSSSS").format(System.currentTimeMillis());
                    mRepLocationInfo.setI_SEQ(seq);

                    // gps 수집시간(현재시간)
                    String gps_dtm = CommonUtil.with().nowYYYYMMDDHHMMSS();

                    // 좌표정보
                    latitude = mTargetLocation.getLatitude();
                    longitude = mTargetLocation.getLongitude();

                    // 마지막 수집한 위치정보 전역변수에 담기
                    AppSetting.GPS_MANAGER.GPS_TIME = gps_dtm;
                    AppSetting.GPS_MANAGER.GPS_LAT = latitude;
                    AppSetting.GPS_MANAGER.GPS_LNG = longitude;

                    mRepLocationInfo.setI_DEVICE_ID(AppSetting.Device.DEVICE_ID);
                    mRepLocationInfo.setI_TERMINAL_NO(AppSetting.Device.PHONE_NUMBER);
                    mRepLocationInfo.setI_GPS_DTM(gps_dtm);
                    mRepLocationInfo.setI_POS_Y(Double.toString(latitude));
                    mRepLocationInfo.setI_POS_X(Double.toString(longitude));

                    // 스피드
                    if( mTargetLocation.hasSpeed() ){

                        try {
                            speed = Math.round(mTargetLocation.getSpeed());
                            speed = (int) Math.round(speed * 3.6); // m/s를 km/h로 변환
                        } catch (Exception ex) {
                            speed = 0;
                        }
                    }
                    mRepLocationInfo.setI_SPEED(Integer.toString(speed));

                    // 방향
                    if (mTargetLocation.hasBearing() ) {
                        try {
                            bearing = (int) Math.round(mTargetLocation.getBearing());
                        } catch (Exception ex) {
                            bearing = 0;
                        }
                    }
                    mRepLocationInfo.setI_ANGEL(Integer .toString(bearing));

                    // 주소검색
                    addr = CommonUtil.with().getAddressString(getApplicationContext(), mTargetLocation);
                    mRepLocationInfo.setI_ADDRESS_INFO(addr);
                    // etc_data (충전여부)
                    etc_data = DeviceUtil.with().getBatteryStatus(getApplicationContext());
                    mRepLocationInfo.setI_ETC_DATA(etc_data);
                    // 배터리잔량 (%)
                    String battery_level = Integer.toString(DeviceUtil.with().getBatteryPercent(getApplicationContext()));
                    mRepLocationInfo.setI_BATTERY_LEVEL(battery_level);

//                    float dist = 0;
//                    // 이동거리
//                    if (mLastLocation != null) {
//                        dist = CommonUtil.with().getDistance(mLastLocation, mTargetLocation);
//                        Loggers.d("###>>> dist : " + dist);
//
//                        mDistance_tot = Math.floor(mDistance_tot + dist);
//                    }

//                    mLocationInfo.setI_DISTANCE_TO(Double.toString(mDistance_tot));

                    if (check_distance>0) {
                        String distance = String.format("%.0f", check_distance);
                        mRepLocationInfo.setI_CHECK_DISTANCE(distance);
                    }
                    mRepLocationInfo.setI_CHECK_TIME(Double.toString(check_time));
                    mRepLocationInfo.setI_CHECK_SPEED(Double.toString(check_speed));

//                    locationInfos.add(mLocationInfo);

                    // 처리된 (유효한) Location을 lastLocation으로 저장
                    mLastLocation = mTargetLocation;


                    AppSetting.GPS_MANAGER.locationInfo = mRepLocationInfo; // 2022.06.22 - 위치정보 객체 추가..

                    // GPS 상태정보 브로드캐스트..
                    gpsBroadCastIntent.putExtra(
                            BroadCastDomain.BROADCAST_TRACE_GPS_STATUS,
                            BroadCastDomain.REPORT_CURRENT_LOCATION);
                    sendBroadcast(gpsBroadCastIntent);

                }catch(Exception e){
                    Log.d(TAG, "[gpsCycleHandler] 보고용 위치정보 Exception : " + e.getMessage());
                }

            } else {
                Log.d(TAG, "[gpsCycleHandler] 보고용 위치정보가 없다..!");
            }


//            if( mCurrLocation != null ) {
//                // 수집처리가 끝나면 현재 curLocation을 직전 beforeLocation으로 일단 저장해 둔다
//                beforeLocation = mCurrLocation;
//            }

            // mGPS_CYCLE주기후에 재호출...
//            nRandMili = 1000 + random.nextInt(999) ;
            mGPS_수집주기 = Integer.parseInt(AppSetting.cvoUserInfo.getGPS_GET_CYCLE()) * 1000; // 주기(초)
//            if (mGPS_CYCLE<=1000) mGPS_CYCLE = 999;
            gpsCycleHandler.sendEmptyMessageDelayed(0, mGPS_수집주기);
        }
    };


    /**
     *  저장한 Location 데이터 보고처리 핸들러
     */
    public Handler gpsSendTimeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            if( mRepLocationInfo != null ) {
                // 출근의 경우에만 위치를 전송..
                String GPS_REPORT_START = SettingPref.with(getApplicationContext()).loadPref(ConstValue.LastWorkStat.GPS_REPORT_START.toString(), "");
                if (
                        (GPS_REPORT_START.equals("Y")) || (AppSetting.todayStatus.equals(TodayStatus._출근))
                ) {
                    reportCVO(mRepLocationInfo); // CVO에 전송
                    // currentPositionSav(mLocationInfo); // HDO에 전송
                }
            }

//            mDistance_tot = 0;
            int mGPS_SEND_TIME = 1000 * Integer.parseInt(AppSetting.cvoUserInfo.getGPS_SEND_TIME()); // 전송(초)
            gpsSendTimeHandler.sendEmptyMessageDelayed(0, mGPS_SEND_TIME);
        }
    };


}
