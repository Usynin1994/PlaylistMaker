package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String): Flow<Resource<List<Track>>>

    fun saveTrack(track: Track)

    fun getHistory() : ArrayList<Track>

    fun clearHistory ()
}