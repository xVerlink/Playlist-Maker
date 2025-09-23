package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerActiityViewModel
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


const val TRACK_KEY = "TRACK"

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private lateinit var track: Track
    private var viewModel: PlayerActiityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(TRACK_KEY) as Track

        viewModel = ViewModelProvider(this, PlayerActiityViewModel.getFactory(track.previewUrl))
            .get(PlayerActiityViewModel::class.java)
        viewModel?.preparePlayer()
        viewModel?.observePlayerState()?.observe(this) {
            when(it) {
                is PlayerState.Playing -> binding.playButton.setImageResource(R.drawable.pause_button)
                is PlayerState.Paused, PlayerState.Prepared -> binding.playButton.setImageResource(R.drawable.play_button)
                else -> Toast.makeText(this, "Exception while preparing player or wait a few seconds", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel?.observeTimer()?.observe(this) {
            binding.trackTimeProgress.text = it
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
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

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }
}