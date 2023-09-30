package com.example.playlistmaker.ui.adapters.playlistadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist

class PlaylistAdapter (private val listener: ClickListener, private val cardObject: CardObjects) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    internal var playlists = listOf<Playlist>()
        set(newValue) {
            val diffCallBack = PlaylistDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    fun interface ClickListener {
        fun onClick(playlist: Playlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return if (cardObject == CardObjects.HORIZONTAL) {
            PlaylistViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_card_second, parent, false))
        } else {
            PlaylistViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_card_main, parent, false))
        }
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], listener)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}