package com.example.playlistmaker.media_library.ui.models

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ListItemPlaylistGridBinding
import com.example.playlistmaker.media_library.domain.models.Playlist

class PlaylistGridViewHolder(private val binding: ListItemPlaylistGridBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.cover)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(binding.cover)
        binding.title.text = playlist.title
        val tracksCount = playlist.tracksCount
        binding.count.text = itemView.context.resources.getQuantityString(R.plurals.numberOfSongs, tracksCount, tracksCount)
    }
}