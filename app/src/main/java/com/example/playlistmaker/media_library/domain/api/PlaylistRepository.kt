package com.example.playlistmaker.media_library.domain.api

import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylist(playlistId: Int): Flow<Playlist>
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    fun getTracksFromPlaylist(trackIdsList: List<String>): Flow<List<Track>>
    suspend fun removeFromPlaylist(track: Track, playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun sharePlaylist(text: String)
}