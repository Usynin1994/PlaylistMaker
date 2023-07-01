package com.example.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private val mediaBinding: ActivityMediaBinding by lazy {
        ActivityMediaBinding.inflate(layoutInflater)
    }

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mediaBinding.root)

        setSupportActionBar(mediaBinding.mediaToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mediaBinding.mediaToolbar.setNavigationOnClickListener {
            finish()
        }

        mediaBinding.viewPager.adapter = MediaAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(mediaBinding.tabLayout, mediaBinding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.liked_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}