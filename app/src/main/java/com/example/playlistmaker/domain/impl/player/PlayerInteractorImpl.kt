package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track

class PlayerInteractorImpl(private val repository: PlayerRepository,
                           private val spClient: SharedPreferencesClient) : PlayerInteractor {
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
        return spClient.getTrack()
    }
}