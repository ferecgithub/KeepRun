package com.ferechamitbeyli.presentation.view.activities.auth_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ferechamitbeyli.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupLayout()

        setupNavigationComponents()
    }

    private fun setupLayout() {
        setTheme(R.style.Theme_KeepRun)
        setContentView(R.layout.activity_auth)
    }

    private fun setupNavigationComponents() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_fcv)
                as NavHostFragment
        navController = navHostFragment.navController
    }


}