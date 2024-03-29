package com.example.playlistmaker.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.KEY
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesClientImpl(private val sp: SharedPreferences) : SharedPreferencesClient {

    override fun setDarkMode(mode: Boolean) {
        if (mode != sp.getBoolean(KEY, false)) sp.edit().putBoolean(KEY, mode).apply()
    }

    override fun getMode() = sp.getBoolean(KEY, false)

    override fun saveTrack(track: Track) {
        val history = getHistory()
        if (history.contains(track)) history.remove(track)
        history.add(FIRST, track)
        if (history.size > MAX_SIZE) history.removeLast()
        saveHistory(history)
    }

    override fun getHistory(): ArrayList<Track> {
        val json = sp.getString(HISTORY_KEY, null) ?: return arrayListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun saveHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sp.edit().putString(HISTORY_KEY, json).apply()
    }

    override fun clearHistory() {
        sp.edit().remove(HISTORY_KEY).apply()
    }

    override fun getTrack(): Track {
        val json = sp.getString(LAST_TRACK_KEY, null)
        val type = object : TypeToken<Track>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun saveLastTrack(track: Track) {
        sp.edit().remove(LAST_TRACK_KEY).apply()
        val json = Gson().toJson(track)
        sp.edit().putString(LAST_TRACK_KEY, json).apply()
    }

    override fun saveCurrentPlaylistId(id: Int) {
        sp.edit().remove(PLAYLIST_ID_KEY).apply()
        sp.edit().putInt(PLAYLIST_ID_KEY, id).apply()
    }

    override fun getCurrentPlaylistId(): Int {
        return sp.getInt(PLAYLIST_ID_KEY, 0)
    }

    companion object {
        const val HISTORY_KEY = "track_key"
        const val LAST_TRACK_KEY = "last_track"
        const val PLAYLIST_ID_KEY = "last_track"
        const val FIRST = 0
        const val MAX_SIZE = 10
    }
}