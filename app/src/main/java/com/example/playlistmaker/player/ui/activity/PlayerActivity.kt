package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerActiityViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.activity.TRACK_KEY
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var cover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artist: TextView
    private lateinit var addToPlaylistButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var addToFavoritesButton: ImageButton
    private lateinit var currentTrackTime: TextView
    private lateinit var trackDuration: TextView
    private lateinit var durationGroup: Group
    private lateinit var album: TextView
    private lateinit var albumGroup: Group
    private lateinit var year: TextView
    private lateinit var yearGroup: Group
    private lateinit var genre: TextView
    private lateinit var genreGroup: Group
    private lateinit var country: TextView
    private lateinit var countryGroup: Group
    private lateinit var track: Track
    private var viewModel: PlayerActiityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        toolbar = findViewById(R.id.playerScreenToolbar)
        cover = findViewById(R.id.playerScreenCover)
        trackName = findViewById(R.id.playerScreenTrackName)
        artist = findViewById(R.id.playerScreenArtistName)
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton)
        playButton = findViewById(R.id.playerScreenPlayButton)
        addToFavoritesButton = findViewById(R.id.addToFavoritesButton)
        currentTrackTime = findViewById(R.id.playerScreenTrackTime)
        trackDuration = findViewById(R.id.playerScreenDuration)
        durationGroup = findViewById(R.id.durationGroup)
        album = findViewById(R.id.playerScreenAlbum)
        albumGroup = findViewById(R.id.albumGroup)
        year = findViewById(R.id.playerScreenYear)
        yearGroup = findViewById(R.id.yearGroup)
        genre = findViewById(R.id.playerScreenGenre)
        genreGroup = findViewById(R.id.genreGroup)
        country = findViewById(R.id.playerScreenCountry)
        countryGroup = findViewById(R.id.countryGroup)
        track = intent.getSerializableExtra(TRACK_KEY) as Track

        viewModel = ViewModelProvider(this, PlayerActiityViewModel.getFactory(track.previewUrl))
            .get(PlayerActiityViewModel::class.java)
        viewModel?.preparePlayer()
        viewModel?.observePlayerState()?.observe(this) {
            when(it) {
                is PlayerState.Playing -> playButton.setImageResource(R.drawable.pause_button)
                is PlayerState.Paused, PlayerState.Prepared -> playButton.setImageResource(R.drawable.play_button)
                else -> Toast.makeText(this, "Exception while preparing player or wait a few seconds", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel?.observeTimer()?.observe(this) {
            currentTrackTime.text = it
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            if (track.previewUrl.isNotEmpty()) {
                viewModel?.playbackControl()
            } else {
                Toast.makeText(this, "There is no track preview", Toast.LENGTH_SHORT).show()
            }
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

    private fun setCover() {
        val roundRadius =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        Glide.with(this)
            .load(track.getCover512())
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundRadius))
            .into(cover)
    }

    private fun setTrackName() {
        trackName.text = track.trackName
    }

    private fun setArtist() {
        artist.text = track.artistName
    }

    private fun setCurrentDuration() {
        currentTrackTime.text = resources.getString(R.string.media_player_default_time)
    }

    private fun setDuration() {
        trackDuration.text = track.trackTime
        durationGroup.isVisible = !trackDuration.text.isNullOrEmpty()
    }

    private fun setAlbum() {
        album.text = track.collectionName
        albumGroup.isVisible = !album.text.isNullOrEmpty()
    }

    private fun setYear() {
        if (!track.releaseDate.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = inputFormat.parse(track.releaseDate)
            val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            year.text = yearFormat.format(date!!)
            yearGroup.isVisible = true
        } else {
            yearGroup.isVisible = false
        }
    }

    private fun setGenre() {
        genre.text = track.primaryGenreName
        genreGroup.isVisible = !genre.text.isNullOrEmpty()
    }

    private fun setCountry() {
        country.text = track.country
        countryGroup.isVisible = !country.text.isNullOrEmpty()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }
}