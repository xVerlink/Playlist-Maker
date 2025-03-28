package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.main_screen_search_button)
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val mediaLibraryButton = findViewById<Button>(R.id.main_screen_media_library_button)
        val mediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
        val mediaLibraryButtonListener = object: View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(mediaLibraryIntent)
            }
        }
        mediaLibraryButton.setOnClickListener(mediaLibraryButtonListener)

        val settingsButton = findViewById<Button>(R.id.main_screen_settings_button)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}