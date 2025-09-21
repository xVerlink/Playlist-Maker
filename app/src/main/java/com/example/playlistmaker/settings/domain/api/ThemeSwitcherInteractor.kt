package com.example.playlistmaker.settings.domain.api

interface ThemeSwitcherInteractor {

    fun switchTheme(isDarkThemeEnabled: Boolean)

    fun isDarkModeEnabled(): Boolean
}