package com.example.playlistmaker.media_library.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.media_library.ui.fragment.MediaLibraryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.pager.adapter = MediaLibraryViewPagerAdapter(fragmentManager = supportFragmentManager, lifecycle = lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) {tab, position ->
            when (position) {
                0 -> tab.apply {
                    text = getString(R.string.favorites)
                }
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}