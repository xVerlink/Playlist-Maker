package com.example.playlistmaker.player.domain.api

import android.media.MediaPlayer

interface MediaPlayerInteractor {

    fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String)

}