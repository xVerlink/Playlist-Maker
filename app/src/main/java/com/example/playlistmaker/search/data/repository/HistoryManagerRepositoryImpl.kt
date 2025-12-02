package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryManagerRepositoryImpl(
    private val storageClient: StorageClient<ArrayList<Track>>) : HistoryManagerRepository {

    override fun getTracksHistory(historyKey: String): Flow<List<Track>> = flow {
        val tracks = storageClient.getData() ?: listOf()
        emit(tracks)
    }

    override suspend fun writeTracksHistory(tracks: MutableList<Track>) {
        storageClient.storeData(ArrayList<Track>(tracks))
    }

    override suspend fun clearHistory() {
        storageClient.clearHistory()
    }
}