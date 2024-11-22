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
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentOrderHistoryBinding
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel


class OrderHistoryFragment : Fragment() {
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModel.OrderHistoryViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        orderHistoryViewModel.orderHistory.value.forEach() {
            Log.e("OrderHistoryFragment", it.toString())
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)

        binding.icOverview.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}