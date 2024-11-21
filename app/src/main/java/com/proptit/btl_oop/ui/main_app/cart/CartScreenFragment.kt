package com.proptit.btl_oop.ui.main_app.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.CartAdapter
import com.proptit.btl_oop.databinding.FragmentCartScreenBinding
import com.proptit.btl_oop.model.Coffee


class CartScreenFragment : Fragment() {
    private var _binding: FragmentCartScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private var coffeeList: List<Coffee> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartScreenBinding.inflate(inflater, container, false)

        binding.rvCartItems.layoutManager = LinearLayoutManager(context)

//        cartAdapter = CartAdapter(coffeeList)
        binding.rvCartItems.adapter = cartAdapter

        binding.btnPay.setOnClickListener{
            findNavController().navigate(R.id.action_cartScreenFragment_to_paymentFragment)
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