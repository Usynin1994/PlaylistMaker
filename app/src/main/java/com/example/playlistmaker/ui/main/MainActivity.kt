package com.example.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.ui.setting.activity.SettingActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.media.MediaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity


class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        mainBinding.mediaButton.setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }

        mainBinding.settingButton.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }
}