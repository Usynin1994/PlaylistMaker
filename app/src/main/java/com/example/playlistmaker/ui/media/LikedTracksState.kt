package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.model.Track

sealed interface LikedTracksState {
    object Loading : LikedTracksState

    data class Content(
        val tracks: List<Track>
    ) : LikedTracksState

    object Empty : LikedTracksState
}