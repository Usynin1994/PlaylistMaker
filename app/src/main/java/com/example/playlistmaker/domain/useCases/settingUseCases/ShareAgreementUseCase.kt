package com.example.playlistmaker.domain.useCases.settingUseCases

import android.content.Intent
import com.example.playlistmaker.domain.useCases.repository.SettingsUseCasesRepository

class ShareAgreementUseCase (private val case : SettingsUseCasesRepository) {

    fun execute() : Intent {
        return case.openUserAgreement()
    }

}