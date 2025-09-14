package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryManagerInteractor {

    fun getTracksHistory(historyKey: String): MutableList<Track>

    fun add(track: Track)

    fun clearHistory()

    fun registerHistoryChangeListener(action: (MutableList<Track>) -> Unit)
}