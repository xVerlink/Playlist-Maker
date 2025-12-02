package com.example.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.media_library.domain.models.FavoritesState
import com.example.playlistmaker.media_library.ui.models.FavoritesAdapter
import com.example.playlistmaker.media_library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private var _adapter: FavoritesAdapter? = null
    private val adapter get() = _adapter!!
    private var isClickAllowed = true
    private val favoritesViewModel: FavoritesViewModel by viewModel<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = FavoritesAdapter { track ->
            if (clickDebounce()) {
                findNavController().navigate(R.id.action_mediaLibraryFragment_to_playerFragment, PlayerFragment.createArgs(track))
            }
        }
        binding.favoritesRecyclerView.adapter = adapter
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favoritesViewModel.fillData()
        favoritesViewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Loading -> showLoading()
            is FavoritesState.Empty -> showEmpty()
            is FavoritesState.Content -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.apply {
            favoritesRecyclerView.isVisible = false
            placeholder.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.apply {
            favoritesRecyclerView.isVisible = false
            placeholder.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showContent(tracks: List<Track>) {
        adapter.updateTracks(tracks)
        adapter.notifyDataSetChanged()
        binding.apply {
            favoritesRecyclerView.isVisible = true
            placeholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        fun newInstance(): Fragment = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}