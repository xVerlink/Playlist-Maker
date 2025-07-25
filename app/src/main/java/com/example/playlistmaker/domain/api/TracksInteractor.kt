package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksProvider

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)


    interface TracksConsumer {
        fun consume(tracks: TracksProvider<List<Track>>)
    }
}