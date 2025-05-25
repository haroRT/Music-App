package com.example.haonv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.SkipQueryVerification
import androidx.room.Transaction
import androidx.room.Update
import com.example.haonv.data.local.entity.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    fun getAllSongs(): List<Song>

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongById(songId: Int): Song?

    @Query("""
        SELECT s.* FROM playlist_songs ps
        INNER JOIN songs s ON ps.songId = s.id
        WHERE ps.playlistId = :playlistId
        ORDER BY duration ASC
    """)
    fun getSongsByPlaylist(playlistId: Int): List<Song>

    @Query("""
        SELECT s.* FROM songs s
        INNER JOIN playlists p ON s.id = p.id
        INNER JOIN users u ON p.id = u.id
        INNER JOIN playlist_songs ps ON s.id = ps.songId
        WHERE u.id = :userId
    """)
    fun getSongsFromUser(userId: Int): List<Song>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: Song)

    @Update
    suspend fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)
}