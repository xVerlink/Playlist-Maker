package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val returnBackButton = findViewById<ImageView>(R.id.settings_screen_return_button)
        returnBackButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<ImageView>(R.id.shareAppButton)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val sharedText = resources.getText(R.string.course_link)
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
            shareIntent.setType("text/plain")
            startActivity(Intent.createChooser(shareIntent, "Куда отправить?"))
        }

        val textSupportButton = findViewById<ImageView>(R.id.textSupportButton)
        textSupportButton.setOnClickListener {
            val email = "justverlink@yandex.ru"
            val uri = Uri.parse("mailto:")
            val mailTitle = resources.getText(R.string.text_support_mail_title)
            val mailText = resources.getText(R.string.text_support_mail_text)
            val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailTitle)
            emailIntent.putExtra(Intent.EXTRA_TEXT, mailText)
            startActivity(emailIntent)
        }

        val licenseAgreementButton = findViewById<ImageView>(R.id.licenseAgreementButton)
        licenseAgreementButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(resources.getString(R.string.practicum_offer))
            startActivity(browserIntent)
        }
    }
}