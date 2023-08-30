package com.example.playlistmaker.ui.adapters.playlistadapter

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.model.Playlist

class PlaylistDiffCallBack (
    private val oldList: List<Playlist>,
    private val newList: List<Playlist>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlaylist = oldList[oldItemPosition]
        val newPlaylist = newList[newItemPosition]
        return oldPlaylist.id == newPlaylist.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlaylist = oldList[oldItemPosition]
        val newPlaylist = newList[newItemPosition]
        return oldPlaylist == newPlaylist
    }
}