package com.proptit.btl_oop

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.proptit.btl_oop.databinding.ActivityMainBinding
import com.proptit.btl_oop.utils.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

        // Setup Navigation Component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Drawer Navigation item handling
        binding.drawerNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawers() // Đóng Drawer khi chọn menu
            val navOptions = createNavOptions() // Tạo NavOptions với animation
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    if (navController.currentDestination?.id != R.id.profileFragment) {
                        navController.navigate(R.id.profileFragment, null, navOptions)
                    }
                    false
                }

                R.id.nav_order -> {
                    if (navController.currentDestination?.id != R.id.orderHistoryFragment) {
                        navController.navigate(R.id.orderHistoryFragment, null, navOptions)
                    }
                    false
                }

                R.id.nav_logout -> {
                    Firebase.auth.signOut()
                    navController.navigate(
                        R.id.action_homeScreenFragment_to_signInScreenFragment,
                        null,
                        navOptions
                    )
                    false
                }

                else -> false
            }
        }

        // Destination change listener for controlling UI components
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInScreenFragment,
                R.id.signUpScreenFragment,
                R.id.forgotPasswordFragment,
                R.id.coffeeDetailsFragment,
                R.id.beanDetailsFragment,
                R.id.addToCartFragment,
                R.id.detailsOrderHistoryFragment,
                R.id.choseMapFragment,
                R.id.profileFragment -> {
                    hideBottomNavigation()
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    showBottomNavigation()
                    showUserInfo() // Cập nhật thông tin người dùng
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
        lifecycleScope.launch {
            val user = Firebase.auth.currentUser
            if (user != null) {
                tvFullName?.text = user.displayName ?: fetchUserName(user.uid)
                tvEmail?.text = user.email
                Glide.with(this@MainActivity)
                    .load(user.photoUrl)
                    .error(R.drawable.ic_logo)
                    .into(imgPhoto!!)
            }
        }
    }

    private suspend fun fetchUserName(uid: String?): String {
        if (uid.isNullOrEmpty()) return "Unknown User"
        val ref = Firebase.database.getReference("Users/$uid")
        val snapshot = ref.get().await()
        return snapshot.child("name").value?.toString() ?: "Unknown User"
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun createNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

    override fun onResume() {
        super.onResume()
        if (Firebase.auth.currentUser != null) {
            showUserInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tvFullName = null
        tvEmail = null
        imgPhoto = null
    }
}
