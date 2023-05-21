package com.example.playlistmaker.domain.useCases.repository

import android.content.Intent

interface SettingsUseCasesRepository {

    fun sendLink () : Intent

    fun callSupport () : Intent

    fun openUserAgreement () : Intent

}