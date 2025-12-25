package com.example.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}
