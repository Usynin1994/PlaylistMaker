package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.data.repositories.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.impl.player.PlayerInteractorImpl
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel {
        PlayerViewModel(get())
    }
    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }
    single<MediaPlayer> {
        MediaPlayer()
    }
}
