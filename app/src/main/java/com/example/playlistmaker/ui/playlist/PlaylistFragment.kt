package com.example.playlistmaker.ui.playlist

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.adapters.playlistadapter.PlaylistViewHolder
import com.example.playlistmaker.ui.adapters.trackadapter.TrackAdapter
import com.example.playlistmaker.util.formatAsMinutes
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import kotlin.math.abs

class PlaylistFragment : Fragment(), TrackAdapter.ClickListener {

    private val viewModel by viewModel<PlaylistViewModel>()
    private var trackAdapter: TrackAdapter? = null
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playlist.observe(viewLifecycleOwner) {
            drawScreen(it)
        }

        trackAdapter = TrackAdapter(this)
        binding.tracksRecycler.adapter = trackAdapter

        binding.goBack.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun drawScreen(playlist: Playlist){
        with(binding) {

            if (playlist.image == null) {
                imagePlaylist.setImageResource(R.drawable.placeholder)
            } else {
                val file = playlist.image.lastPathSegment?.let { File(File(requireContext().getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), DIRECTORY
                ), it) }
                imagePlaylist.setImageURI(file?.toUri())
            }

            trackTime.text = playlist.tracks.sumOf { it.trackTimeMillis }.toLong().formatAsMinutes()

            textPlaylistName.text = playlist.name

            if (playlist.description.isNullOrEmpty()) {
                textPlaylistDescription.visibility = View.GONE
            } else {
                textPlaylistDescription.text = playlist.description
            }

            textPlaylistDescription.text = playlist.description
            trackCount.text = requireContext()
                .getString(R.string.track_count, playlist.tracks.size, getRightWord(playlist.tracks.size))
            trackAdapter?.tracks = playlist.tracks as ArrayList<Track>
        }
    }

    private fun getRightWord(tracks: Int): String {
        var trackCount = tracks
        trackCount = abs(trackCount) % 100
        val tracksTmp = (trackCount % 10)
        if (trackCount in 11..19) {
            return requireContext().getString(R.string.tracks)
        }
        if (tracksTmp in 2..4) {
            return requireContext().getString(R.string.tracks_alt)
        }
        if (trackCount == 0) {
            return requireContext().getString(R.string.tracks)
        }
        return if (tracksTmp == 1) {
            requireContext().getString(R.string.track)
        } else {
            requireContext().getString(R.string.tracks)
        }
    }

    override fun onClick(track: Track) {
        viewModel.saveLastTrack(track)
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        trackAdapter = null
    }

    companion object {
        const val DIRECTORY = "playlist"
    }
}