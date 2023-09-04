package com.example.playlistmaker.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    @TypeConverter
    fun listToJson(value: List<TrackEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<TrackEntity> {
        val listType = object : TypeToken<List<TrackEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
}