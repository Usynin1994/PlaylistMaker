package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.media.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.media.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel (
    private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistState>()
    val stateLiveData = _stateLiveData

    init {
        renderState(PlaylistState.Loading)
    }

    fun fillData() {
        viewModelScope.launch (Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistState.Empty)
        } else {
            renderState(PlaylistState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }
}