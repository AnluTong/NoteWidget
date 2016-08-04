package com.henga.notewidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.henga.notewidget.R;
import com.henga.notewidget.activity.MainActivity;
import com.henga.notewidget.info.SystemInfo;
import com.henga.notewidget.utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/21.
 */
public class SmallTransparentWidgetProvider extends AppWidgetProvider {
    private static final String PREF_KEY = "widgetPrefKey";
    private static final String VALUE_KEY = "widgetValueKey";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SystemInfo.ACTION_MSG.equalsIgnoreCase(action))
        {
            String value = intent.getStringExtra("note_payload");
            if (value == null)
            {
                return;
            }
            RemoteViews remote_views = new RemoteViews(context.getPackageName(), R.layout.small_transparent_widget_layout);
            try
            {
                JSONObject jsonObject = new JSONObject(value);
                String tempString = jsonObject.optString("text", null);
                if (tempString != null)
                {
                    remote_views.setTextViewText(R.id.show_text, tempString);
                    DataUtils.writeStringPreference(context, PREF_KEY, VALUE_KEY, tempString);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            AppWidgetManager appwidget_manager = AppWidgetManager.getInstance(context);
            ComponentName component_name = new ComponentName(context, SmallTransparentWidgetProvider.class.getName());
            appwidget_manager.updateAppWidget(component_name, remote_views);
        }
        else
            super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, 0);

        RemoteViews remote_views = new RemoteViews(context.getPackageName(), R.layout.small_transparent_widget_layout);
        remote_views.setOnClickPendingIntent(R.id.show_text, pendingIntent);
        //更新appwidget
        appWidgetManager.updateAppWidget(appWidgetIds, remote_views);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        String text = DataUtils.readStringPreference(context, PREF_KEY, VALUE_KEY);
        if(text != null) {
            RemoteViews remote_views = new RemoteViews(context.getPackageName(), R.layout.small_transparent_widget_layout);
            remote_views.setTextViewText(R.id.show_text, text);

            AppWidgetManager appwidget_manager = AppWidgetManager.getInstance(context);
            ComponentName component_name = new ComponentName(context, SmallTransparentWidgetProvider.class.getName());
            appwidget_manager.updateAppWidget(component_name, remote_views);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
