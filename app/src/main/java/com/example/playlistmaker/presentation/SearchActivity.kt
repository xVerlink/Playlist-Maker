package com.example.playlistmaker.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.content.edit
import androidx.core.view.isVisible
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repository.SearchHistory
import com.example.playlistmaker.data.network.TracksApi
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksProvider
import com.google.android.material.appbar.MaterialToolbar


const val TRACK_KEY = "TRACK"

class SearchActivity : AppCompatActivity() {

    private var input: String? = ""

    private val interactor = Creator.getTracksInteractor()
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private lateinit var handler: Handler
    private lateinit var searchRunnable: Runnable

    private lateinit var tracks: MutableList<Track>
    private lateinit var historyTracks: MutableList<Track>
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory
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
        handler = Handler(Looper.getMainLooper())
        searchRunnable = Runnable { search() }

        recyclerSearchResults = findViewById(R.id.search_screen_recycler_view)
        recyclerHistoryResults = findViewById(R.id.search_screen_recycler_search_history)

        val sharedPrefs = getSharedPreferences(App.Companion.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)

        tracks = mutableListOf()
        historyTracks = searchHistory.read(sharedPrefs.getString(SearchHistory.SEARCH_HISTORY_KEY, ""))

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == SearchHistory.SEARCH_HISTORY_KEY) {
                historyTracks.clear()
                historyTracks.addAll(searchHistory.read(sharedPrefs.getString(SearchHistory.SEARCH_HISTORY_KEY, "")))
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)

        trackAdapter = TrackAdapter(tracks) { track: Track ->
            searchHistory.add(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }

        searchHistoryAdapter = TrackAdapter(historyTracks) { track: Track ->
            searchHistory.add(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
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
                recyclerSearchResults.isVisible = false
                placeholder.isVisible = false
                errorText.isVisible = false
                if (inputEditText.hasFocus() && text?.isEmpty() == true && historyTracks.isNotEmpty()) {
                    searchHistoryLayout.isVisible = true
                    historyHeader.isVisible = true
                    clearHistory.isVisible = true
                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                } else {
                    searchHistoryLayout.isVisible = false
                    historyHeader.isVisible = false
                    clearHistory.isVisible = false
                }
                searchDebounce()
            },
            { text: Editable? -> input = text.toString() }
        )

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && historyTracks.isNotEmpty()) {
                searchHistoryLayout.isVisible = true
                historyHeader.isVisible = true
                clearHistory.isVisible = true
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
            } else {
                searchHistoryLayout.isVisible = false
                historyHeader.isVisible = false
                clearHistory.isVisible = false
            }
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholder.isVisible = false
            errorText.isVisible = false
            refreshButton.isVisible = false
        }

        clearHistory.setOnClickListener {
            sharedPrefs.edit { remove(SearchHistory.SEARCH_HISTORY_KEY) }
            historyTracks.clear()
            searchHistoryLayout.isVisible = false
        }

        refreshButton.setOnClickListener {
            search()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun search() {
        progressBar.isVisible = !inputEditText.text.isNullOrEmpty()
        if (inputEditText.text.isNotEmpty()) {
            interactor.searchTracks(
                inputEditText.text.toString(),
                object : TracksInteractor.TracksConsumer {

                    override fun consume(data: TracksProvider<List<Track>>) {
                        handler.post {
                            progressBar.isVisible = false
                            recyclerSearchResults.isVisible = true
                            tracks.clear()

                            when (data) {
                                is TracksProvider.Data -> {
                                    tracks.addAll(data.tracksList)
                                    trackAdapter.notifyDataSetChanged()
                                    if (tracks.isEmpty()) {
                                        showMessage(getString(R.string.nothing_found), "")
                                    } else {
                                        showMessage("", "")
                                    }
                                }

                                is TracksProvider.Error -> {
                                    if (data.code == 400) {
                                        showMessage(
                                            getString(R.string.server_error),
                                            data.code.toString()
                                        )
                                    } else {
                                        showMessage(
                                            getString(R.string.connection_problem),
                                            data.code.toString()
                                        )
                                    }
                                }
                            }
                        }

                    }
                })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholder.isVisible = true
            errorText.isVisible = true
            errorText.text = text
            if (additionalMessage.isNotEmpty()) {
                placeholder.setImageResource(R.drawable.error_placeholder_connection_problem)
                refreshButton.isVisible = true
            } else {
                placeholder.setImageResource(R.drawable.error_placeholder_nothing_found)
            }
        } else {
            placeholder.isVisible = false
            errorText.isVisible = false
            refreshButton.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
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
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
