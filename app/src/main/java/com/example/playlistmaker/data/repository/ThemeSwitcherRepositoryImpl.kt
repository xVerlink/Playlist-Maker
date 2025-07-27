package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.ThemeSwitcherRepository

class ThemeSwitcherRepositoryImpl(private val sharedPrefs: SharedPreferences) : ThemeSwitcherRepository {

    override fun readFlag(): Boolean {
        return sharedPrefs.getBoolean(App.IS_THEME_DARK, false)
    }

    override fun writeFlag(darkThemeEnabled: Boolean) {
        sharedPrefs.edit {
            putBoolean(App.IS_THEME_DARK, darkThemeEnabled)
        }
    }
}