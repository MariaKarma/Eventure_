package com.example.ev.data

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name")
    val name: String,
    @SerializedName("dial_code")
    val dialcode: String,
    @SerializedName("code")
    val code: String

)