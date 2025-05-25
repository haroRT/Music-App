package com.example.haonv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.haonv.data.local.entity.PlaylistSong
import com.example.haonv.data.local.entity.Song

@Dao
interface PlaylistSongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongToPlaylist(playlistSong: PlaylistSong)

    @Delete
    suspend fun removeSongFromPlaylist(playlistSong: PlaylistSong)


    @Insert
    suspend fun insertAll(crossRefs: List<PlaylistSong>)

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun deleteAllSongsFromPlaylist(playlistId: Int)

    @Transaction
    suspend fun reOrderSongs(playlistId: Int, songIds: List<Int>) {

        deleteAllSongsFromPlaylist(playlistId)
        val crossRefs = songIds.map { songId ->
            PlaylistSong(playlistId, songId)
        }
        insertAll(crossRefs)
    }

}