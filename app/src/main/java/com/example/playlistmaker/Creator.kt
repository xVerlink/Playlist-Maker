package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.HistoryManagerRepositoryImpl
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.ThemeSwitcherRepositoryImpl
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.domain.api.HistoryManagerRepository
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.ThemeSwitcherInteractor
import com.example.playlistmaker.domain.api.ThemeSwitcherRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.HistoryManagerInteractorImpl
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var appContext: Application

    fun initAppContext(appContext: Application) {
        this.appContext = appContext
    }

    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun getTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    private fun getHistoryRepository(): HistoryManagerRepository {
        return HistoryManagerRepositoryImpl(appContext.getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE))
    }

    fun getHistoryManagerInteractor(): HistoryManagerInteractor {
        return HistoryManagerInteractorImpl(getHistoryRepository())
    }

    private fun getThemeSwitcherRepository(): ThemeSwitcherRepository{
        return ThemeSwitcherRepositoryImpl(appContext.getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE))
    }

    fun getThemeSwitcherInteractor(): ThemeSwitcherInteractor {
        return ThemeSwitcherInteractorImpl(getThemeSwitcherRepository())
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun getMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayer(), getMediaPlayerRepository())
    }
}