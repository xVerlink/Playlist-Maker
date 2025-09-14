package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initAppContext(this)
        val themeSwitcherInteractor = Creator.getThemeSwitcherInteractor()
        val darkThemeEnabled = themeSwitcherInteractor.isDarkModeEnabled()
        themeSwitcherInteractor.switchTheme(darkThemeEnabled)
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_shared_preferences"
        const val IS_THEME_DARK = "is_theme_dark"
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}