package com.example.playlistmaker.ui.playlist.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel (private val interactor: PlaylistInteractor): ViewModel() {

    private var trackId: Int? = null

    private var _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _playlistImage = MutableLiveData<Uri?>()
    val playlistImage: LiveData<Uri?> = _playlistImage

    private val _playlistName = MutableLiveData<String>()
    val playlistName: LiveData<String> = _playlistName

    private val _playlistDescription = MutableLiveData<String?>()
    val playlistDescription: LiveData<String?> = _playlistDescription

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _noTracks = MutableLiveData<Boolean>()
    val noTracks: LiveData<Boolean> = _noTracks

    init {
        takeId()
     }

    private fun takeId(){
        viewModelScope.launch (Dispatchers.IO) {
            trackId = interactor.getCurrentPlaylistId()
        }
    }

    fun fillData () {
        viewModelScope.launch (Dispatchers.IO) {
            interactor.getPlaylist(trackId!!).collect{
                _playlist.postValue(it)
                _playlistImage.postValue(interactor.getImageFile(it.image?.toUri()?.lastPathSegment))
                if (_playlistName.value != it.name) _playlistName.postValue(it.name)
                if (_playlistDescription.value != it.description) _playlistDescription.postValue(it.description)
                _tracks.postValue(it.tracks)
            }
        }
    }

    fun saveLastTrack(track: Track) { interactor.saveLastTrack(track) }

    fun deleteTrack(track: Track) {
        viewModelScope.launch (Dispatchers.IO){
            playlist.value?.tracks?.remove(track)
            playlist.value?.let { interactor.updatePlaylist(it) }
            fillData()
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch (Dispatchers.IO) {
            playlist.value?.id?.let { interactor.deletePlaylist(it) }
        }
    }

    fun sharePlaylist() {
        if (playlist.value?.tracks?.isEmpty() == true) {
            viewModelScope.launch {
                _noTracks.postValue(true)
                delay(TOAST_DELAY)
                _noTracks.postValue(false)
            }
        } else {
            playlist.value?.let { interactor.sharePlaylist(it) }
        }
    }

    companion object {
        const val TOAST_DELAY = 300L
    }
}