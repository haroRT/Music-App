package com.example.haonv.data

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.example.haonv.data.local.dao.PlaylistSongDao
import com.example.haonv.data.local.dao.SongDao
import com.example.haonv.data.local.entity.Song

class SongRepository(
    private val context: Context,
    private val songDao: SongDao,
    private val playlistSongDao: PlaylistSongDao
) {
    suspend fun insert(song: Song) {
        songDao.insertSong(song)
    }

    suspend fun updateListSong(playlistId: Int, songIds: List<Int>) {
        playlistSongDao.reOrderSongs(playlistId, songIds)
    }

    fun getSongsByPlaylist(playlistId: Int): List<Song> {
        return songDao.getSongsByPlaylist(playlistId)
    }


    fun getSongsFromLocal(): List<Song> {
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )
        val selection = "${MediaStore.Audio.Media.DURATION} > 0"
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        )
        val listSongs = mutableListOf<Song>()
        cursor?.use {
            val id = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val idName = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val idData = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val idAuthor = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val idImageUrl = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val idDuration = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val idSong = it.getInt(id)
                val name = it.getString(idName)
                val data = it.getString(idData)
                val author = it.getString(idAuthor)
                val imageUrl = it.getString(idImageUrl)
                val duration = it.getLong(idDuration)
                listSongs.add(
                    Song(
                        id = idSong,
                        name = name,
                        mp3Data = data,
                        author = author,
                        imageURL = "content://media/external/audio/albumart/$imageUrl",
                        duration = duration
                    )
                )
            }
        }
        return listSongs.toList()
    }
}

