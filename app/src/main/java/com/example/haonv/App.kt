package com.example.haonv

import android.app.Application
import androidx.room.Room
import com.example.haonv.data.PlaylistRepository
import com.example.haonv.data.SongRepository
import com.example.haonv.data.UserRepository
import com.example.haonv.data.local.AppDatabase
import java.util.Locale

class App : Application() {
    lateinit var database: AppDatabase
    lateinit var userRepository: UserRepository
    lateinit var songRepository: SongRepository
    lateinit var playlistRepository: PlaylistRepository

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        userRepository = UserRepository(database.userDao())
        songRepository = SongRepository(context = applicationContext, database.songDao(), database.playlistSongDao())
        playlistRepository = PlaylistRepository(database.playlistDao(), database.playlistSongDao())

        SharedPref.init(this)
        val locale = Locale(SharedPref.language)
        Locale.setDefault(locale)

        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
    }
}