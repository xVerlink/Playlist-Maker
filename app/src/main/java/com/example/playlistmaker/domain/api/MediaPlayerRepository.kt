package com.example.playlistmaker.domain.api

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerRepository {
    fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String): PlayerState
}