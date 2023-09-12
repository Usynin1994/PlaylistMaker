package com.example.playlistmaker.domain.model

import android.net.Uri
import java.io.Serializable

data class Playlist (val id: Int = 0,
                     val image: String?,
                     val name: String,
                     val description: String?,
                     val tracks: MutableList<Track> = mutableListOf()
) : Serializable