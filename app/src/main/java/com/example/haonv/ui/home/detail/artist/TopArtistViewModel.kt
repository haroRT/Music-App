package com.example.haonv.ui.home.detail.artist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.data.remote.ApiClient
import com.example.haonv.data.remote.response.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopArtistViewModel : ViewModel(){
    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    fun loadArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.rankService.getTopArtist(1000)
                if (response.isSuccessful) {
                    val artistRes = response.body()
                    _artists.postValue(artistRes?.artists?.artist)
                } else {
                    when (response.code()) {
                        400 -> Log.i("songs", "ERROR 400")
                        401 -> Log.i("songs", "ERROR 401")
                        402 -> Log.i("songs", "ERROR 402")
                        403 -> Log.i("songs", "ERROR 403")
                        404 -> Log.i("songs", "ERROR 404")
                        else -> Log.i("songs", "ERROR")
                    }
                }
            } catch (e: Exception) {
                Log.i("songs", "${e.message}")
            }
        }
    }
}