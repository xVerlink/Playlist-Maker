package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ServerResponse
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<ServerResponse<List<Track>>>
}