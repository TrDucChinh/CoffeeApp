package com.proptit.btl_oop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        drawerLayout = findViewById(R.id.drawerLayout)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeScreenFragment)
                    true
                }
                R.id.nav_favourite -> {
                    navController.navigate(R.id.favouriteScreenFragment)
                    true
                }
                R.id.nav_cart -> {
                    navController.navigate(R.id.cartScreenFragment)
                    true
                }

                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInScreenFragment,
                R.id.signUpScreenFragment,
                R.id.forgotPasswordFragment,
                R.id.coffeeDetailsFragment,
                R.id.beanDetailsFragment -> hideBottomNavigation()

                else -> showBottomNavigation()
            }
        }
    }

    private fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
    }
}
