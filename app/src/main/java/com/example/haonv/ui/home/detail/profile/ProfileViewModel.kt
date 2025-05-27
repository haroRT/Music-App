package com.example.haonv.ui.home.detail.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haonv.SharedPref
import com.example.haonv.data.repository.UserRepository
import com.example.haonv.data.local.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val sharedPref: SharedPref,
    private val userRepository: UserRepository
) : ViewModel(){
    private val _user = MutableLiveData <User>()
    val user: LiveData<User> get() = _user
    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: LiveData<Boolean> get() = _isChanged
    private val _uriImage = MutableLiveData<Uri>()
    val uriImage: LiveData<Uri> get() = _uriImage

    fun loadProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userRepository.getUserById(sharedPref.userId))
        }
    }

    fun setIsChanged(isChanged: Boolean){
        _isChanged.value = isChanged
    }

    fun setUriImage(uri: Uri){
        _uriImage.value = uri
    }


    fun updateImageUrl(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateImageUrl(sharedPref.userId, imageUrl)
            loadProfile()
        }

    }

    fun clearUserId(){
        sharedPref.clearUserId()
    }
}