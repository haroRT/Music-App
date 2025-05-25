package com.example.haonv.ui.home.detail.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.data.remote.ApiClient
import com.example.haonv.data.remote.response.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopAlbumViewModel: ViewModel() {
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.rankService.getTopAlbum(1319)
                if (response.isSuccessful) {
                    val albumRes = response.body()
                    _albums.postValue(albumRes?.topAlbums?.album)
                } else {
                    when (response.code()) {

                    }
                }
            } catch (e: Exception) {
                Log.i("songs", "ERROR NETWORK")
            }
        }
    }
}