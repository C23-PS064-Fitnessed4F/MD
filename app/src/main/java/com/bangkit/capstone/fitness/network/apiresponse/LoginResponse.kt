package com.bangkit.capstone.fitness.network.apiresponse

import com.bangkit.capstone.fitness.network.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("idToken")
    val idToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("user")
    val user: User?
)