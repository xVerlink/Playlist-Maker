package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp(courseLink: String) {
        externalNavigator.shareLink(courseLink)
    }

    override fun openTerms(termsLink: String) {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }
}