package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.models.PlayerState

interface MediaPlayerInteractor {

    fun preparePlayer(dataSource: String, action: () -> Unit)

    fun startPlaying()

    fun pausePlayer()

    fun setOnCompletionListener()

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState

    fun release()
}