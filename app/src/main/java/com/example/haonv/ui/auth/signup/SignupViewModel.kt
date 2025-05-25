package com.example.haonv.ui.auth.signup

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.haonv.App
import com.example.haonv.data.UserRepository
import com.example.haonv.data.local.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel (application: App) : AndroidViewModel(application)  {
    private val _validationUsernameMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> = _validationUsernameMessage

    private val _validationPasswordMessage = MutableLiveData<String>()
    val validationPasswordMessage: LiveData<String> = _validationPasswordMessage

    private val _validationEmailMessage = MutableLiveData<String>()
    val validationEmailMessage: LiveData<String> = _validationEmailMessage

    private val _validationConfirmPasswordMessage = MutableLiveData<String>()
    val validationConfirmPasswordMessage: LiveData<String> = _validationConfirmPasswordMessage

    private val _passwordVisible = MutableLiveData<Boolean>()
    val passwordVisible: LiveData<Boolean> = _passwordVisible

    private val _confirmPasswordVisible = MutableLiveData<Boolean>()
    val confirmPasswordVisible: LiveData<Boolean> = _confirmPasswordVisible

    private val _signupState = MutableLiveData<Boolean>(false)
    val signupState: LiveData<Boolean> = _signupState

    private val repository: UserRepository = application.userRepository

    fun validateUsername(username: String): Boolean {
        if (username.isEmpty()) return false
        username.lowercase().forEach {
            if (!('a'..'z').contains(it) && !('0'..'9').contains(it)) {
                return false
            }
        }
        return true
    }

    fun validateEmail(email: String): Boolean {
        if (email.isEmpty() || email.substringBefore('@').isEmpty()) return false
        else {
            if (email.lowercase().substringAfter('@') != "apero.vn") return false
            email.lowercase().substringBefore('@').forEach {
                if (!('a'..'z').contains(it) && !('0'..'9').contains(it)) {
                    return false
                }
            }
        }
        return true
    }

    fun validatePhoneNumber(phoneNumber: String): Boolean {
        if (phoneNumber.isEmpty() || phoneNumber.length !in 9..11) return false
        phoneNumber.forEach {
            if (!('0'..'9').contains(it))
                return false
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

    fun checkSignup(username: String, password: String, email: String, passwordConfirm: String, phoneNumber: String) {
        if (!validateUsername(username)) {
            _validationUsernameMessage.value = "Invalid format"
        } else if (!validatePassword(password)) {
            _validationPasswordMessage.value = "Invalid format"
        } else if (password != passwordConfirm){
            _validationConfirmPasswordMessage.value = "Confirmation password does not match"

        } else if (!validateEmail(email)){
            _validationEmailMessage.value = "Invalid format"
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insert(User(0, username, password, phoneNumber, email, ""))
                _signupState.postValue(true)
            }
        }
    }

    fun setDefaultValidationUsernameMessage() {
        _validationUsernameMessage.value = ""
    }

    fun setDefaultValidationPasswordMessage() {
        _validationPasswordMessage.value = ""
    }

    fun setDefaultValidationEmailMessage() {
        _validationEmailMessage.value = ""

    }

    fun setDefaultValidationConfirmPasswordMessage() {
        _validationConfirmPasswordMessage.value = ""
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = _passwordVisible.value?.not() ?: false
    }

    fun toggleConfirmPasswordVisibility() {
        _confirmPasswordVisible.value = _confirmPasswordVisible.value?.not() ?: false
    }

    class SignupViewModelFactory(
        private val application: App
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
                return SignupViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}