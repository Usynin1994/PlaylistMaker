package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun reset() {
        repository.reset()
    }

    override fun getPosition(): Long = repository.getPosition()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }

    override fun getTrack(): Track {
        return repository.getTrack()
    }

    override suspend fun insertToFavorites(track: Track) {
        repository.insertToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        repository.deleteFromFavorites(track)
    }

    override suspend fun getTracksId(): Flow<List<Int>> = repository.getTracksId()
}