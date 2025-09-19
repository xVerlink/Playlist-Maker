package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TracksProvider

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)


    interface TracksConsumer {
        fun consume(tracks: TracksProvider)
    }
}