package com.proptit.btl_oop.ui.main_app.order_history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.OrderAdapter
import com.proptit.btl_oop.databinding.FragmentOrderHistoryBinding
import com.proptit.btl_oop.viewmodel.HomeViewModel
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class OrderHistoryFragment : Fragment() {
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModel.OrderHistoryViewModelFactory(requireActivity().application)
    }
    private val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel> {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }

    private lateinit var orderHistoryAdapter: OrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)

        binding.icOverview.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        orderHistoryAdapter =
            OrderAdapter(homeViewModel.coffees.value!!, homeViewModel.beans.value!!) {
                orderHistoryViewModel.orderHistory.value?.get(it)?.let { orderHistory ->
                    findNavController().navigate(
                        OrderHistoryFragmentDirections.actionOrderHistoryFragmentToDetailsOrderHistoryFragment(
                            orderHistory.date
                        )
                    )
                }
            }
        binding.rvOrderHistory.apply {
            adapter = orderHistoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        lifecycleScope.launch {
            orderHistoryViewModel.orderHistory.collect() { order ->
                if (order.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                }
                orderHistoryAdapter.submitList(order)
            }
        }
    }

}