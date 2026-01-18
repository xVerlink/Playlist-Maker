package com.example.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.media_library.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {
    override val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.title = resources.getString(R.string.edit)
            createButton.text = resources.getString(R.string.save)
        }
        viewModel.getPlaylist(requireArguments().get(PLAYLIST_ID) as Int)
        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->
            binding.titleInputEditText.setText(playlist.title)
            binding.descriptionInputEditText.setText(playlist.description)
            binding.placeholder.setImageURI(playlist.cover.toUri())

            setPlaylistInfo(playlist)
        }
    }

    private fun setPlaylistInfo(playlist: Playlist) {
        viewModel.setPlaylistId(playlist.id!!)
        viewModel.setTracksList(playlist.trackIdList)
        viewModel.setTracksCount(playlist.tracksCount)
        viewModel.setUri(playlist.cover)
    }

    override fun onSaveButtonPressed() {
        viewModel.addPlaylist()
        findNavController().navigateUp()
    }

    override fun closeScreen() {
        findNavController().navigateUp()
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createArgs(playlistId: Int) =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}