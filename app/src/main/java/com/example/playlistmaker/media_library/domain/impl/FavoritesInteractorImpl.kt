package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.api.FavoritesInteractor
import com.example.playlistmaker.media_library.domain.api.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override fun addToFavorites(track: Track): Flow<List<String>> {
        return favoritesRepository.addToFavorites(track)
    }

    override fun removeFromFavorites(track: Track): Flow<List<String>> {
        return favoritesRepository.removeFromFavorites(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoritesRepository.getTracks()
    }

    override fun getFavoritesId(): Flow<List<String>> {
        return favoritesRepository.getFavoritesId()
    }
}