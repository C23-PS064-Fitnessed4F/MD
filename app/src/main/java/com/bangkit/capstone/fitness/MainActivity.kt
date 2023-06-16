package com.bangkit.capstone.fitness

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.capstone.fitness.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_workout, R.id.navigation_diet, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val justRegistered = intent.getBooleanExtra("JUST_REGISTERED", false)

        if (justRegistered) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.title_just_registered))
                setMessage(getString(R.string.desc_just_registered))
                setPositiveButton(context.getString(R.string.label_positive_reply)) { dialog, _ ->
                    dialog.dismiss()
                }
            }.create().show()

        }
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.bgDim.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.bgDim.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finishAffinity() // Clear the activity stack
    }
}