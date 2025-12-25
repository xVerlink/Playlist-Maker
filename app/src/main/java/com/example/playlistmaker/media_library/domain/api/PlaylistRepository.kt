package com.example.playlistmaker.media_library.domain.api

import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(title: String, description: String, uri: String)
    fun getPlaylists(): Flow<List<Playlist>>
}