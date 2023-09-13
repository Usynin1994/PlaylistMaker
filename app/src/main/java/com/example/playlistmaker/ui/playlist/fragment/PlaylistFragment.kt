package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.adapters.trackadapter.TrackAdapter
import com.example.playlistmaker.ui.playlist.viewmodel.PlaylistViewModel
import com.example.playlistmaker.ui.playlistcreator.fragment.PlaylistEditorFragment
import com.example.playlistmaker.util.formatAsMinutes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(), TrackAdapter.ClickListener, TrackAdapter.OnLongClickListener {

    private val viewModel by viewModel<PlaylistViewModel>()
    private var trackAdapter: TrackAdapter? = null
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlist: Playlist? = null

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

        viewModel.fillData()

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

        viewModel.playlistImage.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl == null) {
                Glide.with(requireContext())
                    .load(R.drawable.placeholder)
                    .into(binding.imagePlaylist)
                Glide.with(requireContext())
                    .load(R.drawable.placeholder)
                    .into(binding.includedLayout.playlistImage)
            } else {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.imagePlaylist)
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.includedLayout.playlistImage)
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
                binding.textPlaylistDescription.visibility = View.VISIBLE
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                binding.textNotFound.visibility = View.VISIBLE
                binding.tracksRecycler.visibility = View.GONE
            }
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

        viewModel.playlist.observe(viewLifecycleOwner) {
            playlist = it
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
            MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
                .setTitle(requireContext().getString(R.string.delete_playlist))
                .setMessage(requireContext().getString(R.string.certainty_to_remove_playlist))
                .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ ->}
                .setPositiveButton(requireContext().getString(R.string.delete)) { _, _ ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }.show()
        }

        binding.buttonSharePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.sharePlaylist()
        }

        binding.buttonEditPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_playlistEditorFragment,
                playlist?.let { PlaylistEditorFragment.createArgs(it) }
            )
        }
    }

    override fun onClick(track: Track) {
        viewModel.saveLastTrack(track)
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment)
    }

    override fun onLongClick(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(requireContext().getString(R.string.delete_track))
            .setMessage(requireContext().getString(R.string.certainty_to_remove))
            .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ ->}
            .setPositiveButton(requireContext().getString(R.string.delete)) { _, _ ->
                val indexToRemove = trackAdapter?.tracks?.indexOfFirst { it.trackId == track.trackId }
                if (indexToRemove != -1) {
                    indexToRemove?.let { trackAdapter?.tracks?.removeAt(it) }
                    indexToRemove?.let {trackAdapter?.notifyItemRemoved(it)}
                }
                viewModel.deleteTrack(track)
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        trackAdapter = null
        playlist = null
    }

    companion object {
        const val ANIMATION_TIME = 300L
        const val ALPHA_START = 0f
        const val ALPHA_END = 0.7f
    }
}