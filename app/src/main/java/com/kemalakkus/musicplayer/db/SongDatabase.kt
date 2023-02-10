package com.kemalakkus.musicplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kemalakkus.musicplayer.model.Song

@Database(entities = [Song::class], version = 1,exportSchema = false)

abstract class SongDatabase: RoomDatabase() {

    abstract fun getAllSongDao(): SongDao

    companion object{

        @Volatile
        private var instance: SongDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SongDatabase::class.java,
            "song_db"
        ).build()



    }

}