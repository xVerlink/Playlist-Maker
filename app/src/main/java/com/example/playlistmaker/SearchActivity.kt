package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SAVED_STRING: String = "SAVED_STRING"
    }
    private var input: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val returnBackButton = findViewById<ImageView>(R.id.search_screen_return_button)
        val inputEditText = findViewById<EditText>(R.id.edit_text_search)
        val clearButton = findViewById<ImageView>(R.id.clear_text_icon)
        inputEditText.setText(input)

        returnBackButton.setOnClickListener {
            finish()
        }

        inputEditText.setOnClickListener {
            inputEditText.requestFocus()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        inputEditText.addTextChangedListener(
            {text: CharSequence?, start: Int, count: Int, after: Int ->  },
            {text: CharSequence?, start: Int, before: Int, count: Int ->
                if (text.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            },
            {text: Editable? ->  input = text.toString()}
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_STRING, input)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input = savedInstanceState.getString(SAVED_STRING)

    }
}