package com.proptit.btl_oop.ui.main_app.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentSuccessDialogBinding


class SuccessDialogFragment : DialogFragment() {

        private var _binding: FragmentSuccessDialogBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentSuccessDialogBinding.inflate(inflater, container, false)
            setupUI()
            return binding.root
        }

        private fun setupUI() {
            binding.apply {
                btnBackToHome.setOnClickListener {
                    dismiss()
                    findNavController().navigate(R.id.action_successFragment_to_homeScreenFragment)
                }
            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    override fun getTheme(): Int = R.style.DialogTheme

}