package com.example.playlistmaker.di

import com.example.playlistmaker.data.repositories.playlist.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.api.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.api.playlist.PlaylistRepository
import com.example.playlistmaker.domain.impl.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.ui.playlist.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModelModule = module {
    viewModel {
        PlaylistViewModel(get())
    }

    factory <PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single <PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }
}