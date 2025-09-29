package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import androidx.core.content.edit



class PrefsStorageClient<T>(
    private val prefs: SharedPreferences,
    private val dataKey: String,
    private val type: Type,
    private val gson: Gson
) : StorageClient<T> {

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

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

    override fun registerOnSharedPreferenceChangeListener(action: (T?) -> Unit) {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            action.invoke(getData())
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun clearHistory() {
        prefs.edit { remove(dataKey) }
    }

    override fun unregisterOnSharedPreferenceChangeListener() {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
