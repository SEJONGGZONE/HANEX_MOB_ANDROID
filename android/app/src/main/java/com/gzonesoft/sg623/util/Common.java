package com.gzonesoft.sg623.util;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;

public final class Common {

    public static void ShowMessage(Activity act, String title, String message) {

        UDialog.withOne_ok(act, message).forShow(new UDialog.WDialogBtnOne.Callback() {
            @Override
            public void onClick() {

            }
        });

    }


    // ------------------------------------------------------------------------------------------------------
    // 특정뷰안에 텍스트 뷰의 스타일 일괄적용 (일단 LinearLayout 내의 텍스트뷰중 폰트 스타일만 처리한다..나중에 한번더 공통화 모듈로 활용가능.)
    // ------------------------------------------------------------------------------------------------------
    public static void setTextTypefaceInView(View parent, int ParentID, int TextStyle) {
        LinearLayout rootLinearLayout = (LinearLayout) parent.findViewById(ParentID);
        int count = rootLinearLayout.getChildCount();
        for (int j = 0; j < count; j++) {
            View v = rootLinearLayout.getChildAt(j);

            // Loggers.d("v.getId() = " + v.getId());

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTypeface(null, TextStyle);
            }

        }
    }

    // ------------------------------------------------------------------------------------------------------
    // 특정뷰안에 텍스트 뷰의 스타일 일괄적용 (일단 LinearLayout 내의 텍스트뷰중 폰트 스타일만 처리한다..나중에 한번더 공통화 모듈로 활용가능.)
    // ------------------------------------------------------------------------------------------------------
    public static void setTextColorInView(View parent, int ParentID, String ColorString) {
        LinearLayout rootLinearLayout = (LinearLayout) parent.findViewById(ParentID);
        int count = rootLinearLayout.getChildCount();
        for (int j = 0; j < count; j++) {
            View v = rootLinearLayout.getChildAt(j);

            // Loggers.d("v.getId() = " + v.getId());

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextColor(Color.parseColor(ColorString));
            }

        }
    }

    // ------------------------------------------------------------------------------------------------------
    // 리스트 뷰내의 지정 위치에 해당하는 뷰를 가져온다
    // ------------------------------------------------------------------------------------------------------
    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static String getNullAsEmptyString(JsonElement jsonElement) {
        String retString = null;
        if (jsonElement == null) {
            retString = "";
        } else if (jsonElement.isJsonNull()) {
            retString = "";
        } else {
            retString = StringUtil.getNvlStr(jsonElement.getAsString()).toString();
        }
        //return jsonElement.isJsonNull() ? "" : StringUtil.getNvlStr(jsonElement.getAsString()).toString();
        return retString;
    }


    // ----------------------------------------------------------------
    // 최종보고 정보 저장
    // ----------------------------------------------------------------
    public static void saveLastReport(String idx, String orderNo, String type) {
//        Loggers.d("[최종보고 정보 저장]=" + idx + "/" + orderNo + "/" + type);
//        SPref.Data data = new SPref.Data();
//        data.add(SPrefDefine.KEY_ROUTE_IDX, idx);
//        data.add(SPrefDefine.KEY_ORDER_NO, orderNo);
//        data.add(SPrefDefine.KEY_REPORT_TYPE, type);
//        data.add(SPrefDefine.KEY_LAT, StringUtil.getNvlStr(SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).loadPref(ConstValue.GPS_MANAGER.GPS_LAT.name(), "")).toString());
//        data.add(SPrefDefine.KEY_LNG, StringUtil.getNvlStr(SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).loadPref(ConstValue.GPS_MANAGER.GPS_LNG.name(), "")).toString());
//
//        SPref.set(SmartDeliveryApplication.getInstance().getApplicationContext(), SPrefDefine.NAME_LAST_REPORT, data);

        // 최종 보고정보 갱신
        loadLastReport();
    }

//    public static String getLastReport(String key) {
//        return StringUtil.getNvlStr(SPref.getString(SmartDeliveryApplication.getInstance().getApplicationContext(), SPrefDefine.NAME_LAST_REPORT, key, "")).toString();
//    }

    // ----------------------------------------------------------------
    // 최종보고 정보 조회 - 조회후 전역변수 세팅
    // ----------------------------------------------------------------
    public static void loadLastReport() {

//        SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).savePrefWithString(ConstValue.LastReport.ROUTE_IDX.name(), getLastReport(SPrefDefine.KEY_ROUTE_IDX));
//        SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).savePrefWithString(ConstValue.LastReport.ORDER_NO.name(), getLastReport(SPrefDefine.KEY_ORDER_NO));
//        SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).savePrefWithString(ConstValue.LastReport.REPORT_TYPE.name(), getLastReport(SPrefDefine.KEY_REPORT_TYPE));
//        SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).savePrefWithString(ConstValue.LastReport.LAT.name(), getLastReport(SPrefDefine.KEY_LAT));
//        SettingPref.with(SmartDeliveryApplication.getInstance().getApplicationContext()).savePrefWithString(ConstValue.LastReport.LNG.name(), getLastReport(SPrefDefine.KEY_LNG));

    }


}
