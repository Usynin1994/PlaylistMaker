package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.SHARED_PREFS
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.data.repositories.media.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.media.PlaylistsRepositoryImpl
import com.example.playlistmaker.data.repositories.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.repositories.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.data.repositories.setting.SettingRepositoryImpl
import com.example.playlistmaker.data.repositories.track.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPreferencesClientImpl
import com.example.playlistmaker.domain.api.setting.SettingInteractor
import com.example.playlistmaker.domain.api.setting.SettingRepository
import com.example.playlistmaker.domain.impl.setting.SettingInteractorImpl
import com.example.playlistmaker.ui.setting.view_model.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

val settingViewModelModule = module {
    viewModel {
        SettingViewModel(get())
    }

    single<SettingInteractor> {
        SettingInteractorImpl(get())
    }

    single<SettingRepository> {
        SettingRepositoryImpl(get(), get())
    }

    single<SharedPreferencesClient> {
        SharedPreferencesClientImpl(get())
    } binds(arrayOf(TrackRepositoryImpl::class,
        SettingRepositoryImpl::class,
        PlayerRepositoryImpl::class,
        FavoriteTrackRepositoryImpl::class,
        PlaylistRepositoryImpl::class,
        PlaylistsRepositoryImpl::class))

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }
}


