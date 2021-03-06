package com.ferechamitbeyli.presentation.view.activities.home_activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ferechamitbeyli.presentation.R
import com.ferechamitbeyli.presentation.databinding.ActivityHomeBinding
import com.ferechamitbeyli.presentation.utils.helpers.PresentationConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var _binding: ActivityHomeBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout()
        adjustBottomNavigationView()
        setupNavigationComponents()

        navigateToTrackingFragmentWithAction(intent)
    }

    private fun setupLayout() {
        setTheme(R.style.Theme_KeepRun)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupNavigationComponents() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_fcv)
                as NavHostFragment
        navController = navHostFragment.navController
        binding.homeBnv.setupWithNavController(navController)
        binding.homeBnv.setOnItemReselectedListener { /** NO-OP **/ }
    }

    private fun adjustBottomNavigationView() {
        binding.homeBnv.background = null
        binding.homeBnv.menu.getItem(2).isEnabled = false
    }

    private fun navigateToTrackingFragmentWithAction(intent: Intent?) {
        if (intent?.action == PresentationConstants.ACTION_NAVIGATE_TO_TRACKING_FRAGMENT) {
            navController.navigate(R.id.action_global_trackingFragment)
        }
    }

}