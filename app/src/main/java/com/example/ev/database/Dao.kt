package com.example.ev.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ev.data.Events
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
    suspend fun updateUser(user: User)




    @Query("SELECT * FROM Event WHERE eventId = :uid")
    fun getEventByUID(uid: String): Flow<Event>

    @Query("SELECT * FROM Event WHERE eventId = :uid")
    fun getEventByUID2(uid: String): Event

    @Query("SELECT * FROM Event LIMIT 1")
    fun getEventProfileRepo(): Flow<Event>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: Event)
    @Query("SELECT COUNT(*) FROM Event")
    suspend fun getEventsCount(): Int
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllEvents(events: List<Event>)
    @Query("SELECT * FROM Event WHERE category LIKE :category AND date BETWEEN :startDate AND :endDate")
    fun getEventsFilteredByCategoryAndDate(category: String, startDate: String, endDate: String): Flow<List<Event>>
    @Query("DELETE FROM EVENT")
    suspend fun deleteAllEvents()

    @Update
    suspend fun updateEvent(event: Event)



}