package com.example.haonv

import android.app.Application
import com.example.haonv.di.DIContainer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.init(this)
    }
}