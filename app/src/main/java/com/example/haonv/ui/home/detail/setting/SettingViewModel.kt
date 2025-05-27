package com.example.haonv.ui.home.detail.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.haonv.SharedPref

class SettingViewModel(
    private val sharedPref: SharedPref
) : ViewModel() {
    private val _language = MutableLiveData<String>()
    val language: MutableLiveData<String> = _language
    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: MutableLiveData<Boolean> = _isChanged

    fun setLanguage(language: String) {
        _language.value = language
    }

    fun setIsChanged(isChanged: Boolean) {
        _isChanged.value = isChanged
    }

    fun getLanguage(): String {
        return sharedPref.language
    }

    fun setLanguageSharedPref(language: String) {
        sharedPref.language = language
    }
}