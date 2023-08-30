package com.example.playlistmaker.data.repositories.player

import android.media.MediaPlayer
import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl (private val mediaPlayer: MediaPlayer,
                            private val spClient: SharedPreferencesClient,
                            private val databaseClient: DatabaseClient
): PlayerRepository {

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

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun getPosition () = mediaPlayer.currentPosition.toLong()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }

    override fun getTrack(): Track {
        return spClient.getTrack()
    }

    override suspend fun insertToFavorites(track: Track) {
        databaseClient.insertToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        databaseClient.deleteFromFavorites(track)
    }

    override suspend fun getTracksId(): Flow<List<Int>> = databaseClient.getTracksId()

    override suspend fun getPlaylists(): Flow<List<Playlist>> = databaseClient.getPlaylists()

    override suspend fun updatePlaylist(playlist: Playlist) {
        databaseClient.updatePlaylist(playlist)
    }
}