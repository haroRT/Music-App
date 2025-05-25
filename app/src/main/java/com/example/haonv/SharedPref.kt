package com.example.haonv

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPref {
    private lateinit var sharePref: SharedPreferences

    object Key {
        const val USER_ID = "userId"
        const val LANGUAGE = "language"
        const val USER_NAME = "userName"
        const val PASSWORD = "password"
        const val REMEMBER_LOGIN = "rememberLogin"
    }

    fun init(context: Context) {
        if (!::sharePref.isInitialized) {
            sharePref = context.applicationContext.getSharedPreferences(
                "${context.packageName}_pref",
                MODE_PRIVATE
            )
        }
    }

    fun clear() {
        sharePref.edit().clear().apply()
    }

    fun clearUserId() {
        sharePref.edit().remove(Key.USER_ID).apply()
    }

    var userId: Int
        get() = sharePref.getInt(Key.USER_ID, -1)
        set(value) = sharePref.edit().putInt(Key.USER_ID, value).apply()

    var language: String
        get() = sharePref.getString(Key.LANGUAGE, "en") ?: "en"
        set(value) = sharePref.edit().putString(Key.LANGUAGE, value).apply()

    var userName: String
        get() = sharePref.getString(Key.USER_NAME, "") ?: ""
        set(value) = sharePref.edit().putString(Key.USER_NAME, value).apply()

    var password: String
        get() = sharePref.getString(Key.PASSWORD, "") ?: ""
        set(value) = sharePref.edit().putString(Key.PASSWORD, value).apply()

    var rememberLogin: Boolean
        get() = sharePref.getBoolean(Key.REMEMBER_LOGIN, false)
        set(value) = sharePref.edit().putBoolean(Key.REMEMBER_LOGIN, value).apply()
}
