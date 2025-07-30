package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ServerResponse
import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ServerResponse<List<Track>>
}