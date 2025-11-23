package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.App
import com.example.playlistmaker.search.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.ServerResponse
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: TracksInteractor,
    private val historyManager: HistoryManagerInteractor,
) : ViewModel() {
    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var historyList: List<Track> = listOf()
    private val historyStateLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyStateLiveData

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        search(changedText)
    }

    init {
        updateHistory()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        trackSearchDebounce(changedText)
    }

    private fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                interactor.searchTracks(newSearchText).collect { serverResponse ->
                    when (serverResponse) {
                        is ServerResponse.Success -> renderState(TracksState.Data(serverResponse.tracksList))
                        is ServerResponse.Error -> renderState(TracksState.Error(serverResponse.code))
                    }
                }
            }
        }
    }

    private fun renderState (state: TracksState) {
        stateLiveData.postValue(state)
    }

    private fun updateHistory() {
        historyList = historyManager.getTracksHistory(App.SEARCH_HISTORY_KEY)
        historyStateLiveData.postValue(historyList)
    }

    fun addTrackToHistory(track: Track) {
        historyManager.add(track)
        updateHistory()
    }

    fun clearHistory() {
        historyManager.clearHistory()
        updateHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}