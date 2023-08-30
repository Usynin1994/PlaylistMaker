package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun reset()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun getTrack() : Track
    suspend fun insertToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    suspend fun getTracksId(): Flow<List<Int>>
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
}