package com.example.playlistmaker.presentation.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.PlayerInteractorRepositoryImpl
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.useCases.PlayerInteractor
import com.example.playlistmaker.presentation.TimeFormat
import com.example.playlistmaker.presentation.search.EXTRA_KEY
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayerActivity : AppCompatActivity() {

    private val interactor = PlayerInteractorRepositoryImpl()
    private val player = PlayerInteractor(interactor)
    private val handler = Handler(Looper.getMainLooper())
    private val time = startTimer()

    private val playerBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private val playButton: FloatingActionButton get() = playerBinding.playButton
    private val playerTrackTime: TextView get() = playerBinding.playerTrackTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerBinding.root)

        interactor.setOnStateChangeListener { state ->
            playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerTrackTime.text = getString(R.string.default_track_time)
                handler.removeCallbacks(time)
                setPlayIcon()
            }
        }

        // Достаем трек
        val track = intent.getSerializableExtra(EXTRA_KEY) as Track

        // Подготовка плеера
        player.preparePlayer(track.previewUrl)

        with(playerBinding) {

            // Тулбар
            setSupportActionBar(playerToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            // Отображаем информацию о треке
            playerTrackName.text = track.trackName
            playerTrackName.setSelected(true)
            playerArtistName.text = track.artistName
            playerArtistName.setSelected(true)
            trackTotalTime.text = TimeFormat.formatTime(track.trackTimeMillis.toLong())
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

        // Загрузка картинки
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_image_corner_radius)))
            .into(playerBinding.playerTrackImage)
    }

    // Контроль воспроизведения
    private fun controller(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                player.startPlayer()
                setPauseIcon()
                handler.post(time)
            }

            PlayerState.STATE_PLAYING -> {
                player.pausePlayer()
                setPlayIcon()
                handler.removeCallbacks(time)
            }
        }
    }

    private fun setPlayIcon () {
        playButton.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.button_play, null)
    }

    private fun setPauseIcon () {
        playButton.foreground =
            ResourcesCompat.getDrawable(resources, R.drawable.button_pause, null)
    }

    // Создание runnable объекта
    private fun startTimer () : Runnable {
        return object : Runnable {
            override fun run() {
                val position = player.getPosition()
                playerTrackTime.text = TimeFormat.formatTime(position)
                handler.postDelayed(this, DELAY_MS)}
        }
    }

    // Кнопка выхода тулбара
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        player.pausePlayer()
        handler.removeCallbacks(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        handler.removeCallbacks(time)
    }

    companion object {
        const val DELAY_MS = 500L
    }
}