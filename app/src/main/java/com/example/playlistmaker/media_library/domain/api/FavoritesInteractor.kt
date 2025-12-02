package com.example.playlistmaker.media_library.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun addToFavorites(track: Track): Flow<List<String>>

    fun removeFromFavorites(track: Track): Flow<List<String>>

    fun getTracks(): Flow<List<Track>>

    fun getFavoritesId(): Flow<List<String>>
}