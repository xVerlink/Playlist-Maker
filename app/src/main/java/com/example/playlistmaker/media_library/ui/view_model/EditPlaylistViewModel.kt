package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : CreatePlaylistViewModel(playlistInteractor) {
    private val playlist = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlist

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylist(playlistId).collect {
                playlist.postValue(it)
            }
        }
    }
}