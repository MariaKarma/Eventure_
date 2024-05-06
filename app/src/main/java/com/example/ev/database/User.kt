package com.example.ev.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey
    val userId: String,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "phone_country_code") var phoneCountryCode: String,
    @ColumnInfo(name = "phone_number") var phoneNumber: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "role") val role: userRole,


    ) {
    constructor() : this("", "", "", "", "", "", userRole.MEMBER)
}