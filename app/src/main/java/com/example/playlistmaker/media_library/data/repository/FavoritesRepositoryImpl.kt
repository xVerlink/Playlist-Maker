package com.example.playlistmaker.media_library.data.repository

import com.example.playlistmaker.media_library.data.db.TrackDao
import com.example.playlistmaker.media_library.data.db.convertor.TrackDbConverter
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.domain.api.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: TrackDao,
    private val convertor: TrackDbConverter
) : FavoritesRepository {
    override fun addToFavorites(track: Track): Flow<List<String>> = flow {
        appDatabase.addToFavorites(convertor.map(track))
        getFavoritesId()
    }

    override fun removeFromFavorites(track: Track): Flow<List<String>> = flow {
        appDatabase.removeFromFavorites(convertor.map(track))
        getFavoritesId()
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getFavoritesId(): Flow<List<String>> = flow {
        emit(appDatabase.getTracksId())
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> convertor.map(track) }.reversed()
    }
}