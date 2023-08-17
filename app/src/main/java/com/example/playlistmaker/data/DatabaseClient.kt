package com.example.playlistmaker.data

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseClient {

    suspend fun getTracks(): Flow<List<Track>>
    suspend fun getTracksId(): Flow<List<Int>>
    suspend fun insertToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
}