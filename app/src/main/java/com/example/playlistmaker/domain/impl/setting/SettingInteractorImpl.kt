package com.example.playlistmaker.domain.impl.setting

import com.example.playlistmaker.domain.api.setting.SettingInteractor
import com.example.playlistmaker.domain.api.setting.SettingRepository
import com.example.playlistmaker.domain.model.EmailData

class SettingInteractorImpl (private val repository: SettingRepository) : SettingInteractor {

    override fun setDarkMode(mode: Boolean) {
        repository.setDarkMode(mode)
    }

    override fun shareApp(url: String) {
        repository.shareApp(url)
    }

    override fun callSupport(email: EmailData) {
        repository.callSupport(email)
    }

    override fun showUserAgreement(url: String){
        repository.showUserAgreement(url)
    }

    override fun getMode() = repository.getMode()
}