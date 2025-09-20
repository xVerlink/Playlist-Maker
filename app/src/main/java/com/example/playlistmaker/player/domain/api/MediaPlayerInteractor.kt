package com.example.playlistmaker.player.domain.api

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.models.PlayerState

interface MediaPlayerInteractor {

    fun preparePlayer(mediaPlayer: MediaPlayer, dataSource: String)

    //fun startPlaying()

    //fun pausePlayer()

    //fun setOnCompletionListener()

    //fun getCurrentPosition(): Int

    //fun getPlayerState(): PlayerState

    //fun release()
}