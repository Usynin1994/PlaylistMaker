package com.example.playlistmaker.data.storage

import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.toPlaylist
import com.example.playlistmaker.util.toPlaylistEntity
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

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(convertToPlaylistEntity(playlist))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> track.toTrack() }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            playlist -> playlist.toPlaylist()
        }
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlist.toPlaylistEntity()
    }
}