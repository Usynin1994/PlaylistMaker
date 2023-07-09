package com.example.playlistmaker.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.KEY
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesClientImpl (private val sp: SharedPreferences) : SharedPreferencesClient {

    companion object {
        val HISTORY_KEY = "track_key"
    }

    override fun setDarkMode(mode: Boolean) {
        if (mode != sp.getBoolean(KEY, false)) sp.edit().putBoolean(KEY, mode).apply()
    }

    override fun getMode() = sp.getBoolean(KEY, false)

    override fun saveTrack(track: Track) {
            val history = getHistory()
            if (history.contains(track)) history.remove(track)
            history.add(0, track)
            if (history.size > 10) history.removeLast()
            saveHistory(history)
        }

    override fun getHistory () : ArrayList<Track> {
        val json = sp.getString(HISTORY_KEY, null) ?: return arrayListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun saveHistory (tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sp.edit().putString(HISTORY_KEY, json).apply()
    }

    override fun clearHistory () {
        sp.edit().remove(HISTORY_KEY).apply()
    }

    override fun getTrack(): Track {
        return getHistory()[0]
    }
}