package me.andrew.notewidget.ui.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

//和状态栏一样高度的View
public class StatusBarView extends View {
    public StatusBarView(Context context) {
        super(context);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(resourceId), MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (getResources().getDisplayMetrics().density * 25), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
