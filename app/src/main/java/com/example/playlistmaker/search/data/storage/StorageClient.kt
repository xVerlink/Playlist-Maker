package com.example.playlistmaker.search.data.storage


interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
    fun registerOnSharedPreferenceChangeListener (action: (T?) -> Unit)
    fun clearHistory()
    fun unregisterOnSharedPreferenceChangeListener()
}