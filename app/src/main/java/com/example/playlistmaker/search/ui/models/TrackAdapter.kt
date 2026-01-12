package com.example.playlistmaker.search.ui.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ListItemTrackBinding
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(private val onLongClick: ((Track) -> Unit)? = null, private val onClick: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {
    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackBinding.inflate(inflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], onClick)
        holder.itemView.setOnClickListener {
            onClick.invoke(tracks[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(tracks[position])
            onLongClick != null
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateTracks(incomingTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(incomingTracks)
        notifyDataSetChanged()
    }

    fun tracksIsNotEmpty(): Boolean {
        return tracks.isNotEmpty()
    }

    fun clearTracks() {
        tracks.clear()
        notifyDataSetChanged()
    }
}