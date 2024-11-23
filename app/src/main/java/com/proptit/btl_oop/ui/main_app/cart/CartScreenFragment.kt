package com.proptit.btl_oop.ui.main_app.cart

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
import com.proptit.btl_oop.adapter.CartAdapter
import com.proptit.btl_oop.databinding.FragmentCartScreenBinding
import com.proptit.btl_oop.ui.main_app.payment.PaymentFragment
import com.proptit.btl_oop.viewmodel.CartViewModel
import com.proptit.btl_oop.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class CartScreenFragment : Fragment() {
    private var _binding: FragmentCartScreenBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }
    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModel.CartViewModelFactory(requireActivity().application)
    }

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)
        binding.icOverview.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        setUpAdapter()
        binding.btnPay.setOnClickListener {
            findNavController().navigate(R.id.action_cartScreenFragment_to_paymentFragment)
        }
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        lifecycleScope.launch {
            cartViewModel.cartItem.collect { orders ->
                var totalPrice = 0
                orders.forEach {
                    totalPrice += it.price * it.quantity
                }
                binding.tvPriceProduct.text = "${"%,d".format(totalPrice)}đ"
                Log.e("CartScreenFragment", "Total price updated: $totalPrice")
            }
        }
    }


    private fun setUpAdapter() {
        cartAdapter = CartAdapter(homeViewModel.coffees.value!!, homeViewModel.beans.value!!)
        binding.rvCartItems.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // Lắng nghe sự thay đổi trong giỏ hàng
        lifecycleScope.launch {
            cartViewModel.cartItem.collect { orders ->
                // Cập nhật adapter mỗi khi dữ liệu giỏ hàng thay đổi
                cartAdapter.submitList(orders)
                Log.e("CartScreenFragment", "Cart updated: $orders")
            }
        }
    }

}
