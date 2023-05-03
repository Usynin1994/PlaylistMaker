package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.playlistmaker.databinding.ActivityMainBinding

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