package com.example.runners.ui

import android.os.Bundle
import android.view.View

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runners.R
import com.example.runners.databinding.ActivityAppAuthenticationBinding


class AppAuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_app)

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeScreenFragmentMain, R.id.newsHomeFragment, R.id.profileHomeFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newsDetailsScreenFragment -> navView.visibility = View.GONE
                R.id.homeEventsFragment -> navView.visibility = View.GONE
                R.id.eventsDetailsFragment -> navView.visibility = View.GONE
                R.id.registrationEventFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_app)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}