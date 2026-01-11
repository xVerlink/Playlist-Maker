package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.models.PlaylistUiState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val playlistId = MutableStateFlow<Int?>(null)
    private val toastText = MutableLiveData<String>()
    fun observeToastText(): LiveData<String> = toastText

    val uiState: StateFlow<PlaylistUiState?> =
        playlistId
            .filterNotNull()
            .flatMapLatest { id ->
                playlistInteractor.getPlaylist(id)
            }
            .flatMapLatest { playlist ->
                playlistInteractor
                    .getTracksFromPlaylist(playlist.trackIdList)
                    .map { tracks ->
                        PlaylistUiState(
                            playlist = playlist,
                            tracks = tracks
                        )
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )


    fun loadPlaylist(playlistId: Int) {
        this.playlistId.value = playlistId

    }

    fun removeFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistInteractor.removeFromPlaylist(track, uiState.value!!.playlist)
        }
    }

    fun sharePlaylistOrDisplayErrorMessage(errorMessage: String) {
        if (uiState.value!!.tracks.isEmpty()) {
            toastText.postValue(errorMessage)
        } else {
            playlistInteractor.sharePlaylist(uiState.value!!.playlist, uiState.value!!.tracks)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(uiState.value!!.playlist)
        }
    }
}