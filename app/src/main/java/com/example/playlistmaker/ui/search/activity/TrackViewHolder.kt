package com.example.playlistmaker.ui.search.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.formatAsTime

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {

    private val artistName = itemView.findViewById<TextView>(R.id.artistName)
    private val trackName = itemView.findViewById<TextView>(R.id.trackName)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackTime)
    private val trackImage = itemView.findViewById<ImageView>(R.id.trackImage)

    fun bind (model: Track, listener: TrackAdapter.ClickListener) {
        artistName.text = model.artistName
        trackName.text = model.trackName
        trackTime.text = model.trackTimeMillis.toLong().formatAsTime()

        itemView.setOnClickListener {
            listener.onClick(model)
        }

        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.artist_image_corner_radius)))
            .into(trackImage)
    }
}