package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class HistoryManagerRepositoryImpl(
    private val storageClient: StorageClient<ArrayList<Track>>,
    private val appDatabase: AppDatabase) : HistoryManagerRepository {

    override fun getTracksHistory(historyKey: String): Flow<List<Track>> = flow {
        val tracks = storageClient.getData() ?: listOf()
        val favoritesIds = withContext(Dispatchers.IO) {
            appDatabase.trackDao().getTracksId()
        }
        tracks.map { track ->
            track.isFavorite = favoritesIds.contains(track.trackId)
        }
        emit(tracks)
    }

    override fun writeTracksHistory(tracks: MutableList<Track>) {
        storageClient.storeData(ArrayList<Track>(tracks))
    }

    override fun clearHistory() {
        storageClient.clearHistory()
    }
}