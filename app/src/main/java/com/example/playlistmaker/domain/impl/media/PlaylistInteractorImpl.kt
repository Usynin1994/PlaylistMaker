package com.example.playlistmaker.domain.impl.media

import com.example.playlistmaker.domain.api.media.PlaylistInteractor
import com.example.playlistmaker.domain.api.media.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (
    private val playlistRepository: PlaylistRepository): PlaylistInteractor
{
    override suspend fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylists()
}