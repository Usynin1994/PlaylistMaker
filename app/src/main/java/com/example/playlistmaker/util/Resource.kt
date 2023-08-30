package com.example.playlistmaker.util

sealed class Resource<T>(val data: T? = null, val errorCode: Int? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(data: T? = null, errorCode: Int): Resource<T>(data, errorCode)
}
