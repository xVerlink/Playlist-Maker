package com.example.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media_library.domain.models.PlaylistUiState
import com.example.playlistmaker.media_library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.models.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var _adapter: TrackAdapter? = null
    private val adapter get() = _adapter!!

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val viewModel by viewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUi()
        initBottomSheet()
        initListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { playlistState ->
                playlistState ?: return@collect
                renderPlaylistInfo(playlistState)
                adapter.updateTracks(playlistState.tracks)
            }
        }

        viewModel.observeToastText().observe(viewLifecycleOwner) { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _adapter = null
    }

    private fun prepareUi() {
        _adapter = TrackAdapter(
            onLongClick = { track ->
                removeTrack(track)
            },
            onClick = { track ->
                navigateToPlayer(track)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.loadPlaylist(requireArguments().get(PLAYLIST_ID) as Int)
    }

    private fun initListeners() {
        binding.returnButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.menuButton.setOnClickListener {
            binding.overlay.isVisible = true
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.menuShareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.menuEditPlaylist.setOnClickListener {
            editPlaylist()
        }

        binding.menuDeletePlaylist.setOnClickListener {
            deletePlaylist()
        }
    }

    private fun renderPlaylistInfo(playlistState: PlaylistUiState) {
        binding.apply {
            playlistTitle.text = playlistState.playlist.title
            playlistDescription.text = playlistState.playlist.description
            playlistDuration.text = formatPlaylistDuration(playlistState.tracks)
            tracksCount.text = formatTracksCount(playlistState.tracks.size)

            menuPlaylistTitle.text = playlistState.playlist.title
            menuTracksCount.text = formatTracksCount(playlistState.tracks.size)
        }
        setCover(playlistState.playlist.cover)

        if (playlistState.tracks.isEmpty()) {
            binding.placeholder.isVisible = true
        }
    }

    private fun setCover(uri: String) {
        Glide.with(requireContext())
            .load(uri)
            .placeholder(ResourcesCompat.getDrawable(resources, R.drawable.ic_placeholder, null))
            .centerCrop()
            .into(binding.cover)

        Glide.with(requireContext())
            .load(uri)
            .placeholder(ResourcesCompat.getDrawable(resources, R.drawable.ic_placeholder, null))
            .centerCrop()
            .into(binding.menuCover)
    }

    private fun formatPlaylistDuration(tracks: List<Track>): String {
        var durationSum = 0L
        tracks.forEach {
            durationSum += if (it.trackTime.isEmpty()) {
                0
            } else {
                val durationList = it.trackTime.split(":")
                val minutes = durationList[0].toInt()
                val seconds = durationList[1].toInt()
                (minutes * 60 + seconds) * 1000L
            }
        }
        val durationMinutes = SimpleDateFormat("mm", Locale.getDefault()).format(durationSum).toInt()
        return resources.getQuantityString(R.plurals.numberOfMinutes, durationMinutes, durationMinutes)
    }

    private fun formatTracksCount(tracksCount: Int): String {
        return resources.getQuantityString(R.plurals.numberOfSongs, tracksCount, tracksCount)
    }

    private fun removeTrack(track: Track) {
        binding.overlay.isVisible = true
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.want_to_delete_track))
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.removeFromPlaylist(track)
                binding.overlay.isVisible = false
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                binding.overlay.isVisible = false
            }.show()
    }

    private fun navigateToPlayer(track: Track) {
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment, PlayerFragment.createArgs(track))
    }

    private fun sharePlaylist() {
        viewModel.sharePlaylistOrDisplayErrorMessage(resources.getString(R.string.there_is_no_tracks_in_this_playlist),
            resources.getString(R.string.tracks))
    }

    private fun initBottomSheet() {
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        menuBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.overlay.isVisible = false
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun editPlaylist() {
        findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment,
            EditPlaylistFragment.createArgs(requireArguments().get(
            PLAYLIST_ID) as Int))
    }

    private fun deletePlaylist() {
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.overlay.isVisible = true
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_playlist))
            .setMessage(resources.getString(R.string.are_you_sure_you_want_to_delete_playlist))
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                binding.overlay.isVisible = false
            }.show()
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"
        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}