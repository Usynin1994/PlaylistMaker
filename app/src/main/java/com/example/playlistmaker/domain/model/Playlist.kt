package com.example.playlistmaker.domain.model

import android.net.Uri

data class Playlist (val id: Int = 0,
                     val image: Uri?,
                     val name: String,
                     val description: String?,
                     val tracks: MutableList<Track> = mutableListOf()
)