package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlistList ->
                playlistsLiveData.postValue(playlistList)
            }
        }
    }
}