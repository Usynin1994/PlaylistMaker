package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (val listener: ClickListener): RecyclerView.Adapter<TrackViewHolder> () {

    internal var tracks = ArrayList<Track>()
        set(newValue) {
            val diffCallBack = TrackDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    fun clearTracks () {
        tracks = ArrayList<Track>()
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}