package com.example.playlistmaker.data.repositories.playlistcreator

import android.net.Uri
import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.InternalStorageClient
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorRepository
import com.example.playlistmaker.domain.model.Playlist

class PlaylistCreatorRepositroyImpl (
    private val databaseClient: DatabaseClient,
    private val internalStorageClient: InternalStorageClient): PlaylistCreatorRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        databaseClient.insertPlaylist(playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        internalStorageClient.saveImageToPrivateStorage(uri)
    }
}