package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActiityViewModel(private var url: String) : ViewModel() {

    private val playerInteractor = Creator.getMediaPlayerInteractor()
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private var playerState: PlayerState = PlayerState.Default
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private var timer = "00:00"
    private val timerLiveData = MutableLiveData<String>()
    fun observeTimer(): LiveData<String> = timerLiveData

    private val timerRunnable = Runnable {
        if (playerState is PlayerState.Playing) {
            startTimerUpdate()
        }
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer(mediaPlayer, url)
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.Prepared
            playerStateLiveData.postValue(playerState)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.Prepared
            playerStateLiveData.postValue(playerState)
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.Playing
        playerStateLiveData.postValue(playerState)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
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
        timer = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
        timerLiveData.postValue(timer)
        handler.postDelayed(timerRunnable, TRACK_CURRENT_TIME_DELAY)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        timer = "00:00"
        timerLiveData.postValue(timer)
    }

    fun onPause() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    companion object {
        private const val TRACK_CURRENT_TIME_DELAY = 333L

        fun getFactory(trackUrl: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerActiityViewModel(trackUrl)
            }
        }
    }
}