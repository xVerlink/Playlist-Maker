package com.example.playlistmaker.media_library.domain.models

data class Playlist(
    val id: Int?,
    val title: String,
    val description: String,
    var cover: String,
    val trackIdList: MutableList<String>,
    val tracksCount: Int
)