package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.api.FavoritesInteractor
import com.example.playlistmaker.media_library.domain.api.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override suspend fun addToFavorites(track: Track) {
        favoritesRepository.addToFavorites(track)
    }

    override suspend fun removeFromFavorites(track: Track) {
        favoritesRepository.removeFromFavorites(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoritesRepository.getTracks()
    }
}