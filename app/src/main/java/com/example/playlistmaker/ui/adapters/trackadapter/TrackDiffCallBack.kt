package com.example.playlistmaker.ui.adapters.trackadapter

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.model.Track

class TrackDiffCallBack (
    private val oldList: List<Track>,
    private val newList: List<Track>
        ) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldList[oldItemPosition]
        val newTrack = newList[newItemPosition]
        return oldTrack.trackId == newTrack.trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldList[oldItemPosition]
        val newTrack = newList[newItemPosition]
        return oldTrack == newTrack
    }
}