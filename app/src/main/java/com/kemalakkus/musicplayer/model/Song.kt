package com.kemalakkus.musicplayer.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "songTable")
@Parcelize
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "songTitle")
    val songTitle: String?,
    @ColumnInfo(name = "songArtist")
    val songArtist: String?,
    @ColumnInfo(name = "songUri")
    val songUri: String?,
    @ColumnInfo(name = "songDuration")
    val songDuration: String?
): Parcelable