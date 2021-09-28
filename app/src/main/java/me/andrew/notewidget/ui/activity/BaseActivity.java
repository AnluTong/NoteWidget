package me.andrew.notewidget.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.andrew.notewidget.utils.StatusBarUtil;

/**
 * Created by Andrew
 * Date on 21-6-24.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(layoutId());
        initArguments(getIntent());
        initView();
        initData();
    }

    @LayoutRes
    protected abstract int layoutId();

    protected void beforeSetContentView() {
        StatusBarUtil.setStatusBarColor(this, statusBarColor());
        StatusBarUtil.setTranslucent(this, true);
    }

    protected void initArguments(Intent intent) {

    }

    protected void initView() {

    }

    protected void initData() {

    }

    @ColorInt
    protected int statusBarColor() {
        return Color.TRANSPARENT;
    }
}
