package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.PlaylistDao
import com.example.playlistmaker.media_library.data.db.TrackDao
import com.example.playlistmaker.media_library.data.db.TrackPlaylistDao
import com.example.playlistmaker.media_library.data.db.convertor.PlaylistDbConverter
import com.example.playlistmaker.media_library.data.db.convertor.TrackDbConverter
import com.example.playlistmaker.media_library.data.repository.FavoritesRepositoryImpl
import com.example.playlistmaker.media_library.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.media_library.domain.api.FavoritesInteractor
import com.example.playlistmaker.media_library.domain.api.FavoritesRepository
import com.example.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_library.domain.api.PlaylistRepository
import com.example.playlistmaker.media_library.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.media_library.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.media_library.ui.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.media_library.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModel = module {
    single<TrackDao> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME).build().trackDao()
    }

    single<PlaylistDao> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME).build().playlistDao()
    }

    single<TrackPlaylistDao> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME).build().trackPlaylistDao()
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    single {
        TrackDbConverter()
    }

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(get(),get(), get(), androidContext())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single {
        PlaylistDbConverter(get())
    }
}