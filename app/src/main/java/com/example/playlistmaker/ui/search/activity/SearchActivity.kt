package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.player.activity.PlayerActivity

const val EXTRA_KEY = "TRACK_KEY"

class SearchActivity : AppCompatActivity(), TrackAdapter.ClickListener {

    private val searchBinding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val trackAdapter = TrackAdapter (this)
    private val historyAdapter = TrackAdapter (this)
    private var isCkickAllowed = true

    private lateinit var toolbar: Toolbar
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(searchBinding.root)

        toolbar = searchBinding.searchToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this,
            SearchViewModel.getViewModelFactory(this))[SearchViewModel::class.java]

        viewModel.showHistory()

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeClick().observe(this) {
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
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(
                EXTRA_KEY, track)
            viewModel.clickDebounce()
            startActivity(intent)
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
        Log.d("TEST", tracks.toString())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
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
