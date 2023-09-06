package com.example.playlistmaker.data

import com.example.playlistmaker.domain.model.Track

interface SharedPreferencesClient {

    fun setDarkMode(mode: Boolean)

    fun getMode() : Boolean

    fun saveTrack(track: Track)

    fun getHistory() : ArrayList<Track>

    fun saveHistory (tracks: List<Track>)

    fun clearHistory ()

    fun getTrack() : Track

    fun saveLastTrack(track: Track)

    fun saveCurrentPlaylistId(id: Int)

    fun getCurrentPlaylistId(): Int

}