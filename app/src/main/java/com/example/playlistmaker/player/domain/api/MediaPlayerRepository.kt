package com.example.playlistmaker.player.domain.api

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.models.PlayerState

interface MediaPlayerRepository {
    fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String)
}