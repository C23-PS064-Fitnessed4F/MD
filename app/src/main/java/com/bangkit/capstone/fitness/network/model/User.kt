package com.bangkit.capstone.fitness.network.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("food_preferences")
    var food_preferences: FoodPreferences,
    @SerializedName("username")
    val username: String,
    @SerializedName("weight")
    var weight: Int,
    @SerializedName("height")
    var height: Int,
    @SerializedName("workout_preferences")
    var workout_preferences: WorkoutPreferences
)

data class WorkoutPreferences(
    @SerializedName("equipment")
    val equipment: Int,
    @SerializedName("train_level")
    val train_level: Int,
    @SerializedName("type_pref")
    val type_pref: Int,
    @SerializedName("bodypart")
    val bodypart: Int
)

data class FoodPreferences(
    @SerializedName("diet_type")
    var diet_type: Int,
    @SerializedName("cuisine_type")
    var cuisine_type: Int
)