package com.example.playlistmaker.media_library.data.repository

import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.convertor.TrackDbConvertor
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.domain.api.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: TrackDbConvertor
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        appDatabase.trackDao().addToFavorites(convertor.map(track))
    }

    override suspend fun removeFromFavorites(track: Track) {
        appDatabase.trackDao().removeFromFavorites(convertor.map(track))
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> convertor.map(track) }.reversed()
    }
}