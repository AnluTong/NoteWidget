package me.andrew.notewidget.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import me.andrew.notewidget.domain.remoteViewInvalidate

/**
 * Created by Andrew
 * Date on 21-9-28.
 */
class DefaultWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        remoteViewInvalidate(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        remoteViewInvalidate(context)
    }

    override fun onRestored(context: Context, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        remoteViewInvalidate(context)
    }
}