package com.example.playlistmaker.domain.api.setting

import com.example.playlistmaker.domain.model.EmailData

interface SettingRepository {

    fun setDarkMode(mode: Boolean)

    fun getMode() : Boolean

    fun shareApp(url: String)

    fun callSupport(email: EmailData)

    fun showUserAgreement(url: String)

}