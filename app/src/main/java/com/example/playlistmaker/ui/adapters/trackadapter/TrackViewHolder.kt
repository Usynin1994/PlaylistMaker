package com.example.playlistmaker.ui.adapters.trackadapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.formatAsTime

class TrackViewHolder (
    private val binding: TrackCardBinding
): RecyclerView.ViewHolder (binding.root) {

    fun bind (model: Track,
              listener: TrackAdapter.ClickListener,
              longListener: TrackAdapter.OnLongClickListener? = null)
    {
        binding.artistName.text = model.artistName
        binding.trackName.text = model.trackName
        binding.trackTime.text = model.trackTimeMillis.toLong().formatAsTime()

        itemView.setOnClickListener {
            listener.onClick(model)
        }

        if (longListener != null) {
            itemView.setOnLongClickListener {
                longListener.onLongClick(model)
                true
            }
        }

        Glide.with(itemView.context)
            .load(model.artworkUrl60)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.artist_image_corner_radius)))
            .into(binding.trackImage)
    }
}