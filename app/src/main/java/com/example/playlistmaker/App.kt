package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import playerModule
import searchModule
import settingsModule
import sharingModule


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(playerModule, searchModule, settingsModule, sharingModule)
        }

        val themeSwitcher: ThemeSwitcherInteractor = getKoin().get()
        val isDarkModeEnabled = themeSwitcher.isDarkModeEnabled()
        themeSwitcher.switchTheme(isDarkModeEnabled)
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_shared_preferences"
        const val IS_THEME_DARK = "is_theme_dark"
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}