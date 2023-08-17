package com.example.playlistmaker.data.storage

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.toTrack
import com.example.playlistmaker.util.toTrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseClientImpl (private val appDatabase: AppDatabase): DatabaseClient {

    override suspend fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun getTracksId(): Flow<List<Int>> = flow {
        val tracksId = appDatabase.trackDao().getTracksId()
        emit(tracksId)
    }

    override suspend fun insertToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(track.toTrackEntity())
    }

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrackEntity(track.toTrackEntity())
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> track.toTrack() }
    }
}