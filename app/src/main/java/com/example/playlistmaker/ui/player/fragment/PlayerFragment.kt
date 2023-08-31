package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.adapters.playlistadapter.PlaylistAdapter
import com.example.playlistmaker.ui.adapters.playlistadapter.CardObjects
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.util.formatAsTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment(), PlaylistAdapter.ClickListener {

    private val viewModel: PlayerViewModel by viewModel()

    private var playlistAdapter: PlaylistAdapter? = null

    private var track: Track? = null

    private var _playerBinding: FragmentPlayerBinding? = null
    private val playerBinding get() = _playerBinding!!

    private val bottomSheetBehavior get() = BottomSheetBehavior.from(playerBinding.standardBottomSheet).apply {
        state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _playerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return playerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerBinding.backgroundDimLayout.alpha = 0f

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_COLLAPSED,
                    BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING-> {
                        playerBinding.backgroundDimLayout.animate().alpha(0.7f).setDuration(300).start()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        playerBinding.backgroundDimLayout.animate().alpha(0f).setDuration(300).start()
                        viewModel.fillData()
                    }
                    else -> {  }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        playerBinding.playlistsRecycler.layoutManager = LinearLayoutManager(requireContext())
        playlistAdapter = PlaylistAdapter(this, CardObjects.HORIZONTAL)
        playerBinding.playlistsRecycler.adapter = playlistAdapter

        viewModel.fillData()

        viewModel.trackLiveData.observe(viewLifecycleOwner) {
            drawPlayer(it)
            track = it
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

        viewModel.playlists.observe(viewLifecycleOwner) {
            playlistAdapter?.playlists = it as ArrayList<Playlist>
        }

        playerBinding.addToMediaButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

        playerBinding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistCreator)
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

            playerTrackName.text = track.trackName
            playerTrackName.isSelected = true
            playerArtistName.text = track.artistName
            playerArtistName.isSelected = true
            trackTotalTime.text = track.trackTimeMillis.toLong().formatAsTime()
            trackReleaseDate.text?.let {trackReleaseDate.text = track.releaseDate?.substring(0..3)}
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country

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
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .into(playerBinding.playerTrackImage)
    }

    private fun controlLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            playerBinding.likeButton.setImageResource(R.drawable.heart)
        } else {
            playerBinding.likeButton.setImageResource(R.drawable.like_btn)
        }
    }

    override fun onClick(playlist: Playlist) {
        if (playlist.tracks.contains(track)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.track_already_added_to_playlist, playlist.name),
                Toast.LENGTH_SHORT).show()
        } else {
            track?.let{track -> playlist.tracks.add(FIRST, track)}
            viewModel.updatePlaylist(playlist)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.fillData()
            Toast.makeText(
                requireContext(),
                getString(R.string.added_to_playlist, playlist.name),
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
        playlistAdapter = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    companion object {
        const val FIRST = 0
    }
}