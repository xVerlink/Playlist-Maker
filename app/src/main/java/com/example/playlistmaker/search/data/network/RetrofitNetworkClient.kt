package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val appleMusicService: TracksApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            try {
                val response = appleMusicService.getSongs(text = dto.expression).execute()
                val body: Response = response.body() ?: Response()
                return body.apply { resultCode = response.code() }
            } catch (e: Exception) {
                return Response().apply { resultCode = -1 }
            }

        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}