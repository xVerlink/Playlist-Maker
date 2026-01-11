package com.example.playlistmaker.media_library.data.repository

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.media_library.data.db.PlaylistDao
import com.example.playlistmaker.media_library.data.db.TrackPlaylistDao
import com.example.playlistmaker.media_library.data.db.convertor.PlaylistDbConverter
import com.example.playlistmaker.media_library.data.db.convertor.TrackPlaylistDbConverter
import com.example.playlistmaker.media_library.domain.api.PlaylistRepository
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackPlaylistDao: TrackPlaylistDao,
    private val playlistConverter: PlaylistDbConverter,
    private val trackConverter: TrackPlaylistDbConverter,
    private val context: Context
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        val coverUri = addCoverToExternalStorage(playlist.cover)
        playlist.cover = coverUri
        playlistDao.addPlaylist(playlistConverter.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = playlistDao.getPlaylists().map { entityList ->
            entityList.map { entity ->
                playlistConverter.map(entity)
            }
        }
        return playlistFlow
    }

    override suspend fun getPlaylist(playlistId: Int): Flow<Playlist> {
        val playlists = playlistDao.getPlaylist(playlistId).map { playlist->
            playlistConverter.map(playlist)
        }
        return playlists
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlist.trackIdList.add(track.trackId)
        trackPlaylistDao.addTrack(trackConverter.map(track))
        addPlaylist(playlist)
    }

    override fun getTracksFromPlaylist(trackIdsList: List<String>): Flow<List<Track>> {
        val trackListFlow = trackPlaylistDao.getTracks().map { entityList ->
            entityList.filter { entity->
                entity.trackId in trackIdsList
            }.reversed()
                .map { entity->
                    trackConverter.map(entity)
                }
        }
        return trackListFlow
    }

    override suspend fun removeFromPlaylist(track: Track, playlist: Playlist) {
        playlist.trackIdList.remove(track.trackId)
        addPlaylist(playlist)
        val playlists = playlistDao.getPlaylistsOnce().map {
            playlistConverter.map(it)
        }
        playlists.forEach {
            Log.d("ASD", "playlist title: ${it.title}")
            Log.d("ASD", "playlist trackIdList: ${it.trackIdList}")
        }
        removeFromTrackTable(track, playlists)
    }

    override suspend fun deletePlaylist(playlist: Playlist) =
        withContext(Dispatchers.IO) {
            val playlists = playlistDao.getPlaylistsOnce()
                .map { playlistConverter.map(it) }
                .filter { it.id != playlist.id }


            playlistDao.deletePlaylist(playlistConverter.map(playlist))

            trackPlaylistDao.getTracksOnce()
                .filter { it.trackId in playlist.trackIdList }
                .map { trackConverter.map(it) }
                .forEach { track ->
                    removeFromTrackTable(track, playlists)
                }
        }

    private suspend fun removeFromTrackTable(track: Track, playlists: List<Playlist>) {
        var contains = false
        playlists.forEach { playlist ->
            if (playlist.trackIdList.contains(track.trackId)) {
                contains = true
            }
        }
        if (!contains) {
            trackPlaylistDao.removeTrack(trackConverter.map(track))
        }
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

    override fun sharePlaylist(text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        val chooserIntent = Intent.createChooser(intent, "Куда отправить?")
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    companion object {
        private const val PLAYLISTS_COVER_DIR = "Playlists Cover"
    }
}