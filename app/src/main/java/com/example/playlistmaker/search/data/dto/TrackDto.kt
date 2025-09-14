package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TrackDto (val trackId: String,
                     val trackName: String,
                     val artistName: String,
                     val collectionName: String,
                     val releaseDate: String,
                     val primaryGenreName: String,
                     val country: String,
                     val previewUrl: String,
                     @SerializedName("trackTimeMillis") val trackTime: String,
                     val artworkUrl100: String): Serializable {

}