package com.example.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class HistoryManagerRepositoryImpl(val sharedPref: SharedPreferences) : HistoryManagerRepository {

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener


    override fun getTracksHistory(historyKey: String): MutableList<Track> {
        val json = sharedPref.getString(historyKey, "")
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, listType)
    }

    override fun writeTracksHistory(tracks: MutableList<Track>) {
        sharedPref.edit() {
            putString(App.SEARCH_HISTORY_KEY, Gson().toJson(tracks))
        }
    }

    override fun registerHistoryChangeListener (action: (MutableList<Track>) -> Unit) {
        this.listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == App.SEARCH_HISTORY_KEY) {
                action.invoke(getTracksHistory(App.SEARCH_HISTORY_KEY))
            }
        }
        sharedPref.registerOnSharedPreferenceChangeListener(this.listener)
    }

    override fun clearHistory() {
        sharedPref.edit { remove(App.SEARCH_HISTORY_KEY) }
    }
}