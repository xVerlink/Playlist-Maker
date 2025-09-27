package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.sharing.domain.models.EmailData
import com.example.playlistmaker.settings.ui.view_model.SettingsActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsActivityViewModel by viewModel<SettingsActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.themeSwitcher.isChecked = viewModel!!.isChecked()
        viewModel.observeTheme().observe(this) {
            when (it) {
                false -> binding.themeSwitcher.isChecked = false
                true -> binding.themeSwitcher.isChecked = true
            }
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareAppButton.setOnClickListener {
            val sharedText = resources.getString(R.string.course_link)
            viewModel.shareCourseLink(sharedText)
        }

        binding.textSupportButton.setOnClickListener {
            val email = resources.getString(R.string.developers_email)
            val mailTitle = resources.getString(R.string.text_support_mail_title)
            val mailText = resources.getString(R.string.text_support_mail_text)
            viewModel.openSupport(EmailData(email, mailTitle, mailText))
        }

        binding.licenseAgreementButton.setOnClickListener {
            val data = resources.getString(R.string.practicum_offer)
            viewModel.openLicenseAgreement(data)
        }
    }
}