package com.example.playlistmaker.ui.playlistcreator.viewmodel

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorInteractor
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel (
    private val creatorInteractor: PlaylistCreatorInteractor): ViewModel() {

        fun insertPlaylist(playlist: Playlist) {
            viewModelScope.launch {
                creatorInteractor.insertPlaylist(playlist)
            }
        }
}