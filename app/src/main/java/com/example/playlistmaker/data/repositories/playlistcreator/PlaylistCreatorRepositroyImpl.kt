package com.example.playlistmaker.data.repositories.playlistcreator

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorRepository
import com.example.playlistmaker.domain.model.Playlist

class PlaylistCreatorRepositroyImpl (
    private val databaseClient: DatabaseClient): PlaylistCreatorRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        databaseClient.insertPlaylist(playlist)
    }
}