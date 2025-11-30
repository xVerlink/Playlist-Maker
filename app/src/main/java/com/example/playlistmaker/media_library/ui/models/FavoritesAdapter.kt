package com.example.playlistmaker.media_library.ui.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ListItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track

class FavoritesAdapter(private val action: (Track) -> Unit) : RecyclerView.Adapter<FavoritesViewHolder>() {
    private val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackBinding.inflate(inflater, parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            action.invoke(tracks[position])
        }
        holder.bind(tracks[position])
    }

    fun updateTracks(tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }
}