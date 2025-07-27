package com.example.playlistmaker.domain.api

interface ThemeSwitcherInteractor {

    fun switchTheme(flag: Boolean)

    fun isDarkModeEnabled(): Boolean
}