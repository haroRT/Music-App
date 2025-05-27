package com.example.haonv.di

import android.content.Context
import com.example.haonv.SharedPref
import com.example.haonv.data.repository.PlaylistRepository
import com.example.haonv.data.repository.SongRepository
import com.example.haonv.data.repository.UserRepository
import com.example.haonv.data.local.AppDatabase


class RepositoryContainer(private val context: Context) {

    val sharedPref: SharedPref by lazy { SharedPref(context) }

    val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = AppDatabase.getDatabase(context).userDao()
        )
    }

    val songRepository: SongRepository by lazy {
        SongRepository(
            context = context,
            songDao = AppDatabase.getDatabase(context).songDao(),
            playlistSongDao = AppDatabase.getDatabase(context).playlistSongDao()
        )
    }

    val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepository(
            playlistDao = AppDatabase.getDatabase(context).playlistDao(),
            playlistSongDao = AppDatabase.getDatabase(context).playlistSongDao()
        )
    }



}

