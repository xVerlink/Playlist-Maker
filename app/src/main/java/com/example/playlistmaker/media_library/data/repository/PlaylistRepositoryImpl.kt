package com.example.playlistmaker.media_library.data.repository

import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.media_library.data.db.PlaylistDao
import com.example.playlistmaker.media_library.data.db.convertor.PlaylistDbConverter
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.domain.api.PlaylistRepository
import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException


class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val converter: PlaylistDbConverter,
    private val context: Context
) : PlaylistRepository {
    override suspend fun addPlaylist(title: String, description: String, uri: String) {
        val coverUri = addCoverToExternalStorage(uri)
        playlistDao.addPlaylist(PlaylistEntity(
            id = null,
            title = title,
            description = description,
            cover = coverUri,
            trackIdListJson = "",
            tracksCount = 0
        ))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getPlaylists().map {
            converter.map(it)
        }
        emit(playlists)
    }

    private fun addCoverToExternalStorage(cover: String): String {
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