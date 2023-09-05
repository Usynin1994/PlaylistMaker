package com.example.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel (private val interactor: PlaylistInteractor): ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    var playlist: LiveData<Playlist> = _playlist

    fun prepareViewModel(playlistId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            interactor.getPlaylist(playlistId).collect{
                _playlist.postValue(it)
            }
        }
    }
}