package com.example.playlistmaker.data.repositories.track

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.toTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl (private val networkClient: NetworkClient,
                           private val spClient: SharedPreferencesClient
) : TrackRepository {

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(errorCode = -1))
            }
            200 -> {
                with (response as TrackSearchResponse) {
                    val data = results.map { it.toTrack() }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(errorCode = -2))
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

    override fun saveLastTrack(track: Track) {
        spClient.saveLastTrack(track)
    }
}