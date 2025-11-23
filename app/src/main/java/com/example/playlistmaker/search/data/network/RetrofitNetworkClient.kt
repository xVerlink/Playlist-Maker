package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val appleMusicService: TracksApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                appleMusicService.getSongs(dto.expression).apply { resultCode = 200 }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = -1 }
            }
        }
    }
}