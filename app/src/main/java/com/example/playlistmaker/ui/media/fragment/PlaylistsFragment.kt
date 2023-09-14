package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.adapters.playlistadapter.PlaylistAdapter
import com.example.playlistmaker.ui.adapters.playlistadapter.CardObjects
import com.example.playlistmaker.ui.media.ItemDecorator
import com.example.playlistmaker.ui.media.PlaylistState
import com.example.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), PlaylistAdapter.ClickListener {

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

    private var playlistAdapter: PlaylistAdapter? = null

    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fillData()

        playlistAdapter = PlaylistAdapter(this, CardObjects.VERTICAL)
        with(binding.playlistsRecycler) {
            adapter = playlistAdapter
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
            setHasFixedSize(true)
            addItemDecoration(ItemDecorator(ITEM_SPACING, ITEM_MARGIN))
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.playlistsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistCreator)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlists)
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Loading -> showLoading()
        }
    }

    private fun showEmpty() {
        clearContent()
        binding.emptyPlaylist.visibility = View.VISIBLE
    }

    private fun showContent(playlists: List<Playlist>){
        clearContent()
        playlistAdapter?.playlists = playlists
        binding.playlistsRecycler.visibility = View.VISIBLE
    }

    private fun showLoading() {
        clearContent()
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun clearContent() {
        binding.emptyPlaylist.visibility = View.GONE
        binding.playlistsRecycler.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }


    override fun onClick(playlist: Playlist) {
        viewModel.saveCurrentPlaylistId(playlist.id)
        findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstanse() = PlaylistsFragment()
        const val SPAN_COUNT = 2
        const val ITEM_SPACING = 40
        const val ITEM_MARGIN = 0
    }
}