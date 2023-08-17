package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.media.MediaInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.media.LikedTracksState
import kotlinx.coroutines.launch

class TracksFragmentViewModel(private val mediaInteractor: MediaInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<LikedTracksState>()
    val stateLiveData = _stateLiveData

    fun fillData() {
        renderState(LikedTracksState.Loading)
        viewModelScope.launch {
            mediaInteractor.getTracks().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(LikedTracksState.Empty)
        } else {
            renderState(LikedTracksState.Content(tracks))
        }
    }

    private fun renderState(state: LikedTracksState) {
        stateLiveData.postValue(state)
    }

    fun saveLastTrack(track: Track) {
        mediaInteractor.saveLastTrack(track)
    }
}