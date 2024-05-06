package com.example.ev.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM User WHERE UserId = :uid")
    fun getUserByUID(uid: String): Flow<User>

    @Query("SELECT * FROM User WHERE UserId = :uid")
    fun getUserByUID2(uid: String): User

    @Query("SELECT * FROM User LIMIT 1")
    fun getUserProfileRepo(): Flow<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM USER")
    suspend fun deleteAllUsers()

    @Update
    suspend fun update(user: User)
}