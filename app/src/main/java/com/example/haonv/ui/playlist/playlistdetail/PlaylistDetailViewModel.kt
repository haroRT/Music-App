package com.example.haonv.ui.playlist.playlistdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.data.repository.PlaylistRepository
import com.example.haonv.data.repository.SongRepository
import com.example.haonv.data.local.entity.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {
    private val _listSong: MutableLiveData<List<Song>> = MutableLiveData()
    val listSong: LiveData<List<Song>> = _listSong
    private val _song: MutableLiveData<Song?> = MutableLiveData()
    val song: LiveData<Song?> = _song
    private val _playlistId: MutableLiveData<Int> = MutableLiveData()
    val playlistId: LiveData<Int> = _playlistId


    fun loadListSong(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _listSong.postValue(songRepository.getSongsByPlaylist(playlistId))
        }
    }

    fun updateListSong(playlistId: Int, listSong: List<Song>) {
        val songIds = listSong.map { it.id }
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.updateListSong(playlistId, songIds)
        }
    }

    fun removeSongFromPlaylist(playlistId: Int, songId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.removeSongFromPlaylist(playlistId, songId)
            loadListSong(playlistId)
        }
    }

    fun setPlaylistId(playlistId: Int) {
        _playlistId.value = playlistId
    }

}