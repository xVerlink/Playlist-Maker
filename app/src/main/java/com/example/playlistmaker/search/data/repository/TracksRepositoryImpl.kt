package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.ServerResponse
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase
    ) : TracksRepository {
    override fun searchTracks(expression: String): Flow<ServerResponse<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val favoritesIds = database.trackDao().getTracksId()
            val trackList = (response as TracksSearchResponse).results.map { trackDto ->
                val formattedTime = if (!trackDto.trackTime.isNullOrEmpty()) SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDto.trackTime.toLong()) else ""
                Track(
                    trackId = checkNull(trackDto.trackId),
                    trackName = checkNull(trackDto.trackName),
                    artistName = checkNull(trackDto.artistName),
                    collectionName = checkNull(trackDto.collectionName),
                    releaseDate = checkNull(trackDto.releaseDate),
                    primaryGenreName = checkNull(trackDto.primaryGenreName),
                    country = checkNull(trackDto.country),
                    previewUrl = checkNull(trackDto.previewUrl),
                    trackTime = checkNull(formattedTime),
                    artworkUrl100 = checkNull(trackDto.artworkUrl100),
                    isFavorite = favoritesIds.contains(trackDto.trackId)
                )
            }
            emit(ServerResponse.Success(trackList))
        } else {
            emit(ServerResponse.Error(response.resultCode))
        }
    }

    private fun checkNull(field: String?): String =
        field ?: ""
}