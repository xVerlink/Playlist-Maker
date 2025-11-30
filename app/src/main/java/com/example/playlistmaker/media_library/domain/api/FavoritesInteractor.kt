package com.example.playlistmaker.media_library.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addToFavorites(track: Track)

    suspend fun removeFromFavorites(track: Track)

    fun getTracks(): Flow<List<Track>>
}