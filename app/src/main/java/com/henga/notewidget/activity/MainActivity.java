package com.henga.notewidget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.henga.notewidget.R;
import com.henga.notewidget.base.HGBaseActivity;
import com.henga.notewidget.info.SystemInfo;
import com.henga.notewidget.utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrew on 2016/7/21.
 */
public class MainActivity extends HGBaseActivity {
    private static final String PREF_KEY = "hgPrefKey";
    private static final String VALUE_KEY = "valueKey";
    private View mVBack;
    private View mVMore;
    private TextView mVTitle;
    private EditText mETPayload;
    private View mBtnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hg_main_activity);

        bindView();
        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeText();
    }

    private void bindView() {
        mVBack = findViewById(R.id.module_a_3_return_btn);
        mVMore = findViewById(R.id.module_a_3_return_more_more_btn);
        mVTitle = (TextView) findViewById(R.id.module_a_3_return_title);
        mETPayload = (EditText) findViewById(R.id.main_text_payload);
        mBtnOk = findViewById(R.id.main_btn_ok);
    }

    private void initTitle() {
        mVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVTitle.setText(R.string.main_activity_title);

        mVMore.setVisibility(View.GONE);

        setBarPadding(findViewById(R.id.title_bar));
    }

    private void initView() {
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
                finish();
            }
        });

    }

    private void readText() {
        String text = DataUtils.readStringPreference(activity(), PREF_KEY, VALUE_KEY);
        if (text != null) {
            mETPayload.setText(text);
        }
    }

    private void writeText() {
        String text = mETPayload.getText().toString();
        DataUtils.writeStringPreference(activity(), PREF_KEY, VALUE_KEY, text);
    }

    private void sendText() {
        try {
            Date nowTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String retStrFormatNowDate = sdFormatter.format(nowTime);

            String text = retStrFormatNowDate + "\n" + mETPayload.getText();

            JSONObject object = new JSONObject();
            object.put("text", text);

            Intent intent = new Intent();
            intent.setAction(SystemInfo.ACTION_MSG);
            intent.putExtra("note_payload", object.toString());
            sendBroadcast(intent);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
