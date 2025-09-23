package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryManagerInteractor {

    fun getTracksHistory(historyKey: String): List<Track>
    fun add(track: Track)
    fun clearHistory()
    fun registerHistoryChangeListener(action: (List<Track>?) -> Unit)
    fun unregisterOnSharedPreferenceChangeListener()
}