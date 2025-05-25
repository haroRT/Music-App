package com.example.haonv.ui.playlist.playlistdetail

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.haonv.App
import com.example.haonv.data.PlaylistRepository
import com.example.haonv.data.SongRepository
import com.example.haonv.data.local.entity.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(application: App) : AndroidViewModel(application) {
    private val songRepository: SongRepository = application.songRepository
    private val playlistRepository: PlaylistRepository = application.playlistRepository
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

    class PlaylistDetailViewModelFactory(
        private val application: App
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistDetailViewModel::class.java)) {
                return PlaylistDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}