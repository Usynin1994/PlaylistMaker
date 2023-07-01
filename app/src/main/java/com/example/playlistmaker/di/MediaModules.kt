package com.example.playlistmaker.di

import com.example.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.media.view_model.TracksFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tracksFragmentViewModelModule = module {
    viewModel {
        TracksFragmentViewModel()
    }
}

val playlistsFragmentViewModelModule = module {
    viewModel {
        PlaylistsFragmentViewModel()
    }
}