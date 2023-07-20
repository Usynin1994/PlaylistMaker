package com.example.playlistmaker.ui.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.ClickListener {

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var searchBinding: FragmentSearchBinding

    private val trackAdapter = TrackAdapter (this)
    private val historyAdapter = TrackAdapter (this)
    private var isCkickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeClick().observe(viewLifecycleOwner) {
            isCkickAllowed = it
        }

        searchBinding.searchRecycler.adapter = trackAdapter
        searchBinding.historyRecycler.adapter = historyAdapter

        searchBinding.inputText.doOnTextChanged { text, _, _, _ ->
            searchBinding.clearIcon.visibility = clearButtonVisibility(text)
            text?.let { viewModel.searchDebounce(it.toString()) }
        }

        searchBinding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        searchBinding.clearIcon.setOnClickListener {
            searchClear()
        }

        searchBinding.buttonReload.setOnClickListener {
            viewModel.doLatestSearch()
        }
    }

    override fun onClick (track: Track) {
        if (isCkickAllowed) {
            viewModel.saveTrack(track)
            viewModel.clickDebounce()
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment)
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError()
            is SearchState.Empty -> showEmpty()
            is SearchState.History -> showHistory(state.tracks)
            is SearchState.ClearScreen -> showClearScreen()
        }

    }

    private fun showLoading() {
        clearContent()
        searchBinding.progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        clearContent()
        searchBinding.messageError.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        clearContent()
        searchBinding.messageNotFound.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>) {
        clearContent()
        trackAdapter.clearTracks()
        trackAdapter.tracks.addAll(tracks)
        searchBinding.searchRecycler.visibility = View.VISIBLE
    }

    private fun showHistory (tracks: List<Track>) {
        clearContent()
        historyAdapter.tracks = tracks as ArrayList<Track>
        searchBinding.historyLayout.visibility = View.VISIBLE
    }

    private fun showClearScreen () {
        clearContent()
    }

    private fun clearContent () {
        searchBinding.messageNotFound.visibility = View.GONE
        searchBinding.messageError.visibility = View.GONE
        searchBinding.searchRecycler.visibility = View.GONE
        searchBinding.historyLayout.visibility = View.GONE
        searchBinding.progressBar.visibility = View.GONE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchClear () {
        searchBinding.inputText.setText("")
        searchBinding.inputText.clearFocus()
        clearContent()
        viewModel.showHistory()
    }

    override fun onResume() {
        super.onResume()
        historyAdapter.tracks = viewModel.getHistory()
    }
}