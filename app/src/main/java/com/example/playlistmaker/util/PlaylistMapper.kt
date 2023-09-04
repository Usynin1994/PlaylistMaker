package com.example.playlistmaker.util

import androidx.core.net.toUri
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.domain.model.Playlist

fun Playlist.toPlaylistEntity() : PlaylistEntity {
    return PlaylistEntity(
        id = id,
        image = image?.toString(),
        name = name,
        description = description,
        tracks.map { track -> track.toTrackEntity() }.toList()
    )
}

fun PlaylistEntity.toPlaylist() : Playlist {
    return Playlist(
        id = id,
        image = image?.toUri(),
        name = name,
        description = description,
        tracks.map { track -> track.toTrack() }.toMutableList()
    )
}