package com.example.playlistmaker.player.domain.api

import android.media.MediaPlayer
import com.example.playlistmaker.search.domain.models.PlayerState

interface MediaPlayerRepository {
    fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String): PlayerState
}