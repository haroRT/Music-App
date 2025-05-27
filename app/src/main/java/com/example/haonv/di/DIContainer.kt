package com.example.haonv.di

import android.content.Context

object DIContainer {
    private var _appContext: Context? = null
    val appContext: Context
        get() = _appContext
            ?: throw IllegalStateException("AppContext not initialized. Call init(context, ..) first.")

    private var _viewModelContainer: ViewModelContainer? = null
    val viewModelContainer: ViewModelContainer
        get() = _viewModelContainer
            ?: throw IllegalStateException("ViewModelContainer not initialized. Call init(context) first.")

    val repositoryContainer by lazy { RepositoryContainer(appContext) }

    fun init(context: Context){
        _appContext = context
        _viewModelContainer = ViewModelContainer()
    }
}