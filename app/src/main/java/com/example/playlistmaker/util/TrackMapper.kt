package com.example.playlistmaker.util

import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

fun Track.toTrackEntity() : TrackEntity {
    return TrackEntity(artistName,
        artworkUrl100,
        country,
        previewUrl,
        primaryGenreName,
        releaseDate,
        trackId,
        trackName,
        trackTimeMillis,
        collectionName,
        System.currentTimeMillis()
    )
}

fun TrackEntity.toTrack() : Track {
    return Track(artistName,
        artworkUrl100,
        country,
        previewUrl,
        primaryGenreName,
        releaseDate,
        trackId,
        trackName,
        trackTimeMillis,
        collectionName)
}

fun TrackDto.toTrack() : Track {
    return Track(artistName,
        artworkUrl100,
        country,
        previewUrl,
        primaryGenreName,
        releaseDate,
        trackId,
        trackName,
        trackTimeMillis,
        collectionName)
}