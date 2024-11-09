package com.proptit.btl_oop.ui.main_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.BeanAdapter
import com.proptit.btl_oop.adapter.CoffeeAdapter
import com.proptit.btl_oop.databinding.FragmentHomeScreenBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean

class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var coffeeAdapter: CoffeeAdapter
    private lateinit var beanAdapter: BeanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        setupCoffeeRecycler()
        setupCoffeeBeanRecycler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)

        binding.icOverview.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        setupTabsWithRecycler()
    }

    private fun setupTabsWithRecycler() {
        val allCoffees = listOf(
            Coffee(1L, "Cappuccino", R.drawable.cappuccino, "With steamed milk", 50000, 2L, false),
            Coffee(2L, "Mocha", R.drawable.bean_mocha, "Medium Roasted", 50000, 1L, false),
            Coffee(3L, "Cherry", R.drawable.bean_cherry, "Medium Roasted", 60000, 2L, false),
            Coffee(4L, "Bac xiu", R.drawable.bean_robusta, "Dark Roasted", 40000, 3L, false)
        )

        val coffeeByCategory = allCoffees.groupBy { it.categoryId }
        val coffeeTitles = listOf("All", "Machine Brewed", "Cold Brew", "Vietnamese Coffee")
        coffeeTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        coffeeAdapter.updateData(allCoffees)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedIndex = tab?.position ?: 0
                val selectedCoffees = when (selectedIndex) {
                    0 -> allCoffees // All coffees
                    1 -> coffeeByCategory[1] ?: emptyList() // Machine Brewed
                    2 -> coffeeByCategory[2] ?: emptyList() // Cold Brew
                    3 -> coffeeByCategory[3] ?: emptyList() // Vietnamese Coffee
                    else -> emptyList()
                }
                coffeeAdapter.updateData(selectedCoffees)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupCoffeeRecycler() {
        coffeeAdapter = CoffeeAdapter(mutableListOf()) { coffeeId ->
            val action = HomeScreenFragmentDirections
                .actionHomeScreenFragmentToCoffeeDetailsFragment(coffeeId)
            findNavController().navigate(action)
        }
        binding.recyclerViewCoffeeTabs.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = coffeeAdapter
        }
    }

    private fun setupCoffeeBeanRecycler() {
        beanAdapter = BeanAdapter(mutableListOf()) { beanId ->
            val action = HomeScreenFragmentDirections
                .actionHomeScreenFragmentToBeanDetailsFragment(beanId)
            findNavController().navigate(action)
        }
        binding.recyclerViewBean.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = beanAdapter
        }

        val coffeeBeans = listOf(
            CoffeeBean(1, "Bean 1", R.drawable.bean_arabica, "Description 1", 30000, false),
            CoffeeBean(2, "Bean 2", R.drawable.bean_mocha, "Description 2", 35000, false),
            CoffeeBean(3, "Bean 3", R.drawable.bean_robusta, "Description 3", 40000, false)
        )

        beanAdapter.updateData(coffeeBeans)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
