package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.models.EmailData

class SettingsActivityViewModel() : ViewModel() {

    private val sharingInteractor = Creator.getSharingInteractor()
    private val themeInteractor = Creator.getThemeSwitcherInteractor()
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

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsActivityViewModel()
            }
        }
    }
}