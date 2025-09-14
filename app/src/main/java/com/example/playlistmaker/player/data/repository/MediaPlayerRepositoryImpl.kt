package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import com.example.playlistmaker.search.domain.models.PlayerState

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    override fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String): PlayerState {
        return try {
            mediaPlayer.setDataSource(dataSource)
            mediaPlayer.prepareAsync()
            PlayerState.Prepared()
        } catch (e: Exception) {
            e.printStackTrace()
            PlayerState.Default()
        }
    }
}