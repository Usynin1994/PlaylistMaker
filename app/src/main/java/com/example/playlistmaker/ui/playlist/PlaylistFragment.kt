package com.example.playlistmaker.ui.playlist

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.adapters.trackadapter.TrackAdapter
import com.example.playlistmaker.util.formatAsMinutes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistFragment : Fragment(), TrackAdapter.ClickListener, TrackAdapter.OnLongClickListener {

    private val viewModel by viewModel<PlaylistViewModel>()
    private var trackAdapter: TrackAdapter? = null
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetBehavior get() = BottomSheetBehavior.from(binding.actionsBottomSheet).apply {
        state = BottomSheetBehavior.STATE_HIDDEN
    }

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

        binding.backgroundDimLayout.alpha = ALPHA_START

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_COLLAPSED,
                    BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING-> {
                        binding.backgroundDimLayout.animate().alpha(ALPHA_END).setDuration(
                            ANIMATION_TIME
                        ).start()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.backgroundDimLayout.animate().alpha(ALPHA_START).setDuration(
                            ANIMATION_TIME
                        ).start()
                    }
                    else -> {  }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        trackAdapter = TrackAdapter(this, this)
        binding.tracksRecycler.adapter = trackAdapter

        viewModel.playlistImage.observe(viewLifecycleOwner) { it ->
            if (it == null) {
                binding.imagePlaylist.setImageResource(R.drawable.placeholder)
                binding.includedLayout.playlistImage.setImageResource(R.drawable.placeholder)
            } else {
                val file = it.lastPathSegment?.let { File(File(requireContext().getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), DIRECTORY
                ), it) }
                binding.imagePlaylist.setImageURI(file?.toUri())
                binding.includedLayout.playlistImage.setImageURI(file?.toUri())
            }
        }

        viewModel.playlistName.observe(viewLifecycleOwner) {
            binding.textPlaylistName.text = it
            binding.includedLayout.playlistName.text = it
        }

        viewModel.playlistDescription.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.textPlaylistDescription.visibility = View.GONE
            } else {
                binding.textPlaylistDescription.text = it
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { list ->
            trackAdapter?.tracks = list as ArrayList<Track>
            binding.trackCount.text = resources
                .getQuantityString(R.plurals.plural_tracks, list.size, list.size)
            binding.trackTime.text = resources.getQuantityString(
                R.plurals.plural_minutes,
                list.sumOf { it.trackTimeMillis.toLong() }.formatAsMinutes().toInt(),
                list.sumOf { it.trackTimeMillis.toLong() }.formatAsMinutes())
            binding.includedLayout.playlistTrackCount.text = resources
                .getQuantityString(R.plurals.plural_tracks, list.size, list.size)
        }

        viewModel.noTracks.observe(viewLifecycleOwner) {
            if (it == true) Toast.makeText(requireContext(),
                requireContext().getString(R.string.no_tracks_to_share),
                Toast.LENGTH_SHORT).show()
        }

        binding.goBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.imageShare.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.imageMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.buttonDeletePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(requireContext().getString(R.string.delete_playlist))
                .setMessage(requireContext().getString(R.string.certainty_to_remove_playlist))
                .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->}
                .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }.show()
        }

        binding.buttonSharePlaylist.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.buttonEditPlaylist.setOnClickListener {

        }
    }

    override fun onClick(track: Track) {
        viewModel.saveLastTrack(track)
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment)
    }

    override fun onLongClick(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.delete_track))
            .setMessage(requireContext().getString(R.string.certainty_to_remove))
            .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ ->}
            .setPositiveButton(requireContext().getString(R.string.delete)) { _, _ ->
                viewModel.deleteTrack(track)
                viewModel.fillData()
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        trackAdapter = null
    }

    companion object {
        const val DIRECTORY = "playlist"
        const val ANIMATION_TIME = 300L
        const val ALPHA_START = 0f
        const val ALPHA_END = 0.7f
    }
}