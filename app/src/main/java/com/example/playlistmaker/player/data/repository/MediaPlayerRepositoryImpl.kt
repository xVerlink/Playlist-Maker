package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    override fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String) {
        try {
            mediaPlayer.setDataSource(dataSource)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}