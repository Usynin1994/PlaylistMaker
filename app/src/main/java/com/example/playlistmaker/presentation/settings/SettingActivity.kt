package com.example.playlistmaker.presentation.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.KEY
import com.example.playlistmaker.presentation.SHARED_PREFS
import com.example.playlistmaker.data.SettingsUseCasesRepositoryImpl
import com.example.playlistmaker.databinding.ActivitySettingBinding
import com.example.playlistmaker.domain.useCases.settingUseCases.CallSupportUseCase
import com.example.playlistmaker.domain.useCases.settingUseCases.ShareAgreementUseCase
import com.example.playlistmaker.domain.useCases.settingUseCases.ShareAppUseCase

class SettingActivity : AppCompatActivity() {

    private val shareAppUseCase by lazy { ShareAppUseCase(SettingsUseCasesRepositoryImpl(context = applicationContext)) }
    private val callSupportUseCase by lazy { CallSupportUseCase(SettingsUseCasesRepositoryImpl(context = applicationContext)) }
    private val shareAgreementUseCase by lazy { ShareAgreementUseCase(SettingsUseCasesRepositoryImpl(context = applicationContext)) }

    private val settingBinding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(settingBinding.root)

        val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)

        with (settingBinding) {

            //Тулбар
            setSupportActionBar(settingToolbar)
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
                startActivity(shareAppUseCase.execute())
            }

            // Написать в поддержку
            buttonSupport.setOnClickListener {
                startActivity(callSupportUseCase.execute())
            }

            // Пользовательское соглашение
            buttonUserAgreement.setOnClickListener {
                startActivity(shareAgreementUseCase.execute())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}