package com.example.playlistmaker.domain.api.media

import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<Playlist>>
    fun saveCurrentPlaylistId(id: Int)
}