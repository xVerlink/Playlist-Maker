package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.api.PlaylistRepository
import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow


class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addPlaylist(title: String, description: String, uri: String) {
        repository.addPlaylist(title, description, uri)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }
}