package com.bangkit.capstone.fitness.network

import com.bangkit.capstone.fitness.network.apiresponse.*
import com.bangkit.capstone.fitness.network.model.WorkoutPreferences
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("signup")
    @FormUrlEncoded
    fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") username: String,
        @Field("height") height: Int,
        @Field("weight") weight: Int
    ): Call<RegisterResponse>

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("fetch")
    fun fetchUser(
        @Header("Authorization") idToken: String
    ): Call<UserFetchResponse>

    @POST("update")
    fun updateUser(
        @Header("Authorization") idToken: String,
        @Body updateRequest: UpdateRequest
    ): Call<UpdateResponse>

    @POST("update-workout")
    fun updateWorkoutPreference(
        @Header("Authorization") idToken: String,
        @Body workoutPreferences: WorkoutPreferences
    ): Call<UpdateWorkoutResponse>

    @POST("update-food")
    fun updateFood(
        @Header("Authorization") idToken: String,
        @Body updateRequest: UpdateFoodRequest
    ): Call<UpdateFoodResponse>

    @GET("workout")
    fun getWorkout(
        @Header("Authorization") idToken: String
    ): Call<WorkoutResponse>

    @GET("food-recommender")
    fun getFood(
        @Header("Authorization") idToken: String
    ): Call<FoodResponse>
}
