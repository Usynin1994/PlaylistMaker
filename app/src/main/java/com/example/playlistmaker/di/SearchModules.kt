package com.example.playlistmaker.di

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.ItunesApi
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repositories.track.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.api.search.TrackRepository
import com.example.playlistmaker.domain.impl.track.TrackInteractorImpl
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<ItunesApi> {
        Retrofit.Builder().baseUrl("http://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }
}
