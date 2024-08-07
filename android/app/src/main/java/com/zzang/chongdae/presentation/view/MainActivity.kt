package com.zzang.chongdae.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.ActivityMainBinding
import com.zzang.chongdae.presentation.view.offeringdetail.OfferingDetailActivity

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initNavController()
        setupBottomNavigation()
    }

    private fun initBinding() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun setupBottomNavigation() {
        binding.mainBottomNavigation.setupWithNavController(navController)
    }

    fun hideBottomNavigation() {
        binding.mainBottomNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.mainBottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun startActivity(context: Context) =
            Intent(context, MainActivity::class.java).run {
                context.startActivity(this)
            }
    }
}
