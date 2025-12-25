package com.example.playlistmaker.media_library.ui.models

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ListItemPlaylistBinding
import com.example.playlistmaker.media_library.domain.models.Playlist

class PlaylistViewHolder(private val binding: ListItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        val roundRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, itemView.context.resources.displayMetrics).toInt()
        Glide.with(itemView)
            .load(playlist.cover)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            //.transform(RoundedCorners(roundRadius))
            .into(binding.cover)
        binding.title.text = playlist.title
        binding.count.text = playlist.tracksCount.toString()
    }
}