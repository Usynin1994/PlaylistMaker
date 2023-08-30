package com.example.playlistmaker.domain.api.playlistcreator

import com.example.playlistmaker.domain.model.Playlist

interface PlaylistCreatorInteractor {

    suspend fun insertPlaylist(playlist: Playlist)
}