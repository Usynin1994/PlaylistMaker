package com.example.playlistmaker.domain.api.playlistcreator

import android.net.Uri
import com.example.playlistmaker.domain.model.Playlist

interface PlaylistCreatorRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun saveImageToPrivateStorage(uri: Uri)
}