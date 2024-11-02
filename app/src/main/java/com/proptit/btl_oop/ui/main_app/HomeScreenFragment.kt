package com.proptit.btl_oop.ui.main_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.CoffeeAdapter
import com.proptit.btl_oop.adapter.CoffeeBeanAdapter
import com.proptit.btl_oop.databinding.FragmentHomeScreenBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean

class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var coffeeAdapter: CoffeeAdapter
    private lateinit var coffeeBeanAdapter: CoffeeBeanAdapter

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
        setupTabsWithRecycler()
    }

    private fun setupTabsWithRecycler() {
        val allCoffees = listOf(
            Coffee(1L, "Cappuccino", R.drawable.cappuccino, "With steamed milk", 50000, 2L),
            Coffee(2L, "Mocha", R.drawable.bean_mocha, "Medium Roasted", 50000, 1L),
            Coffee(3L, "Cherry", R.drawable.bean_cherry, "Medium Roasted", 60000, 2L),
            Coffee(4L, "Bac xiu", R.drawable.bean_robusta, "Dark Roasted", 40000, 3L)
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
        coffeeAdapter = CoffeeAdapter(mutableListOf())
        binding.recyclerViewCoffeeTabs.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = coffeeAdapter
        }
    }

    private fun setupCoffeeBeanRecycler() {
        coffeeBeanAdapter = CoffeeBeanAdapter(mutableListOf())
        binding.recyclerViewBean.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = coffeeBeanAdapter
        }

        val coffeeBeans = listOf(
            CoffeeBean(1, "Bean 1", R.drawable.bean_arabica, "Description 1", 30000),
            CoffeeBean(2, "Bean 2", R.drawable.bean_mocha, "Description 2", 35000),
            CoffeeBean(3, "Bean 3", R.drawable.bean_robusta, "Description 3", 40000)
        )

        coffeeBeanAdapter.updateData(coffeeBeans)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
