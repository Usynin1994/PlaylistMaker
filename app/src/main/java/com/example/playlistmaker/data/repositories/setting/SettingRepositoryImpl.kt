package com.example.playlistmaker.data.repositories.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.SharedPreferencesClient
import com.example.playlistmaker.domain.api.setting.SettingRepository
import com.example.playlistmaker.domain.model.EmailData

class SettingRepositoryImpl (private val context: Context,
                             private val sp: SharedPreferencesClient) : SettingRepository {
    override fun setDarkMode(mode: Boolean) {
        sp.setDarkMode(mode)
    }

    override fun getMode() :Boolean = sp.getMode()

    override fun shareApp(url: String) {
        val sendLink: Intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val shareApp = Intent.createChooser(sendLink, null)
        shareApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(shareApp)
    }

    override fun callSupport(email: EmailData) {
        val callSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, email.mail)
            putExtra(Intent.EXTRA_SUBJECT, email.subject)
            putExtra(Intent.EXTRA_TEXT, email.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(callSupport)
    }

    override fun showUserAgreement(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


}