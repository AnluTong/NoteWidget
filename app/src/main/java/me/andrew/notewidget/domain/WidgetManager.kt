package me.andrew.notewidget.domain

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import me.andrew.notewidget.R
import me.andrew.notewidget.ui.activity.HomeActivity
import me.andrew.notewidget.ui.widget.DefaultWidgetProvider
import me.andrew.notewidget.utils.PersistUtils
import me.andrew.notewidget.utils.catch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Andrew
 * Date on 21-9-28.
 */
private const val PERSIST_KEY = "persistKey"
private val timeFormat = SimpleDateFormat("yyyy.MM.dd")

fun saveWidgetText(txt: String) {
    PersistUtils.put(
        PERSIST_KEY,
        catch { JSONObject().put("text", txt).put("time", System.currentTimeMillis()) })
}

fun getWidgetText(): String {
    val string = PersistUtils.getString(PERSIST_KEY, "{}")
    val json = catch { JSONObject(string) }
    return json?.optString("text") ?: ""
}

fun remoteViewInvalidate(ctx: Context) {
    val string = PersistUtils.getString(PERSIST_KEY, "{}")
    val json = catch { JSONObject(string) }

    val content = json?.optString("text") ?: ""
    val now = System.currentTimeMillis()
    val time = json?.optLong("time", now) ?: now
    val setTime = timeFormat.format(Date(time))

    val remoteView = RemoteViews(ctx.packageName, R.layout.view_default_widget_provider)
    remoteView.setTextViewText(R.id.tv_content, content)
    remoteView.setTextViewText(R.id.tv_date, setTime)
    val intent = Intent(ctx, HomeActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0)
    remoteView.setOnClickPendingIntent(R.id.tv_content, pendingIntent)

    val manager = AppWidgetManager.getInstance(ctx)
    val cp = ComponentName(ctx, DefaultWidgetProvider::class.java.name)
    manager.updateAppWidget(cp, remoteView)
}