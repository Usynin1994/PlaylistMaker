package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

sealed interface PlaylistState {
    object Loading : PlaylistState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistState

    object Empty : PlaylistState
}