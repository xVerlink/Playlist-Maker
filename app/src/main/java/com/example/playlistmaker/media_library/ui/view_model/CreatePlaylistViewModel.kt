package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val trackIdLiveData = MutableLiveData<Int>()
    private val titleLiveData = MutableLiveData<String>()
    private val descriptionLiveData = MutableLiveData<String>()
    private val uriLiveData = MutableLiveData<String>()
    fun observeUri(): LiveData<String> = uriLiveData
    private val tracksLiveData = MutableLiveData<MutableList<String>>()
    private val tracksCountLiveData = MutableLiveData<Int>()

    fun addPlaylist() {
        viewModelScope.launch {
            val cover = uriLiveData.value ?: ""
            playlistInteractor.addPlaylist(Playlist(
                id = trackIdLiveData.value,
                title = titleLiveData.value!!,
                description = descriptionLiveData.value ?: "",
                cover = cover,
                trackIdList = tracksLiveData.value ?: mutableListOf(),
                tracksCount = tracksCountLiveData.value ?: 0
            ))
        }
    }

    fun setPlaylistId(id: Int) {
        trackIdLiveData.postValue(id)
    }

    fun setTitle(title: String) {
        titleLiveData.postValue(title)
    }

    fun setDescription(description: String) {
        descriptionLiveData.postValue(description)
    }

    fun setUri(uri: String) {
        uriLiveData.postValue(uri)
    }

    fun setTracksList(tracks: MutableList<String>) {
        tracksLiveData.postValue(tracks)
    }

    fun setTracksCount(count: Int) {
        tracksCountLiveData.postValue(count)
    }
}