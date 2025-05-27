package com.example.haonv.di

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.haonv.ui.auth.signin.SigninViewModel
import com.example.haonv.ui.auth.signup.SignupViewModel
import com.example.haonv.ui.home.HomeViewModel
import com.example.haonv.ui.home.detail.profile.ProfileViewModel
import com.example.haonv.ui.home.detail.setting.SettingViewModel
import com.example.haonv.ui.library.LibraryViewModel
import com.example.haonv.ui.playlist.PlaylistViewModel
import com.example.haonv.ui.playlist.playlistdetail.PlaylistDetailViewModel

class ViewModelFactory: AbstractSavedStateViewModelFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    DIContainer.repositoryContainer.sharedPref,
                    DIContainer.repositoryContainer.userRepository
                ) as T
            }

            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(
                    DIContainer.repositoryContainer.sharedPref,
                    DIContainer.repositoryContainer.userRepository
                ) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(
                    DIContainer.repositoryContainer.userRepository
                ) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    DIContainer.repositoryContainer.sharedPref,
                    DIContainer.repositoryContainer.userRepository
                ) as T
            }

            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(
                    DIContainer.repositoryContainer.sharedPref
                ) as T
            }

            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                LibraryViewModel(
                    DIContainer.repositoryContainer.sharedPref,
                    DIContainer.repositoryContainer.songRepository,
                    DIContainer.repositoryContainer.playlistRepository
                ) as T
            }

            modelClass.isAssignableFrom(PlaylistViewModel::class.java) -> {
                PlaylistViewModel(
                    DIContainer.repositoryContainer.sharedPref,
                    DIContainer.repositoryContainer.playlistRepository
                ) as T
            }

            modelClass.isAssignableFrom(PlaylistDetailViewModel::class.java) -> {
                PlaylistDetailViewModel(
                    DIContainer.repositoryContainer.songRepository,
                    DIContainer.repositoryContainer.playlistRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

class ViewModelContainer {

    private val viewModelFactory = ViewModelFactory()

    fun <T : ViewModel> getViewModel(
        owner: ViewModelStoreOwner,
        viewModelClass: Class<T>
    ): T {
        return ViewModelProvider(owner, viewModelFactory)[viewModelClass]
    }
}