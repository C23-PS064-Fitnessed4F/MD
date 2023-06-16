package com.bangkit.capstone.fitness.ui.diet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.fitness.R
import com.bangkit.capstone.fitness.databinding.FragmentDietBinding
import com.bangkit.capstone.fitness.network.ApiService
import com.bangkit.capstone.fitness.network.apiresponse.FoodResponse
import com.bangkit.capstone.fitness.network.model.User
import com.bangkit.capstone.fitness.network.retrofit
import com.bangkit.capstone.fitness.ui.utils.ConstValue
import com.bangkit.capstone.fitness.ui.utils.SessionManager
import com.bangkit.capstone.fitness.ui.utils.showOKDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DietFragment : Fragment() {

    companion object {
        fun newInstance() = DietFragment()
    }

    private lateinit var binding: FragmentDietBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var idToken: String
    private var user: User? = null

    private lateinit var rvFoods: RecyclerView
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDietBinding.inflate(inflater, container, false)
        val view = binding.root
        sessionManager = SessionManager(requireContext())
        idToken = sessionManager.getTokenValue.toString()
        Log.d("token in session", idToken)

        setupRecyclerView()
        fetchFoods()

        return view
    }
    private fun setupRecyclerView() {
        rvFoods = binding.rvFoods
        rvFoods.layoutManager = LinearLayoutManager(requireContext())
        foodAdapter = FoodAdapter()
        rvFoods.adapter = foodAdapter
    }

    //API SERVICE start
    private fun fetchFoods() {

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getFood(idToken)

        call.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foodResponse = response.body()
                    if (foodResponse != null) {
                        val recipes = foodResponse.result
                        foodAdapter.setData(recipes) // Set data ke adapter RecyclerView
                        Log.d("GetFood", "$recipes")

                        sessionManager.setFoodResponse(ConstValue.KEY_FOOD_RESPONSE, foodResponse)
                    }
                } else {
                    showOKDialog(requireContext(), "No Data", getString(R.string.error_profile))
                    Log.e("getFood", response.message())
                    Log.e("getFood", "API call failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Log.e("getFood", "API call failed: ${t.message}")
                showOKDialog(requireContext(), "Network Error", getString(R.string.error_internet))
                val foodResponse = sessionManager.getFoodResponse()
                if (foodResponse != null) {
                    val recipes = foodResponse.result
                    foodAdapter.setData(recipes) // Set data ke adapter RecyclerView
                    Log.d("GetFood from session", "$recipes")
                }
            }
        })
    }

}