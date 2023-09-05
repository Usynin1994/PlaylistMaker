package com.example.playlistmaker.domain.impl.playlist

import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (private val repository: PlaylistRepository) : PlaylistInteractor {
    override fun saveLastTrack(track: Track) {
        repository.saveLastTrack(track)
    }

    override suspend fun getPlaylist(playlistId: Int):
            Flow<Playlist> = repository.getPlaylist(playlistId)

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }
}