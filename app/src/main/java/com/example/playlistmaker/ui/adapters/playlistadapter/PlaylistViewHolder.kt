package com.example.playlistmaker.ui.adapters.playlistadapter

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import java.io.File

class PlaylistViewHolder (itemView: View) : RecyclerView.ViewHolder (itemView){

    private val filePath = File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
    private val playlistName = itemView.findViewById<TextView>(R.id.playlistName)
    private val playlistTracks = itemView.findViewById<TextView>(R.id.playlistTrackCount)
    private val playlistImage = itemView.findViewById<ImageView>(R.id.playlistImage)

    fun bind (model: Playlist, listener: PlaylistAdapter.ClickListener) {
        playlistName.text = model.name
        playlistTracks.text = itemView.context
            .resources.getQuantityString(R.plurals.plural_tracks, model.tracks.size, model.tracks.size)
            //и тут поменял на глайд
            val file = model.image?.let { File(filePath, it.toUri().lastPathSegment) }
            Glide.with(itemView)
                .load(file)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(playlistImage)


        itemView.setOnClickListener {
            listener.onClick(model)
        }
    }


    companion object {
        const val DIRECTORY = "playlist"
    }
}