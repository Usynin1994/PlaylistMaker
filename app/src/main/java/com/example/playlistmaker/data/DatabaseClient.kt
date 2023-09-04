package com.example.playlistmaker.data

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseClient {

    suspend fun getTracks(): Flow<List<Track>>
    suspend fun getTracksId(): Flow<List<Int>>
    suspend fun insertToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}