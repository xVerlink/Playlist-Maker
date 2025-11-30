package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryManagerInteractor {

    suspend fun getTracksHistory(historyKey: String): Flow<List<Track>>
    suspend fun add(track: Track)
    fun clearHistory()
}