package com.example.playlistmaker.domain.api.playlistcreator

import com.example.playlistmaker.domain.model.Playlist

interface PlaylistCreatorRepository {
    suspend fun insertPlaylist(playlist: Playlist)
}