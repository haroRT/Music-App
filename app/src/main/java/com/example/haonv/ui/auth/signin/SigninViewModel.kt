package com.example.haonv.ui.auth.signin

import androidx.lifecycle.*
import com.example.haonv.SharedPref
import com.example.haonv.data.repository.UserRepository
import com.example.haonv.data.local.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SigninViewModel(
    private val sharedPref: SharedPref,
    private val repository: UserRepository
) : ViewModel() {
    private val _validationUsernameMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> = _validationUsernameMessage
    private val _validationPasswordMessage = MutableLiveData<String>()
    val validationPasswordMessage: LiveData<String> = _validationPasswordMessage
    private val _passwordVisible = MutableLiveData<Boolean>(false)
    val passwordVisible: LiveData<Boolean> = _passwordVisible

    private val _loginState = MutableLiveData<Boolean>(false)
    val loginState: LiveData<Boolean> = _loginState
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun insert(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(user)
        }
    }

    fun validateUsername(username: String): Boolean {
        if (username.isEmpty()) return false
        username.lowercase().forEach {
            if (!('a'..'z').contains(it) && !('0'..'9').contains(it)) {
                return false
            }
        }
        return true
    }

    fun validatePassword(password: String): Boolean {
        if (password.isEmpty()) return false
        password.forEach {
            if (!('0'..'9').contains(it)
                && !('a'..'z').contains(it)
                && !('A'..'Z').contains(it)
            )
                return false
        }
        return true
    }

    fun checkLogin(username: String, password: String) {
        if (!validateUsername(username)) {
            _validationUsernameMessage.value = "Invalid username"
        } else if (!validatePassword(password)) {
            _validationPasswordMessage.value = "Invalid password"
        } else {
            _validationUsernameMessage.value = ""
            _validationPasswordMessage.value = ""

            viewModelScope.launch(Dispatchers.IO) {
                val userResult = repository.getUserByUsername(username)
                if (userResult == null) {
                    _validationUsernameMessage.postValue("Username does not exist")
                } else if (userResult.password == password) {
                    _loginState.postValue(true)
                    sharedPref.userId = userResult.id
                } else {
                    _validationPasswordMessage.postValue("Incorrect password")
                }
            }
        }
    }

    fun getSharedPref(): SharedPref {
        return sharedPref
    }


    fun setDefaultValidationUsernameMessage() {
        _validationUsernameMessage.value = ""
    }

    fun setDefaultValidationPasswordMessage() {
        _validationPasswordMessage.value = ""
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = _passwordVisible.value?.not() ?: false
    }

}
