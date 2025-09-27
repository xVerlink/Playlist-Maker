package com.example.playlistmaker.settings.data.repository

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherRepository

class ThemeSwitcherRepositoryImpl(private val storageClient: StorageClient<Boolean>) :
    ThemeSwitcherRepository {

    override fun readFlag(): Boolean {
        return storageClient.getData() ?: false
    }

    override fun writeFlag(isDarkThemeEnabled: Boolean) {
        storageClient.storeData(isDarkThemeEnabled)
    }

    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}