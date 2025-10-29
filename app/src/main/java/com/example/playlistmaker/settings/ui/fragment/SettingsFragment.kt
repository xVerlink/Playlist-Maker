package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsActivityViewModel
import com.example.playlistmaker.sharing.domain.models.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsActivityViewModel by viewModel<SettingsActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.themeSwitcher.isChecked = viewModel.isChecked()
        viewModel.observeTheme().observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}