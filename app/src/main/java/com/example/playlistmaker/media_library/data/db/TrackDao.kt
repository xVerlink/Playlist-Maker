package com.example.playlistmaker.media_library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
     suspend fun addToFavorites(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
     suspend fun removeFromFavorites(track: TrackEntity)

    @Query("SELECT * FROM track_table")
     suspend fun getTracks(): List<TrackEntity>

     @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<String>
}