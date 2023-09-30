package com.example.playlistmaker.di

import com.example.playlistmaker.data.InternalStorageClient
import com.example.playlistmaker.data.repositories.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.data.repositories.playlistcreator.PlaylistCreatorRepositroyImpl
import com.example.playlistmaker.data.storage.InternalStorageClientImpl
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorInteractor
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorRepository
import com.example.playlistmaker.domain.impl.playlistcreator.PlaylistCreatorInteractorImpl
import com.example.playlistmaker.ui.playlistcreator.viewmodel.PlaylistCreatorViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

val playlistCreatorViewModelModule = module {
    viewModel {
        PlaylistCreatorViewModel(get())
    }

    factory <PlaylistCreatorInteractor> {
        PlaylistCreatorInteractorImpl(get())
    }

    single <PlaylistCreatorRepository> {
        PlaylistCreatorRepositroyImpl(get(), get())
    } binds(arrayOf(PlaylistRepositoryImpl::class))

    single <InternalStorageClient> {
        InternalStorageClientImpl(androidContext())
    }
}