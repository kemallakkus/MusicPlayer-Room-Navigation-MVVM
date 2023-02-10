package com.kemalakkus.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kemalakkus.musicplayer.databinding.ActivityMainBinding
import com.kemalakkus.musicplayer.db.SongDatabase
import com.kemalakkus.musicplayer.repository.SongRepository
import com.kemalakkus.musicplayer.viewmodel.SongViewModel
import com.kemalakkus.musicplayer.viewmodel.SongViewModelProviderFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: SongViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navController= Navigation.findNavController(this,R.id.fragmentContainer)
        //setupWithNavController(binding.bottomNavigationView,navController)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView,navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.musicListFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.playlistFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.playMusicFragment -> binding.bottomNavigationView.visibility = View.GONE
            }
        }



        setupViewModel()

    }

    private fun setupViewModel(){

        val songRepository = SongRepository(SongDatabase(this))

        val viewModelProviderFactory = SongViewModelProviderFactory(application, songRepository)

        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(SongViewModel::class.java)

    }
}