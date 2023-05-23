package com.example.playlistmaker.domain.useCases.repository

import com.example.playlistmaker.domain.models.PlayerState

interface PlayerInteractorRepository {

    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition() : Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}