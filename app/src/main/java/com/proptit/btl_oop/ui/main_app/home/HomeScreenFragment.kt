package com.proptit.btl_oop.ui.main_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.adapter.BeanAdapter
import com.proptit.btl_oop.adapter.CoffeeAdapter
import com.proptit.btl_oop.databinding.FragmentHomeScreenBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.viewmodel.CartViewModel
import com.proptit.btl_oop.viewmodel.FavouriteViewModel
import com.proptit.btl_oop.viewmodel.HomeViewModel
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }
    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModel.CartViewModelFactory(requireActivity().application)
    }
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModel.OrderHistoryViewModelFactory(requireActivity().application)
    }
    private val favouriteViewModel: FavouriteViewModel by activityViewModels {
        FavouriteViewModel.FavouriteViewModelFactory(requireActivity().application)
    }

    private var coffeeList = mutableListOf<Coffee>()
    private var favouriteItems = mutableListOf<FavouriteItem>()
    private lateinit var coffeeAdapter: CoffeeAdapter
    private lateinit var beanAdapter: BeanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        homeViewModel.loadCategory()
        homeViewModel.loadCoffee()
        homeViewModel.loadCoffeeBean()
        favouriteViewModel.loadFavourite()
        orderHistoryViewModel.loadOrderHistory()
        cartViewModel.loadCart()
        lifecycleScope.launch {
            favouriteViewModel.favouriteItem.collect {
                favouriteItems = it
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)
        binding.icOverview.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        setupCoffeeRecycler()
        setupCoffeeBeanRecycler()
        setupTabsWithRecycler()
    }

    private fun setupTabsWithRecycler() {
        //Load category and setup Tab Layout
        homeViewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            binding.tabLayout.removeAllTabs()
            categories.forEach { category ->
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(category.name))
            }
            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        // Lấy categoryId của tab được chọn
                        val selectedCategoryId = categories[it.position].id
                        if (selectedCategoryId == 0) {
                            coffeeAdapter.updateData(coffeeList)
                            return
                        }
                        // Lọc danh sách cà phê theo categoryId
                        val filteredCoffees = coffeeList.filter { coffee ->
                            coffee.categoryId == selectedCategoryId
                        }
                        // Cập nhật adapter với danh sách cà phê đã lọc
                        coffeeAdapter.updateData(filteredCoffees)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        })
        //Load coffee to recycle view
        homeViewModel.coffees.observe(viewLifecycleOwner, Observer { coffees ->
            coffeeList = coffees.toMutableList()
            // Hiển thị tất cả cà phê khi không chọn tab cụ thể nào
            coffeeAdapter.updateData(coffeeList)
        })

    }

    private fun setupCoffeeRecycler() {
        coffeeAdapter = CoffeeAdapter(mutableListOf()) { coffeeId ->
            val isFavourite = favouriteItems.any { it.id == coffeeId && it.type == Type.COFFEE.toString() }
            val action = HomeScreenFragmentDirections
                .actionHomeScreenFragmentToCoffeeDetailsFragment(coffeeId, isFavourite)
            findNavController().navigate(action)
        }
        binding.recyclerViewCoffeeTabs.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = coffeeAdapter
        }
    }

    private fun setupCoffeeBeanRecycler() {
        beanAdapter = BeanAdapter(mutableListOf()) { beanId ->
            val isFavourite = favouriteItems.any { it.id == beanId && it.type == Type.BEANS.toString() }

            val action = HomeScreenFragmentDirections
                .actionHomeScreenFragmentToBeanDetailsFragment(beanId, isFavourite)
            findNavController().navigate(action)
        }
        binding.recyclerViewBean.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = beanAdapter
        }

        homeViewModel.beans.observe(viewLifecycleOwner, Observer { beans ->
            beanAdapter.updateData(beans)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
