package com.example.haonv.ui.home.detail.profile

import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.haonv.App
import com.example.haonv.data.UserRepository
import com.example.haonv.data.local.dao.UserDao
import com.example.haonv.data.local.entity.User
import com.example.haonv.ui.library.LibraryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: App) : AndroidViewModel(application){
    private val _user = MutableLiveData <User>()
    val user: LiveData<User> get() = _user
    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: LiveData<Boolean> get() = _isChanged
    private val _uriImage = MutableLiveData<Uri>()
    val uriImage: LiveData<Uri> get() = _uriImage

    private val userRepository: UserRepository = application.userRepository


    fun loadProfile(userId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userRepository.getUserById(userId))
        }
    }

    fun setIsChanged(isChanged: Boolean){
        _isChanged.value = isChanged
    }

    fun setUriImage(uri: Uri){
        _uriImage.value = uri
    }


    fun updateImageUrl(userId: Int, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateImageUrl(userId, imageUrl)
            loadProfile(userId)
        }

    }

    class ProfileViewModelFactory(
        private val application: App
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}