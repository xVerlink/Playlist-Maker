package com.example.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val albumCover: ImageView = itemView.findViewById(R.id.search_screen_album_cover)
    private val trackName: TextView = itemView.findViewById(R.id.search_screen_track_name)
    private val artistName: TextView = itemView.findViewById(R.id.search_screen_artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.search_screen_track_length)

    fun bind(model: Track, searchHistory: SearchHistory) {
        itemView.setOnClickListener {
            searchHistory.add(model)
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
        if (!model.trackTime.isNullOrEmpty()) {
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime.toLong())
        }
    }
}