package com.example.playlistmaker.ui.playlistcreator.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _image = MutableLiveData<Uri?>()
    val image: LiveData<Uri?> = _image

    fun insertPlaylist(name: String, image: String, description: String, trackList: MutableList<Track> = mutableListOf()) {
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

    fun saveToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            creatorInteractor.saveImageToPrivateStorage(uri)
        }
    }

    fun updatePlaylist(id: Int, image: String?, name: String, description: String?, tracks: MutableList<Track>){
        viewModelScope.launch (Dispatchers.IO){
            creatorInteractor.updatePlaylist(Playlist(
            id = id,
            image = image,
            name = name,
            description = description,
            tracks = tracks
        ))
        }
    }

    fun getImageFile(segment: String?){
        viewModelScope.launch(Dispatchers.IO) {
            _image.postValue(creatorInteractor.getImageFile(segment))
        }
    }


}