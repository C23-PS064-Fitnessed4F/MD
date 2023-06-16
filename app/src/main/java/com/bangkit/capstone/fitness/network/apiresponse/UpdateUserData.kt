package com.bangkit.capstone.fitness.network.apiresponse

import com.google.gson.annotations.SerializedName

data class UpdateRequest(
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int
)

data class UpdateResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("new_height")
    val new_height: Int,
    @SerializedName("new_weight")
    val new_weight: Int
)
