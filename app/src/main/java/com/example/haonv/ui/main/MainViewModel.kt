package com.example.haonv.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.haonv.data.local.entity.Song

class MainViewModel: ViewModel() {
    private val _song = MutableLiveData<Song?>()
    val song: LiveData<Song?> = _song

    private val _listSong = MutableLiveData<List<Song>?>()
    val listSong: LiveData<List<Song>?> = _listSong

    private val _musicBarState = MutableLiveData<Boolean>(false)
    val musicBarState: LiveData<Boolean> = _musicBarState

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _isBound = MutableLiveData<Boolean>(false)
    val isBound: LiveData<Boolean> = _isBound

    fun setSong(song: Song?) {
        _song.value = song
    }

    fun setListSong(listSong: List<Song>) {
        _listSong.value = listSong
    }

    fun setIsPlaying(isPlay: Boolean) {
        _isPlaying.value = isPlay
    }

    fun setMusicBarState(isVisible: Boolean){
        _musicBarState.value = isVisible
    }

    fun setIsBound(isBound: Boolean){
        _isBound.value = isBound
    }

}