package com.henga.notewidget.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.henga.notewidget.utils.FlymeUtils;
import com.henga.notewidget.utils.MIUIUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Andrew on 2016/3/29.
 */
public abstract class HGBaseActivity extends Activity {

    public final Activity activity() {
        return this;
    }

    // 弱引用XmPluginBaseActivity对象，推荐使用该机制
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        mHandler = new ActivityHandler(this);
    }

    private static class ActivityHandler extends Handler {

        WeakReference<HGBaseActivity> mRefActivity;

        private ActivityHandler(HGBaseActivity activity) {
            mRefActivity = new WeakReference<HGBaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mRefActivity != null) {
                HGBaseActivity activity = mRefActivity.get();
                if (activity != null && !activity.isFinishing()) {
                    activity.handleMessage(msg);
                }
            }
        }
    }

    /**
     * 如果需要用到mHandler发送消息，需要子类重载该函数
     *
     * @param msg
     */
    public void handleMessage(Message msg) {
    }

    @TargetApi(19)
    private void initWindow() {
        if(canEnbeddedBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    public void setBarPadding(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && canEnbeddedBar()) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) params;
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            if (statusBarHeight == 0) {
                statusBarHeight = 30;
            }
            marginLayoutParams.height += statusBarHeight;
            view.setLayoutParams(marginLayoutParams);

            int top = view.getPaddingTop();
            top += statusBarHeight;
            int bottom = view.getPaddingBottom();
            int left = view.getPaddingLeft();
            int right = view.getPaddingRight();

            view.setPadding(left, top, right, bottom);

            setStatusBarTextDarkColor();
        }
    }

    private boolean canEnbeddedBar() {
        return MIUIUtils.isMIUI() ||
                FlymeUtils.isFlyme() ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void setStatusBarTextDarkColor() {
        if(FlymeUtils.isFlyme()) {
            FlymeSetStatusBarLightMode(getWindow(), true);
        }else if(MIUIUtils.isMIUI()) {
            MIUISetStatusBarLightMode(getWindow(), true);
        }else {
            AndroidMSetStatusBarLightMode(getWindow());
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    private boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    private boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field  field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }

    private void AndroidMSetStatusBarLightMode(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
