package com.example.playlistmaker.player.domain.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ListItemPlaylistBinding
import com.example.playlistmaker.media_library.domain.models.Playlist

class PlaylistAdapter(private val action: (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistViewHolder>() {
    private val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlaylistBinding.inflate(inflater, parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            action(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateList(playlistList: List<Playlist>) {
        playlists.clear()
        playlists.addAll(playlistList)
        notifyDataSetChanged()
    }
}