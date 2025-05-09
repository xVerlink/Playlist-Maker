package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {
    @GET("/search")
    fun getSongs(@Query("term") text: String) : Call<TracksResponse>
}