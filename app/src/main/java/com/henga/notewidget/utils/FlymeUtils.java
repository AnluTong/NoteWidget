package com.henga.notewidget.utils;

import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FlymeUtils {
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
