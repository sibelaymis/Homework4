package com.patika.homework4.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user") val user: User,
    val token : String
)
