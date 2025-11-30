package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.convertor.TrackDbConvertor
import com.example.playlistmaker.media_library.data.repository.FavoritesRepositoryImpl
import com.example.playlistmaker.media_library.domain.api.FavoritesInteractor
import com.example.playlistmaker.media_library.domain.api.FavoritesRepository
import com.example.playlistmaker.media_library.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.media_library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModel = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel()
    }

    single {
        TrackDbConvertor()
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}