package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_KEY = "track_key"

class SearchHistory(val prefs: SharedPreferences) {

    fun addTrack(track: Track) {
        val history = getHistory()
        if (history.contains(track)) history.remove(track)
        history.add(0, track)
        if (history.size > 10) history.removeLast()
        saveHistory(history)
    }
    fun getHistory () : ArrayList<Track> {
        val json = prefs.getString(HISTORY_KEY, null) ?: return arrayListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }
    private fun saveHistory (tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        prefs.edit().putString(HISTORY_KEY, json).apply()
    }
    fun clearHistory () {
        prefs.edit().remove(HISTORY_KEY).apply()
    }
}