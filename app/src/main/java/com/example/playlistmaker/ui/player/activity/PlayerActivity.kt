package com.example.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.activity.EXTRA_KEY
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.util.formatAsTime

class PlayerActivity : AppCompatActivity() {

    private val playerBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerBinding.root)

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory())[PlayerViewModel::class.java]

        val track = intent.getSerializableExtra(EXTRA_KEY) as Track
        viewModel.prepare(track.previewUrl)

        viewModel.observeState().observe(this) { state ->
            playerBinding.playButton.setOnClickListener {
                viewModel.onPlayClick()
                imageController(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.playerTrackTime.text = getString(R.string.default_track_time)
                setPlayIcon()
            }
        }

        viewModel.observeTime().observe(this) {
            playerBinding.playerTrackTime.text = it
        }

        with(playerBinding) {

            // Тулбар
            setSupportActionBar(playerToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            // Отображаем информацию о треке
            playerTrackName.text = track.trackName
            playerTrackName.setSelected(true)
            playerArtistName.text = track.artistName
            playerArtistName.setSelected(true)
            trackTotalTime.text = track.trackTimeMillis.toLong().formatAsTime()
            trackReleaseDate.text = track.releaseDate.substring(0..3)
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country

            //Отображение альбома
            if (track.collectionName.isNullOrEmpty()) {
                albumName.visibility = View.GONE
                trackAlbum.visibility = View.GONE
            } else {
                trackAlbum.text = track.collectionName
                playerTrackTime.text = getString(R.string.default_track_time)

            }
        }

        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_image_corner_radius)))
            .into(playerBinding.playerTrackImage)

    }
    private fun setPlayIcon () {
        playerBinding.playButton.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.button_play, null)
    }

    private fun setPauseIcon () {
        playerBinding.playButton.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.button_pause, null)
    }

    private fun imageController(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> setPlayIcon()
            else -> setPauseIcon()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}