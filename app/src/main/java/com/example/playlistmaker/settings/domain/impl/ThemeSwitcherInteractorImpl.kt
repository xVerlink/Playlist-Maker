package com.example.playlistmaker.settings.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherRepository

class ThemeSwitcherInteractorImpl(private val repository: ThemeSwitcherRepository) :
    ThemeSwitcherInteractor {

    private var darkTheme = false

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        repository.writeFlag(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun isDarkModeEnabled(): Boolean {
        return repository.readFlag()
    }
}