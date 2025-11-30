package com.example.playlistmaker.search.ui.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ListItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(private val action: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {
    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackBinding.inflate(inflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], action)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateTracks(incomingTracks: List<Track>) {
        tracks.addAll(incomingTracks)
    }

    fun tracksIsNotEmpty(): Boolean {
        return tracks.isNotEmpty()
    }

    fun clearTracks() {
        tracks.clear()
    }
}