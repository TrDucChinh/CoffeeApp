package com.proptit.btl_oop.ui.main_app.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proptit.btl_oop.R
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.databinding.FragmentPaymentBinding
import com.proptit.btl_oop.model.OrderHistory
import com.proptit.btl_oop.ui.main_app.dialog.SuccessDialogFragment
import com.proptit.btl_oop.viewmodel.CartViewModel
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel

class PaymentFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels(){
        CartViewModel.CartViewModelFactory(requireActivity().application)
    }
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels(){
        OrderHistoryViewModel.OrderHistoryViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.apply {
            icClose.setOnClickListener { dismiss() }
            btnPay.setOnClickListener {
                val orderItem = cartViewModel.cartItem.value
                val orderHistory = OrderHistory(
                    System.currentTimeMillis().toString(),
                    orderItem,
                    orderItem.sumBy { it.price }
                )
                SaveToDB.saveOrderHistoryToDB(orderHistory)
                SaveToDB.clearCart()
                dismiss()
                SuccessDialogFragment().show(
                    parentFragmentManager,
                    "SuccessDialog"
                )
            }
            paymentMethodGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.cashRadioButton -> {
                    }

                    R.id.vnPayRadioButton -> {
                    }

                    R.id.momoRadioButton -> {
                    }

                    R.id.zaloPayRadioButton -> {
                    }

                    R.id.cardsRadioButton -> {
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}