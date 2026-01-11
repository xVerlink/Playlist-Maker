package com.example.playlistmaker.media_library.ui.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ListItemPlaylistGridBinding
import com.example.playlistmaker.media_library.domain.models.Playlist

class PlaylistGridAdapter(private val actionOnClick: (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistGridViewHolder>() {
    private val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlaylistGridBinding.inflate(inflater, parent, false)
        return PlaylistGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistGridViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            actionOnClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateList(playlistList: List<Playlist>) {
        playlists.clear()
        playlists.addAll(playlistList)
        notifyDataSetChanged()
    }
}