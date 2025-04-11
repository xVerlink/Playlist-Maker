package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val returnBackButton = findViewById<ImageView>(R.id.search_screen_return_button)
        returnBackButton.setOnClickListener {
            finish()
        }
    }
}