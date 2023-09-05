package com.example.playlistmaker.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        val navController = navHostFragment.navController

        mainBinding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment, R.id.playlistCreator, R.id.playlistFragment  -> hideNavigationView()

                else -> {
                    mainBinding.bottomNavigationView.visibility = View.VISIBLE
                    mainBinding.border.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun hideNavigationView() {
        mainBinding.bottomNavigationView.visibility = View.GONE
        mainBinding.border.visibility = View.GONE
    }
}