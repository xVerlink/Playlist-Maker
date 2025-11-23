package com.example.playlistmaker.search.data.repository

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

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<ServerResponse<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val trackList = (response as TracksSearchResponse).results.map {
                val formattedTime = if (!it.trackTime.isNullOrEmpty()) SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTime.toLong()) else ""
                Track(
                    checkNull(it.trackId),
                    checkNull(it.trackName),
                    checkNull(it.artistName),
                    checkNull(it.collectionName),
                    checkNull(it.releaseDate),
                    checkNull(it.primaryGenreName),
                    checkNull(it.country),
                    checkNull(it.previewUrl), //убрать и проверить с beatles
                    checkNull(formattedTime),
                    checkNull(it.artworkUrl100)
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