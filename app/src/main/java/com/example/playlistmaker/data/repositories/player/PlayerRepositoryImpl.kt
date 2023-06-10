package com.example.playlistmaker.data.repositories.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.model.PlayerState

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var stateCallback: ((PlayerState) -> Unit)? = null

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateCallback?.invoke(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            stateCallback?.invoke(PlayerState.STATE_COMPLETE)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        stateCallback?.invoke(PlayerState.STATE_PLAYING)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        stateCallback?.invoke(PlayerState.STATE_PAUSED)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getPosition () = mediaPlayer.currentPosition.toLong()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }
}