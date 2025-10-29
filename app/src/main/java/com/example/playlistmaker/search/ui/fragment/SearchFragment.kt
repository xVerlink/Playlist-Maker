package com.example.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksProvider
import com.example.playlistmaker.search.ui.models.TrackAdapter
import com.example.playlistmaker.search.ui.view_model.SearchActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var input: String? = ""

    private val viewModel by viewModel<SearchActivityViewModel>()
    private var _trackAdapter: TrackAdapter? = null
    private val trackAdapter get() = _trackAdapter!!
    private var _searchHistoryAdapter: TrackAdapter? = null
    private val searchHistoryAdapter get() = _searchHistoryAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        _trackAdapter = TrackAdapter() { track: Track ->
            viewModel.addTrackToHistory(track)
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(track))
        }
        _searchHistoryAdapter = TrackAdapter() { track: Track ->
            viewModel.addTrackToHistory(track)
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(track))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            if (binding.editText.text.isNotEmpty()) {
                render(it)
            }
        }
        viewModel.observeHistory().observe(viewLifecycleOwner) {
            searchHistoryAdapter.updateTracks(it)
            searchHistoryAdapter.notifyDataSetChanged()
        }

        binding.apply {
            searchRecyclerView.adapter = trackAdapter
            historyRecyclerView.adapter = searchHistoryAdapter
        }

        binding.editText.setText(input)

        binding.editText.setOnClickListener {
            binding.editText.requestFocus()
        }

        binding.editText.addTextChangedListener(
            { text: CharSequence?, start: Int, count: Int, after: Int -> },
            { text: CharSequence?, start: Int, before: Int, count: Int ->
                binding.clearTextButton.isVisible = !text.isNullOrEmpty()
                if (binding.editText.hasFocus() && text?.isEmpty() == true && searchHistoryAdapter.tracksIsNotEmpty()) {
                    showHistory()
                }
                viewModel.searchDebounce(text.toString())
            },
            { text: Editable? -> input = text.toString() }
        )

        binding.editText.setOnFocusChangeListener { view, hasFocus ->
            if (binding.editText.hasFocus() && binding.editText.text.isEmpty() && searchHistoryAdapter.tracksIsNotEmpty()) {
                showHistory()
            }
        }

        binding.clearTextButton.setOnClickListener {
            binding.editText.setText("")
            binding.editText.requestFocus()
            trackAdapter.clearTracks()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.history.isVisible = false
        }

        binding.refreshButton.setOnClickListener {
            val input = input ?: return@setOnClickListener
            viewModel.searchDebounce(input)
        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(input!!)
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _trackAdapter = null
        _searchHistoryAdapter = null
    }

    private fun showLoading() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyRecyclerView.isVisible = false
            placeholder.isVisible = false
            errorText.isVisible = false
            refreshButton.isVisible = false
            history.isVisible = false
            historyHeader.isVisible = false
            clearHistoryButton.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showHistory() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyRecyclerView.isVisible = true
            placeholder.isVisible = false
            errorText.isVisible = false
            refreshButton.isVisible = false
            history.isVisible = true
            historyHeader.isVisible = true
            clearHistoryButton.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showContent(tracksList: List<Track>) {
        trackAdapter.clearTracks()
        trackAdapter.updateTracks(tracksList)
        trackAdapter.notifyDataSetChanged()
        if (tracksList.isEmpty()) {
            showEmpty()
        } else {
            binding.apply {
                placeholder.isVisible = false
                errorText.isVisible = false
                searchRecyclerView.isVisible = true
                historyRecyclerView.isVisible = false
                refreshButton.isVisible = false
                history.isVisible = false
                historyHeader.isVisible = false
                clearHistoryButton.isVisible = false
                progressBar.isVisible = false
            }
        }
    }

    private fun showError(errorCode: Int) {
        if (errorCode in 400..499) {
            binding.placeholder.setImageResource(R.drawable.error_placeholder_nothing_found)
            binding.errorText.text = getString(R.string.server_error)
        } else {
            binding.placeholder.setImageResource(R.drawable.error_placeholder_connection_problem)
            binding.errorText.text = getString(R.string.connection_problem)
            binding.refreshButton.isVisible = true
        }
        binding.apply {
            searchRecyclerView.isVisible = false
            historyRecyclerView.isVisible = false
            placeholder.isVisible = true
            errorText.isVisible = true
            history.isVisible = false
            historyHeader.isVisible = false
            clearHistoryButton.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeholder.setImageResource(R.drawable.error_placeholder_nothing_found)
            errorText.text = getString(R.string.nothing_found)
            searchRecyclerView.isVisible = false
            historyRecyclerView.isVisible = false
            placeholder.isVisible = true
            errorText.isVisible = true
            refreshButton.isVisible = false
            history.isVisible = false
            historyHeader.isVisible = false
            clearHistoryButton.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun render(data: TracksProvider) {
        when (data) {
            is TracksProvider.Data -> showContent(data.tracksList)
            is TracksProvider.Error -> showError(data.code)
            else -> showLoading()
        }
    }
}