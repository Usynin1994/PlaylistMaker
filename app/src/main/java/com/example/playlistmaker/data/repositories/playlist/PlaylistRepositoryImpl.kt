package com.example.playlistmaker.data.repositories.playlist

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl (private val databaseClient: DatabaseClient,
                              private val spClient: SharedPreferencesClient
): PlaylistRepository {
    override fun saveLastTrack(track: Track) {
        spClient.saveLastTrack(track)
    }

    override suspend fun getPlaylist(playlistId: Int):
            Flow<Playlist> = databaseClient.getPlaylistById(playlistId)

    override suspend fun updatePlaylist(playlist: Playlist) {
        databaseClient.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
       databaseClient.deletePlaylistById(playlistId)
    }
}