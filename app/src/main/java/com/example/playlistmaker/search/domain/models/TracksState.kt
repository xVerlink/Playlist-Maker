package com.example.playlistmaker.search.domain.models

sealed interface TracksState {
    data object Loading : TracksState
    data class Data(val tracksList: List<Track>) : TracksState
    data class Error(val code: Int) : TracksState
}