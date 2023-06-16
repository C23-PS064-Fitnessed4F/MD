package com.bangkit.capstone.fitness.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.fitness.MainActivity
import com.bangkit.capstone.fitness.R
import com.bangkit.capstone.fitness.databinding.FragmentWorkoutBinding
import com.bangkit.capstone.fitness.network.apiServices
import com.bangkit.capstone.fitness.network.apiresponse.WorkoutResponse
import com.bangkit.capstone.fitness.network.model.User
import com.bangkit.capstone.fitness.network.model.Workout
import com.bangkit.capstone.fitness.ui.utils.ConstValue
import com.bangkit.capstone.fitness.ui.utils.SessionManager
import com.bangkit.capstone.fitness.ui.utils.showOKDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutFragment : Fragment() {

    companion object {
        fun newInstance() = WorkoutFragment()
    }

    private lateinit var binding: FragmentWorkoutBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var idToken: String
    private var user: User? = null

    private lateinit var rvWorkouts: RecyclerView
    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val view = binding.root
        sessionManager = SessionManager(requireContext())
        idToken = sessionManager.getTokenValue.toString()
        Log.d("token in session", idToken)

//        if (isNetworkAvailable(requireContext())) {
//            fetchWorkout()
//        } else {
//            Log.d("FetchFromSession", idToken)
//        }

        setupRecyclerView()
        fetchWorkout()

        return view
    }

    private fun setupRecyclerView() {
        rvWorkouts = binding.rvWorkouts
        rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
        workoutAdapter = WorkoutAdapter(sessionManager)
        rvWorkouts.adapter = workoutAdapter
    }

    fun fetchWorkout() {

        val call = apiServices.getWorkout(idToken)
        call.enqueue(object : Callback<WorkoutResponse> {
            override fun onResponse(call: Call<WorkoutResponse>, response: Response<WorkoutResponse>) {
                if (response.isSuccessful) {
                    val workoutResponse = response.body()
                    if (workoutResponse != null) {
                        val workoutNames = workoutResponse.result
                        val workouts = workoutNames.map { Workout(it) }
                        workoutAdapter.setWorkouts(workouts)

                        Log.d("GetWorkout", "$workoutResponse")
                        //save in session for offline use
                        sessionManager.setWorkoutResponse(ConstValue.KEY_WORKOUT_RESPONSE, workoutResponse)
                    }
                } else {
                    // Handle API error
                    Log.e("GetWorkout", "API call failed with code: ${response.code()}")

                    showOKDialog(requireContext(), "No Data", getString(R.string.error_profile))
                }
            }

            override fun onFailure(call: Call<WorkoutResponse>, t: Throwable) {
                // Handle network error
                Log.e("GetWorkout", "API call failed: ${t.message}")
                showOKDialog(requireContext(), "Network Error", getString(R.string.error_internet))
                val workoutNames = sessionManager.getWorkoutResponse()?.result
                val workouts = workoutNames?.map { Workout(it) }
                if (workouts != null) {
                    workoutAdapter.setWorkouts(workouts)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}