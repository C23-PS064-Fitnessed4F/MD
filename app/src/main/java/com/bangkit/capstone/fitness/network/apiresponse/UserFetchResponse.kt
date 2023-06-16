package com.bangkit.capstone.fitness.network.apiresponse

import com.bangkit.capstone.fitness.network.ApiService
import com.bangkit.capstone.fitness.network.model.User
import com.bangkit.capstone.fitness.network.retrofit
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class UserFetchResponse(
    @SerializedName("user")
    val user: User
)

fun fetchUser(idToken: String) {
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    val call = apiService.fetchUser(idToken)
    var user: User? = null

    call.enqueue(object : Callback<UserFetchResponse> {
        override fun onResponse(call: Call<UserFetchResponse>, response: Response<UserFetchResponse>) {
            if (response.isSuccessful) {
                val fetchResponse = response.body()
                user = fetchResponse?.user

                // Process the user object

            } else {
                // Handle the error response
            }
        }

        override fun onFailure(call: Call<UserFetchResponse>, t: Throwable) {
            // Handle the network failure
        }
    })
}
