package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherRepository

class ThemeSwitcherInteractorImpl(private val repository: ThemeSwitcherRepository) :
    ThemeSwitcherInteractor {

    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        repository.writeFlag(isDarkThemeEnabled)
        repository.switchTheme(isDarkThemeEnabled)
    }

    override fun isDarkModeEnabled(): Boolean {
        return repository.readFlag()
    }
}