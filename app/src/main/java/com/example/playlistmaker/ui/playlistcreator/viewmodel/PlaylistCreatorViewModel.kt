package com.example.playlistmaker.ui.playlistcreator.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorInteractor
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(
    private val creatorInteractor: PlaylistCreatorInteractor
) : ViewModel() {

    fun insertPlaylist(name: String, image: Uri?, description: String) {
        viewModelScope.launch (Dispatchers.IO) {
            creatorInteractor.insertPlaylist(
                Playlist(
                    name = name,
                    image = image,
                    description = description
                )
            )
        }
    }

    fun saveToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            creatorInteractor.saveImageToPrivateStorage(uri)
        }
    }
}