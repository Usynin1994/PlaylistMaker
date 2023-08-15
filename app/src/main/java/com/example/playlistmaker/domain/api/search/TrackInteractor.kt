package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String) : Flow<Pair<List<Track>?, Int?>>

    fun saveTrack(track: Track)

    fun getHistory() : ArrayList<Track>

    fun clearHistory ()

    fun saveLastTrack(track: Track)
}