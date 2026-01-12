package com.example.playlistmaker.media_library.data.db.convertor

import com.example.playlistmaker.media_library.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackPlaylistDbConverter {
    fun map (track: Track) : TrackPlaylistEntity {
        return TrackPlaylistEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            isFavorite = track.isFavorite
        )
    }

    fun map (trackPlaylistEntity: TrackPlaylistEntity) : Track {
        return Track(
            trackId = trackPlaylistEntity.trackId,
            trackName = trackPlaylistEntity.trackName,
            artistName = trackPlaylistEntity.artistName,
            collectionName = trackPlaylistEntity.collectionName,
            releaseDate = trackPlaylistEntity.releaseDate,
            primaryGenreName = trackPlaylistEntity.primaryGenreName,
            country = trackPlaylistEntity.country,
            previewUrl = trackPlaylistEntity.previewUrl,
            trackTime = trackPlaylistEntity.trackTime,
            artworkUrl100 = trackPlaylistEntity.artworkUrl100,
            isFavorite = trackPlaylistEntity.isFavorite
        )
    }
}