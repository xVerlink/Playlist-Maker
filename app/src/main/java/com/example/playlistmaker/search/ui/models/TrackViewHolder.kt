package com.example.playlistmaker.search.ui.models

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackItemSearchBinding
import com.example.playlistmaker.search.domain.models.Track

class TrackViewHolder(private val binding: ActivityTrackItemSearchBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track, action: (Track) -> Unit) {
        itemView.setOnClickListener {
            action.invoke(model)
        }

        val roundRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, itemView.context.resources.displayMetrics).toInt()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundRadius))
            .into(binding.albumCover)
        binding.viewHolderTrackName.text = model.trackName
        binding.viewHolderArtistName.text = model.artistName
        binding.trackLength.text = model.trackTime
    }
}