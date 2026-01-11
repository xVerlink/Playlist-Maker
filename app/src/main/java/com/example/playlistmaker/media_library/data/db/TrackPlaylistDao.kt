package com.example.playlistmaker.media_library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackPlaylistDao {
    @Insert(entity = TrackPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TrackPlaylistEntity)

    @Query("SELECT * FROM track_playlist_table")
    fun getTracks(): Flow<List<TrackPlaylistEntity>>

    @Query("SELECT * FROM track_playlist_table")
    suspend fun getTracksOnce(): List<TrackPlaylistEntity>

    @Delete(entity = TrackPlaylistEntity::class)
    suspend fun removeTrack(track: TrackPlaylistEntity)
}