package com.example.haonv.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.SharedPref
import com.example.haonv.data.repository.UserRepository
import com.example.haonv.data.local.entity.User
import com.example.haonv.data.remote.ApiClient
import com.example.haonv.data.remote.response.Album
import com.example.haonv.data.remote.response.Artist
import com.example.haonv.data.remote.response.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    private val sharedPref: SharedPref,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorNetwork = MutableLiveData<Boolean>(false)
    val isErrorNetwork: LiveData<Boolean> = _isErrorNetwork

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private fun handleError(code: Int) {
        val errorMessage = when (code) {
            400 -> "ERROR 400"
            401 -> "ERROR 401"
            402 -> "ERROR 402"
            403 -> "ERROR 403"
            404 -> "ERROR 404"
            else -> "ERROR"
        }
        Log.i("songs", errorMessage)
    }

    fun loadAllData() {
        _isLoading.value = true
        _isErrorNetwork.value = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val albumsDeferred = async { safeApiCall { ApiClient.rankService.getTopAlbum(6) } }
                val tracksDeferred = async { safeApiCall { ApiClient.rankService.getTopTrack(5) } }
                val artistsDeferred =
                    async { safeApiCall { ApiClient.rankService.getTopArtist(5) } }
                val albumsResponse = albumsDeferred.await()
                val tracksResponse = tracksDeferred.await()
                val artistsResponse = artistsDeferred.await()

                if (albumsResponse != null) {
                    if (albumsResponse.isSuccessful) {
                        albumsResponse.body()?.topAlbums?.album?.let { albums ->
                            _albums.postValue(albums)
                        }
                    } else {
                        handleError(albumsResponse.code())
                    }
                }

                if (tracksResponse != null) {
                    if (tracksResponse.isSuccessful) {
                        tracksResponse.body()?.topTracks?.track?.let { tracks ->
                            _tracks.postValue(tracks)
                        }
                    } else {
                        handleError(tracksResponse.code())
                    }
                }

                if (artistsResponse != null) {
                    if (artistsResponse.isSuccessful) {
                        artistsResponse.body()?.artists?.artist?.let { artists ->
                            _artists.postValue(artists)
                        }
                    } else {
                        handleError(artistsResponse.code())
                    }
                }

            } catch (e: Exception) {
                Log.e("LoadData", "Error loading data: ${e.message}", e)
                _isErrorNetwork.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Response<T>? {
        return try {
            apiCall()
        } catch (e: Exception) {
            _isErrorNetwork.postValue(true)
            Log.e("API", "API call failed: ${e.message}", e)
            null
        }
    }

    fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userRepository.getUserById(sharedPref.userId))
        }
    }
}