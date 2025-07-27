package com.example.playlistmaker

import android.app.Application



class App: Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.initAppContext(this)
        val themeSwitcherInteractor = Creator.getThemeSwitcherInteractor()
        val darkThemeEnabled = themeSwitcherInteractor.isDarkModeEnabled()
        themeSwitcherInteractor.switchTheme(darkThemeEnabled)
    }

    /*fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE).edit() {
                putBoolean(IS_THEME_DARK, darkThemeEnabled)
            }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }*/

    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_shared_preferences"
        const val IS_THEME_DARK = "is_theme_dark"
        const val SEARCH_HISTORY_KEY = "search_history_key"
    }
}