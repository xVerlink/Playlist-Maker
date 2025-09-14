package com.example.playlistmaker.search.ui.models

import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val albumCover: ImageView = itemView.findViewById(R.id.search_screen_album_cover)
    private val trackName: TextView = itemView.findViewById(R.id.search_screen_track_name)
    private val artistName: TextView = itemView.findViewById(R.id.search_screen_artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.search_screen_track_length)
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun bind(model: Track, action: (Track) -> Unit) {
        itemView.setOnClickListener {
                if (clickDebounce()) {
                    action.invoke(model)
                }
        }

        val roundRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, itemView.context.resources.displayMetrics).toInt()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundRadius))
            .into(albumCover)
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}