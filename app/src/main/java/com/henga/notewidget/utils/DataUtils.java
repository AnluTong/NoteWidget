package com.henga.notewidget.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Andrew on 2015/12/4.
 */
public class DataUtils {

    /**
     * 数据类型为String
     **/
    public static boolean writeStringPreference(Context context, String preferenceKey, String key, String raw) {
        if (key == null || raw == null) {
            return false;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceKey, Activity.MODE_PRIVATE);
        if (sharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.putString(key, raw);
        editor.apply();
        return true;
    }

    /**
     * 数据类型为String
     **/
    public static String readStringPreference(Context context, String preferenceKey, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceKey, Activity.MODE_PRIVATE);
        if (sharedPreferences == null) {
            return null;
        }

        return sharedPreferences.getString(key, null);
    }
}
