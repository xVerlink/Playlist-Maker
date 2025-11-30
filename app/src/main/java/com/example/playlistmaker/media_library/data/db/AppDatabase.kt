package com.example.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}
