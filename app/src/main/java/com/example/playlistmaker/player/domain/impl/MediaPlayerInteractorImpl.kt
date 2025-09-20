package com.example.playlistmaker.player.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    override fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String) {
        repository.preparePlayer(mediaPlayer, dataSource)
    }
}