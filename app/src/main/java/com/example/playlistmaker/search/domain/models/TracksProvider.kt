package com.example.playlistmaker.search.domain.models

sealed interface TracksProvider {
    object Loading : TracksProvider
    data class Data(val tracksList: List<Track>) : TracksProvider
    data class Error(val code: Int) : TracksProvider
}