package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActiityViewModel(
    private val application: Application,
    private val url: String,
    private val playerInteractor: MediaPlayerInteractor,
    private val handler: Handler
) : ViewModel() {

    private var playerState: PlayerState = PlayerState.Default
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private var timer: String = application.applicationContext.getString(R.string.media_player_default_time)
    private val timerLiveData = MutableLiveData<String>()
    fun observeTimer(): LiveData<String> = timerLiveData

    private val timerRunnable = Runnable {
        if (playerState is PlayerState.Playing) {
            startTimerUpdate()
        }
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer(url)
        playerInteractor.setOnPreparedListener {
            onPlayerPrepared()
        }
        playerInteractor.setOnCompletionListener {
            onTrackCompleted()
        }
    }

    private fun startPlayer() {
        playerInteractor.playerStart()
        playerState = PlayerState.Playing
        playerStateLiveData.postValue(playerState)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        playerInteractor.playerPause()
        playerState = PlayerState.Paused
        playerStateLiveData.postValue(playerState)
        pauseTimer()
    }

    fun playbackControl() {
        when (playerState) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, PlayerState.Paused, PlayerState.Default -> startPlayer()
        }
    }

    private fun startTimerUpdate() {
        timer = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.getCurrentPosition())
        timerLiveData.postValue(timer)
        handler.postDelayed(timerRunnable, TRACK_CURRENT_TIME_DELAY)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        timer = application.applicationContext.getString(R.string.media_player_default_time)
        timerLiveData.postValue(timer)
    }

    private fun onPlayerPrepared() {
        playerState = PlayerState.Prepared
        playerStateLiveData.postValue(playerState)
    }

    private fun onTrackCompleted() {
        playerState = PlayerState.Prepared
        playerStateLiveData.postValue(playerState)
        resetTimer()
    }

    fun onPause() {
        pausePlayer()
        resetPlayer()
    }

    private fun resetPlayer() {
        playerInteractor.reset()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        resetTimer()
    }

    companion object {
        private const val TRACK_CURRENT_TIME_DELAY = 333L
    }
}