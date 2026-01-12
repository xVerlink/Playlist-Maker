package com.example.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media_library.ui.models.PlaylistGridAdapter
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistsViewModel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()
    private var _adapter: PlaylistGridAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = PlaylistGridAdapter() { playlist ->
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_playlistFragment,
                PlaylistFragment.createArgs(playlist.id!!))
        }

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        playlistsViewModel.getPlaylists()

        playlistsViewModel.observePlaylists().observe(viewLifecycleOwner) { playlistList ->
            if (playlistList.isEmpty()) {
                binding.placeholder.isVisible = true
                binding.recyclerView.isVisible = false
            } else {
                binding.placeholder.isVisible = false
                binding.recyclerView.isVisible = true
            }
            adapter.updateList(playlistList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    companion object {
        fun newInstance(): Fragment = PlaylistsFragment()
    }
}