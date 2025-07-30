package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerInteractor {

    fun preparePlayer(dataSource: String, action: () -> Unit)

    fun startPlaying()

    fun pausePlayer()

    fun setOnCompletionListener()

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState

    fun release()
}