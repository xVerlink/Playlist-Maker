package com.example.playlistmaker.sharing.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.setType("text/plain")
        val chooserIntent = Intent.createChooser(shareIntent, "Куда отправить?")
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    override fun openLink(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = link.toUri()
        browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(browserIntent)
    }

    override fun openEmail (emailData: EmailData) {
        val uri = "mailto:".toUri()
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.mailTitle)
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailData.mailText)
        context.startActivity(emailIntent)
    }
}