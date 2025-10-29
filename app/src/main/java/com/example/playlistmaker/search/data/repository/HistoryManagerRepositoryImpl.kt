package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.models.Track

class HistoryManagerRepositoryImpl(private val storageClient: StorageClient<ArrayList<Track>>) : HistoryManagerRepository {

    override fun getTracksHistory(historyKey: String): List<Track> {
        val tracks = storageClient.getData()
        return tracks ?: mutableListOf()
    }

    override fun writeTracksHistory(tracks: MutableList<Track>) {
        storageClient.storeData(ArrayList<Track>(tracks))
    }

    override fun clearHistory() {
        storageClient.clearHistory()
    }
}