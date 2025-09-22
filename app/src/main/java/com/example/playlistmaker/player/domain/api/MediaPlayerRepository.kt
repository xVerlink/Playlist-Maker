package com.example.playlistmaker.player.domain.api

import android.media.MediaPlayer

interface MediaPlayerRepository {
    fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String)
}