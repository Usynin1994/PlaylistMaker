package com.example.playlistmaker.ui.adapters.playlistadapter

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import java.io.File
import kotlin.math.abs

class PlaylistViewHolder (itemView: View) : RecyclerView.ViewHolder (itemView){

    private val filePath = File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
    private val playlistName = itemView.findViewById<TextView>(R.id.playlistName)
    private val playlistTracks = itemView.findViewById<TextView>(R.id.playlistTrackCount)
    private val playlistImage = itemView.findViewById<ImageView>(R.id.playlistImage)

    fun bind (model: Playlist, listener: PlaylistAdapter.ClickListener) {
        playlistName.text = model.name
        playlistTracks.text = itemView.context
            .getString(R.string.track_count, model.tracks.size, getRightWord(model.tracks.size))

        if (model.image == null) {
            playlistImage.setImageResource(R.drawable.placeholder)
        } else {
            val file = model.image.lastPathSegment?.let { File(filePath, it) }
            playlistImage.setImageURI(file?.toUri())
        }

        itemView.setOnClickListener {
            listener.onClick(model)
        }
    }
    private fun getRightWord(tracks: Int): String {
        var trackCount = tracks
        trackCount = abs(trackCount) % 100
        val tracksTmp = (trackCount % 10)
        if (trackCount in 11..19) {
            return itemView.context.getString(R.string.tracks)
        }
        if (tracksTmp in 2..4) {
            return itemView.context.getString(R.string.tracks_alt)
        }
        if (trackCount == 0) {
            return itemView.context.getString(R.string.tracks)
        }
        return if (tracksTmp == 1) {
            itemView.context.getString(R.string.track)
        } else {
            itemView.context.getString(R.string.tracks)
        }
    }


    companion object {
        const val DIRECTORY = "playlist"
    }
}