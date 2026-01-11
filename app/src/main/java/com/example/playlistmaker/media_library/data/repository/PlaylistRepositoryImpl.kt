package com.example.playlistmaker.media_library.data.repository

import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.media_library.data.db.PlaylistDao
import com.example.playlistmaker.media_library.data.db.TrackPlaylistDao
import com.example.playlistmaker.media_library.data.db.convertor.PlaylistDbConverter
import com.example.playlistmaker.media_library.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker.media_library.domain.api.PlaylistRepository
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException


class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackPlaylistDao: TrackPlaylistDao,
    private val converter: PlaylistDbConverter,
    private val context: Context
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        if (playlist.id == null) {
            val coverUri = addCoverToExternalStorage(playlist.cover)
            playlist.cover = coverUri
        }
        playlistDao.addPlaylist(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getPlaylists().map {
            converter.map(it)
        }
        emit(playlists)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlist.trackIdList.add(track.trackId)
        trackPlaylistDao.addTrack(TrackPlaylistEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            isFavorite = track.isFavorite
        ))
        addPlaylist(playlist)
    }

    private fun addCoverToExternalStorage(cover: String): String {
        if (cover.isEmpty()) {
            return ""
        }

        val directory = File(
           context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            , PLAYLISTS_COVER_DIR)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(cover.toUri()).use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            } ?: throw IOException("Не удалось открыть входной поток")
        }
        return file.toString()
    }

    companion object {
        private const val PLAYLISTS_COVER_DIR = "Playlists Cover"
    }
}