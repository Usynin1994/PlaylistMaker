package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun reset()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun getTrack() : Track
}