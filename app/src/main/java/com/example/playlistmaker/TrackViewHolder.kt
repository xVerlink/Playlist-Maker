package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val albumCover: ImageView = itemView.findViewById(R.id.search_screen_album_cover)
    private val trackName: TextView = itemView.findViewById(R.id.search_screen_track_name)
    private val artistName: TextView = itemView.findViewById(R.id.search_screen_artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.search_screen_track_length)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(albumCover)
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }
}