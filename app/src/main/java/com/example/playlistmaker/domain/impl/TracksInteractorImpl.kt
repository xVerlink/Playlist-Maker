package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.ServerResponse
import com.example.playlistmaker.domain.models.TracksProvider
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            val serverResponse = repository.searchTracks(expression)
            when (serverResponse) {
                is ServerResponse.Success -> consumer.consume(TracksProvider.Data(serverResponse.tracksList))
                is ServerResponse.Error -> consumer.consume(TracksProvider.Error(serverResponse.code))
            }
        }
    }
}