package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFS = "SHARED_PREFS"
const val KEY = "KEY"

class App : Application() {

    var darkMode = false

    override fun onCreate() {

        super.onCreate()
        val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        darkMode = sp.getBoolean(KEY, false)
        switchTheme(darkMode)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkMode = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}