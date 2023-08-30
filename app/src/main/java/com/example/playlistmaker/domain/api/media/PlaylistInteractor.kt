package com.example.playlistmaker.domain.api.media

import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun getPlaylists(): Flow<List<Playlist>>

}