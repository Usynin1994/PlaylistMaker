package com.example.playlistmaker.data.repositories.track

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl (private val networkClient: NetworkClient,
                           private val spClient: SharedPreferencesClient
) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(errorCode = -1)
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(it.artistName,
                        it.artworkUrl100,
                        it.country,
                        it.previewUrl,
                        it.primaryGenreName,
                        it.releaseDate,
                        it.trackId,
                        it.trackName,
                        it.trackTimeMillis,
                        it.collectionName)
                }
                )
            }
            else -> {
                Resource.Error(errorCode = -2)
            }
        }
    }
    override fun saveTrack(track: Track) {
        spClient.saveTrack(track)
    }

    override fun getHistory() : ArrayList<Track> {
        return spClient.getHistory()
    }

    override fun clearHistory () { spClient.clearHistory()}
}