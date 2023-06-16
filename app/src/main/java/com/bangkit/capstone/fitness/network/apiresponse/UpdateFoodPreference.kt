package com.bangkit.capstone.fitness.network.apiresponse

import com.bangkit.capstone.fitness.network.model.FoodPreferences
import com.google.gson.annotations.SerializedName

data class UpdateFoodRequest(
    @SerializedName("diet_type")
    val dietType: Int,
    @SerializedName("cuisine_type")
    val cuisineType: Int
)

data class UpdateFoodResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("new_preferences")
    val newPreferences: FoodPreferences
)