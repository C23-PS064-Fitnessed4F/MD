package com.bangkit.capstone.fitness.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.capstone.fitness.MainActivity
import com.bangkit.capstone.fitness.R
import com.bangkit.capstone.fitness.databinding.FragmentLoginBinding
import com.bangkit.capstone.fitness.network.apiServices
import com.bangkit.capstone.fitness.network.apiresponse.LoginResponse
import com.bangkit.capstone.fitness.ui.utils.ConstValue
import com.bangkit.capstone.fitness.ui.utils.SessionManager
import com.bangkit.capstone.fitness.ui.utils.showOKDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var isPasswordVisible = false
    private lateinit var sessionManager: SessionManager

    private var justRegistered: Boolean = false
    private var emailExtra: String? = null
    private var passwordExtra: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        sessionManager = SessionManager(requireContext())

        // Retrieve the values passed from AuthenticationActivity
        justRegistered = requireActivity().intent.getBooleanExtra("JUST_REGISTERED", false)
        emailExtra = requireActivity().intent.getStringExtra("EMAIL_EXTRA")
        passwordExtra = requireActivity().intent.getStringExtra("PASSWORD_EXTRA")

        setupUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (justRegistered && emailExtra != null && passwordExtra != null) {
            // Perform login with the passed values
            performLogin(emailExtra!!, passwordExtra!!)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (isEmailValid(email)) {
                binding.textInputLayoutEmail.error = null
                // Email is valid, proceed with login
                if(password.isEmpty()){
                    binding.textInputLayoutPassword.error = getString(R.string.error_empty_password)
                }
                else{
                    performLogin(email,password)
                }

            } else {
                // Show error message for invalid email
                binding.textInputLayoutEmail.error = getString(R.string.error_invalid_email)
            }
        }

    }

    private fun setupUI(){
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
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun performLogin(email: String, password: String) {
        // Show progress bar
        (activity as AuthenticationActivity).showLoading(true)

        // api service request
        val call = apiServices.login(email, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Hide progress bar
                (activity as AuthenticationActivity).showLoading(false)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val idToken = loginResponse?.idToken
                    val refreshToken = loginResponse?.refreshToken
                    val user = loginResponse?.user

                    Log.d("LoginResponse", "user: $user")


                    // Process the login response and save the necessary data using the SessionManager
                    sessionManager.setStringPreference(ConstValue.KEY_TOKEN, idToken ?: "")
                    sessionManager.setStringPreference(ConstValue.KEY_REFRESH_TOKEN, refreshToken ?: "")
                    sessionManager.setBooleanPreference(ConstValue.KEY_IS_LOGGED_IN, true)
                    sessionManager.setStringPreference(ConstValue.KEY_EMAIL, email)

                    sessionManager.setUser(ConstValue.KEY_USER, user)

                    Log.d("LoginResponse", "idToken: $idToken")
                    Log.d("LoginResponse", "refreshToken: $refreshToken")
                    Log.d("LoginResponse", "user: $user")

                    // Go to MainActivity and pass the JUST_REGISTERED value
                    val intent = Intent(requireContext(), MainActivity::class.java).apply {
                        putExtra("JUST_REGISTERED", justRegistered)
                        if(justRegistered) {
                            putExtra("FRAGMENT_ID", R.id.navigation_profile) // Set the desired fragment ID to navigate to
                        }
                    }
                    startActivity(intent)
                    requireActivity().finish() // Optional: Finish the current activity if needed
                } else {
                    // Handle the login error
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProfileFragment", "Error response: $errorBody")
                    binding.textInputLayoutPassword.error = getString(R.string.invalid_password)

                    // Show an error message to the user or perform any other necessary actions
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Hide progress bar
                (activity as AuthenticationActivity).showLoading(false)

                // Handle the login failure
                Log.e("ProfileFragment", "Error: request failed", t)
                showOKDialog(requireContext(), "Login Failed", getString(R.string.error_internet))
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
}
