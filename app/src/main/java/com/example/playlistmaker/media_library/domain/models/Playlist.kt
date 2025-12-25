package com.example.playlistmaker.media_library.domain.models

data class Playlist(
    val id: Int?,
    val title: String,
    val description: String,
    val cover: String,
    val trackIdList: List<String>,
    val tracksCount: Int
)