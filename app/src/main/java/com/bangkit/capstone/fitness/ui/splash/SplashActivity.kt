package com.bangkit.capstone.fitness.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bangkit.capstone.fitness.MainActivity
import com.bangkit.capstone.fitness.R
import com.bangkit.capstone.fitness.ui.authentication.AuthenticationActivity
import com.bangkit.capstone.fitness.ui.utils.ConstValue.LOADING_TIME
import com.bangkit.capstone.fitness.ui.utils.SessionManager

class SplashActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)
        val isLogin = sessionManager.isUserLoggedIn
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                isLogin -> {
                    intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                    finish()
                }
                else -> {
                    intent = Intent(this, AuthenticationActivity::class.java)
                    this.startActivity(intent)
                    finish()
                }
            }
        }, LOADING_TIME)
    }
}