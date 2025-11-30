package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList

class HistoryManagerInteractorImpl(private val repository: HistoryManagerRepository) : HistoryManagerInteractor {

    override suspend fun getTracksHistory(historyKey: String): Flow<List<Track>> {
        return repository.getTracksHistory(historyKey)
     }

    override suspend fun add(track: Track) {
        val historyList: MutableList<Track> =  getTracksHistory(App.SEARCH_HISTORY_KEY).single().toMutableList()
        var indexNumber = -1
        historyList.forEachIndexed { index, item ->
            if (item.trackId.toInt() == track.trackId.toInt()) {
                indexNumber = index
            }
        }
        if (indexNumber != -1) {
            historyList.removeAt(indexNumber)
        }
        if (historyList.size >= 10) {
            historyList.removeAt(0)
        }
        historyList.add(track)
        repository.writeTracksHistory(historyList)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}