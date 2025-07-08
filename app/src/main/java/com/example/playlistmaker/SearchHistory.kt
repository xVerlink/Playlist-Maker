package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import androidx.core.content.edit



class SearchHistory(val sharedPref: SharedPreferences) {

    fun add(track: Track) {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, "")
        val historyList: MutableList<Track> = read(json)
        var indexNumber = -1
        historyList.forEachIndexed { index, item ->
            if (item.trackId.toInt() == track.trackId.toInt()) {
                indexNumber = index
            }
        }
        if (indexNumber != -1) {
            historyList.removeAt(indexNumber)
        }
        if (historyList.size >= 10) {
            historyList.removeAt(0)
        }
        historyList.add(track)
        write(historyList)
    }

    fun read(json: String?): MutableList<Track> {
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun write(tracks: MutableList<Track>) {
        sharedPref.edit() {
            putString(SEARCH_HISTORY_KEY, Gson().toJson(tracks))
        }
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}
