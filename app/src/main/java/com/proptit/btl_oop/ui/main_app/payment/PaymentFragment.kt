package com.proptit.btl_oop.ui.main_app.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.utils.Payment
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.databinding.FragmentPaymentBinding
import com.proptit.btl_oop.model.OrderHistory
import com.proptit.btl_oop.ui.main_app.dialog.SuccessDialogFragment
import com.proptit.btl_oop.viewmodel.CartViewModel
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel

class PaymentFragment : Fragment() {
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
            btnBack.setOnClickListener { findNavController().popBackStack() }

            paymentMethodGroup.setOnCheckedChangeListener { _, _ ->
                // Nếu có radio button được chọn thì btnPay mới được enable
                val isEnabled = paymentMethodGroup.checkedRadioButtonId != -1
                btnPay.isEnabled = isEnabled

                val backgroundColor = if (isEnabled) R.color.orange else R.color.light_orange
                val textColor = if (isEnabled) R.color.white else R.color.white

                btnPay.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
                btnPay.setTextColor(ContextCompat.getColor(requireContext(), textColor))
            }
            btnPay.setOnClickListener {
                val orderItem = cartViewModel.cartItem.value
                val orderHistory = OrderHistory(
                    System.currentTimeMillis(),
                    orderItem,
                    orderItem.sumBy { it.price * it.quantity },
                    checkPayment()
                )
                SaveToDB.saveOrderHistoryToDB(orderHistory)
                SaveToDB.clearCart()
//                dismiss()
                SuccessDialogFragment().show(
                    parentFragmentManager,
                    "SuccessDialog"
                )
            }
            // disable btnPay neu khong duoc chon
            val isEnabled = paymentMethodGroup.checkedRadioButtonId != -1
            btnPay.isEnabled = isEnabled
            val backgroundColor = if (isEnabled) R.color.orange else R.color.light_orange
            val textColor = if (isEnabled) R.color.white else R.color.white

            btnPay.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
            btnPay.setTextColor(ContextCompat.getColor(requireContext(), textColor))

        }
    }
    private fun checkPayment(): String{
        return when(binding.paymentMethodGroup.checkedRadioButtonId){
            R.id.cashRadioButton -> Payment.Cash.toString()
            R.id.vnPayRadioButton -> Payment.VNPAY.toString()
            R.id.momoRadioButton -> Payment.MoMo.toString()
            R.id.zaloPayRadioButton -> Payment.ZaloPay.toString()
            R.id.cardsRadioButton -> Payment.Cards.toString()
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}