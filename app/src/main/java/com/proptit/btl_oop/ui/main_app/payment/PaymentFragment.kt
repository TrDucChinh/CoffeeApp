package com.proptit.btl_oop.ui.main_app.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentPaymentBinding
import com.proptit.btl_oop.ui.main_app.dialog.SuccessDialogFragment

class PaymentFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
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