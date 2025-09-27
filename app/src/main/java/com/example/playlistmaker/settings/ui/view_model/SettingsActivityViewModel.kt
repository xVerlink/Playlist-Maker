package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.models.EmailData

class SettingsActivityViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeSwitcherInteractor
) : ViewModel() {

    private var darkTheme: Boolean = themeInteractor.isDarkModeEnabled()
    private val themeLiveData = MutableLiveData<Boolean>()
    fun observeTheme(): LiveData<Boolean> = themeLiveData

    fun shareCourseLink(courseLink: String) {
        sharingInteractor.shareApp(courseLink)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }

    fun openLicenseAgreement(termsLink: String) {
        sharingInteractor.openTerms(termsLink)
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        darkTheme = isDarkThemeEnabled
        themeLiveData.postValue(isDarkThemeEnabled)
        themeInteractor.switchTheme(isDarkThemeEnabled)
    }

    fun isChecked(): Boolean = themeInteractor.isDarkModeEnabled()
}