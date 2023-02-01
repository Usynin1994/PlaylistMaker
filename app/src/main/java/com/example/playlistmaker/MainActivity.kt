package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.search_button).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        findViewById<View>(R.id.media_button).setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }

        findViewById<View>(R.id.setting_button).setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }
}