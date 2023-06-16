package com.bangkit.capstone.fitness.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bangkit.capstone.fitness.MainActivity
import com.bangkit.capstone.fitness.R.string
import com.bangkit.capstone.fitness.databinding.FragmentProfileBinding
import com.bangkit.capstone.fitness.network.apiServices
import com.bangkit.capstone.fitness.network.apiresponse.*
import com.bangkit.capstone.fitness.network.model.User
import com.bangkit.capstone.fitness.network.model.WorkoutPreferences
import com.bangkit.capstone.fitness.ui.authentication.AuthenticationActivity
import com.bangkit.capstone.fitness.ui.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    companion object {
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var idToken: String
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        sessionManager = SessionManager(requireContext())
        idToken = sessionManager.getTokenValue.toString()
        Log.d("token in session", idToken)

        if (isNetworkAvailable(requireContext())) {
            fetchUserFromApiService()

            Log.d("FetchUserApiService", idToken)
        } else {
            fetchUserFromSession()

            Log.d("FetchUserSession", idToken)
        }

        setupAction()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUI(){

        //Data User START
        binding.tvUserName.text = user?.username
        binding.valueHeight.text = (user?.height ?: 0).toString()
        binding.valueWeight.text = (user?.weight ?: 0).toString()

        var inputHeight = binding.valueHeight.text.toString().toInt() ?: 0
        var inputWeight = binding.valueWeight.text.toString().toInt() ?: 0
        binding.btnEditHeight.setOnClickListener {
            showInputDialog("Enter Height", inputHeight.toString()) { value ->
                inputHeight = value.toInt()
                binding.valueHeight.text = inputHeight.toString()
                binding.btnSaveUserData.visibility = View.VISIBLE
            }
        }

        binding.btnEditWeight.setOnClickListener {
            showInputDialog("Enter Weight", inputWeight.toString()) { value ->
                inputWeight = value.toInt()
                binding.valueWeight.text = inputWeight.toString()
                binding.btnSaveUserData.visibility = View.VISIBLE
            }
        }

        binding.btnSaveUserData.setOnClickListener {
            performUpdateUserData(idToken, inputHeight, inputWeight)
            binding.btnSaveUserData.visibility = View.GONE
        }
        //Data User END

        //Workout preference START
        binding.valueWorkoutType.text = (user?.workout_preferences?.type_pref).toString()
        if (user?.workout_preferences?.type_pref == 0){
            binding.nameWorkoutType.text = getString(string.label_empty)
        }else{
            binding.nameWorkoutType.text = workoutTypes[user?.workout_preferences?.type_pref!! - 1]
        }

        binding.valueBodypart.text = (user?.workout_preferences?.bodypart).toString()
        if (user?.workout_preferences?.bodypart == 0){
            binding.nameBodypart.text = getString(string.label_empty)
        }else{
            binding.nameBodypart.text = bodyparts[user?.workout_preferences?.bodypart!! - 1]
        }

        binding.valueEquipment.text = (user?.workout_preferences?.equipment).toString()
        if(user?.workout_preferences?.equipment == 0){
            binding.nameEquipment.text = getString(string.label_empty)
        }else{
            binding.nameEquipment.text = equipments[user?.workout_preferences?.equipment!! - 1]
        }

        binding.valueTrainLevel.text = (user?.workout_preferences?.train_level).toString()
        if(user?.workout_preferences?.train_level == 0){
            binding.nameTrainLevel.text = getString(string.label_empty)
        }else{
            binding.nameTrainLevel.text = levels[user?.workout_preferences?.train_level!! - 1]
        }

        var type_pref = user?.workout_preferences?.type_pref ?: 0
        var bodypart = user?.workout_preferences?.bodypart ?: 0
        var equipment = user?.workout_preferences?.equipment ?: 0
        var train_level = user?.workout_preferences?.train_level ?: 0

        binding.btnEditWorkoutType.setOnClickListener {
            showWorkoutTypeSelectionDialog { selectedOptionNumber ->
                type_pref = selectedOptionNumber
                Log.d("ProfileFragment", "type_pref: $type_pref")
                binding.btnSave.visibility = View.VISIBLE
            }
        }

        binding.btnEditBodypart.setOnClickListener {
            showBodypartSelectionDialog { selectedOptionNumber ->
                bodypart = selectedOptionNumber
                Log.d("ProfileFragment", "bodypart: $bodypart")
                binding.btnSave.visibility = View.VISIBLE
            }
        }

        binding.btnEditEquipment.setOnClickListener {
            showEquipmentSelectionDialog { selectedOptionNumber ->
                equipment = selectedOptionNumber
                Log.d("ProfileFragment", "equipment: $equipment")
                binding.btnSave.visibility = View.VISIBLE
            }
        }

        binding.btnEditTrainLevel.setOnClickListener {
            showTrainLevelSelectionDialog { selectedOptionNumber ->
                train_level = selectedOptionNumber
                Log.d("ProfileFragment", "train_level: $train_level")
                binding.btnSave.visibility = View.VISIBLE
            }
        }

        binding.btnSave.setOnClickListener {
            requestUpdateWorkoutPreference(idToken, bodypart, type_pref, equipment,train_level)
            binding.btnSave.visibility = View.GONE
        }
        //Workout preference END

        //Data User START
        binding.tvUserName.text = user?.username
        binding.valueHeight.text = (user?.height ?: 0).toString()
        binding.valueWeight.text = (user?.weight ?: 0).toString()

        var valueHeight = binding.valueHeight.text.toString().toInt() ?: 0
        var valueWeight = binding.valueWeight.text.toString().toInt() ?: 0
        binding.btnEditHeight.setOnClickListener {
            showInputDialog("Enter Height", valueHeight.toString()) { value ->
                valueHeight = value.toInt()
                binding.valueHeight.text = valueHeight.toString()
                binding.btnSaveUserData.visibility = View.VISIBLE
            }
        }

        binding.btnEditWeight.setOnClickListener {
            showInputDialog("Enter Weight", valueWeight.toString()) { value ->
                valueWeight = value.toInt()
                binding.valueWeight.text = valueWeight.toString()
                binding.btnSaveUserData.visibility = View.VISIBLE
            }
        }

        binding.btnSaveUserData.setOnClickListener {
            performUpdateUserData(idToken, valueHeight, valueWeight)
            binding.btnSaveUserData.visibility = View.GONE
        }
        //Data User END

        //Food preference START
        binding.valueDietType.text = (user?.food_preferences?.diet_type).toString()
        if (user?.food_preferences?.diet_type == 0){
            binding.nameDietType.text = getString(string.label_empty)
        }else{
            binding.nameDietType.text = dietTypes[user?.food_preferences?.diet_type!! - 1]
        }

        binding.valueCuisineType.text = (user?.food_preferences?.cuisine_type).toString()
        if (user?.food_preferences?.cuisine_type == 0){
            binding.nameCuisineType.text = getString(string.label_empty)
        }else{
            binding.nameCuisineType.text = cuisineTypes[user?.food_preferences?.cuisine_type!! - 1]
        }

        var dietType = user?.food_preferences?.diet_type ?: 0
        var cuisineType = user?.food_preferences?.cuisine_type ?: 0

        binding.btnEditDietType.setOnClickListener {
            showDietTypeSelectionDialog { selectedOptionNumber ->
                dietType = selectedOptionNumber
                Log.d("ProfileFragment", "diet_type: $dietType")
                binding.btnSaveFoodPref.visibility = View.VISIBLE
            }
        }

        binding.btnEditCuisineType.setOnClickListener {
            showDietCuisineSelectionDialog { selectedOptionNumber ->
                cuisineType = selectedOptionNumber
                Log.d("ProfileFragment", "cuisine_type: $cuisineType")
                binding.btnSaveFoodPref.visibility = View.VISIBLE
            }
        }

        binding.btnSaveFoodPref.setOnClickListener {
            requestUpdateFoodPreference(idToken, dietType, cuisineType)
            binding.btnSaveFoodPref.visibility = View.GONE
        }
        //Food preference END
    }

    private fun setupAction(){
        binding.btnLogout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle(getString(string.message_logout_confirm))
                ?.setPositiveButton(getString(string.action_confirm)) { _, _ ->
                    sessionManager.clearPreferences()
//                    sessionManager.setBooleanPreference(ConstValue.KEY_IS_LOGGED_IN, false)
                    val intent = Intent(requireContext(), AuthenticationActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish() // Finish the current activity
                }
                ?.setNegativeButton(getString(string.action_cancel), null)
            val alert = alertDialog.create()
            alert.show()
        }

    }

    //START when there is no internet
    private fun fetchUserFromSession() {
        user = sessionManager.getUser()
        Log.d("user in session", "$user")
        setupUI()
    }
    //END when there is no internet


    // START function inputDialog
    private fun showWorkoutTypeSelectionDialog(callback: (Int) -> Unit) {
        val workoutTypes = workoutTypes

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(string.label_workout_type))
            .setItems(workoutTypes) { _, which ->
                val selectedWorkoutTypeNumber = which + 1
                binding.nameWorkoutType.text = workoutTypes[which]
                binding.valueWorkoutType.text = selectedWorkoutTypeNumber.toString()
                callback(selectedWorkoutTypeNumber)
            }
        builder.create().show()
    }

    private fun showBodypartSelectionDialog(callback: (Int) -> Unit) {
        val list = bodyparts

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(string.label_bodypart))
            .setItems(list) { _, which ->
                val selectedOptionNumber = which + 1
                binding.nameBodypart.text = list[which]
                binding.valueBodypart.text = selectedOptionNumber.toString()
                callback(selectedOptionNumber)
            }
        builder.create().show()
    }

    private fun showEquipmentSelectionDialog(callback: (Int) -> Unit) {
        val list = equipments

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(string.label_equipment))
            .setItems(list) { _, which ->
                val selectedOptionNumber = which + 1
                binding.nameEquipment.text = list[which]
                binding.valueEquipment.text = selectedOptionNumber.toString()
                callback(selectedOptionNumber)
            }
        builder.create().show()
    }

    private fun showTrainLevelSelectionDialog(callback: (Int) -> Unit) {
        val list = levels

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(string.label_train_level))
            .setItems(list) { _, which ->
                val selectedOptionNumber = which + 1
                binding.nameTrainLevel.text = list[which]
                binding.valueTrainLevel.text = selectedOptionNumber.toString()
                callback(selectedOptionNumber)
            }
        builder.create().show()
    }

    private fun showInputDialog(title: String, defaultValue: String, callback: (String) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText(defaultValue)

        builder.setTitle(title)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                callback(input.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDietTypeSelectionDialog(callback: (Int) -> Unit) {
        val list = dietTypes

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(string.label_diet_type))
            .setItems(list) { _, which ->
                val selectedOptionNumber = which + 1
                binding.nameDietType.text = list[which]
                binding.valueDietType.text = selectedOptionNumber.toString()
                callback(selectedOptionNumber)
            }
        builder.create().show()
    }

    private fun showDietCuisineSelectionDialog(callback: (Int) -> Unit) {
        val list = cuisineTypes

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(string.label_cuisine_type))
            .setItems(list) { _, which ->
                val selectedOptionNumber = which + 1
                binding.nameCuisineType.text = list[which]
                binding.valueCuisineType.text = selectedOptionNumber.toString()
                callback(selectedOptionNumber)
            }
        builder.create().show()
    }

// END function inputDialog

    //FUNCTION REQUEST API start
    private fun fetchUserFromApiService() {
        (activity as MainActivity).showLoading(true)

        val call = apiServices.fetchUser(idToken)
        call.enqueue(object : Callback<UserFetchResponse> {
            override fun onResponse(call: Call<UserFetchResponse>, response: Response<UserFetchResponse>) {
                (activity as MainActivity).showLoading(false)
                if (response.isSuccessful) {
                    val fetchResponse = response.body()
                    user = fetchResponse?.user

                    // Process the user object
                    Log.d("user from apiservice", "$user")
                    setupUI()
                } else {
                    // Handle the error response
                    val responseCode = response.code()
                    val errorMessage = response.message()
                    Log.e("API Error", "Response code: $responseCode, Error message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<UserFetchResponse>, t: Throwable) {
                (activity as MainActivity).showLoading(false)
                // Handle the network failure
                Log.e("API Error", "Network failure: ${t.message}")
                fetchUserFromSession()
            }
        })
    }

    fun requestUpdateWorkoutPreference(idToken: String,bodypart: Int, type_pref: Int, equipment: Int,train_level: Int){
        (activity as MainActivity).showLoading(true)

        val workoutPreferences = WorkoutPreferences(equipment, train_level, type_pref, bodypart)
        val call = apiServices.updateWorkoutPreference(idToken, workoutPreferences)
        call.enqueue(object : Callback<UpdateWorkoutResponse> {
            override fun onResponse(
                call: Call<UpdateWorkoutResponse>,
                response: Response<UpdateWorkoutResponse>
            ) {
                (activity as MainActivity).showLoading(false)
                if (response.isSuccessful) {
                    val updateWorkoutResponse = response.body()
                    // Handle the response here
                    if (updateWorkoutResponse != null) {
                        user?.workout_preferences = updateWorkoutResponse.new_preferences
                    }

                    Log.d("updateWorkoutResponse", "$updateWorkoutResponse")
                    Log.d("user preference update", "$user")
                    sessionManager.setUser(ConstValue.KEY_USER, user)

                    binding.btnSave.visibility = View.GONE

                } else {
                    (activity as MainActivity).showLoading(false)
                    // Handle API error
                    Log.e("updateWorkoutResponse", "API call failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UpdateWorkoutResponse>, t: Throwable) {
                (activity as MainActivity).showLoading(false)
                // Handle network error
                Log.e("updateWorkoutResponse", "API call failed: ${t.message}")
                showOKDialog(requireContext(), "Network Error", getString(string.error_internet))
            }
        })
    }
    private fun performUpdateUserData(idToken: String, valueHeight: Int, valueWeight: Int) {
        (activity as MainActivity).showLoading(true)

        val updateRequest = UpdateRequest(valueHeight, valueWeight)
        val call = apiServices.updateUser(idToken, updateRequest)
        call.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                (activity as MainActivity).showLoading(false)
                if (response.isSuccessful) {
                    val updateResponse = response.body()
                    // Handle the response here
                    if (updateResponse != null) {
                        user?.height = updateResponse.new_height
                        user?.weight = updateResponse.new_weight
                        sessionManager.setUser(ConstValue.KEY_USER, user)
                        val workoutPref = sessionManager.getUser()?.workout_preferences
                        val foodPref = sessionManager.getUser()?.food_preferences

                        val justRegistered = workoutPref?.equipment == 0 || workoutPref?.train_level == 0 || workoutPref?.type_pref == 0 || workoutPref?.bodypart == 0 ||
                                foodPref?.diet_type == 0 || foodPref?.cuisine_type == 0
                        sessionManager.setBooleanPreference(ConstValue.JUST_REGISTERED, justRegistered)
                    }
                } else {
                    // Handle API error
                    Log.e("UpdateUser", "API call failed with code: ${response.code()}")
                    showOKDialog(requireContext(), "Error", getString(string.session_invalid))
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                (activity as MainActivity).showLoading(false)
                // Handle network error
                Log.e("UpdateUser", "API call failed: ${t.message}")
                showOKDialog(requireContext(), "Network Error", getString(string.error_internet))
            }
        })
    }

    private fun requestUpdateFoodPreference(idToken: String, dietType: Int, cuisineType: Int) {
        (activity as MainActivity).showLoading(true)

        val updateRequest = UpdateFoodRequest(dietType, cuisineType)
        val call = apiServices.updateFood(idToken, updateRequest)
        call.enqueue(object : Callback<UpdateFoodResponse> {
            override fun onResponse(call: Call<UpdateFoodResponse>, response: Response<UpdateFoodResponse>) {
                (activity as MainActivity).showLoading(false)
                if (response.isSuccessful) {
                    val updateResponse = response.body()
                    // Handle the response here
                    if (updateResponse != null) {
                        user?.food_preferences?.diet_type = updateResponse.newPreferences.diet_type
                        user?.food_preferences?.cuisine_type = updateResponse.newPreferences.cuisine_type
                        sessionManager.setUser(ConstValue.KEY_USER, user)
                        val workoutPref = sessionManager.getUser()?.workout_preferences
                        val foodPref = sessionManager.getUser()?.food_preferences

                        val justRegistered = workoutPref?.equipment == 0 || workoutPref?.train_level == 0 || workoutPref?.type_pref == 0 || workoutPref?.bodypart == 0 ||
                                foodPref?.diet_type == 0 || foodPref?.cuisine_type == 0
                        sessionManager.setBooleanPreference(ConstValue.JUST_REGISTERED, justRegistered)
                    }
                } else {
                    // Handle API error
                    Log.e("UpdateFood", "API call failed with code: ${response.code()}")
                    showOKDialog(requireContext(), "Error", getString(string.session_invalid))
                }
            }

            override fun onFailure(call: Call<UpdateFoodResponse>, t: Throwable) {
                (activity as MainActivity).showLoading(false)
                // Handle network error
                Log.e("UpdateFood", "API call failed: ${t.message}")
                showOKDialog(requireContext(), "Network Error", getString(string.error_internet))
            }
        })
    }
    //FUNCTION REQUEST API end

}