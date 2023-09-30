package com.example.playlistmaker.data.repositories.media

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.media.PlaylistsRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl (private val databaseClient: DatabaseClient,
                               private val spClient: SharedPreferencesClient): PlaylistsRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> = databaseClient.getPlaylists()

    override fun saveCurrentPlaylistId(id: Int) {
        spClient.saveCurrentPlaylistId(id)
    }
}