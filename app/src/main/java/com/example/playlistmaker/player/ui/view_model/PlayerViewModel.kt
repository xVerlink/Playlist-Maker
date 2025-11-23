package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val application: Application,
    private val url: String,
    private val playerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private var playerState: PlayerState = PlayerState.Default
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val timerLiveData = MutableLiveData<String>()
    fun observeTimer(): LiveData<String> = timerLiveData

    private var timerJob: Job? = null

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
        timerJob = viewModelScope.launch {
            while (playerState is PlayerState.Playing) {
                delay(TRACK_CURRENT_TIME_DELAY)
                timerLiveData.postValue(getCurrentPlayerPosition())
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        timerLiveData.postValue(getCurrentPlayerPosition())
    }

    private fun resetTimer() {
        timerJob?.cancel()
        val timer = application.applicationContext.getString(R.string.media_player_default_time)
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

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.getCurrentPosition())
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