package com.kemalakkus.musicplayer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.musicplayer.databinding.SongBinding
import com.kemalakkus.musicplayer.fragments.MusicListFragmentDirections
import com.kemalakkus.musicplayer.model.Song


class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var binding: SongBinding? = null

    inner class SongViewHolder(itemBinding: SongBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    private val differCallback = object :
        DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.songUri == newItem.songUri &&
                    oldItem.songTitle == newItem.songTitle &&
                    oldItem.songArtist == newItem.songArtist
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return newItem == oldItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        binding = SongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return SongViewHolder(binding!!)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currSong = differ.currentList[position]


            binding?.songTitle?.text = currSong.songTitle
            binding?.songArtist?.text = currSong.songArtist
            binding?.tvDuration?.text = currSong.songDuration
            binding?.tvOrder?.text = "${position + 1}"


        holder.itemView.setOnClickListener {
            onItemClick?.let {
                it.invoke(currSong)
            }
        }


    }

    var onItemClick: ((Song) -> Unit)? = null

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}