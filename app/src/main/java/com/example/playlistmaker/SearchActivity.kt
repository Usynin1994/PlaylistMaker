package com.example.playlistmaker

import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.retrofit.ItunesApi
import com.example.playlistmaker.retrofit.ItunesResult
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val EXTRA_KEY = "TRACK_KEY"

class SearchActivity : AppCompatActivity(), TrackAdapter.ClickListener {

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
    lateinit var historyLayout: View
    lateinit var historyRecycler: RecyclerView
    lateinit var buttonClearHistory: Button
    lateinit var searchHistory: SearchHistory
    lateinit var inputMethodManager: InputMethodManager

    //Создаем Retrofit
    private val baseUrl = "https://itunes.apple.com"
    val trackList = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val itunesApi = trackList.create(ItunesApi::class.java)

    //Создаем адаптеры
    private val trackAdapter = TrackAdapter (this)
    private val historyAdapter = TrackAdapter (this)

     //Сохранение и восстановление текста поиска при повороте
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

        val pref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)

        //Инициализация
        editText = searchBinding.inputText
        clearButton = searchBinding.clearIcon
        placeholderNotFound = searchBinding.messageNotFound
        placeholderError = searchBinding.messageError
        buttonReload = searchBinding.buttonReload
        savedText = ""
        searchRecycler = searchBinding.searchRecycler
        historyLayout = searchBinding.historyLayout
        historyRecycler = searchBinding.historyRecycler
        buttonClearHistory = searchBinding.buttonClearHistory
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //Создаем объект SearchHistory
        searchHistory = SearchHistory (pref)

        //Инициализация тулбара
        toolbar = searchBinding.searchToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Присваиваем адаптеры
        searchRecycler.adapter = trackAdapter
        historyRecycler.adapter = historyAdapter

        //Устанавливаем начальный экран в зависимости от истории поиска
        if (searchHistory.getHistory().isEmpty()) {
            showContent(Placeholder.SUCCESS)
            } else {
                showContent(Placeholder.HISTORY)
            }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (editText.hasFocus() && s?.isNotEmpty() == true) {
                    showContent(Placeholder.SUCCESS)
                }
                if (editText.hasFocus() && s?.isNotEmpty() == false && searchHistory.getHistory().isNotEmpty()) {
                    showContent(Placeholder.HISTORY)
                }
                historyAdapter.notifyDataSetChanged()
            }
            override fun afterTextChanged(s: Editable?) {
                savedText = editText.text.toString()
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        //Отчистка поискового запроса
        clearButton.setOnClickListener {
            searchClear()
        }

        //Выполениние последнего запроса по нажатию
        buttonReload.setOnClickListener{
            search()
        }

        //Чистим историю поиска
        buttonClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            showContent(Placeholder.SUCCESS)
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
                                showContent(Placeholder.SUCCESS, response.body()?.results!!)
                            } else {
                                showContent(Placeholder.NOTHING_FOUND)
                            }
                        }
                        else -> showContent(Placeholder.INTERNET_PROBLEM)
                    }
                }
                override fun onFailure(call: Call<ItunesResult>, t: Throwable) {
                    showContent(Placeholder.INTERNET_PROBLEM)
                }
            })
    }

    //Функция отображения
    private fun showContent (placeholder: Placeholder, trackList: List<Track> = emptyList()) {
        clearContent()
        when (placeholder) {
            Placeholder.NOTHING_FOUND -> placeholderNotFound.visibility = View.VISIBLE
            Placeholder.INTERNET_PROBLEM -> placeholderError.visibility = View.VISIBLE
            Placeholder.SUCCESS -> {
                searchRecycler.visibility = View.VISIBLE;
                trackAdapter.setTracks(trackList)
            }
            Placeholder.HISTORY -> {
                historyLayout.visibility = View.VISIBLE;
                historyAdapter.tracks = searchHistory.getHistory()
                historyAdapter.notifyDataSetChanged()
            }
        }
    }

    //Функция убирает все с экрана
    private fun clearContent () {
        placeholderNotFound.visibility = View.GONE
        placeholderError.visibility = View.GONE
        searchRecycler.visibility = View.GONE
        historyLayout.visibility = View.GONE
    }

    //Функция отчисти поисковой строки
    private fun searchClear () {
        editText.setText("")
        clearContent()
        trackAdapter.clearTracks()
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.clearFocus()
        if (searchHistory.getHistory().isEmpty()) showContent(Placeholder.SUCCESS) else showContent(Placeholder.HISTORY)
        historyAdapter.notifyDataSetChanged()
    }

    //Запуск плеера с выбранным треком
    override fun onClick (track: Track) {
        searchHistory.addTrack(track = track)
        val intent = Intent(this, PlayerActivity::class.java)
        val trackTmp = Gson().toJson(track)
        intent.putExtra(EXTRA_KEY, trackTmp)
        startActivity(intent)
    }

    // Обновляем историю после возврата из плеера
    override fun onResume() {
        super.onResume()
        historyAdapter.tracks = searchHistory.getHistory()
        historyAdapter.notifyDataSetChanged()
    }
}