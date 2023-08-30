package com.example.playlistmaker.di

import com.example.playlistmaker.data.repositories.playlistcreator.PlaylistCreatorRepositroyImpl
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorInteractor
import com.example.playlistmaker.domain.api.playlistcreator.PlaylistCreatorRepository
import com.example.playlistmaker.domain.impl.playlistcreator.PlaylistCreatorInteractorImpl
import com.example.playlistmaker.ui.playlistcreator.viewmodel.PlaylistCreatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistCreatorViewModelModule = module {
    viewModel {
        PlaylistCreatorViewModel(get())
    }

    factory <PlaylistCreatorInteractor> {
        PlaylistCreatorInteractorImpl(get())
    }

    single <PlaylistCreatorRepository> {
        PlaylistCreatorRepositroyImpl(get())
    }
}