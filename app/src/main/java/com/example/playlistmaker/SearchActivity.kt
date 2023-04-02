package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.retrofit.ItunesApi
import com.example.playlistmaker.retrofit.ItunesResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SAVED_DATA = "SAVED_DATA"
    }

    lateinit var editText:EditText
    lateinit var clearButton:ImageView
    lateinit var savedText:String
    lateinit var searchBinding: ActivitySearchBinding
    lateinit var toolbar: Toolbar
    lateinit var searchRecycler: RecyclerView
    lateinit var placeholderNotFound: View
    lateinit var placeholderError: View
    lateinit var buttonReload: Button

    private val baseUrl = "https://itunes.apple.com"
    val trackList = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val itunesApi = trackList.create(ItunesApi::class.java)
    val trackAdapter = TrackAdapter()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_DATA,savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(SAVED_DATA,"")
        if (savedText.isNotEmpty()) {
            editText.setText(savedText)
            search()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        editText = searchBinding.inputText
        clearButton = searchBinding.clearIcon
        placeholderNotFound = searchBinding.messageNotFound
        placeholderError = searchBinding.messageError
        buttonReload = searchBinding.buttonReload
        savedText = ""
        toolbar = searchBinding.searchToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        searchRecycler = searchBinding.searchRecycler

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        searchRecycler.adapter = trackAdapter

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                savedText = editText.text.toString()
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)

        searchRecycler.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }
        clearButton.setOnClickListener {
            editText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            trackAdapter.clearTracks()
            trackAdapter.notifyDataSetChanged()
            editText.clearFocus()
        }
        buttonReload.setOnClickListener{
            search()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    private fun search() {
        itunesApi.search(editText.text.toString())
            .enqueue(object: Callback<ItunesResult> {
                override fun onResponse(
                    call: Call<ItunesResult>,
                    response: Response<ItunesResult>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackAdapter.clearTracks()
                                trackAdapter.setTracks(response.body()?.results!!)
                                clearPlaceholder()
                            } else {
                                showPlaceholder(trackAdapter, Placeholder.NOTHING_FOUND)
                            }
                        }
                        else -> showPlaceholder(trackAdapter, Placeholder.INTERNET_PROBLEM)
                    }
                }
                override fun onFailure(call: Call<ItunesResult>, t: Throwable) {
                    showPlaceholder(trackAdapter, Placeholder.INTERNET_PROBLEM)
                }
            })
    }
    private fun showPlaceholder (adapter: TrackAdapter, placeholder: Placeholder) {
        adapter.clearTracks()
        clearPlaceholder()
        when (placeholder) {
            Placeholder.NOTHING_FOUND -> placeholderNotFound.visibility = View.VISIBLE
            Placeholder.INTERNET_PROBLEM -> placeholderError.visibility = View.VISIBLE
        }
    }
    private fun clearPlaceholder () {
        placeholderNotFound.visibility = View.GONE
        placeholderError.visibility = View.GONE
    }
}
