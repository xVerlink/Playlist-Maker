package com.example.playlistmaker.domain.models

sealed interface PlayerState {
    class Playing: PlayerState
    class Prepared: PlayerState
    class Paused: PlayerState
    class Default: PlayerState
}