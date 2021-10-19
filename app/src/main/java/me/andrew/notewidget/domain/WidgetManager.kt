package me.andrew.notewidget.domain

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import me.andrew.notewidget.R
import me.andrew.notewidget.ui.activity.HomeActivity
import me.andrew.notewidget.ui.viewdata.ListNoteData
import me.andrew.notewidget.ui.widget.DefaultWidgetProvider
import me.andrew.notewidget.utils.PersistUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Andrew
 * Date on 21-9-28.
 */
private const val PERSIST_KEY = "persistKey"
private val timeFormat = SimpleDateFormat("yyyy.MM.dd")
private var noteList: MutableList<ListNoteData>? = null

fun getNotes(): MutableList<ListNoteData> {
    if (noteList != null) {
        return noteList!!
    }
    val ret = mutableListOf<ListNoteData>()
    PersistUtils.getAll()?.let {
        for (e in it.entries) {
            if (e.value is String) {
                ret.add(ListNoteData().apply {
                    fromJson(e.value as String)
                    id = e.key.toLong()
                })
            }
        }
    }
    ret.sort()
    noteList = ret
    return ret
}

fun addNote(noteData: ListNoteData) {
    val list = getNotes()
    list.remove(noteData)
    list.add(noteData)
    list.sort()
    PersistUtils.put(noteData.id.toString(), noteData.toJson())
}

fun deleteNote(noteData: ListNoteData) {
    val list = getNotes()
    list.remove(noteData)
    PersistUtils.remove(noteData.id.toString())
}

fun getTime(time: Long): String = timeFormat.format(Date(time))

fun remoteViewInvalidate(ctx: Context) {
    val list = getNotes()
    if (list.isNotEmpty()) {
        val data = list[0]

        val remoteView = RemoteViews(ctx.packageName, R.layout.view_default_widget_provider)
        remoteView.setTextViewText(R.id.tv_content, data.content)
        remoteView.setTextViewText(R.id.tv_date, getTime(data.time))
        val intent = Intent(ctx, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0)
        remoteView.setOnClickPendingIntent(R.id.tv_content, pendingIntent)

        val manager = AppWidgetManager.getInstance(ctx)
        val cp = ComponentName(ctx, DefaultWidgetProvider::class.java.name)
        manager.updateAppWidget(cp, remoteView)
    }
}