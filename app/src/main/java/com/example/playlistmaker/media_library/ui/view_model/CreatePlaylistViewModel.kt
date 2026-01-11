package com.example.playlistmaker.media_library.ui.view_model

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val drawableLiveData = MutableLiveData<Drawable>()
    fun observeDrawable(): LiveData<Drawable> = drawableLiveData

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }

    fun fillImage(drawable: Drawable) {
        drawableLiveData.postValue(drawable)
    }
}