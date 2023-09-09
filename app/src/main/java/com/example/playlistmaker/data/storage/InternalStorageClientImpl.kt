package com.example.playlistmaker.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.InternalStorageClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class InternalStorageClientImpl (val context: Context): InternalStorageClient {

    override suspend fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            DIRECTORY
        )
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, uri.lastPathSegment.toString())
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun getImageFile(segment: String?) : Uri? {
        return if (segment == null) {
            null
        } else {
            File(File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), DIRECTORY
            ), segment
            ).toUri()
        }
    }

    companion object {
        const val DIRECTORY = "playlist"
    }





}