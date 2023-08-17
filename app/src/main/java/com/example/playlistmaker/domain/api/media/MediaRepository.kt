package com.example.playlistmaker.domain.api.media

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun getTracks(): Flow<List<Track>>

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(track: Track)

    fun saveLastTrack(track: Track)
}