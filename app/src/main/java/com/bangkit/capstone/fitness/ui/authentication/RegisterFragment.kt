package com.bangkit.capstone.fitness.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bangkit.capstone.fitness.R
import com.bangkit.capstone.fitness.databinding.FragmentRegisterBinding
import com.bangkit.capstone.fitness.network.apiServices
import com.bangkit.capstone.fitness.network.apiresponse.RegisterResponse
import com.bangkit.capstone.fitness.ui.utils.SessionManager
import com.bangkit.capstone.fitness.ui.utils.showOKDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var isPasswordVisible = false
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        sessionManager = SessionManager(requireContext())
        setupUI()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val username = binding.etName.text.toString()
            val height = binding.etHeight.text.toString()
            val weight = binding.etWeight.text.toString()

            if (isEmailValid(email)) {
                binding.textInputLayoutEmail.error = null
                // Email is valid, proceed with registration
                if (password.isNotEmpty() && username.isNotEmpty() && height.isNotEmpty() && weight.isNotEmpty()) {
                    val heightInput = height.toIntOrNull()
                    val weightInput = weight.toIntOrNull()

                    if (heightInput != null) {
                        // All values are not empty and of the correct type
                        if (weightInput != null){
                            registerUser(email, password, username, heightInput, weightInput)
                        }
                        else{
                            binding.textInputLayoutWeight.error = getString(R.string.error_invalid_weight)
                        }

                    } else {
                        binding.textInputLayoutHeight.error = getString(R.string.error_invalid_height)
                    }
                } else {
                    isEmptyFields(password, username, height, weight)
                }
            } else {
                // Show error message for invalid email
                binding.textInputLayoutEmail.error = getString(R.string.error_invalid_email)
                isEmptyFields(password, username, height, weight)
            }
        }
    }

    private fun isEmptyFields(password: String, username: String, height: String, weight: String){
        // Empty values, show error message
        if (password.isEmpty()){
            binding.textInputLayoutPassword.error = getString(R.string.error_empty_field)
        }
        if (username.isEmpty()){
            binding.textInputLayoutName.error = getString(R.string.error_empty_field)
        }
        if (height.isEmpty()){
            binding.textInputLayoutHeight.error = getString(R.string.error_empty_field)
        }
        if (weight.isEmpty()){
            binding.textInputLayoutWeight.error = getString(R.string.error_empty_field)
        }
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun registerUser(email: String, password: String, username: String, height: Int, weight: Int) {
        (activity as AuthenticationActivity).showLoading(true)

        // Perform the registration API request here
        val call = apiServices.registerUser(email, password, username, height, weight)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                (activity as AuthenticationActivity).showLoading(false)

                if (response.isSuccessful) {

                    // go into profile, user need to set training preference
                    val title = "Registration Succeed"
                    val message = "Proceed to login"
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(title)
                        setMessage(message)
                        setPositiveButton(context.getString(R.string.label_positive_reply)) { dialog, _ ->
                            dialog.dismiss()
                            // Navigate to the Login
                            val intent = Intent(requireContext(), AuthenticationActivity::class.java).apply {
                                putExtra("JUST_REGISTERED", true)
                                putExtra("EMAIL_EXTRA",email)
                                putExtra("PASSWORD_EXTRA",password)
//                                putExtra("FRAGMENT_ID", R.id.navigation_profile) // Set the desired fragment ID to navigate to
                            }
                            startActivity(intent)
                            requireActivity().finish() // Optional: Finish the current activity if needed
                        }
                    }.create().show()

                } else {
                    // Registration failed, handle the error
                    val responseCode = response.code()
                    val errorMessage = response.message()
                    Log.e("API Error", "Response code: $responseCode, Error message: $errorMessage")
                    showOKDialog(requireContext(), "Error", getString(R.string.message_error))
                    // You can parse the error response and show an appropriate message to the user
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                (activity as AuthenticationActivity).showLoading(false)
                // Registration request failed, handle the failure
                Log.e("RegisterResponse", "API call failed: ${t.message}")
                showOKDialog(requireContext(), "Error: request failed", getString(R.string.error_internet))
            }
        })
    }


    private fun togglePasswordVisibility() {
        val passwordEditText = binding.etPassword
        val buttonVisibility = binding.btnHideShowPassword

        if (isPasswordVisible) {
            // Show password
            passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            buttonVisibility.setImageResource(R.drawable.ic_visibility_off)
        } else {
            // Hide password
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            buttonVisibility.setImageResource(R.drawable.ic_visibility)
        }

        // Move the cursor to the end of the password text
        passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
    }

    private fun setupUI() {
        // Set hint animation for each TextInputLayout
        binding.etName.setOnFocusChangeListener { _, hasFocus ->
            binding.textInputLayoutName.hint = if (hasFocus) "" else getString(R.string.edit_name)
        }

        binding.etHeight.setOnFocusChangeListener { _, hasFocus ->
            binding.textInputLayoutHeight.hint = if (hasFocus) "" else getString(R.string.edit_height)
        }

        binding.etWeight.setOnFocusChangeListener { _, hasFocus ->
            binding.textInputLayoutWeight.hint = if (hasFocus) "" else getString(R.string.edit_weight)
        }

        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            binding.textInputLayoutEmail.hint = if (hasFocus) "" else getString(R.string.edit_email_address)
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            binding.textInputLayoutPassword.hint = if (hasFocus) "" else getString(R.string.edit_password)
        }

        binding.btnHideShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }
    }


}
