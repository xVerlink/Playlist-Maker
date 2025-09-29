package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.ServerResponse
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): ServerResponse<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return if (response.resultCode == 200) {
            val trackList = (response as TracksSearchResponse).results
            ServerResponse.Success(trackList.map {
                val formattedTime = if (!it.trackTime.isNullOrEmpty()) SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTime.toLong()) else ""
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl ?: "", //если убрать ?:, то запрос с beatles вызывает краш
                    formattedTime,
                    it.artworkUrl100
                )
            })
        } else {
            ServerResponse.Error(response.resultCode)
        }
    }
}