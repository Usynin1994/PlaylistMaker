package com.example.playlistmaker.ui.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingBinding
import com.example.playlistmaker.domain.model.EmailData
import com.example.playlistmaker.ui.setting.view_model.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModel()

    private var _settingBinding: FragmentSettingBinding? = null
    private val settingBinding get() = _settingBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _settingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return settingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDarkMode().observe(viewLifecycleOwner) {
            settingBinding.switchDarkMode.isChecked = it
            (activity?.applicationContext as App).switchTheme(it)
        }

        with (settingBinding) {

            buttonShareApp.setOnClickListener {
                viewModel.shareApp(getString(R.string.androidDevLink))
            }

            buttonSupport.setOnClickListener {
                viewModel.callSupport(
                    EmailData(
                    getString(R.string.supportMail),
                    getString(R.string.mailSubject),
                    getString(R.string.supportMessage))
                )
            }

            buttonUserAgreement.setOnClickListener {
                viewModel.showUserAgreement(getString(R.string.userAgreement))
            }

            switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setDarkMode(isChecked)
            }
        }
    }
}