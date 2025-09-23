package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import androidx.core.content.edit
import com.example.playlistmaker.App


class PrefsStorageClient<T>(
    private val prefs: SharedPreferences,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private val gson = Gson()

    override fun storeData(data: T) {
        prefs.edit() {
            putString(dataKey, gson.toJson(data, type))
        }
    }

    override fun getData(): T? {
        val jsonData = prefs.getString(dataKey, null)
        return if (jsonData == null) {
            null
        } else {
            gson.fromJson(jsonData, type)
        }
    }

    override fun registerHistoryChangeListener(action: (T?) -> Unit) {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            action.invoke(getData())
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun clearHistory() {
        prefs.edit { remove(App.SEARCH_HISTORY_KEY) }
    }

    override fun unregisterOnSharedPreferenceChangeListener() {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
