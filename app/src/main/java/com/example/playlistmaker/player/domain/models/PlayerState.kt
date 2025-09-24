package com.example.playlistmaker.player.domain.models

sealed interface PlayerState {
    data object Playing: PlayerState
    data object Prepared: PlayerState
    data object Paused: PlayerState
    data object Default: PlayerState
}