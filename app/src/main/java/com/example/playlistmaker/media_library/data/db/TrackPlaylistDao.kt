package com.example.playlistmaker.media_library.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.media_library.data.db.entity.TrackPlaylistEntity

@Dao
interface TrackPlaylistDao {
    @Insert(entity = TrackPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TrackPlaylistEntity)
}