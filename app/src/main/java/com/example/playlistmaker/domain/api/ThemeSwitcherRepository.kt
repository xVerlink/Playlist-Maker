package com.example.playlistmaker.domain.api

interface ThemeSwitcherRepository {

    fun readFlag(): Boolean

    fun writeFlag(flag: Boolean)
}