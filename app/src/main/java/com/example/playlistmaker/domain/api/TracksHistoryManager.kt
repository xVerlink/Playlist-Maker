package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryManager {

    fun getTracksHistory(json: String): MutableList<Track>

    fun add(track: Track)

    fun registerHistoryChangeListener (listener: (MutableList<Track>) -> Unit)

    fun clearHistory()
}