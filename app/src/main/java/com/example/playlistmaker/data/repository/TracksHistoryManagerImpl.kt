package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.TracksHistoryManager
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TracksHistoryManagerImpl(val sharedPref: SharedPreferences) : TracksHistoryManager {

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun add(track: Track) {
        val historyList: MutableList<Track> = getTracksHistory(App.SEARCH_HISTORY_KEY)
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

    override fun getTracksHistory(historyKey: String): MutableList<Track> {
        if (historyKey.isNullOrEmpty()) {
            return mutableListOf()
        }
        val json = sharedPref.getString(historyKey, "")
        val listType: Type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun write(tracks: MutableList<Track>) {
        sharedPref.edit() {
            putString(SEARCH_HISTORY_KEY, Gson().toJson(tracks))
        }
    }

    override fun registerHistoryChangeListener (listener: (MutableList<Track>) -> Unit) {
        this.listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == App.SEARCH_HISTORY_KEY) {
                listener.invoke(getTracksHistory(App.SEARCH_HISTORY_KEY))
            }
        }
        sharedPref.registerOnSharedPreferenceChangeListener(this.listener)
    }

    override fun clearHistory() {
        sharedPref.edit { remove(App.SEARCH_HISTORY_KEY) }
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}