package com.example.playlistmaker.player.domain.api

interface MediaPlayerRepository {

    fun preparePlayer(dataSource: String)
    fun setOnPreparedListener(listener: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
    fun playerStart()
    fun playerPause()
    fun getCurrentPosition(): Int
    fun release()
}