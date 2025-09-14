package com.example.playlistmaker.search.domain.models

sealed interface ServerResponse<T> {
    data class Success<T>(val tracksList: T): ServerResponse<T>
    data class Error<T>(val code: Int): ServerResponse<T>
}