package com.example.haonv.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.SharedPref
import com.example.haonv.data.repository.PlaylistRepository
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.model.PlaylistWithCountSong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val sharedPref: SharedPref,
    private val playlistRepository: PlaylistRepository
) : ViewModel(){
    private val _playlists = MutableLiveData<List<PlaylistWithCountSong>>()
    val playlists: MutableLiveData<List<PlaylistWithCountSong>> = _playlists
    private val _playlistIsEmpty = MutableLiveData<Boolean>()
    val playlistIsEmpty: LiveData<Boolean> = _playlistIsEmpty


    fun getPlaylistByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistResult = playlistRepository.getPlaylistByUserId(sharedPref.userId)
            _playlists.postValue(playlistResult)
            _playlistIsEmpty.postValue(playlistResult.isEmpty())
        }
    }

    fun removePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.deletePlaylist(playlistId)
            getPlaylistByUserId()
        }
    }

    fun addPlaylist(namePlaylist: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.insert(Playlist(0, namePlaylist, "", "", sharedPref.userId))
            getPlaylistByUserId()
        }

    }

    fun renamePlaylist(playlistId: Int, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.updateNamePlaylist(playlistId, newName)
            getPlaylistByUserId()
        }
    }

}