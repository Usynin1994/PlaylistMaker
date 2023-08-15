package com.example.playlistmaker.data.repositories.media

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.media.MediaRepository
import com.example.playlistmaker.domain.model.Track

class MediaRepositoryImpl(private val databaseClient: DatabaseClient,
                          private val spClient: SharedPreferencesClient): MediaRepository {

    override suspend fun getTracks() = databaseClient.getTracks()

    override suspend fun addToFavorites(track: Track) {
        databaseClient.insertToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        databaseClient.deleteFromFavorites(track)
    }

    override fun saveLastTrack(track: Track) {
        spClient.saveLastTrack(track)
    }
}