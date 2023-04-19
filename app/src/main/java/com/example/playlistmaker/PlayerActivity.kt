package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    lateinit var playerBinding: ActivityPlayerBinding
    lateinit var track: Track

    // Элементы экрана
    lateinit var toolbar: Toolbar
    lateinit var playerTrackImage: ImageView
    lateinit var playerTrackName: TextView
    lateinit var playerArtistName: TextView
    lateinit var trackTotalTime: TextView
    lateinit var trackAlbum: TextView
    lateinit var trackReleaseDate: TextView
    lateinit var trackGenre: TextView
    lateinit var trackCountry: TextView
    lateinit var albumName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        // Инициализация элементов экрана
        toolbar = playerBinding.playerToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playerTrackImage = playerBinding.playerTrackImage
        playerTrackName = playerBinding.playerTrackName
        playerArtistName = playerBinding.playerArtistName
        trackTotalTime = playerBinding.trackTotalTime
        trackAlbum = playerBinding.trackAlbum
        trackReleaseDate = playerBinding.trackReleaseDate
        trackGenre = playerBinding.trackGenre
        trackCountry = playerBinding.trackCountry
        albumName = playerBinding.albumName

        // Достаем трек
        track = Gson().fromJson(intent.getStringExtra(EXTRA_KEY), Track::class.java)

        // Отображаем информацию о треке
        playerTrackName.text = track.trackName
        playerTrackName.setSelected(true)
        playerArtistName.text = track.artistName
        playerArtistName.setSelected(true)
        trackTotalTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
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

        // Загрузка картинки
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(playerTrackImage.resources.getDimensionPixelSize(R.dimen.player_image_corner_radius)))
            .into(playerTrackImage)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}