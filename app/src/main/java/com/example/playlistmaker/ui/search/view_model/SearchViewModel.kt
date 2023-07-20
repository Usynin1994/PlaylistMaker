package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.search.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.SearchState

class SearchViewModel (private val trackInteractor: TrackInteractor): ViewModel()  {

    private val stateLiveData = MutableLiveData<SearchState>()

    init {
        showHistory()
    }

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null

    fun observeState(): LiveData<SearchState> = stateLiveData

    private val clickLiveData = MutableLiveData<Boolean>()
    fun observeClick(): LiveData<Boolean> = clickLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        // Если это убрать в дебаунс - ловится баг с вьюхами, не стал делать
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            trackInteractor.searchTrack(newSearchText, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?, errorCode: Int?) {

                    when {
                        errorCode != null -> {
                            renderState(
                                SearchState.Error()
                            )
                        }

                        foundTracks.isNullOrEmpty() -> {
                            renderState(
                                SearchState.Empty()
                            )
                        }

                        else -> {
                            renderState(
                                SearchState.Content(
                                    tracks = foundTracks,
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
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

    fun clickDebounce() {
        if (clickLiveData.value != true) {
            clickLiveData.postValue(false)
            handler.postDelayed({ clickLiveData.postValue(true) }, CLICK_DEBOUNCE_DELAY)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}