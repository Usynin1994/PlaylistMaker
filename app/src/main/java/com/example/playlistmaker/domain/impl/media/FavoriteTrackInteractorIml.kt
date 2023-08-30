package com.example.playlistmaker.domain.impl.media

import com.example.playlistmaker.domain.api.media.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.media.FavoriteTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl (private val favoriteTrackRepository: FavoriteTrackRepository): FavoriteTrackInteractor {
    override suspend fun getTracks(): Flow<List<Track>> = favoriteTrackRepository.getTracks()

    override suspend fun insertToFavorites(track: Track) {
        favoriteTrackRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoriteTrackRepository.deleteFromFavorites(track)
    }

    override fun saveLastTrack(track: Track) {
        favoriteTrackRepository.saveLastTrack(track)
    }
}