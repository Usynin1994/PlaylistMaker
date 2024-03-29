package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.AvailabilityInfo
import com.example.playlistmaker.util.formatAsTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel (private val playerInteractor: PlayerInteractor): ViewModel() {

    private var timerJob: Job? = null

    private var track: Track? = null

    private val _stateLiveData = MutableLiveData<PlayerState>()
    val stateLiveData: LiveData<PlayerState> = _stateLiveData

    private val _timeLiveData = MutableLiveData<String>()
    val timeLiveData : LiveData<String> = _timeLiveData

    private val _trackLiveData = MutableLiveData<Track>()
    val trackLiveData: LiveData<Track> = _trackLiveData

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private var _isInPlaylist = MutableLiveData<AvailabilityInfo>()
    val isInPlaylist: LiveData<AvailabilityInfo> = _isInPlaylist

    init {
        playerInteractor.setOnStateChangeListener { state ->
            _stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) timerJob?.cancel()
        }
        prepare()
    }

    private fun startTimer () {
        timerJob = viewModelScope.launch {
            while(isActive) {
                delay(TRACK_TIME_DELAY)
                _timeLiveData.postValue(playerInteractor.getPosition().formatAsTime())
            }
        }
    }

    private fun prepare () {
        track = playerInteractor.getTrack()
        viewModelScope.launch {
            track?.previewUrl?.let { playerInteractor.preparePlayer(it) }
            playerInteractor.getTracksId().collect {
                track?.trackId?.let { id -> _isFavorite.postValue(it.contains(id)) }
            }
            track?.let { _trackLiveData.postValue(it) }
        }
    }

    private fun play () {
        playerInteractor.startPlayer()
        startTimer()
    }

    fun pause () {
        timerJob?.cancel()
        playerInteractor.pausePlayer()
    }

    fun reset () {
        timerJob?.cancel()
        playerInteractor.reset()
    }

    fun onPlayClick () {
        when (_stateLiveData.value) {
            PlayerState.STATE_PLAYING -> pause()
            else -> play()
        }
    }

    fun onLikeClick() {
        if (_isFavorite.value == true) {
            track?.let { deleteFromFavorites(it) }
        } else {
            track?.let { insertToFavorites(it) }
        }
    }

    private fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch (Dispatchers.IO) { playerInteractor.updatePlaylist(playlist) }
    }

    fun fillData() {
        viewModelScope.launch (Dispatchers.IO) {
            playerInteractor.getPlaylists().collect {
                _playlists.postValue(it)
            }
        }
    }

    fun addToPlaylist(playlistId: Int) {
        viewModelScope.launch {
            val playlist = playlists.value?.find { it.id == playlistId }
            if (playlist?.tracks?.contains(track) == true) {
                _isInPlaylist.value = AvailabilityInfo.AvailabilityData(true, playlist.name)
                delay(TOAST_DELAY)
                _isInPlaylist.value = AvailabilityInfo.NoNotification
            } else {
                track?.let { track -> playlist?.tracks?.add(FIRST, track) }
                _isInPlaylist.value = playlist?.name?.let { AvailabilityInfo.AvailabilityData(false, it) }
                playlist?.let { updatePlaylist(it) }
                delay(TOAST_DELAY)
                _isInPlaylist.value = AvailabilityInfo.NoNotification
            }
        }
    }

    private fun insertToFavorites(track: Track) {
        viewModelScope.launch (Dispatchers.IO) {
            playerInteractor.insertToFavorites(track)
            _isFavorite.postValue(true)
        }
    }

    private fun deleteFromFavorites(track: Track) {
        viewModelScope.launch (Dispatchers.IO) {
            playerInteractor.deleteFromFavorites(track)
            _isFavorite.postValue(false)
        }
    }

    companion object {
        private const val TRACK_TIME_DELAY = 300L
        private const val FIRST = 0
        private const val TOAST_DELAY = 300L
    }
}