package com.kemalakkus.musicplayer.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kemalakkus.musicplayer.R
import com.kemalakkus.musicplayer.adapter.SongAdapter
import com.kemalakkus.musicplayer.databinding.FragmentMusicListBinding
import com.kemalakkus.musicplayer.helper.Constants
import com.kemalakkus.musicplayer.helper.Constants.toast
import com.kemalakkus.musicplayer.model.Song
import java.util.*

class MusicListFragment : Fragment(R.layout.fragment_music_list) {

    private var _binding: FragmentMusicListBinding? = null
    private val binding get() = _binding!!
    var songList: MutableList<Song> = ArrayList()
    private lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setActionBar(binding.toolbar)
        loadSong()
        setUpRecyclerView()
        checkUserPermissions()
        onItemClick()
    }

    private fun onItemClick(){
        songAdapter.onItemClick ={
            val bundle = Bundle().apply {
                putParcelable("song",it)
            }
            findNavController().navigate(R.id.action_musicListFragment_to_playMusicFragment,bundle)
        }
    }

    @SuppressLint("Range")
    private fun loadSong() {
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val sortOrder = " ${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = activity?.applicationContext?.contentResolver!!.query(
            allSongsURI, null, selection, null, sortOrder
        )

        if (cursor != null) {

            while (cursor.moveToNext()) {
                val songURI =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val songAuthor =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val songName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val songDuration =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val songDurLong = songDuration.toLong()
                songList.add(
                    Song(
                        id = 0,songName, songAuthor,
                        songURI, Constants.durationConverter(songDurLong)
                    )
                )
            }
            cursor.close()
        }
    }

    private fun setUpRecyclerView() {
        songAdapter = SongAdapter()
        binding.rvSongList.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = songAdapter
            addItemDecoration(object : DividerItemDecoration(
                activity, LinearLayout.VERTICAL
            ) {})
        }
        songAdapter.differ.submitList(songList)
        songList.clear()
    }

    private fun checkUserPermissions() {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.REQUEST_CODE_ASK_PERMISSIONS
            )
            return
        }
        loadSong()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSong()
            } else {
                activity?.toast("Permission Denied, Add permission!!")
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}