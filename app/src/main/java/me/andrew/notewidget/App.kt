package me.andrew.notewidget

import android.app.Application
import me.andrew.notewidget.utils.PersistUtils
import me.andrew.notewidget.utils.UIUtils

/**
 * Created by Andrew
 * Date on 21-9-28.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PersistUtils.initialize(this)
        UIUtils.initialize(this)
    }
}