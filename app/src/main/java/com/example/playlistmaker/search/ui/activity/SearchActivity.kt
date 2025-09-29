package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.view.isVisible
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.player.ui.activity.TRACK_KEY
import com.example.playlistmaker.search.ui.models.TrackAdapter
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksProvider
import com.example.playlistmaker.search.ui.view_model.SearchActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private var input: String? = ""

    private val viewModel by viewModel<SearchActivityViewModel>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.observeHistory().observe(this) {
            searchHistoryAdapter.updateTracks(it)
        }

        viewModel.registerHistoryChangeListener { data ->
            searchHistoryAdapter.clearTracks()
            if (data.isNullOrEmpty()) {
                searchHistoryAdapter.updateTracks(mutableListOf())
            } else {
                searchHistoryAdapter.updateTracks(data)
            }
            searchHistoryAdapter.notifyDataSetChanged()
        }

        trackAdapter = TrackAdapter() { track: Track ->
            viewModel.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }

        searchHistoryAdapter = TrackAdapter() { track: Track ->
            viewModel.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
        viewModel.updateHistory(App.SEARCH_HISTORY_KEY)
        binding.searchRecyclerView.adapter = trackAdapter
        binding.historyRecyclerView.adapter = searchHistoryAdapter

        binding.toolbar.setNavigationOnClickListener {
            finish()
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
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            searchHistoryAdapter.clearTracks()
            searchHistoryAdapter.notifyDataSetChanged()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_STRING, input)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input = savedInstanceState.getString(SAVED_STRING)
    }

    companion object {
        private const val SAVED_STRING: String = "SAVED_STRING"
    }
}
