package com.example.playlistmaker

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
     private lateinit var returnButton: ImageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("qweasd", "here")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        returnButton = findViewById(R.id.playerScreenReturnButton)
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


        returnButton.setOnClickListener {
            finish()
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
        val roundRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
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
        currentTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTime.toLong())
    }

    private fun setDuration() {
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTime.toLong())
        if (trackDuration.text.isNullOrEmpty()) {
            durationGroup.isVisible = false
        } else {
            durationGroup.isVisible = true
        }
    }

    private fun setAlbum() {
        album.text = track.collectionName
        if (album.text.isNullOrEmpty()) {
            albumGroup.isVisible = false
        } else {
            albumGroup.isVisible = true
        }
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
        if (genre.text.isNullOrEmpty()) {
            genreGroup.isVisible = false
        } else {
            genreGroup.isVisible = true
        }
    }

    private fun setCountry() {
        country.text = track.country
        if (country.text.isNullOrEmpty()) {
            countryGroup.isVisible = false
        } else {
            countryGroup.isVisible = true
        }
    }

}