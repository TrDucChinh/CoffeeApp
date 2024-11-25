package com.proptit.btl_oop.ui.main_app.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.utils.Payment
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.databinding.FragmentPaymentBinding
import com.proptit.btl_oop.model.OrderHistory
import com.proptit.btl_oop.ui.main_app.dialog.SuccessDialogFragment
import com.proptit.btl_oop.viewmodel.CartViewModel
import com.proptit.btl_oop.viewmodel.ChoseAddressViewModel
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels(){
        CartViewModel.CartViewModelFactory(requireActivity().application)
    }
    private lateinit var choseAddressViewModel: ChoseAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        choseAddressViewModel = ViewModelProvider(requireActivity()).get(ChoseAddressViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            btnBack.setOnClickListener {
                choseAddressViewModel.setAddress("")
                findNavController().popBackStack()
            }

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
                if (isPhoneNumberValid(etPhone.text.toString())) {
                    val orderItem = cartViewModel.cartItem.value
                    val orderHistory = OrderHistory(
                        System.currentTimeMillis(),
                        orderItem,
                        orderItem.sumBy { it.price * it.quantity },
                        checkPayment(),
                        btnNavigate.text.toString(),
                        etPhone.text.toString()
                    )
                    SaveToDB.saveOrderHistoryToDB(orderHistory)
                    SaveToDB.clearCart()
                    //                dismiss()
                    SuccessDialogFragment().show(
                        parentFragmentManager,
                        "SuccessDialog"
                    )
                }
                else{
                    etPhone.error = "Invalid phone number."
                }
            }
            // disable btnPay neu khong duoc chon
            val isEnabled = paymentMethodGroup.checkedRadioButtonId != -1
            btnPay.isEnabled = isEnabled
            val backgroundColor = if (isEnabled) R.color.orange else R.color.light_orange
            val textColor = if (isEnabled) R.color.white else R.color.white

            btnPay.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
            btnPay.setTextColor(ContextCompat.getColor(requireContext(), textColor))

            btnNavigate.setOnClickListener {
                val action = PaymentFragmentDirections.actionPaymentFragmentToChoseMapFragment()
                findNavController().navigate(action)
            }
            lifecycleScope.launch {
                choseAddressViewModel.address.collect {
                    if (it.isNotEmpty()) {
                        btnNavigate.text = it
                        btnNavigate.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
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
    private fun isPhoneNumberValid(phone: String): Boolean {
        val regex = Regex("^[0-9]{10,11}$") // Chấp nhận số điện thoại từ 10-11 số
        return phone.matches(regex)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}