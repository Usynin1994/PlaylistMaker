package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var settingBinding: ActivitySettingBinding
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingBinding.root)

        toolbar = settingBinding.settingToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingBinding.buttonShareApp.setOnClickListener {
            val sendLink: Intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.androidDevLink))
                type = "text/plain"
            }
            val shareApp = Intent.createChooser(sendLink, null)
            startActivity(shareApp)
        }

        settingBinding.buttonSupport.setOnClickListener {
            val callSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportMail)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailSubject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.supportMessage))
            }
            startActivity(callSupport)
        }

        settingBinding.buttonUserAgreement.setOnClickListener {
            val openPage = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.userAgreement)))
            startActivity(openPage)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}