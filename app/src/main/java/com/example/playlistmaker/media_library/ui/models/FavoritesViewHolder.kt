package com.example.playlistmaker.media_library.ui.models

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ListItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track

class FavoritesViewHolder(private val binding: ListItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        val roundRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, itemView.context.resources.displayMetrics).toInt()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundRadius))
            .into(binding.albumCover)
        binding.viewHolderTrackName.text = track.trackName
        binding.viewHolderArtistName.text = track.artistName
        binding.trackLength.text = track.trackTime
    }
}