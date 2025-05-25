package com.example.haonv.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.haonv.data.local.dao.PlaylistDao
import com.example.haonv.data.local.dao.PlaylistSongDao
import com.example.haonv.data.local.dao.SongDao
import com.example.haonv.data.local.dao.UserDao
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.entity.PlaylistSong
import com.example.haonv.data.local.entity.Song
import com.example.haonv.data.local.entity.User


@Database(
    entities = [
        User::class,
        Playlist::class,
        Song::class,
        PlaylistSong::class,
    ],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun songDao(): SongDao
    abstract fun playlistSongDao(): PlaylistSongDao
}
