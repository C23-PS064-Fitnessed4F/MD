package com.bangkit.capstone.fitness.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.bangkit.capstone.fitness.network.apiresponse.FoodResponse
import com.bangkit.capstone.fitness.network.apiresponse.WorkoutResponse
import com.bangkit.capstone.fitness.network.model.WorkoutPreferences
import com.bangkit.capstone.fitness.network.model.User
import com.bangkit.capstone.fitness.ui.utils.ConstValue.JUST_REGISTERED
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_EMAIL
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_FOOD_RESPONSE
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_IS_LOGGED_IN
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_PREFERENCE
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_REFRESH_TOKEN
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_TOKEN
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_USER
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_USER_NAME
import com.bangkit.capstone.fitness.ui.utils.ConstValue.KEY_WORKOUT_RESPONSE
import com.bangkit.capstone.fitness.ui.utils.ConstValue.PREFERENCE_NAME
import com.google.gson.Gson

class SessionManager(context: Context) {
    private var preferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun setStringPreference(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun setBooleanPreference(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun setUser(key: String, user: User?) {
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString(key, userJson)
        editor.apply()
    }

    fun setWorkoutResponse(key: String, workout: WorkoutResponse?) {
        val gson = Gson()
        val workoutResponseJson = gson.toJson(workout)
        editor.putString(key, workoutResponseJson)
        editor.apply()
    }
    fun getWorkoutResponse(): WorkoutResponse? {
        val gson = Gson()
        val workoutResponseJson = preferences.getString(KEY_WORKOUT_RESPONSE, null)
        return gson.fromJson(workoutResponseJson, WorkoutResponse::class.java)
    }

    fun setFoodResponse(key: String, foodResponse: FoodResponse?) {
        val gson = Gson()
        val foodResponseJson = gson.toJson(foodResponse)
        editor.putString(key, foodResponseJson)
        editor.apply()
    }
    fun getFoodResponse(): FoodResponse? {
        val gson = Gson()
        val foodResponseJson = preferences.getString(KEY_FOOD_RESPONSE, null)
        return gson.fromJson(foodResponseJson, FoodResponse::class.java)
    }

    fun setPreference(key: String, preference: WorkoutPreferences?) {
        val gson = Gson()
        val preferenceJson = gson.toJson(preference)
        editor.putString(key, preferenceJson)
        editor.apply()
    }
    fun clearPreferences() {
        editor.clear().apply()
    }


    val getTokenValue = preferences.getString(KEY_TOKEN, "")
    val getRefreshTokenValue = preferences.getString(KEY_REFRESH_TOKEN, "")
    val isUserLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false)
    val getUserNameValue = preferences.getString(KEY_USER_NAME, "")
    val getEmailValue = preferences.getString(KEY_EMAIL, "")
    val getJustRegistered = preferences.getBoolean(JUST_REGISTERED, false)

    fun getUser(): User? {
        val gson = Gson()
        val userJson = preferences.getString(KEY_USER, null)
        return gson.fromJson(userJson, User::class.java)
    }
    fun getPreference(): WorkoutPreferences? {
        val gson = Gson()
        val userJson = preferences.getString(KEY_PREFERENCE, null)
        return gson.fromJson(userJson, WorkoutPreferences::class.java)
    }
}