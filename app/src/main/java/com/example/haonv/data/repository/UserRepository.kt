package com.example.haonv.data.repository

import com.example.haonv.data.local.dao.UserDao
import com.example.haonv.data.local.entity.User



class UserRepository(
    private val userDao: UserDao
) {

    fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    fun getUserById(userId: Int): User?{
        return userDao.getUserByUserId(userId)
    }

    suspend fun updateImageUrl(userId: Int, imageUrl: String) {
        userDao.updateImageUrl(userId, imageUrl)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

}