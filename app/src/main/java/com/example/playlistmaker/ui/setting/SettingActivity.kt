package com.example.playlistmaker.ui.setting

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingBinding
import com.example.playlistmaker.domain.model.EmailData
import com.example.playlistmaker.presentation.setting.SettingViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingViewModel

    private val settingBinding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(settingBinding.root)

        viewModel = ViewModelProvider(this,
            SettingViewModel.getViewModelFactory(this)
        )[SettingViewModel::class.java]

        viewModel.observeDarkMode().observe(this) {
            settingBinding.switchDarkMode.isChecked = it
            (applicationContext as App).switchTheme(it)
        }

        with (settingBinding) {
            setSupportActionBar(settingToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            buttonShareApp.setOnClickListener {
                viewModel.shareApp(getString(R.string.androidDevLink))
            }

            buttonSupport.setOnClickListener {
                viewModel.callSupport(EmailData(
                    getString(R.string.supportMail),
                    getString(R.string.mailSubject),
                    getString(R.string.supportMessage)))
            }

            buttonUserAgreement.setOnClickListener {
                viewModel.showUserAgreement(getString(R.string.userAgreement))
            }
        }

        settingBinding.switchDarkMode.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.setDarkMode(isChecked)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}