package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.models.PlayerState

class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayer, private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    private var playerState = STATE_DEFAULT

    override fun preparePlayer(dataSource: String, action: () -> Unit) {
        val state = repository.preparePlayer(mediaPlayer, dataSource)
        if (state is PlayerState.Prepared) {
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                action.invoke()
            }
        } else {
            playerState = STATE_DEFAULT
        }

    }

    override fun startPlaying() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener {

        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPlayerState(): PlayerState {
        return when (playerState) {
            STATE_PLAYING -> PlayerState.Playing()
            STATE_PREPARED-> PlayerState.Prepared()
            STATE_PAUSED -> PlayerState.Paused()
            else -> PlayerState.Default()
        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}