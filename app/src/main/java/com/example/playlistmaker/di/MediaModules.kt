package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.DatabaseClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.repositories.media.MediaRepositoryImpl
import com.example.playlistmaker.data.repositories.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.storage.DatabaseClientImpl
import com.example.playlistmaker.domain.api.media.MediaInteractor
import com.example.playlistmaker.domain.api.media.MediaRepository
import com.example.playlistmaker.domain.impl.media.MediaInteractorImpl
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
        PlaylistsFragmentViewModel()
    }

    single<DatabaseClient> {
        DatabaseClientImpl(get())
    } binds (arrayOf(PlayerRepositoryImpl::class))

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single <MediaRepository> {
        MediaRepositoryImpl(get(), get())
    }

    factory <MediaInteractor> {
        MediaInteractorImpl(get())
    }
}