package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val appleMusicBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(appleMusicBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val appleMusicService = retrofit.create(TracksApi::class.java)

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