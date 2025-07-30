package com.example.playlistmaker.presentation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private val themeSwitcherInteractor = Creator.getThemeSwitcherInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.settingsScreenToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> themeSwitcher.isChecked = false
            Configuration.UI_MODE_NIGHT_YES -> themeSwitcher.isChecked = true
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            themeSwitcherInteractor.switchTheme(checked)
        }

        val shareButton = findViewById<TextView>(R.id.shareAppButton)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val sharedText = resources.getText(R.string.course_link)
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
            shareIntent.setType("text/plain")
            startActivity(Intent.createChooser(shareIntent, "Куда отправить?"))
        }

        val textSupportButton = findViewById<TextView>(R.id.textSupportButton)
        textSupportButton.setOnClickListener {
            val email = resources.getString(R.string.developers_email)
            val uri = "mailto:".toUri()
            val mailTitle = resources.getText(R.string.text_support_mail_title)
            val mailText = resources.getText(R.string.text_support_mail_text)
            val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailTitle)
            emailIntent.putExtra(Intent.EXTRA_TEXT, mailText)
            startActivity(emailIntent)
        }

        val licenseAgreementButton = findViewById<TextView>(R.id.licenseAgreementButton)
        licenseAgreementButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = resources.getString(R.string.practicum_offer).toUri()
            startActivity(browserIntent)
        }
    }
}