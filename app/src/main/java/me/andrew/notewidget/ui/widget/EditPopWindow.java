package me.andrew.notewidget.ui.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.andrew.notewidget.R;
import me.andrew.notewidget.utils.UIUtils;


public class EditPopWindow extends PopupWindow {

    public EditPopWindow(final Context context, int[] titles, final PopClickListener listener) {
        if (titles == null || titles.length < 2) {
            throw new IllegalArgumentException("title size should bigger than 2");
        }
        ViewGroup content = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.window_list_edit, null);
        this.setContentView(content);
        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);

        LinearLayout ll = content.findViewById(R.id.ll_container);

        int r = UIUtils.dipToPx(5f);
        GradientDrawable gd = UIUtils.makeRoundDrawable(new float[]{r, r, r, r, r, r, r, r}, Color.TRANSPARENT);
        gd.setStroke(3, UIUtils.getColor(R.color.public_line));
        ll.setBackground(gd);

        for (int i = 0; i < titles.length; ++i) {
            View item = LayoutInflater.from(context).inflate(R.layout.window_edit_item, content, false);
            TextView tv = item.findViewById(R.id.tv_text);
            tv.setText(titles[i]);

            float[] ra;
            if (i == 0) {
                ra = new float[]{r, r, r, r, 0, 0, 0, 0};
            } else if (i == titles.length - 1) {
                ra = new float[]{0, 0, 0, 0, r, r, r, r};
            } else {
                ra = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
            }
            Drawable dr = new UIUtils.SelectorDrawableBuilder()
                    .pressedDrawable(
                            UIUtils.makeRoundDrawable(ra, UIUtils.getColor(R.color.public_click_cover)), true
                    )
                    .build();
            item.setBackground(dr);
            final int pos = i;
            item.setOnClickListener(v -> {
                dismiss();
                if (listener != null) {
                    listener.onClick(v, pos);
                }
            });
            ll.addView(item);
        }

        this.update();
    }

    public void showPopupWindow(View parent, float offsetX) {
        if (!this.isShowing()) {
            showAsDropDown(parent, (int) offsetX, -UIUtils.dipToPx(20));
        }
    }

    public interface PopClickListener {
        void onClick(View view, int pos);
    }
}
