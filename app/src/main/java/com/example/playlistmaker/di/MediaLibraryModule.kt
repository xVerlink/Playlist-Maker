package com.example.playlistmaker.di

import com.example.playlistmaker.media_library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModel = module {
    viewModel {
        FavoritesViewModel()
    }
    viewModel {
        PlaylistsViewModel()
    }
}