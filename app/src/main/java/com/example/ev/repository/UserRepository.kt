package com.example.ev.repository

import com.example.ev.database.AppDatabase
import com.example.ev.database.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDatabase: AppDatabase){

    fun getUser() = userDatabase.Dao().getUserProfileRepo()

    fun getUserByUid(uid: String): Flow<User> {
        return userDatabase.Dao().getUserByUID(uid)
    }

    suspend fun updateUser(user: User){
        userDatabase.Dao().update(user)
    }

    suspend fun deleteUser() = userDatabase.Dao().deleteAllUsers()

    suspend fun insertUserRep(user: User){
        userDatabase.Dao().insertUser(user)
    }
}