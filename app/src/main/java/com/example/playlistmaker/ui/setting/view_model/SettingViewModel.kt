package com.example.playlistmaker.ui.setting.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.setting.SettingInteractor
import com.example.playlistmaker.domain.model.EmailData

class SettingViewModel (private val settingInteractor: SettingInteractor): ViewModel() {

    val mode = settingInteractor.getMode()

    private val darkMode = MutableLiveData(mode)
    fun observeDarkMode(): LiveData<Boolean> = darkMode

    fun shareApp(url: String) {settingInteractor.shareApp(url)}

    fun callSupport(email: EmailData) {settingInteractor.callSupport(email)}

    fun showUserAgreement(url: String) {settingInteractor.showUserAgreement(url)}

    fun setDarkMode(mode: Boolean) {
        settingInteractor.setDarkMode(mode)
        darkMode.postValue(mode)
    }
}