package com.example.playlistmaker.media_library.domain.models

import com.example.playlistmaker.search.domain.models.Track

data class PlaylistUiState(val playlist: Playlist, val tracks: List<Track>)