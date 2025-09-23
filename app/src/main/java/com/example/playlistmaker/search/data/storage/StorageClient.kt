package com.example.playlistmaker.search.data.storage


interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
    fun registerHistoryChangeListener (action: (T?) -> Unit)
    fun clearHistory()
    fun unregisterOnSharedPreferenceChangeListener()
}