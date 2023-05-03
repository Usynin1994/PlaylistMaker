package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private val settingBinding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }


    lateinit var toolbar: Toolbar
    lateinit var switchDarkMode: Switch
    lateinit var buttonShareApp: Button
    lateinit var buttonSupport: Button
    lateinit var buttonUserAgreement: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(settingBinding.root)

        // Инициализуруем элементы
        val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        switchDarkMode = settingBinding.switchDarkMode
        buttonShareApp = settingBinding.buttonShareApp
        buttonSupport = settingBinding.buttonSupport
        buttonUserAgreement = settingBinding.buttonUserAgreement
        toolbar = settingBinding.settingToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Выставляем переключатель
        switchDarkMode.isChecked = (applicationContext as App).darkMode

        switchDarkMode.setOnCheckedChangeListener { switcher, isChecked ->
            if (isChecked != sp.getBoolean(KEY,false)) {
                sp.edit().putBoolean(KEY, isChecked).apply()
            }
            (applicationContext as App).switchTheme(isChecked)
        }

        // Поделиться приложением
        buttonShareApp.setOnClickListener {
            val sendLink: Intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.androidDevLink))
                type = "text/plain"
            }
            val shareApp = Intent.createChooser(sendLink, null)
            startActivity(shareApp)
        }

        // Написать в поддержку
        buttonSupport.setOnClickListener {
            val callSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportMail)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailSubject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.supportMessage))
            }
            startActivity(callSupport)
        }

        // Пользовательское соглашение
        buttonUserAgreement.setOnClickListener {
            val openPage = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.userAgreement)))
            startActivity(openPage)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}