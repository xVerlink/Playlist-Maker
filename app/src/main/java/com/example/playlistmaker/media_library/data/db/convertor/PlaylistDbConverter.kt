package com.example.playlistmaker.media_library.data.db.convertor

import android.util.Log
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val gson: Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        val idList = playlist.trackIdList
        val idListJson = gson.toJson(idList, object : TypeToken<List<String>>() {}.type)
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            cover = playlist.cover,
            trackIdListJson = idListJson,
            tracksCount = idList.size
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        Log.d("ASD", "json = ${playlist.trackIdListJson}")
        Log.d("ASD", "isNull = ${playlist.trackIdListJson == null}")
        Log.d("ASD", "isEmpty = ${playlist.trackIdListJson.isEmpty()}")

        val idList = if (playlist.trackIdListJson.isEmpty()) {
            mutableListOf<String>()
        } else {
            gson.fromJson<List<String>>(playlist.trackIdListJson, object : TypeToken<List<String>>() {}.type)
        }
        return Playlist(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            cover = playlist.cover,
            trackIdList = idList,
            tracksCount = idList.size
        )
    }
}