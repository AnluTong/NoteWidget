package me.andrew.notewidget.ui.viewdata

import me.andrew.notewidget.utils.catch
import org.json.JSONObject

/**
 * Created by Andrew
 * Date on 21-10-15.
 */
class ListNoteData(var id: Long, var time: Long, var content: String?, var priority: Int) :
    Comparable<ListNoteData> {

    constructor() : this(System.currentTimeMillis(), 0, null, 0)

    fun toJson(): String {
        return JSONObject().put("time", time)
            .put("content", content)
            .put("id", id)
            .toString()
    }

    fun fromJson(string: String) {
        catch {
            val obj = JSONObject(string)
            time = obj.optLong("time")
            content = obj.optString("content", null)
            id = obj.optLong("id")
        }
    }

    override fun compareTo(other: ListNoteData): Int {
        return return when {
            priority != other.priority -> other.priority - priority
            else -> -time.compareTo(other.time)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListNoteData

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}