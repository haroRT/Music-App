package com.example.haonv.ui.player

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.haonv.App
import com.example.haonv.data.local.entity.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    private val _isBound = MutableLiveData<Boolean>(false)
    val isBound: LiveData<Boolean> = _isBound

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun setSong(song: Song) {
        _song.value = song
    }

    fun setIsPlaying(isPlay: Boolean) {
        _isPlaying.value = isPlay
    }

    fun setIsBound(idBound: Boolean) {
        _isBound.value = idBound
    }
}