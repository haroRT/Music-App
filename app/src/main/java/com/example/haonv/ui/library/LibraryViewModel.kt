package com.example.haonv.ui.library

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.haonv.data.remote.ApiClient
import com.example.haonv.App
import com.example.haonv.data.SongRepository
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.entity.Song
import com.example.haonv.data.local.model.PlaylistWithCountSong
import com.example.haonv.data.remote.response.SongResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import kotlin.random.Random

class LibraryViewModel(application: App) : AndroidViewModel(application) {
    val songRepository: SongRepository = application.songRepository
    val playlistRepository = application.playlistRepository

    private val _songs: MutableLiveData<List<Song>> = MutableLiveData()
    val songs: LiveData<List<Song>> = _songs

    private val _song: MutableLiveData<Song?> = MutableLiveData()
    val song: LiveData<Song?> = _song

    private val _playlists = MutableLiveData<List<PlaylistWithCountSong>>()
    val playlists: MutableLiveData<List<PlaylistWithCountSong>> = _playlists

    private val _isLocal = MutableLiveData<Boolean>(false)
    val isLocal: LiveData<Boolean> = _isLocal

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorNetwork = MutableLiveData<Boolean>(false)
    val isErrorNetwork: LiveData<Boolean> = _isErrorNetwork

    fun getListSongFromRemote() {
        _isLoading.value = true
        val listSongs = mutableListOf<Song>()
        viewModelScope.launch(Dispatchers.IO) {
            val call = ApiClient.musicService.getSongsFromRemote()
            call.enqueue(object : retrofit2.Callback<List<SongResponse>> {
                override fun onResponse(
                    call: Call<List<SongResponse>>,
                    response: Response<List<SongResponse>>
                ) {
                    _isLoading.postValue(false)
                    when {
                        response.isSuccessful -> {
                            val songRes = response.body()
                            songRes?.map {
                                listSongs.add(
                                    Song(
                                        Random.nextInt(10000, 99999),
                                        it.name,
                                        it.author,
                                        "",
                                        it.mp3Data,
                                        it.duration
                                    )
                                )
                            }
                            _songs.postValue(listSongs)
                            Log.i("songs", "SUCCESS")
                        }

                        response.code() == 400 -> {
                            Log.i("songs", "ERROR 400")
                        }

                        response.code() == 401 -> {
                            Log.i("songs", "ERROR 401")
                        }

                        response.code() == 402 -> {
                            Log.i("songs", "ERROR 402")
                        }

                        response.code() == 403 -> {
                            Log.i("songs", "ERROR 403")
                        }

                        response.code() == 404 -> {
                            Log.i("songs", "ERROR 404")
                        }

                        else -> {
                            Log.i("songs", "ERROR")
                        }
                    }
                }

                override fun onFailure(call: Call<List<SongResponse>>, t: Throwable) {
                    _isLoading.postValue(false)
                    _isErrorNetwork.postValue(true)
                    Log.i("songs", "ERROR NETWORK")
                }
            })
        }
    }

    fun getListSongsFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            _songs.postValue(songRepository.getSongsFromLocal())
        }
    }

    fun getPlaylistByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _playlists.postValue(playlistRepository.getPlaylistByUserId(userId))
        }
    }

    fun addSongToPlaylist(playlistId: Int, songId: Int, song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.insert(song)
            playlistRepository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun setIsLocal(isLocal: Boolean) {
        _isLocal.value = isLocal
    }

    fun setIsErrorNetwork(isErrorNetwork: Boolean) {
        _isErrorNetwork.value = isErrorNetwork
    }

    fun setIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setSong(song: Song?){
        _song.value = song
    }


    class LibraryViewModelFactory(
        private val application: App
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
                return LibraryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}