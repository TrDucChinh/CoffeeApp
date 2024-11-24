package com.proptit.btl_oop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.proptit.btl_oop.databinding.ActivityMainBinding
import com.proptit.btl_oop.utils.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tvFullName: TextView? = null
    private var tvEmail: TextView? = null
    private var imgPhoto: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.drawerLayout)
        initUI()
        showUserInfo()


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        binding.drawerNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    navController.navigate(R.id.profileFragment)
                    false
                }

                R.id.nav_order -> {
                    navController.navigate(R.id.orderHistoryFragment)
                    false
                }

                R.id.nav_logout -> {
                    Firebase.auth.signOut()
//                    Log.e("Logout", "${auth.currentUser}")
                    navController.navigate(R.id.action_homeScreenFragment_to_signInScreenFragment)

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
                R.id.beanDetailsFragment,
                R.id.addToCartFragment,
                R.id.detailsOrderHistoryFragment,
                R.id.profileFragment
                    -> {
                    hideBottomNavigation()
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    showUserInfo()
                    showBottomNavigation()
                }
            }
        }
    }

    private fun initUI() {
        val headerView = binding.drawerNavigation.getHeaderView(0)
        tvFullName = headerView.findViewById(R.id.tvFullName)
        tvEmail = headerView.findViewById(R.id.tvEmail)
        imgPhoto = headerView.findViewById(R.id.ic_logo)
    }

    private fun showUserInfo() {
        val user = Firebase.auth.currentUser

        binding.apply {
            if (user?.displayName.isNullOrEmpty()) {
                val ref = Firebase.database.getReference("users/${user?.uid}")
                ref.get().addOnSuccessListener {
                    val name = it.child("name").value.toString()
                    tvFullName?.text = name
                }
            } else {
                tvFullName?.text = user?.displayName
            }
            tvEmail?.text = user?.email
        }

        val photo = user?.photoUrl
        Glide.with(this)
            .load(photo)
            .error(R.drawable.ic_logo)
            .into(imgPhoto!!)

    }

    private fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        if (Firebase.auth.currentUser !== null) {
            showUserInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tvFullName = null
        tvEmail = null
        imgPhoto = null
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}