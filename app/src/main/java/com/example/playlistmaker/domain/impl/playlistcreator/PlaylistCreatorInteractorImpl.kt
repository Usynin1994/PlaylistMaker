package com.example.playlistmaker.domain.impl.playlistcreator

import android.net.Uri
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorInteractor
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorRepository
import com.example.playlistmaker.domain.model.Playlist

class PlaylistCreatorInteractorImpl(
    private val repository: PlaylistCreatorRepository): PlaylistCreatorInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: String) {
        repository.saveImageToPrivateStorage(uri)
    }
}