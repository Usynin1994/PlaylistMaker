package com.example.playlistmaker.domain.api.setting

import com.example.playlistmaker.domain.model.EmailData

interface SettingInteractor {

    fun setDarkMode(mode: Boolean)

    fun shareApp(url: String)

    fun callSupport(email: EmailData)

    fun showUserAgreement(url: String)

    fun getMode() : Boolean
}