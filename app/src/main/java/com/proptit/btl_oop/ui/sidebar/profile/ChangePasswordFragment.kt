package com.proptit.btl_oop.ui.sidebar.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentChangePasswordBinding


class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

}