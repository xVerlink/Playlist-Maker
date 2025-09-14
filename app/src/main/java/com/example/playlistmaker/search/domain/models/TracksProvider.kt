package com.example.playlistmaker.search.domain.models

sealed interface TracksProvider<T> {
    data class Data<T>(val tracksList: T) : TracksProvider<T>
    data class Error<T>(val code: Int) : TracksProvider<T>
}