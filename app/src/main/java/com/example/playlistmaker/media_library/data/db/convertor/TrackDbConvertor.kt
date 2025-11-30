package com.example.playlistmaker.media_library.data.db.convertor

import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConvertor {
    fun map (track: Track): TrackEntity {
        return TrackEntity(
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

    fun map (track: TrackEntity): Track {
        return Track(
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
}