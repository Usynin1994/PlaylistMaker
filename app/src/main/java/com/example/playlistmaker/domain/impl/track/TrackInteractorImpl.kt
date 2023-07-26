package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl (private val repository: TrackRepository): TrackInteractor {

    override fun searchTrack(expression: String) : Flow<Pair<List<Track>?, Int?>> {
        return repository.searchTrack(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.errorCode)
                }
            }
        }
    }

    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun getHistory(): ArrayList<Track> {
        return repository.getHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}