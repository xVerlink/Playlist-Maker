package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksProvider


class SearchActivityViewModel() : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val interactor = Creator.getTracksInteractor()
    private val historyManager = Creator.getHistoryManagerInteractor()
    private var latestSearchText: String? = null

    private var currentState: TracksProvider = TracksProvider.Loading
    private val stateLiveData = MutableLiveData<TracksProvider>()
    fun observeState(): LiveData<TracksProvider> = stateLiveData

    private var historyList: List<Track> = listOf()
    private val historyStateLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyStateLiveData

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksProvider.Loading)
            interactor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(data: TracksProvider) {
                    handler.post {
                        currentState = data
                        when (data) {
                            is TracksProvider.Data -> renderState(TracksProvider.Data(data.tracksList))
                            is TracksProvider.Error -> renderState(TracksProvider.Error(data.code))
                            is TracksProvider.Loading -> renderState(TracksProvider.Loading)
                        }
                    }
                }
            })
        }
    }

    private fun renderState (state: TracksProvider) {
        stateLiveData.postValue(state)
    }

    fun updateHistory(historyKey: String) {
        historyList = historyManager.getTracksHistory(historyKey)
        historyStateLiveData.postValue(historyList)
    }

    fun addTrackToHistory(track: Track) {
        historyManager.add(track)
    }

    fun registerHistoryChangeListener(action: (List<Track>?) -> Unit) {
        historyManager.registerHistoryChangeListener(action)
    }

    fun clearHistory() {
        historyManager.clearHistory()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchActivityViewModel()
            }
        }
    }
}