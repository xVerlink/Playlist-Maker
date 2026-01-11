package com.example.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.PlaylistAdapter
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var _adapter: PlaylistAdapter? = null
    private val adapter get() = _adapter!!
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel<PlayerViewModel> {
        parametersOf(track.previewUrl)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        track = requireArguments().get(TRACK_KEY) as Track
        _adapter = PlaylistAdapter { playlist ->  
            addToPlaylist(playlist)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        viewModel.getPlaylists()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.observePlaylists().observe(viewLifecycleOwner) { playlists ->
            binding.recyclerView.isVisible = playlists.isNotEmpty()
            adapter.updateList(playlists)
        }

        viewModel.observeFlag().observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                if (pair.first) {
                    Toast.makeText(requireContext(), "${resources.getString(R.string.track_already_added)} ${pair.second}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "${resources.getString(R.string.added_to_playlist)} ${pair.second}", Toast.LENGTH_SHORT).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }

        viewModel.setupFavoritesList()
        viewModel.preparePlayer()
        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            when(it) {
                is PlayerState.Playing -> binding.playButton.setImageResource(R.drawable.pause_button)
                is PlayerState.Paused -> binding.playButton.setImageResource(R.drawable.play_button)
                is PlayerState.Prepared -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.trackTimeProgress.text = requireContext().getString(R.string.media_player_default_time)
                }
                else -> Toast.makeText(context, "Exception while preparing player or wait a few seconds", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.observeTimer().observe(viewLifecycleOwner) {
            binding.trackTimeProgress.text = it
        }

        viewModel.observeFavoriteTracks().observe(viewLifecycleOwner) { favoriteTracks->
            setFavoritesButton(favoriteTracks)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            if (track.previewUrl.isNotEmpty()) {
                viewModel.playbackControl()
            } else {
                Toast.makeText(context, "There is no track preview", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addToFavoritesButton.setOnClickListener {
            onFavoritesButtonClicked(track.isFavorite)
        }

        binding.addToPlaylistButton.setOnClickListener {
            binding.overlay.isVisible = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.newPlaylistButton.setOnClickListener {
            viewModel.resetToastMessage()
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
        }

        setCover()
        setTrackName()
        setArtist()
        setCurrentDuration()
        setDuration()
        setAlbum()
        setYear()
        setGenre()
        setCountry()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun setCover() {
        val roundRadius =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        Glide.with(this)
            .load(track.getCover512())
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundRadius))
            .into(binding.cover)
    }

    private fun setTrackName() {
        binding.trackName.text = track.trackName
    }

    private fun setArtist() {
        binding.artistName.text = track.artistName
    }

    private fun setCurrentDuration() {
        binding.trackTimeProgress.text = resources.getString(R.string.media_player_default_time)
    }

    private fun setDuration() {
        binding.durationTime.text = track.trackTime
        binding.durationGroup.isVisible = !binding.durationTime.text.isNullOrEmpty()
    }

    private fun setAlbum() {
        binding.album.text = track.collectionName
        binding.albumGroup.isVisible = !binding.album.text.isNullOrEmpty()
    }

    private fun setYear() {
        if (!track.releaseDate.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = inputFormat.parse(track.releaseDate)
            val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            binding.year.text = yearFormat.format(date!!)
            binding.yearGroup.isVisible = true
        } else {
            binding.yearGroup.isVisible = false
        }
    }

    private fun setGenre() {
        binding.genre.text = track.primaryGenreName
        binding.genreGroup.isVisible = !binding.genre.text.isNullOrEmpty()
    }

    private fun setCountry() {
        binding.country.text = track.country
        binding.countryGroup.isVisible = !binding.country.text.isNullOrEmpty()
    }

    private fun setFavoritesButton(favoritesIds: List<String>) {
        if (favoritesIds.contains(track.trackId)) {
            track.isFavorite = true
            binding.addToFavoritesButton.setImageResource(R.drawable.add_to_favorites_button_pressed)
        } else {
            track.isFavorite = false
            binding.addToFavoritesButton.setImageResource(R.drawable.add_to_favorites_button_unpressed)
        }
    }

    private fun onFavoritesButtonClicked(isFavorite: Boolean) {
        if (isFavorite) {
            track.isFavorite = false
            binding.addToFavoritesButton.setImageResource(R.drawable.add_to_favorites_button_unpressed)
            viewModel.removeFromFavorites(track)
        } else {
            track.isFavorite = true
            binding.addToFavoritesButton.setImageResource(R.drawable.add_to_favorites_button_pressed)
            viewModel.addToFavorites(track)
        }
    }

    private fun addToPlaylist(playlist: Playlist) {
        viewModel.addToPlaylist(track, playlist)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        private const val TRACK_KEY = "TRACK"
        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK_KEY to track)
    }
}