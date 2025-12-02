package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryManagerInteractor {

    fun getTracksHistory(historyKey: String): Flow<List<Track>>
    fun add(track: Track): Flow<List<Track>>
    fun clearHistory(): Flow<List<Track>>
}