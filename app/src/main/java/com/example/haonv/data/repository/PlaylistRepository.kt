package com.example.haonv.data.repository

import com.example.haonv.data.local.dao.PlaylistDao
import com.example.haonv.data.local.dao.PlaylistSongDao
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.entity.PlaylistSong
import com.example.haonv.data.local.model.PlaylistWithCountSong

class PlaylistRepository(
    private val playlistDao: PlaylistDao,
    private val playlistSongDao: PlaylistSongDao
) {

    fun getPlaylistByUserId(userId: Int): List<PlaylistWithCountSong> {
        return playlistDao.getPlaylistsByUserId(userId)
    }

    suspend fun insert(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylistById(playlistId)
    }

    suspend fun addSongToPlaylist(playlistId: Int, songId: Int) {
        playlistSongDao.addSongToPlaylist(
            PlaylistSong(
                playlistId = playlistId,
                songId = songId
            )
        )
    }

    suspend fun updateNamePlaylist(playlistId: Int, newName: String) {
        playlistDao.updateNamePlaylist(playlistId, newName)
    }

    suspend fun removeSongFromPlaylist(playlistId: Int, songId: Int) {
        playlistSongDao.removeSongFromPlaylist(
            PlaylistSong(
                playlistId = playlistId,
                songId = songId,
            )
        )
    }


}

