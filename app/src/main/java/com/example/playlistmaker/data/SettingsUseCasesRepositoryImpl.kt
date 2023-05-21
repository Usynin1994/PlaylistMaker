package com.example.playlistmaker.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.useCases.repository.SettingsUseCasesRepository

class SettingsUseCasesRepositoryImpl (val context: Context): SettingsUseCasesRepository {

    override fun sendLink() : Intent {
        val sendLink: Intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, R.string.androidDevLink)
            type = "text/plain"
        }
        val shareApp = Intent.createChooser(sendLink, null)
        return shareApp
    }

    override fun callSupport() : Intent {
        val callSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.supportMail))
            putExtra(Intent.EXTRA_SUBJECT, R.string.mailSubject)
            putExtra(Intent.EXTRA_TEXT, R.string.supportMessage)
        }
        return callSupport
    }

    override fun openUserAgreement() : Intent {
        return  Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.userAgreement)))
    }

}