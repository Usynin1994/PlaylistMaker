package com.example.playlistmaker.domain.api.playlist

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun saveLastTrack(track: Track)
    suspend fun getPlaylist(playlistId: Int): Flow<Playlist>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Int)
}