package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.ui.models.TrackAdapter
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksProvider
import com.example.playlistmaker.search.ui.view_model.SearchActivityViewModel
import com.google.android.material.appbar.MaterialToolbar


const val TRACK_KEY = "TRACK"

class SearchActivity : AppCompatActivity() {

    private var input: String? = ""

    private var viewModel: SearchActivityViewModel? = null
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var recyclerSearchResults: RecyclerView
    private lateinit var recyclerHistoryResults: RecyclerView

    private lateinit var toolbar: MaterialToolbar
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeholder: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var historyHeader: TextView
    private lateinit var clearHistory: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.searchScreenToolbar)
        inputEditText = findViewById(R.id.edit_text_search)
        clearButton = findViewById(R.id.clear_text_icon)
        placeholder = findViewById(R.id.search_screen_error_placeholder)
        errorText = findViewById(R.id.search_screen_error_text)
        refreshButton = findViewById(R.id.search_screen_refresh_button)
        searchHistoryLayout = findViewById(R.id.search_screen_history)
        historyHeader = findViewById(R.id.search_history_header)
        clearHistory = findViewById(R.id.search_screen_clear_history)
        progressBar = findViewById(R.id.progressBar)
        recyclerSearchResults = findViewById(R.id.search_screen_recycler_view)
        recyclerHistoryResults = findViewById(R.id.search_screen_recycler_search_history)

        viewModel = ViewModelProvider(this, SearchActivityViewModel.getFactory()).get(SearchActivityViewModel::class.java)
        viewModel?.observeState()?.observe(this) {
            render(it)
        }
        viewModel?.observeHistory()?.observe(this) {
            searchHistoryAdapter.updateTracks(it)
        }

        viewModel?.registerHistoryChangeListener { data ->
            searchHistoryAdapter.clearTracks()
            if (data.isNullOrEmpty()) {
                searchHistoryAdapter.updateTracks(mutableListOf())
            } else {
                searchHistoryAdapter.updateTracks(data)
            }
            searchHistoryAdapter.notifyDataSetChanged()
        }

        trackAdapter = TrackAdapter() { track: Track ->
            viewModel?.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }

        searchHistoryAdapter = TrackAdapter() { track: Track ->
            viewModel?.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
        viewModel?.updateHistory(App.SEARCH_HISTORY_KEY)
        recyclerSearchResults.adapter = trackAdapter
        recyclerHistoryResults.adapter = searchHistoryAdapter

        toolbar.setNavigationOnClickListener {
            finish()
        }

        inputEditText.setText(input)

        inputEditText.setOnClickListener {
            inputEditText.requestFocus()
        }

        inputEditText.addTextChangedListener(
            { text: CharSequence?, start: Int, count: Int, after: Int -> },
            { text: CharSequence?, start: Int, before: Int, count: Int ->
                clearButton.isVisible = !text.isNullOrEmpty()
                if (inputEditText.hasFocus() && text?.isEmpty() == true && searchHistoryAdapter.tracksIsNotEmpty()) {
                    showHistory()
                }
                viewModel?.searchDebounce(text.toString())
            },
            { text: Editable? -> input = text.toString() }
        )

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && searchHistoryAdapter.tracksIsNotEmpty()) {
                showHistory()
            }
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        clearHistory.setOnClickListener {
            viewModel?.clearHistory()
            searchHistoryAdapter.clearTracks()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
        }

        refreshButton.setOnClickListener {
            viewModel?.searchDebounce(input!!) //Не забыть посмотреть
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel?.searchDebounce(input!!)
            }
            false
        }
    }

    private fun showLoading() {
        recyclerSearchResults.isVisible = false
        recyclerHistoryResults.isVisible = false
        placeholder.isVisible = false
        errorText.isVisible = false
        refreshButton.isVisible = false
        searchHistoryLayout.isVisible = false
        historyHeader.isVisible = false
        clearHistory.isVisible = false
        progressBar.isVisible = true
    }

    private fun showHistory() {
        recyclerSearchResults.isVisible = false
        recyclerHistoryResults.isVisible = true
        placeholder.isVisible = false
        errorText.isVisible = false
        refreshButton.isVisible = false
        searchHistoryLayout.isVisible = true
        historyHeader.isVisible = true
        clearHistory.isVisible = true
        progressBar.isVisible = false
    }

    private fun showContent(tracksList: List<Track>) {
        trackAdapter.clearTracks()
        trackAdapter.updateTracks(tracksList)
        trackAdapter.notifyDataSetChanged()
        if (tracksList.isEmpty()) {
            showEmpty()
        } else {
            placeholder.isVisible = false
            errorText.isVisible = false
            recyclerSearchResults.isVisible = true
            recyclerHistoryResults.isVisible = false
            refreshButton.isVisible = false
            searchHistoryLayout.isVisible = false
            historyHeader.isVisible = false
            clearHistory.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showError(errorCode: Int) {
        if (errorCode == 400) {
            placeholder.setImageResource(R.drawable.error_placeholder_nothing_found)
            errorText.text = getString(R.string.server_error)
        } else {
            placeholder.setImageResource(R.drawable.error_placeholder_connection_problem)
            errorText.text = getString(R.string.connection_problem)
            refreshButton.isVisible = true
        }

        recyclerSearchResults.isVisible = false
        recyclerHistoryResults.isVisible = false
        placeholder.isVisible = true
        errorText.isVisible = true
        searchHistoryLayout.isVisible = false
        historyHeader.isVisible = false
        clearHistory.isVisible = false
        progressBar.isVisible = false
    }

    private fun showEmpty() {
        placeholder.setImageResource(R.drawable.error_placeholder_nothing_found)
        errorText.text = getString(R.string.nothing_found)
        recyclerSearchResults.isVisible = false
        recyclerHistoryResults.isVisible = false
        placeholder.isVisible = true
        errorText.isVisible = true
        refreshButton.isVisible = false
        searchHistoryLayout.isVisible = false
        historyHeader.isVisible = false
        clearHistory.isVisible = false
        progressBar.isVisible = false
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
