package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LikedTracksFragmentBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.adapters.trackadapter.TrackAdapter
import com.example.playlistmaker.ui.media.LikedTracksState
import com.example.playlistmaker.ui.media.view_model.TracksFragmentViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment(), TrackAdapter.ClickListener {

    private val viewModel by viewModel<TracksFragmentViewModel>()
    private var trackAdapter: TrackAdapter? = null

    private var _binding: LikedTracksFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LikedTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fillData()

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false) { track ->
            viewModel.saveLastTrack(track)
            findNavController().navigate(R.id.action_mediaFragment_to_playerFragment)
        }

        trackAdapter = TrackAdapter (this)
        binding.likedTracksRecycler.adapter = trackAdapter

        viewModel.stateLiveData.observe(viewLifecycleOwner){
            render(it)
        }

    }

    private fun render(state: LikedTracksState) {
        when (state) {
            is LikedTracksState.Content -> showContent(state.tracks)
            is LikedTracksState.Empty -> showEmpty()
            is LikedTracksState.Loading -> showLoading()
        }
    }

    private fun showEmpty() {
        clearContent()
        binding.emptyMedia.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>){
        clearContent()
        trackAdapter?.tracks = tracks as ArrayList<Track>
        binding.likedTracksRecycler.visibility = View.VISIBLE
    }

    private fun showLoading() {
        clearContent()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun clearContent() {
        binding.emptyMedia.visibility = View.GONE
        binding.likedTracksRecycler.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    override fun onClick (track: Track) {
        onTrackClickDebounce(track)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstanse() = TracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}