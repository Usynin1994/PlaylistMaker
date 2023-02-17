package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SAVED_DATA = "SAVED_DATA"
    }

    lateinit var editText:EditText
    lateinit var clearButton:ImageView
    lateinit var savedText:String

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_DATA,savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(SAVED_DATA,"")
        editText.setText(savedText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<View>(R.id.go_back).setOnClickListener {
            finish()
        }

        editText = findViewById<EditText>(R.id.inputText)
        clearButton = findViewById<ImageView>(R.id.clearIcon)
        savedText = ""

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        clearButton.setOnClickListener {
            editText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.clearFocus()
        }
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
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
