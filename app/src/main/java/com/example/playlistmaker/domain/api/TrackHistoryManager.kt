package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackHistoryManager {
    fun saveTracks(tracks: MutableList<Track>)

    fun getTracks(): MutableList<Track>

    fun add(track: Track)
}