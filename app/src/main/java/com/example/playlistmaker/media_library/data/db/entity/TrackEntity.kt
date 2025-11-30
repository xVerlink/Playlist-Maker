package com.example.playlistmaker.media_library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    @SerializedName("trackTimeMillis")
    val trackTime: String,
    val artworkUrl100: String,
    val isFavorite: Boolean) {
}