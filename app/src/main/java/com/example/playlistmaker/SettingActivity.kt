package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<View>(R.id.go_back).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.buttonShareApp).setOnClickListener {
            val sendLink: Intent = Intent(Intent.ACTION_SEND)
            sendLink.putExtra(Intent.EXTRA_TEXT, getString(R.string.androidDevLink))
            sendLink.type = "text/plain"
            val shareApp = Intent.createChooser(sendLink, null)
            startActivity(shareApp)
        }

        findViewById<View>(R.id.buttonSupport).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportMail)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailSubject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.supportMessage))
            startActivity(shareIntent)
        }

        findViewById<View>(R.id.buttonUserAgreement).setOnClickListener {
            val openPage = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.userAgreement)))
            startActivity(openPage)
        }
    }
}