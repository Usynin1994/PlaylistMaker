package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorCode: Int? = null)
    }

    fun saveTrack(track: Track)

    fun getHistory() : ArrayList<Track>

    fun clearHistory ()
}