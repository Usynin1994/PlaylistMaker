package com.example.playlistmaker.ui.playlistcreator.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(
    private val creatorInteractor: PlaylistCreatorInteractor
) : ViewModel() {

    fun insertPlaylist(name: String, image: String?, description: String, trackList: MutableList<Track> = mutableListOf()) {
        viewModelScope.launch (Dispatchers.IO) {
            creatorInteractor.insertPlaylist(
                Playlist(
                    name = name,
                    image = image,
                    description = description,
                    tracks = trackList
                )
            )
        }
    }

    fun saveToPrivateStorage(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            creatorInteractor.saveImageToPrivateStorage(uri)
        }
    }
}