package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SAVED_STRING: String = "SAVED_STRING"
    }
    private var input: String? = ""
    private val appleMusicBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(appleMusicBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val appleMusicService = retrofit.create(TracksApi::class.java)

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    lateinit var tracks: MutableList<Track>
    lateinit var historyTracks: MutableList<Track>
    lateinit var trackAdapter: TrackAdapter
    lateinit var searchHistoryAdapter: TrackAdapter

    lateinit var returnBackButton: ImageView
    lateinit var inputEditText: EditText
    lateinit var clearButton: ImageView
    lateinit var placeholder: ImageView
    lateinit var errorText: TextView
    lateinit var refreshButton: Button
    lateinit var searchHistoryLayout: LinearLayout
    lateinit var clearHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPrefs = getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)

        tracks = mutableListOf()
        historyTracks = searchHistory.read(sharedPrefs.getString(SearchHistory.SEARCH_HISTORY_KEY, ""))
        Log.d("trackList", "tracklist updated")

        listener = SharedPreferences.OnSharedPreferenceChangeListener {sharedPreferences, key ->
            if (key == SearchHistory.SEARCH_HISTORY_KEY) {
                historyTracks.clear()
                historyTracks.addAll(searchHistory.read(sharedPrefs.getString(SearchHistory.SEARCH_HISTORY_KEY, "")))
                searchHistoryAdapter.notifyDataSetChanged()
                Log.d("SearchActivity", "отработал листенер на изменение состояния хистори листа")
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)


        trackAdapter = TrackAdapter(tracks, searchHistory)
        searchHistoryAdapter = TrackAdapter(historyTracks, searchHistory)

        val recyclerSearchResults = findViewById<RecyclerView>(R.id.search_screen_recycler_view)
        recyclerSearchResults.adapter = trackAdapter

        val recyclerHistoryResults = findViewById<RecyclerView>(R.id.search_screen_recycler_search_history)
        recyclerHistoryResults.adapter = searchHistoryAdapter

        returnBackButton = findViewById(R.id.search_screen_return_button)
        inputEditText = findViewById(R.id.edit_text_search)
        clearButton = findViewById(R.id.clear_text_icon)
        placeholder = findViewById(R.id.search_screen_error_placeholder)
        errorText = findViewById(R.id.search_screen_error_text)
        refreshButton = findViewById(R.id.search_screen_refresh_button)
        searchHistoryLayout = findViewById(R.id.search_screen_history)
        clearHistory = findViewById(R.id.search_screen_clear_history)

        clearHistory.setOnClickListener {
            sharedPrefs.edit() { remove(SearchHistory.SEARCH_HISTORY_KEY) }
            historyTracks.clear()
            searchHistoryLayout.visibility = View.GONE
        }

        returnBackButton.setOnClickListener {
            finish()
        }

        inputEditText.setText(input)

        inputEditText.setOnClickListener {
            inputEditText.requestFocus()
        }
        
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            //searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryLayout.visibility = if (hasFocus && inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholder.visibility = View.GONE
            errorText.visibility = View.GONE
            refreshButton.visibility = View.GONE
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

        inputEditText.addTextChangedListener(
            {text: CharSequence?, start: Int, count: Int, after: Int ->  },
            {text: CharSequence?, start: Int, before: Int, count: Int ->
                if (text.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
                //searchHistoryAdapter.notifyDataSetChanged()
                searchHistoryLayout.visibility = if (inputEditText.hasFocus() && text?.isEmpty() == true) View.VISIBLE else View.GONE
            },
            {text: Editable? ->  input = text.toString()}
        )
    }

    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            appleMusicService.getSongs(inputEditText.text.toString()).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.isSuccessful) {
                        tracks.clear()
                        val results = response.body()?.results
                        if (results?.isNotEmpty() == true) {
                            tracks.addAll(results)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                        } else {
                            showMessage("", "")
                        }
                    } else {
                        showMessage(resources.getString(R.string.server_error), response.code().toString())
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(getString(R.string.connection_problem), t.message.toString())
                }

            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholder.visibility = View.VISIBLE
            errorText.visibility = View.VISIBLE
            errorText.text = text
            if (additionalMessage.isNotEmpty()) {
                placeholder.setImageResource(R.drawable.error_placeholder_connection_problem)
                refreshButton.visibility = View.VISIBLE
            } else {
                placeholder.setImageResource(R.drawable.error_placeholder_nothing_found)
            }
        } else {
            placeholder.visibility = View.GONE
            errorText.visibility = View.GONE
            refreshButton.visibility = View.GONE
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
}