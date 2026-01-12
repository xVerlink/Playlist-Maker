package com.example.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.media_library.ui.view_model.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    protected val binding get() = _binding!!
    protected open val viewModel by viewModel<CreatePlaylistViewModel>()
    private var uri: String? = null
    private lateinit var returnDialog: MaterialAlertDialogBuilder

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.placeholder.setImageURI(uri)
            this.uri = uri.toString()
            viewModel.setUri(uri.toString())
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
        uri = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        overrideOnBackPressed()

        returnDialog = configureDialog()

        viewModel.observeUri().observe(viewLifecycleOwner) {
            binding.placeholder.setImageURI(it.toUri())
        }
    }

    private fun initListeners() {
        binding.titleTextInputLayout.editText?.setOnClickListener {
            binding.titleInputEditText.requestFocus()
        }

        binding.descriptionTextInputLayout.editText?.setOnClickListener {
            binding.descriptionInputEditText.requestFocus()
        }

        binding.titleInputEditText.doOnTextChanged { text, start, before, count ->
            val isNotBlank = text?.isNotBlank() ?: false
            binding.createButton.isEnabled = (text?.isNotEmpty() == true) && isNotBlank
        }

        binding.titleInputEditText.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int -> },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                viewModel.setTitle(text.toString())
            },
            afterTextChanged = { text: Editable? -> }
        )

        binding.descriptionInputEditText.addTextChangedListener(
            beforeTextChanged = { text: CharSequence?, start: Int, count: Int, after: Int -> },
            onTextChanged = { text: CharSequence?, start: Int, before: Int, count: Int ->
                viewModel.setDescription(text.toString())
            },
            afterTextChanged = { text: Editable? -> }
        )

        binding.placeholder.setOnClickListener {
            choosePlaylistCover()
        }

        binding.createButton.setOnClickListener {
            onSaveButtonPressed()
        }

        binding.toolbar.setNavigationOnClickListener {
            closeScreen()
        }
    }

    private fun overrideOnBackPressed() {
        val returnCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeScreen()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, returnCallback)
    }

    protected open fun onSaveButtonPressed() {
        viewModel.addPlaylist()
        val title = binding.titleInputEditText.text.toString()
        Toast.makeText(requireContext(), "Плейлист $title создан", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun choosePlaylistCover() {
        pickMedia.launch(PickVisualMediaRequest())
    }

    private fun configureDialog(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.finish_playlist_creation))
            .setMessage(resources.getString(R.string.all_unsaved_data_will_be_lost))
            .setPositiveButton(resources.getString(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->

            }
    }

    protected open fun closeScreen() {
        if (binding.placeholder.drawable != null
            || !binding.descriptionInputEditText.text.isNullOrEmpty()
            || !binding.titleInputEditText.text.isNullOrEmpty()) {
            returnDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }
}