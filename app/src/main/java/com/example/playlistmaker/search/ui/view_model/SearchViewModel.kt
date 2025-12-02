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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: TracksInteractor,
    private val historyManager: HistoryManagerInteractor,
) : ViewModel() {
    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val historyStateLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyStateLiveData

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        search(changedText)
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

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
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

    fun updateHistory() {
        viewModelScope.launch {
            historyManager.getTracksHistory(App.SEARCH_HISTORY_KEY).collect { tracks ->
                historyStateLiveData.postValue(tracks)
            }
        }
    }

    fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            historyManager.add(track).collect { tracks ->
                historyStateLiveData.postValue(tracks)
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyManager.clearHistory().collect { tracks ->
                historyStateLiveData.postValue(tracks)
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}