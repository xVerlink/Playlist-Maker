package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.api.FavoritesInteractor
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val url: String,
    private val playerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var playerState: PlayerState = PlayerState.Default
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val timerLiveData = MutableLiveData<String>()
    fun observeTimer(): LiveData<String> = timerLiveData

    private val favoriteTracksLiveData = MutableLiveData<List<String>>()
    fun observeFavoriteTracks(): LiveData<List<String>> = favoriteTracksLiveData

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = playlistsLiveData

    private val containsFlag = MutableLiveData<Pair<Boolean, String>?>()
    fun observeFlag(): LiveData<Pair<Boolean, String>?> = containsFlag

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
        timerJob?.cancel()
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

    fun setupFavoritesList() {
        if (favoriteTracksLiveData.value == null) {
            viewModelScope.launch {
                favoritesInteractor.getFavoritesId().collect { idList ->
                    favoriteTracksLiveData.postValue(idList)
                    delay(1000L) //Зачем я это сделал? Разобраться
                }
            }
        }
    }

    fun addToFavorites(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.addToFavorites(track).collect { tracks ->
                updateFavorites(tracks)
            }
        }
    }

    fun removeFromFavorites(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.removeFromFavorites(track).collect { tracks ->
                updateFavorites(tracks)
            }
        }
    }

    private fun updateFavorites(track: List<String>) {
        favoriteTracksLiveData.postValue(track)
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                playlistsLiveData.postValue(playlists)
            }
        }
    }

    fun addToPlaylist(track: Track, playlist: Playlist) {
        if (playlist.trackIdList.contains(track.trackId)) {
            containsFlag.postValue(Pair(true, playlist.title))
        } else {
            containsFlag.postValue(Pair(false, playlist.title))
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist)
            }
            getPlaylists()
        }
    }

    fun resetToastMessage() {
        containsFlag.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        resetTimer()
    }

    companion object {
        private const val TRACK_CURRENT_TIME_DELAY = 300L
    }
}