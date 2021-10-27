package com.ferechamitbeyli.presentation.view.activities.home_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ferechamitbeyli.presentation.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_fcv)
                as NavHostFragment
        navController = navHostFragment.navController
    }
}