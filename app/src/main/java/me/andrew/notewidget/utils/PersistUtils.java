package me.andrew.notewidget.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PersistUtils {

    private static volatile SharedPreferences sPreferences;

    public static void initialize(Context context) {
        sPreferences = context.getSharedPreferences("widget_sp", Activity.MODE_PRIVATE);
    }

    public static void put(String key, Object object) {
        put(key, object, false);
    }

    public static void put(String key, Object object, boolean sync) {
        SharedPreferences.Editor editor = sPreferences.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        if (sync) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static String getString(String key, String defaultValue) {
        return sPreferences.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sPreferences.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return sPreferences.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sPreferences.getLong(key, defaultValue);
    }

    public static Map<String, ?> getAll() {
        return sPreferences.getAll();
    }

    public static void remove(String key) {
        remove(key, false);
    }

    public static void remove(String key, boolean sync) {
        if (sync) {
            sPreferences.edit().remove(key).commit();
        } else {
            sPreferences.edit().remove(key).apply();
        }
    }

    public static boolean contains(String key) {
        return sPreferences.contains(key);
    }
}
