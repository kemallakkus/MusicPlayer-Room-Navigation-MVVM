package com.kemalakkus.musicplayer.fragments

import android.content.ClipData.Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.kemalakkus.musicplayer.MainActivity
import com.kemalakkus.musicplayer.R
import com.kemalakkus.musicplayer.adapter.SongAdapter
import com.kemalakkus.musicplayer.databinding.FragmentMusicListBinding
import com.kemalakkus.musicplayer.databinding.FragmentPlaylistBinding
import com.kemalakkus.musicplayer.model.Song
import com.kemalakkus.musicplayer.viewmodel.SongViewModel
import java.util.ArrayList

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    var songList: MutableList<Song> = ArrayList()
    private lateinit var playlistAdapter: SongAdapter
    private lateinit var viewModel: SongViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(
            inflater,container,false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel


        setUpRecyclerView()
        observeSong()
        onItemClick()
        swipeToDelete(view)
    }

    private fun setUpRecyclerView() {
        playlistAdapter = SongAdapter()
        binding.rvPlaylist.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = playlistAdapter
            addItemDecoration(object : DividerItemDecoration(
                activity, LinearLayout.VERTICAL
            ) {})
        }
        playlistAdapter.differ.submitList(songList)
        songList.clear()
    }

    private fun observeSong(){

        viewModel.playlistLiveData.observe(viewLifecycleOwner, Observer { playlist ->
            playlist?.let {
                playlistAdapter.differ.submitList(it)
            }

        })

    }

    private fun onItemClick(){
        playlistAdapter.onItemClick = {
            val bundle = Bundle().apply {
            putParcelable("song",it)
            }
            findNavController().navigate(R.id.action_playlistFragment_to_playMusicFragment,bundle)
        }
    }

    private fun swipeToDelete(view: View){

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val music = playlistAdapter.differ.currentList.get(position)
                viewModel.deleteSong(music)
                Snackbar.make(view, "Delete song successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insertSong(music)
                    }
                    show()
                }

            }

        }
            ItemTouchHelper(itemTouchHelperCallBack).apply {
                attachToRecyclerView(binding.rvPlaylist)
            }
    }

}