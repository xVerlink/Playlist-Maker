package com.example.playlistmaker.settings.domain.api

interface ThemeSwitcherInteractor {

    fun switchTheme(flag: Boolean)

    fun isDarkModeEnabled(): Boolean
}