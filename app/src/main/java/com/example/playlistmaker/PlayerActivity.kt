package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_MS = 500L
    }

    private var playerState = STATE_DEFAULT

    private val playerBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private val play: ImageView get() = playerBinding.playButton
    private val playerTrackTime: TextView get() = playerBinding.playerTrackTime
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val time = startTimer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerBinding.root)

        // Достаем трек
        val track = intent.getSerializableExtra(EXTRA_KEY) as Track

        // Подготовка плеера
        preparePlayer(track.previewUrl)

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
            }
        }

        // Загрузка картинки
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_image_corner_radius)))
            .into(playerBinding.playerTrackImage)

        // Контроль воспроизведения
        play.setOnClickListener {
            playbackControl()
        }

    }

    // Кнопка выхода тулбара
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    // Подготовка плеера
    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(time)
            playerState = STATE_PREPARED
            playerTrackTime.text = getString(R.string.default_track_time)
            play.setImageResource(R.drawable.button_play)
        }
    }

    // Звпуск трека
    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        handler.post(time)
    }

    // Трек на паузу
    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(time)
    }

    // Контроль воспроизведения
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    // Начало отсчета времени
    private fun startTimer () : Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    playerTrackTime.text = TimeFormat.formatTime(mediaPlayer.currentPosition.toLong())
                handler.postDelayed(this, DELAY_MS)}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(time)
    }
}