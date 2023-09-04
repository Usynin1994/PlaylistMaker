package com.example.playlistmaker.domain.model

import java.io.Serializable

data class Track(val artistName: String,
                 val artworkUrl100: String,
                 val country: String,
                 val previewUrl: String?,
                 val primaryGenreName: String,
                 val releaseDate: String?,
                 val trackId: Int,
                 val trackName: String,
                 val trackTimeMillis: Int,
                 val collectionName: String?,
                 ) : Serializable
