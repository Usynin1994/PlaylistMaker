package com.example.playlistmaker.ui.playlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.formatAsTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel (private val interactor: PlaylistInteractor): ViewModel() {

    private var playlist: Playlist? = null

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
        fillData()
    }

    fun fillData () {
        viewModelScope.launch (Dispatchers.IO) {
            interactor.getPlaylist(interactor.getCurrentPlaylistId()).collect{
                playlist = it
                _playlistImage.postValue(it.image)
                if (_playlistName.value != it.name) _playlistName.postValue(it.name)
                if (_playlistDescription.value != it.description) _playlistDescription.postValue(it.description)
                _tracks.postValue(it.tracks)
            }
        }
    }

    fun saveLastTrack(track: Track) { interactor.saveLastTrack(track) }

    fun deleteTrack(track: Track) {
        viewModelScope.launch (Dispatchers.IO){
            playlist?.tracks?.remove(track)
            playlist?.let { interactor.updatePlaylist(it) }
            fillData()
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch (Dispatchers.IO) {
            playlist?.id?.let { interactor.deletePlaylist(it) }
        }
    }

    fun sharePlaylist() {
        if (playlist?.tracks?.isEmpty() == true) {
            viewModelScope.launch {
                _noTracks.postValue(true)
                delay(TOAST_DELAY)
                _noTracks.postValue(false)
            }
        } else {
            playlist?.let { interactor.sharePlaylist(getMessage(it)) }
        }
    }

    private fun getMessage(playlist: Playlist) : String {
        var message = "${playlist.name} \n ${playlist.description} \n"
        for ((index, obj) in playlist.tracks.withIndex()) {
            message += "${index + 1}. ${obj.artistName} - " +
                    "${obj.trackName} " +
                    "(${obj.trackTimeMillis.toLong().formatAsTime()}) \n"
        }
        return message
    }

    companion object {
        const val TOAST_DELAY = 300L
    }
}