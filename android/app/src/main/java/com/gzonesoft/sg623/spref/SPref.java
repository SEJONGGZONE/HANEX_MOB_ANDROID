package com.gzonesoft.sg623.spref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

//import com.gzonesoft.cookzzang.util.Crypto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * SharedPreference 관리
 */
public class SPref {

    public static void set(Context context, String name, Data data) {

        Map<String, Object> map = data.get();
        if (map.size() == 0)
            return;

        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Iterator<String> it = map.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();

            Object value = map.get(key);
            try {
                if (value instanceof String) {
                    //암호화처리
//                    editor.putString(key, Crypto.encrypt((String) value));
                    editor.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                } else if (value instanceof Float) {
                    editor.putFloat(key, (Float) value);
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                } else if (value instanceof Double) {
                    editor.putFloat(key, (float) ((double) ((Double) value)));
                } else if (value instanceof byte[]) {
                    editor.putString(key, new String((byte[]) value));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        editor.commit();

    }

    public static Object get(Context context, String name, String key) {
        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        Map<String, ?> map = pref.getAll();
        return map.get(key);
    }

    public static Long getLong(Context context, String name, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        Map<String, ?> map = pref.getAll();
        Object obj = map.get(key);

        if (obj == null)
            return value;

        return (Long) map.get(key);
    }

    public static Integer getInteger(Context context, String name, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        Map<String, ?> map = pref.getAll();
        Object obj = map.get(key);

        if (obj == null)
            return value;

        return (Integer) map.get(key);
    }

    public static byte[] getArrayByte(Context context, String name, String key, byte[] value) {
        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        Map<String, ?> map = pref.getAll();
        Object obj = map.get(key);

        if (obj == null)
            return value;

        return ((String) map.get(key)).getBytes();
    }

    public static String getString(Context context, String name, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        Map<String, ?> map = pref.getAll();
        Object obj = map.get(key);

        if (obj == null)
            return value;

        String mapValue = null;
        try {
            //복호화처리
//            mapValue = Crypto.decrypt((String) map.get(key));
        } catch (Exception e) {
            e.printStackTrace();
            mapValue = (String) map.get(key);
        }
        return mapValue;

//        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
//
//        Map<String, ?> map = pref.getAll();
//        Object obj = map.get(key);
//
//        if (obj == null)
//            return value;
//
//        return (String) map.get(key);

    }

    public static Boolean getBoolean(Context context, String name, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);

        Map<String, ?> map = pref.getAll();

        Object obj = map.get(key);

        if (obj == null)
            return value;

        return (Boolean) map.get(key);
//		return map.get(key);
    }

    public static class Data {
        Map<String, Object> map = new HashMap<String, Object>();

        public void add(String key, Object value) {
            map.put(key, value);
        }

        public Map<String, Object> get() {
            return map;
        }
    }

    public static void clear(Context context, String[] prefNames) {
        for (String name : prefNames) {
            SharedPreferences pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        }
    }


}
