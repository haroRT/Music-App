package com.example.haonv.ui.playlist

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.haonv.App
import com.example.haonv.SharedPref
import com.example.haonv.data.PlaylistRepository
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.model.PlaylistWithCountSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(application: App) : AndroidViewModel(application){
    private val _playlists = MutableLiveData<List<PlaylistWithCountSong>>()
    val playlists: MutableLiveData<List<PlaylistWithCountSong>> = _playlists
    private val _playlistIsEmpty = MutableLiveData<Boolean>()
    val playlistIsEmpty: LiveData<Boolean> = _playlistIsEmpty

    private val playlistRepository: PlaylistRepository = application.playlistRepository

    fun getPlaylistByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistResult = playlistRepository.getPlaylistByUserId(userId)
            _playlists.postValue(playlistResult)
            _playlistIsEmpty.postValue(playlistResult.isEmpty())
        }
    }

    fun removePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.deletePlaylist(playlistId)
            getPlaylistByUserId(SharedPref.userId)
        }
    }

    fun addPlaylist(userId: Int, namePlaylist: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.insert(Playlist(0, namePlaylist, "", "", userId))
            getPlaylistByUserId(userId)
        }

    }

    fun renamePlaylist(playlistId: Int, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.updateNamePlaylist(playlistId, newName)
            getPlaylistByUserId(SharedPref.userId)
        }
    }

    class PlaylistViewModelFactory(
        private val application: App
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
                return PlaylistViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}