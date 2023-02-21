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

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
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