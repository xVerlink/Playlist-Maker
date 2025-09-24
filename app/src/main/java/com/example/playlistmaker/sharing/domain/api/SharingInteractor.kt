package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.sharing.domain.models.EmailData

interface SharingInteractor {
    fun shareApp(courseLink: String)
    fun openTerms(termsLink: String)
    fun openSupport(emailData: EmailData)
}