package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (val listener: ClickListener): RecyclerView.Adapter<TrackViewHolder> () {

    internal var tracks = ArrayList<Track>()

    fun clearTracks () {
        tracks.clear()
        notifyDataSetChanged()
    }

    fun setTracks (newTracks: List<Track>) {
        tracks.clear()
        if (newTracks.isNotEmpty()) tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { listener.onClick(tracks.get(position)) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}