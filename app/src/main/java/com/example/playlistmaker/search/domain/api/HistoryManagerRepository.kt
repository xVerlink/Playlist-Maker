package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryManagerRepository {

    fun getTracksHistory(json: String): Flow<List<Track>>
    suspend fun clearHistory()
    suspend fun writeTracksHistory(tracks: MutableList<Track>)
}