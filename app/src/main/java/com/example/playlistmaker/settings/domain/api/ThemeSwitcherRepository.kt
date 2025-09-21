package com.example.playlistmaker.settings.domain.api

interface ThemeSwitcherRepository {

    fun readFlag(): Boolean

    fun writeFlag(isDarkThemeEnabled: Boolean)
}