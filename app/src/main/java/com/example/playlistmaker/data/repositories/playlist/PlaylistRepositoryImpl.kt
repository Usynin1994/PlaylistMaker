package com.example.playlistmaker.data.repositories.playlist

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.InternalStorageClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.formatAsTime
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl (private val databaseClient: DatabaseClient,
                              private val spClient: SharedPreferencesClient,
                              private val context: Context,
                              private val internalStorageClient: InternalStorageClient
): PlaylistRepository {
    override fun saveLastTrack(track: Track) {
        spClient.saveLastTrack(track)
    }

    override suspend fun getPlaylist(playlistId: Int):
            Flow<Playlist> = databaseClient.getPlaylistById(playlistId)

    override suspend fun updatePlaylist(playlist: Playlist) {
        databaseClient.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
       databaseClient.deletePlaylistById(playlistId)
    }

    override fun getCurrentPlaylistId(): Int = spClient.getCurrentPlaylistId()

    override suspend fun getImageFile(segment: String?): Uri? {
        return internalStorageClient.getImageFile(segment)
    }

    override fun sharePlaylist(playlist: Playlist) {
        var message = "${playlist.name} \n ${playlist.description} \n"
        for ((index, obj) in playlist.tracks.withIndex()) {
            message += "${index + 1}. ${obj.artistName} - " +
                    "${obj.trackName} " +
                    "(${obj.trackTimeMillis.toLong().formatAsTime()}) \n"
        }
        val sendText: Intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val sharePlaylist = Intent.createChooser(sendText, null)
        sharePlaylist.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(sharePlaylist)
    }
}