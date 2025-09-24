package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ServerResponse
import com.example.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ServerResponse<List<Track>>
}