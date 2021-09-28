package me.andrew.notewidget.utils

/**
 * Created by Andrew
 * Date on 21-9-9.
 */

inline fun <T> catch(block: () -> T?): T? {
    try {
        return block()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}