package com.example.playlistmaker.domain.impl.media

import com.example.playlistmaker.domain.api.media.MediaInteractor
import com.example.playlistmaker.domain.api.media.MediaRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl (private val mediaRepository: MediaRepository): MediaInteractor {
    override suspend fun getTracks(): Flow<List<Track>> = mediaRepository.getTracks()

    override suspend fun insertToFavorites(track: Track) {
        mediaRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        mediaRepository.deleteFromFavorites(track)
    }

    override fun saveLastTrack(track: Track) {
        mediaRepository.saveLastTrack(track)
    }
}