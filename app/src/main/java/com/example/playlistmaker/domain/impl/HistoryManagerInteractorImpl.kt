package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.domain.api.HistoryManagerRepository
import com.example.playlistmaker.domain.models.Track

class HistoryManagerInteractorImpl(private val repository: HistoryManagerRepository) : HistoryManagerInteractor {

    override fun getTracksHistory(historyKey: String): MutableList<Track> {
        return repository.getTracksHistory(historyKey)
    }

    override fun add(track: Track) {
        val historyList: MutableList<Track> = getTracksHistory(App.SEARCH_HISTORY_KEY)
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

    override fun registerHistoryChangeListener(action: (MutableList<Track>) -> Unit) {
        repository.registerHistoryChangeListener(action)
    }
}