package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.search.domain.models.Track

class HistoryManagerInteractorImpl(private val repository: HistoryManagerRepository) : HistoryManagerInteractor {

    override fun getTracksHistory(historyKey: String): List<Track> {
        return repository.getTracksHistory(historyKey)
     }

    override fun add(track: Track) {
        val historyList: MutableList<Track> =  getTracksHistory(App.SEARCH_HISTORY_KEY).toMutableList()
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