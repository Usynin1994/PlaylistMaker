package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.SearchState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel (private val trackInteractor: TrackInteractor): ViewModel()  {

    private var latestSearchText: String? = null

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true) {
        searchRequest(it)
    }

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData : LiveData<SearchState> = _stateLiveData

    init {
        showHistory()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                trackInteractor
                    .searchTrack(newSearchText)
                    .collect { pair ->
                        when {
                            pair.second != null -> {
                                renderState(
                                    SearchState.Error()
                                )
                            }

                            pair.first.isNullOrEmpty() -> {
                                renderState(
                                    SearchState.Empty()
                                )
                            }

                            else -> {
                                renderState(
                                    SearchState.Content(
                                        tracks = pair.first!!,
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

    fun showHistory() {
        if (trackInteractor.getHistory().isNotEmpty()) {
            renderState(SearchState.History(trackInteractor.getHistory()))
        } else {
            renderState(SearchState.ClearScreen())
        }
    }

    fun saveTrack (track: Track) {
        trackInteractor.saveTrack(track)
    }

    fun clearHistory() {
        trackInteractor.clearHistory()
        renderState(SearchState.ClearScreen())
    }

    fun doLatestSearch () {
        if (!latestSearchText.isNullOrEmpty()) {
            searchRequest(latestSearchText!!)
        }
    }

    fun getHistory() = trackInteractor.getHistory()

    fun saveLastTrack(track: Track) {
        trackInteractor.saveLastTrack(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}