package com.bangkit.capstone.fitness.network.apiresponse

import com.google.gson.annotations.SerializedName

data class WorkoutResponse(
    @SerializedName("result")
    val result: List<String>
)

data class FoodResponse(
    @SerializedName("result")
    val result: List<Recipe>
)

data class Recipe(
    @SerializedName("Recipe_name")
    var recipeName: String,
    @SerializedName("Diet_type")
    val dietType: String,
    @SerializedName("Cuisine_type")
    var cuisineType: String,
    @SerializedName("Protein(g)")
    var protein: Float,
    @SerializedName("Carbs(g)")
    var carbs: Float,
    @SerializedName("Fat(g)")
    var fat: Float,
    @SerializedName("Calories")
    var calories: Float
)