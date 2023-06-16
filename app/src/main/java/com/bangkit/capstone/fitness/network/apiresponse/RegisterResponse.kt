package com.bangkit.capstone.fitness.network.apiresponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("idToken")
    val idToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?
)
