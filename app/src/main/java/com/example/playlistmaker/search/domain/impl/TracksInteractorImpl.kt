package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.ServerResponse
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    override fun searchTracks(expression: String): Flow<ServerResponse<List<Track>>> {
        return repository.searchTracks(expression)
    }
}