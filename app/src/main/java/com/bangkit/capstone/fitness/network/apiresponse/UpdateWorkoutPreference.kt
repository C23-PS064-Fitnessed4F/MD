package com.bangkit.capstone.fitness.network.apiresponse

import com.bangkit.capstone.fitness.network.model.WorkoutPreferences
import com.google.gson.annotations.SerializedName

data class UpdateWorkoutResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("new_preferences")
    val new_preferences: WorkoutPreferences
)