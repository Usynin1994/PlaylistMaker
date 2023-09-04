package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.repositories.media.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.media.PlaylistRepositoryImpl
import com.example.playlistmaker.data.repositories.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.repositories.playlistcreator.PlaylistCreatorRepositroyImpl
import com.example.playlistmaker.data.storage.DatabaseClientImpl
import com.example.playlistmaker.domain.api.media.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.media.FavoriteTrackRepository
import com.example.playlistmaker.domain.api.media.PlaylistInteractor
import com.example.playlistmaker.domain.api.media.PlaylistRepository
import com.example.playlistmaker.domain.impl.media.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.media.PlaylistInteractorImpl
import com.example.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.media.view_model.TracksFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

val mediaModule = module {

    viewModel {
        TracksFragmentViewModel(get())
    }

    viewModel {
        PlaylistsFragmentViewModel(get())
    }

    single<DatabaseClient> {
        DatabaseClientImpl(get())
    } binds (arrayOf(
        PlayerRepositoryImpl::class,
        PlaylistRepositoryImpl::class,
        PlaylistCreatorRepositroyImpl::class))

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single <FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }

    factory <FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    single <PlaylistRepository> {
        PlaylistRepositoryImpl(get())
    }

    factory <PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

}