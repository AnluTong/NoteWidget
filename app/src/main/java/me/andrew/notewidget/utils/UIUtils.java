package me.andrew.notewidget.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import me.andrew.notewidget.R;

/**
 * Created by Andrew
 * Date on 21-6-10.
 */
public class UIUtils {

    private static volatile Context sContext;

    public static void initialize(Context context) {
        sContext = context;
    }

    public static Resources getResources() {
        return sContext.getResources();
    }

    public static String getString(int res) {
        return sContext.getString(res);
    }

    public static String getString(int resId, Object... formatArgs) {
        return sContext.getString(resId, formatArgs);
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(sContext, colorId);
    }

    public static Drawable getDrawable(int resId) {
        return sContext.getDrawable(resId);
    }

    public static AssetManager getAssets() {
        return sContext.getAssets();
    }

    public static int dipToPx(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * pxToDip
     */
    public static int pxToDip(float pxValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * getScreenWidth
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 0;
    }

    /**
     * getScreenHeight
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 0;
    }

    public static class SelectorDrawableBuilder {
        private Map<Integer, Drawable> map = new HashMap<>();
        private Drawable normalDrawable;

        public SelectorDrawableBuilder() {
        }

        public SelectorDrawableBuilder normalDrawable(Drawable dw) {
            normalDrawable = dw;
            return this;
        }

        public SelectorDrawableBuilder pressedDrawable(Drawable dw, boolean pressed) {
            int r = android.R.attr.state_pressed;
            map.put(r, dw);
            return this;
        }

        public SelectorDrawableBuilder checkDrawable(Drawable dw, boolean checked) {
            int r = android.R.attr.state_checked;
            map.put(r, dw);
            return this;
        }

        public SelectorDrawableBuilder selectDrawable(Drawable dw, boolean selected) {
            int r = android.R.attr.state_selected;
            map.put(r, dw);
            return this;
        }

        public StateListDrawable build() {
            StateListDrawable sd = new StateListDrawable();
            if (normalDrawable != null && map.size() == 0) {
                sd.addState(new int[0], normalDrawable);
            }
            for (Map.Entry<Integer, Drawable> et : map.entrySet()) {
                if (normalDrawable != null && et.getKey() > 0 && !map.containsKey(-et.getKey())) {
                    sd.addState(new int[]{-et.getKey()}, normalDrawable);
                }
                sd.addState(new int[]{et.getKey()}, et.getValue());
            }
            return sd;
        }
    }

    public static GradientDrawable makeRoundDrawable(float[] radii, int... bgColors) {
        if (radii.length < 8) {
            throw new IllegalArgumentException("radii should set 8 param");
        }
        if (bgColors.length == 0 || bgColors.length > 3) {
            throw new IllegalArgumentException("bg color param should below 3");
        }
        if (bgColors.length == 1) {
            bgColors = new int[] {bgColors[0], bgColors[0]};
        }
        GradientDrawable dw = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, bgColors);
        dw.setShape(GradientDrawable.RECTANGLE);
        dw.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置线性渐变
        dw.setCornerRadii(radii);
        return dw;
    }

    public static Drawable makeRoundClickCover(float[] radii, int... bgColors) {
        RoundRectShape sp = new RoundRectShape(radii, null, null);
        ShapeDrawable dw = new ShapeDrawable(sp);
        dw.getPaint().setColor(getColor(R.color.public_click_cover));
        dw.getPaint().setAntiAlias(true);
        return makeLayerDrawable(makeRoundDrawable(radii, bgColors), dw);
    }

    public static Drawable clone(Drawable raw) {
        return raw.getConstantState().newDrawable();
    }

    public static Drawable makeLayerDrawable(Drawable... ds) {
        return new LayerDrawable(ds);
    }
}
