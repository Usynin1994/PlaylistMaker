package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.util.formatAsTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private val viewModel: PlayerViewModel by viewModel()

    private lateinit var playerBinding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return playerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.trackLiveData.observe(viewLifecycleOwner) {
            drawPlayer(it)
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            imageController(state)
        }

        viewModel.timeLiveData.observe(viewLifecycleOwner) {
            playerBinding.playerTrackTime.text = it
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            controlLikeButton(it)
        }

        playerBinding.playButton.setOnClickListener {
            viewModel.onPlayClick()
        }

        playerBinding.goBack.setOnClickListener {
            findNavController().navigateUp()
        }

        playerBinding.likeButton.setOnClickListener{
            viewModel.onLikeClick()
        }
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
            PlayerState.STATE_PLAYING -> setPauseIcon()
            PlayerState.STATE_COMPLETE -> {
                playerBinding.playerTrackTime.text = getString(R.string.default_track_time)
                setPlayIcon()
            }
            else -> setPlayIcon()
        }
    }

    private fun drawPlayer (track: Track) {
        with(playerBinding) {

            // Отображаем информацию о треке
            playerTrackName.text = track.trackName
            playerTrackName.isSelected = true
            playerArtistName.text = track.artistName
            playerArtistName.isSelected = true
            trackTotalTime.text = track.trackTimeMillis.toLong().formatAsTime()
            trackReleaseDate.text?.let {trackReleaseDate.text = track.releaseDate?.substring(0..3)}
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

    private fun controlLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            playerBinding.likeButton.setImageResource(R.drawable.heart)
        } else {
            playerBinding.likeButton.setImageResource(R.drawable.like_btn)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}