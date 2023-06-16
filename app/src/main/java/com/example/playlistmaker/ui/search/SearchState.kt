package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.model.Track

sealed interface SearchState{
    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val tracks: List<Track>? = null
    ) : SearchState

    data class Empty(
        val tracks: List<Track>? = null
    ) : SearchState

    data class History(
        val tracks: List<Track>
    ) : SearchState

    data class ClearScreen(
        val tracks: List<Track>? = null
    ) : SearchState
}