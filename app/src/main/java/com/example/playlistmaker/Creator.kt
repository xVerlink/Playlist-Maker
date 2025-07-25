package com.example.playlistmaker

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.SearchHistory
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.TrackHistoryManager
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
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

    fun getTrackHistoryManager(): TrackHistoryManager {
        return SearchHistory(appContext.getSharedPreferences(App.Companion.PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE))
    }
}