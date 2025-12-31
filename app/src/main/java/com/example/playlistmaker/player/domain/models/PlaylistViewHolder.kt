package com.example.playlistmaker.player.domain.models

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ListItemPlaylistBinding
import com.example.playlistmaker.media_library.domain.models.Playlist

class PlaylistViewHolder(private val binding: ListItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind (playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.cover)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(binding.cover)
        binding.playlistTitle.text = playlist.title
        val tracksCount = "${playlist.tracksCount} треков"
        binding.tracksCount.text = tracksCount
    }
}