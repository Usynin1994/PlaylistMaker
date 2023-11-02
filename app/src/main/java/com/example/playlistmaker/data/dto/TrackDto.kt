package com.example.playlistmaker.data.dto

data class TrackDto(val artistName: String,
                    val artworkUrl100: String,
                    val artworkUrl60: String,
                    val collectionName: String?,
                    val country: String,
                    val previewUrl: String?,
                    val primaryGenreName: String,
                    val releaseDate: String?,
                    val trackId: Int,
                    val trackName: String,
                    val trackTimeMillis: Int
)