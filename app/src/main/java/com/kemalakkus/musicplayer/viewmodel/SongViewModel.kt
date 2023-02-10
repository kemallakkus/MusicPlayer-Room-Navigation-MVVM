package com.kemalakkus.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kemalakkus.musicplayer.model.Song
import com.kemalakkus.musicplayer.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SongViewModel(application: Application, private val songRepository: SongRepository): AndroidViewModel(application) {


    val playlistLiveData = songRepository.getSaveSong()

    fun deleteSong(song: Song) = viewModelScope.launch {
        songRepository.deleteSong(song)
    }

    fun insertSong(song: Song) = viewModelScope.launch {
        songRepository.insertSong(song)
    }


}