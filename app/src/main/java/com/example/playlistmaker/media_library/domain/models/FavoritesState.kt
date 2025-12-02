package com.example.playlistmaker.media_library.domain.models

import com.example.playlistmaker.search.domain.models.Track

interface FavoritesState {
    object Loading : FavoritesState
    object Empty : FavoritesState
    data class Content(val tracks: List<Track>) : FavoritesState
}