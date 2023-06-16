package com.bangkit.capstone.fitness.network

import com.bangkit.capstone.fitness.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// using Retrofit
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.API_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiServices: ApiService = retrofit.create(ApiService::class.java)