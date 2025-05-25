package com.example.haonv.ui.home.detail.track

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.data.remote.ApiClient
import com.example.haonv.data.remote.response.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopTrackViewModel : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    fun loadTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.rankService.getTopTrack(3133)
                if (response.isSuccessful) {
                    val trackRes = response.body()
                    _tracks.postValue(trackRes?.topTracks?.track)
                    Log.i("tracks", "SUCCESS")
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
                Log.i("songs", "ERROR NETWORK")
            }
        }
    }
}