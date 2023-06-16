package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.repositories.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.repositories.setting.SettingRepositoryImpl
import com.example.playlistmaker.data.repositories.track.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.storage.SharedPreferencesClientImpl
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.setting.SettingRepository
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.impl.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.setting.SettingInteractorImpl
import com.example.playlistmaker.domain.impl.track.TrackInteractorImpl

object Creator {

    private fun getSettingRepository(context: Context): SettingRepository {
        return SettingRepositoryImpl(context, SharedPreferencesClientImpl(context))
    }
    fun provideSettingInteractor (context: Context) : SettingInteractorImpl {
        return SettingInteractorImpl(getSettingRepository(context))
    }

    private fun getMoviesRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context), SharedPreferencesClientImpl(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getMoviesRepository(context))
    }

    fun providePlayerInteractor () = PlayerInteractorImpl(PlayerRepositoryImpl())
}