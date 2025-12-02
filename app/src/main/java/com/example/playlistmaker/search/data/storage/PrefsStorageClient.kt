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

    override fun clearHistory() {
        prefs.edit { remove(dataKey) }
    }
}
