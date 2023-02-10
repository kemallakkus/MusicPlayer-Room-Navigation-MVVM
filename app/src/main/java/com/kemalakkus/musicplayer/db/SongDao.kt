package com.kemalakkus.musicplayer.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kemalakkus.musicplayer.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(song: Song)

    @Delete
    suspend fun delete(song: Song)

    @Query("SELECT * FROM songTable")
    fun getAllSong() : LiveData<List<Song>>

}