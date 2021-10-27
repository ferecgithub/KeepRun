package com.ferechamitbeyli.presentation.view.activities.auth_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ferechamitbeyli.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_KeepRun)
        setContentView(R.layout.activity_auth)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_fcv)
                as NavHostFragment
        navController = navHostFragment.navController
    }


}