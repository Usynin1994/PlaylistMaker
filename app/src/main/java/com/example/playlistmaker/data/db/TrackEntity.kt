package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity (
    val artistName: String,
    val artworkUrl100: String,
    val artworkUrl60: String,
    val country: String,
    val previewUrl: String?,
    val primaryGenreName: String,
    val releaseDate: String?,
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val addingTime: Long
        )