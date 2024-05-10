package com.example.ev.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Event")
data class Event(
    @PrimaryKey
    val eventId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "latitude") var latitude: String,
    @ColumnInfo(name = "longitude") var longitude: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "startTime") val startTime: String,
    @ColumnInfo(name = "endTime") val endTime: String,
    @ColumnInfo(name = "entrancefee") val entrancefee: String,
    @ColumnInfo(name = "description") val description: String,

    )