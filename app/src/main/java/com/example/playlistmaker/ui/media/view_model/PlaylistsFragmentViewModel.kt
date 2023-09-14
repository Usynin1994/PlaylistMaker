package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.media.PlaylistsInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.media.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel (
    private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistState>()
    val stateLiveData: LiveData<PlaylistState> = _stateLiveData

    init {
        renderState(PlaylistState.Loading)
    }

    fun fillData() {
        viewModelScope.launch (Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    fun saveCurrentPlaylistId(id: Int) {
        playlistsInteractor.saveCurrentPlaylistId(id)
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistState.Empty)
        } else {
            renderState(PlaylistState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistState) {
        _stateLiveData.postValue(state)
    }
}