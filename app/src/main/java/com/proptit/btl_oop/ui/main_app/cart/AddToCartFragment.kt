package com.proptit.btl_oop.ui.main_app.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.databinding.FragmentAddToCartBinding
import com.proptit.btl_oop.model.CartItem
import com.proptit.btl_oop.viewmodel.HomeViewModel


class AddToCartFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!
    private val args: AddToCartFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        binding.btnClose.setOnClickListener { dismiss() }
        binding.tvQuantity.text = "1"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddToCart.setOnClickListener {
            addToCart()
        }
        updateQuantity()
        updateUI()

    }

    private fun updateQuantity() {
        binding.btnIncrease.setOnClickListener {
            var quantity = binding.tvQuantity.text.toString().toInt()
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }
        binding.btnDecrease.setOnClickListener {
            var quantity = binding.tvQuantity.text.toString().toInt()
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }
    }

    private fun updateUI() {
        if (args.type == Type.COFFEE.toString()) {
            val selectedCoffee = homeViewModel.coffees.value?.find { it.id == args.id }
            binding.apply {
                tvProductName.text = selectedCoffee?.name
                tvPriceProduct.text = "${"%,d".format(selectedCoffee?.price?.get(args.sizeIdx))}đ"
                Glide.with(requireContext()).load(selectedCoffee?.image_url).into(imgProduct)
            }
        } else {
            val selectedBean = homeViewModel.beans.value?.find { it.id == args.id }
            binding.apply {
                tvProductName.text = selectedBean?.name
                tvPriceProduct.text = "${"%,d".format(selectedBean?.price?.get(args.sizeIdx))}đ"
                Glide.with(requireContext()).load(selectedBean?.image_url).into(imgProduct)
            }
        }
    }

    private fun addToCart() {
        if (args.type == Type.COFFEE.toString()) {
            val selectedCoffee = homeViewModel.coffees.value?.find { it.id == args.id }
            if (selectedCoffee != null) {
                val price = selectedCoffee.price[args.sizeIdx]
                val quantity = binding.tvQuantity.text.toString().toInt()
                // Tạo đối tượng Order
                val cartItem = CartItem(
                    type = Type.COFFEE.toString(),
                    id = selectedCoffee.id,
                    sizeIdx = args.sizeIdx,
                    price = price,
                    quantity = quantity
                )
                SaveToDB.updateOderInFirebase(cartItem)
            }
        } else {
            val selectedBean = homeViewModel.beans.value?.find { it.id == args.id }
            if (selectedBean != null) {
                val price = selectedBean.price[args.sizeIdx]
                val quantity = binding.tvQuantity.text.toString().toInt()

                // Tạo đối tượng Order
                val cartItem = CartItem(
                    type = Type.BEANS.toString(),
                    id = selectedBean.id,
                    sizeIdx = args.sizeIdx,
                    price = price,
                    quantity = quantity
                )

                SaveToDB.updateOderInFirebase(cartItem)
            }
        }
        dismiss()
    }

}