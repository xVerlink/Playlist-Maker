package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.FavoritesInteractor
import com.example.playlistmaker.media_library.domain.models.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor): ViewModel() {
    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun fillData() {
        viewModelScope.launch {
            favoritesInteractor.getTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    renderState(FavoritesState.Empty)
                } else {
                    renderState(FavoritesState.Content(tracks))
                }
            }
        }
    }
    private fun renderState(state: FavoritesState) {
        stateLiveData.postValue(state)
    }
}