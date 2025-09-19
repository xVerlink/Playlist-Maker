package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryManagerRepository {

    fun getTracksHistory(json: String): MutableList<Track>

    fun registerHistoryChangeListener (action: (List<Track>?) -> Unit)

    fun clearHistory()

    fun writeTracksHistory(tracks: MutableList<Track>)
}