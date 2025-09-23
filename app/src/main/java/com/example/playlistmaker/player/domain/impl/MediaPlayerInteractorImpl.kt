package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    override fun preparePlayer(dataSource: String) {
        repository.preparePlayer(dataSource)
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        repository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        repository.setOnCompletionListener(listener)
    }

    override fun playerStart() {
        repository.playerStart()
    }

    override fun playerPause() {
        repository.playerPause()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun release() {
        repository.release()
    }
}