package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository): TrackInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.searchTrack(expression)) {
                is Resource.Success -> { consumer.consume(resource.data!!, null) }
                is Resource.Error -> { consumer.consume(null, resource.errorCode) }
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