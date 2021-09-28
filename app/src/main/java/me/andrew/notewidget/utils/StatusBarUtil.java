package me.andrew.notewidget.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.core.view.ViewCompat;


public class StatusBarUtil {

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeightPx(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


    //修改为全屏
    public static void setTranslucent(final Activity activity, final boolean translucent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            if (translucent) {
                decorView.setOnApplyWindowInsetsListener(
                        (v, insets) -> {
                            WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                            return defaultInsets.replaceSystemWindowInsets(
                                    defaultInsets.getSystemWindowInsetLeft(),
                                    0,
                                    defaultInsets.getSystemWindowInsetRight(),
                                    defaultInsets.getSystemWindowInsetBottom());
                        });
            } else {
                decorView.setOnApplyWindowInsetsListener(null);
            }
            ViewCompat.requestApplyInsets(decorView);
        }
    }

    //修改状态栏颜色
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }
    }

    public static int getNavigationBarHeight(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Display display = activity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                Point realSize = new Point();
                display.getSize(size);
                display.getRealSize(realSize);
                Resources resources = activity.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                int height = resources.getDimensionPixelSize(resourceId);
                //超出系统默认的导航栏高度以上，则认为存在虚拟导航
                if ((realSize.y - size.y) > (height - 10)) {
                    return height;
                }

                return 0;
            } else {
                boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
                boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                if (menu || back) {
                    return 0;
                } else {
                    Resources resources = activity.getResources();
                    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                    int height = resources.getDimensionPixelSize(resourceId);
                    return height;
                }
            }
        } catch (Exception exception) {
            Log.e("StatusBarUtil", "", exception);
        }
        return 0;
    }

}
