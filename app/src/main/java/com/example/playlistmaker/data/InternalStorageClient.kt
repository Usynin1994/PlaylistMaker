package com.example.playlistmaker.data

import android.net.Uri

interface InternalStorageClient {
    suspend fun saveImageToPrivateStorage(uri: Uri)
    suspend fun getImageFile(segment: String?) : Uri?
}