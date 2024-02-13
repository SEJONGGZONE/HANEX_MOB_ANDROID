package com.gzonesoft.sg623.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingPref {
    private static SettingPref singleton;

    private SettingPref() {
    }

    public static WSettingPref with(Context pCon) {
        if (singleton == null) {
            singleton = new SettingPref();
        }
        return singleton.get(pCon);
    }

    private WSettingPref get(Context pCon) {
        return new WSettingPref(pCon);
    }

    public class WSettingPref {
        private Context mCon;

        private WSettingPref(Context pCon) {
            mCon = pCon;
        }

/*		public Enty_Setting setDefaultSetting() {
            Enty_Setting enty = new Enty_Setting(true, true, false, "eng", "start", "");
			return enty;
		}*/

		/*
         * public void saveSettingEnty(Enty_Setting settingEnty){
		 * SharedPreferences prefs = mCon.getSharedPreferences("setting_enty",
		 * Context.MODE_PRIVATE); Editor editor = prefs.edit(); try {
		 * editor.putString("setting_",
		 * ObjectSerializer.serialize(settingEnty)); } catch (IOException e) {
		 * e.printStackTrace(); } editor.commit(); }
		 */

        public void savePrefWithBoolean(String key, boolean item) {
            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
            Editor editor = prefs.edit();
            editor.putBoolean(key, item);
            editor.commit();
        }

        public void savePrefWithString(String key, String item) {
//            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
//            Editor editor = prefs.edit();
//            try {
//                //암호화 처리
//                editor.putString(key, Crypto.encrypt(item));
//                editor.commit();
//            } catch (Exception e) {
//                e.printStackTrace();
//                editor.putString(key, item);
//            }
//            editor.commit();
            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
            Editor editor = prefs.edit();
            editor.putString(key, item);
            editor.commit();

        }

        public void savePrefWithLong(String key, long item) {
            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
            Editor editor = prefs.edit();
            editor.putLong(key, item);
            editor.commit();
        }


        public void saveSetting_ReceiveCall(boolean isReceive) {
            savePrefWithBoolean("isCallReceiveAlert", isReceive);
        }

        public void saveSetting_ReceiveSms(boolean isReceive) {
            savePrefWithBoolean("isSmsAlert", isReceive);
        }

        public void saveSetting_ReceiveSchedule(boolean isReceive) {
            savePrefWithBoolean("isScheduleAlert", isReceive);
        }

        public void saveSetting_RecordingLanguage(String lan) {
            savePrefWithString("RecordingLanguage", lan);
        }

        public void saveSetting_PenPairing(String mode) {
            savePrefWithString("PenPairingMode", mode);
        }

        public void saveSetting_Deviceid(String id) {
            savePrefWithString("DeviceId", id);
        }

        public void saveSetting_Headsetid(String id) {
            savePrefWithString("HeadsetId", id);
        }

//		// 왼손 설정
//		public void saveSetting_Left(boolean isLeft) {
//			savePrefWithString("Left", isLeft);
//		}
//		
//		// 분실 알림
//		public void saveSetting_Alarm(boolean isAlarm) {
//			savePrefWithString("Alarm", isAlarm);
//		}

		/*
         * public Enty_Setting loadSettingEnty(){ Enty_Setting enty = new
		 * Enty_Setting(); SharedPreferences prefs =
		 * mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE); try
		 * { enty = (Enty_Setting)
		 * ObjectSerializer.deserialize(prefs.getString("setting_",
		 * ObjectSerializer.serialize(new Enty_Setting()))); if(null == enty)
		 * return setDefaultSetting(); } catch (Exception e) {
		 * e.printStackTrace(); return null; }
		 * 
		 * return enty; }
		 */

        public boolean loadPref(String key, boolean def) {
            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
            return prefs.getBoolean(key, def);

        }

        public String loadPref(String key, String def) {
//            String decodeValue = null;
//            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
//            try {
//                //복호화처리
//                decodeValue = Crypto.decrypt(prefs.getString(key, def));
//            } catch (Exception e) {
//                e.printStackTrace();
//                decodeValue = prefs.getString(key, def);
//            }
//            return decodeValue;
            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
            return prefs.getString(key, def);
        }

        public long loadPref(String key, long def) {
            SharedPreferences prefs = mCon.getSharedPreferences("setting_enty", Context.MODE_PRIVATE);
            return prefs.getLong(key, def);
        }


        public boolean loadSetting_ReceiveCall() {
            return loadPref("isCallReceiveAlert", true);
        }

        public boolean loadSetting_ReceiveSms() {
            return loadPref("isSmsAlert", true);
        }

        public boolean loadSetting_ReceiveSchedule() {
            return loadPref("isScheduleAlert", false);
        }

        public String loadSetting_RecordingLanguage() {
            return loadPref("RecordingLanguage", "kor");
        }

        public String loadSetting_PenPairing() {
            return loadPref("PenPairingMode", "start");
        }

        public String loadSetting_Deviceid() {
            return loadPref("DeviceId", "");
        }

        public String loadSetting_Headsetid() {
            return loadPref("HeadsetId", "");
        }

//		public boolean loadSetting_Left() {
//			return loadPref("Left", false);
//		}
//		
//		public boolean loadSetting_Alarm() {
//			return loadPref("Alarm", false);
//		}
    }
}
