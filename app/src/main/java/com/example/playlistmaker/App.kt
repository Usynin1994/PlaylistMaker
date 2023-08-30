package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playerViewModelModule
import com.example.playlistmaker.di.playlistCreatorViewModelModule
import com.example.playlistmaker.di.searchViewModelModule
import com.example.playlistmaker.di.settingViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val SHARED_PREFS = "SHARED_PREFS"
const val KEY = "KEY"

class App : Application() {

    var darkMode = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                settingViewModelModule,
                playerViewModelModule,
                searchViewModelModule,
                mediaModule,
                playlistCreatorViewModelModule
            )
        }

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