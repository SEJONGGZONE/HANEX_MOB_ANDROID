package com.gzonesoft.sg623.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PermissionSupport {

    private String TAG = "#PermissionSupport";

    private Context context;
    private Activity activity;

    /**
     * 사용자에게 요청할 권한
     */
    private String[] needPermissions = {
            Manifest.permission.READ_PHONE_NUMBERS,

            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.ACCESS_BACKGROUND_LOCATION, // 별도 체크 요청

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.MANAGE_EXTERNAL_STORAGE, // 별도 체크 요청

            Manifest.permission.CAMERA,

            Manifest.permission.INTERNET,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.FOREGROUND_SERVICE
    };


    // DENIED PERMISSION
    private List deniedPermissionList;

    //권한 요청 구분코드
    public static final int MULTIPLE_PERMISSIONS = 2001;

    public PermissionSupport(Activity mActivity, Context mContext){

        this.activity = mActivity;
        this.context = mContext;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            needPermissions[0] = Manifest.permission.READ_PHONE_NUMBERS;
        }else{
            needPermissions[0] = Manifest.permission.READ_PHONE_STATE;
        }
    }

    /**
     * 배열로 선언한 권한 중 허용되지 않은 권한 있는지 체크
     * @return boolean
     */
    public boolean checkPermission() {
        int result;
        deniedPermissionList = new ArrayList<>();
        for(String permission : needPermissions){
            result = ContextCompat.checkSelfPermission(context, permission);
            Log.d(TAG,"permission:" + permission + ", result:" + result);
            if(result == PackageManager.PERMISSION_DENIED){
                deniedPermissionList.add(permission);
                Log.d(TAG, "deniedPermissionList.add(" + permission + ")");
            }
        }
        if(!deniedPermissionList.isEmpty()){
            Log.d(TAG, "!deniedPermissionList.isEmpty()");
            return false;
        }
        return true;
    }

    /**
     * 배열로 선언한 권한에 대해 사용자에게 허용 요청
     */
    public void requestPermission(){
        Log.d(TAG,"requestPermission()");

        Log.d(TAG, deniedPermissionList.isEmpty() + "");
        Log.d(TAG, deniedPermissionList.size() + "");
        Iterator<String> it = deniedPermissionList.iterator();
        while(it.hasNext()) {
            Log.d(TAG, it.next());
        }

        ActivityCompat.requestPermissions(activity, (String[]) deniedPermissionList.toArray(new String[deniedPermissionList.size()]), MULTIPLE_PERMISSIONS);
    }

    /**
     * 요청한 권한에 대한 결과값 판단 및 처리
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return boolean
     */
    public boolean resultPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean rtn = true;
        if(requestCode == MULTIPLE_PERMISSIONS && (grantResults.length > 0)) {
            for(int i=0; i<grantResults.length; i++){
                Log.d(TAG,"permissions[" + i + "]:" + permissions[i] + ", grantResults[" + i + "]:" + grantResults[i]);
                // grantResults가 0이면 사용자가 허용, -1이면 거부, -1이 있는지 체크하여 하나라도 -1이 있으면 false를 리턴
                if(grantResults[i] == -1){
                    rtn = false;
                }
            }
        }else{
            Log.d(TAG, "grantResults.length == 0");
        }
        return rtn;
    }

}
