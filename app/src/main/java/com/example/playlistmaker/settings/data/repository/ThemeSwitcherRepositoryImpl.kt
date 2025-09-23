package com.example.playlistmaker.settings.data.repository


import com.example.playlistmaker.search.data.storage.StorageClient
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherRepository

class ThemeSwitcherRepositoryImpl(private val prefs: StorageClient<Boolean>) :
    ThemeSwitcherRepository {

    override fun readFlag(): Boolean {
        return prefs.getData() ?: false
    }

    override fun writeFlag(isDarkThemeEnabled: Boolean) {
        prefs.storeData(isDarkThemeEnabled)
    }
}