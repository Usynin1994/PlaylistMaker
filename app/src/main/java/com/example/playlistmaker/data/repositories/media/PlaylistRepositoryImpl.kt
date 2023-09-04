package com.example.playlistmaker.data.repositories.media

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.domain.api.media.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl (private val databaseClient: DatabaseClient): PlaylistRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> = databaseClient.getPlaylists()
}