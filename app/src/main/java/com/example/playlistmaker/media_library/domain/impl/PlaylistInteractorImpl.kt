package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.api.PlaylistRepository
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getPlaylist(playlistId: Int): Flow<Playlist> {
        return repository.getPlaylist(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        repository.addTrackToPlaylist(track, playlist)
    }

    override fun getTracksFromPlaylist(trackIdsList: List<String>): Flow<List<Track>> {
        return repository.getTracksFromPlaylist(trackIdsList)
    }

    override suspend fun removeFromPlaylist(track: Track, playlist: Playlist) {
        repository.removeFromPlaylist(track, playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun sharePlaylist(playlist: Playlist, tracks: List<Track>, tracksWord: String) {
        val builder = StringBuilder()
        builder.append(playlist.title).appendLine()
            .append(playlist.description).appendLine()
            .append(tracks.size).append(" $tracksWord").appendLine()

        var trackIndex = 1
        tracks.forEach { track ->
            builder.append("$trackIndex. ${track.artistName} - ${track.trackName} (${track.trackTime})").appendLine()
            trackIndex++
        }
        repository.sharePlaylist(builder.toString())
    }
}