package com.kemalakkus.musicplayer.repository

import com.kemalakkus.musicplayer.db.SongDatabase
import com.kemalakkus.musicplayer.model.Song



class SongRepository(private val db : SongDatabase) {

    //val db = SongDatabase(application)

    suspend fun insertSong(song: Song){
        db.getAllSongDao().upsert(song)
    }
    suspend fun deleteSong(song: Song) {
        db.getAllSongDao().delete(song)
    }
    fun getSaveSong() = db.getAllSongDao().getAllSong()

}