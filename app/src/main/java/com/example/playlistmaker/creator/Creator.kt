package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.repository.HistoryManagerRepositoryImpl
import com.example.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.settings.data.repository.ThemeSwitcherRepositoryImpl
import com.example.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.HistoryManagerInteractor
import com.example.playlistmaker.search.domain.api.HistoryManagerRepository
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaPlayerRepository
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.HistoryManagerInteractorImpl
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.settings.domain.impl.ThemeSwitcherInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var appContext: Application

    fun initAppContext(appContext: Application) {
        Creator.appContext = appContext
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

    private fun getThemeSwitcherRepository(): ThemeSwitcherRepository {
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