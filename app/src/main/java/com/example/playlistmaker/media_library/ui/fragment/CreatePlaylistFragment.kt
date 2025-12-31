package com.example.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.media_library.ui.view_model.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private var uri: String? = null
    private lateinit var returnDialog: MaterialAlertDialogBuilder

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.placeholder.setImageURI(uri)
            this.uri = uri.toString()
            viewModel.fillImage(binding.placeholder.drawable)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returnDialog = MaterialAlertDialogBuilder(requireContext())
        configureDialog()
        val returnCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeScreen()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, returnCallback)

        viewModel.observeDrawable().observe(viewLifecycleOwner) {
            binding.placeholder.setImageDrawable(it)
        }

        binding.editTextTitle.setOnClickListener {
            binding.editTextTitle.requestFocus()
        }

        binding.editTextDescription.setOnClickListener {
            binding.editTextTitle.requestFocus()
        }

        binding.editTextTitle.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.title.setTextColor(resources.getColor(R.color.PM_blue))
            } else {
                binding.title.setTextColor(resources.getColor(R.color.PM_grey))
            }
        }

        binding.editTextDescription.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.description.setTextColor(resources.getColor(R.color.PM_blue))
            } else {
                binding.description.setTextColor(resources.getColor(R.color.PM_grey))
            }
        }

        binding.editTextTitle.addTextChangedListener(
            { text: CharSequence?, start: Int, count: Int, after: Int -> },
            { text: CharSequence?, start: Int, before: Int, count: Int ->
                binding.createButton.isEnabled = text?.isNotEmpty() == true

            }
        )

        binding.placeholder.setOnClickListener {
            choosePlaylistCover()
        }

        binding.createButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val uri = this.uri ?: ""
            viewModel.addPlaylist(Playlist(id = null,
                title = title,
                description = description,
                cover = uri,
                mutableListOf(),
                tracksCount = 0
            ))
            Toast.makeText(requireContext(), "Плейлист $title создан", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.toolbar.setNavigationOnClickListener {
            closeScreen()
        }
    }

    private fun choosePlaylistCover() {
        pickMedia.launch(PickVisualMediaRequest())
    }

    private fun configureDialog() {
        returnDialog
            .setTitle(resources.getString(R.string.finish_playlist_creation))
            .setMessage(resources.getString(R.string.all_unsaved_data_will_be_lost))
            .setPositiveButton(resources.getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->

            }
    }

    private fun closeScreen() {
        if (binding.placeholder.drawable != null
            || binding.editTextDescription.text.isNotEmpty()
            || binding.editTextTitle.text.isNotEmpty()) {
            returnDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }
}