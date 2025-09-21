package com.example.playlistmaker.settings.ui.activity


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.settings.ui.view_model.SettingsActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private var viewModel: SettingsActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(this, SettingsActivityViewModel.getFactory())
            .get(SettingsActivityViewModel::class.java)


        val toolbar = findViewById<MaterialToolbar>(R.id.settingsScreenToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = viewModel!!.isChecked()
        viewModel?.observeTheme()?.observe(this) {
            when (it) {
                false -> themeSwitcher.isChecked = false
                true -> themeSwitcher.isChecked = true
            }
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel?.switchTheme(checked)
        }

        val shareButton = findViewById<TextView>(R.id.shareAppButton)
        shareButton.setOnClickListener {
            val sharedText = resources.getString(R.string.course_link)
            viewModel?.shareCourseLink(sharedText)
        }

        val textSupportButton = findViewById<TextView>(R.id.textSupportButton)
        textSupportButton.setOnClickListener {
            val email = resources.getString(R.string.developers_email)
            val mailTitle = resources.getString(R.string.text_support_mail_title)
            val mailText = resources.getString(R.string.text_support_mail_text)
            viewModel?.openSupport(EmailData(email, mailTitle, mailText))
        }

        val licenseAgreementButton = findViewById<TextView>(R.id.licenseAgreementButton)
        licenseAgreementButton.setOnClickListener {
            val data = resources.getString(R.string.practicum_offer)
            viewModel?.openLicenseAgreement(data)
        }
    }
}