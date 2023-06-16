package com.bangkit.capstone.fitness.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.capstone.fitness.databinding.ActivityAuthenticationBinding
import com.bangkit.capstone.fitness.ui.authentication.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Menghubungkan ViewPager2 dengan TabLayout menggunakan TabLayoutMediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Login"
                1 -> tab.text = "Register"
            }
        }.attach()
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