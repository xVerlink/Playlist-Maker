package com.example.playlistmaker.search.domain.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(val trackId: String,
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
                 var isFavorite: Boolean = false): Serializable {
    fun getCover512(): String = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

}