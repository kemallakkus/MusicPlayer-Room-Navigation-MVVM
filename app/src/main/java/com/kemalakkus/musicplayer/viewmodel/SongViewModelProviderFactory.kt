package com.kemalakkus.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kemalakkus.musicplayer.repository.SongRepository

class SongViewModelProviderFactory (val app: Application, private val songRepository: SongRepository):
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongViewModel(app, songRepository) as T
    }
}
