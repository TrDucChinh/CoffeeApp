package com.proptit.btl_oop.ui.main_app.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proptit.btl_oop.databinding.FragmentAddToCartBinding


class AddToCartFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        binding.btnClose.setOnClickListener{ dismiss()}
        return binding.root
    }

}