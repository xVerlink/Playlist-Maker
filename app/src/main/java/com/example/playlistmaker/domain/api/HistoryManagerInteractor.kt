package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryManagerInteractor {

    fun getTracksHistory(historyKey: String): MutableList<Track>

    fun add(track: Track)

    fun clearHistory()

    fun registerHistoryChangeListener(action: (MutableList<Track>) -> Unit)
}