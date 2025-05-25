package com.example.haonv.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.model.PlaylistWithCountSong


@Dao
interface PlaylistDao {
    @Query("""
        SELECT playlists.*, COUNT(playlist_songs.songId) AS songCount  FROM playlists 
        LEFT JOIN playlist_songs ON playlists.id = playlist_songs.playlistId 
        WHERE userId = :userId 
        GROUP BY playlists.id
    """)
    fun getPlaylistsByUserId(userId: Int): List<PlaylistWithCountSong>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlist: Playlist)

    @Update
    suspend fun update(playlist: Playlist)

    @Delete
    suspend fun delete(playlistId: Playlist)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Int)

    @Query("UPDATE playlists SET name = :newName WHERE id = :playlistId")
    suspend fun updateNamePlaylist(playlistId: Int, newName: String)

}