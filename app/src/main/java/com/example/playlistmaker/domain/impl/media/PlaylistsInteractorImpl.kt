package com.example.playlistmaker.domain.impl.media

import com.example.playlistmaker.domain.api.media.PlaylistsInteractor
import com.example.playlistmaker.domain.api.media.PlaylistsRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl (
    private val playlistsRepository: PlaylistsRepository): PlaylistsInteractor
{
    override suspend fun getPlaylists(): Flow<List<Playlist>> = playlistsRepository.getPlaylists()

    override fun saveCurrentPlaylistId(id: Int) {
        playlistsRepository.saveCurrentPlaylistId(id)
    }

}