package com.proptit.btl_oop.ui.sidebar.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root

    }
    private fun setupUI(){
        binding.apply {
            btnBack.setOnClickListener{ findNavController().popBackStack() }
            icEdit.setOnClickListener{ }
            cardYourProfile.setOnClickListener{ findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)}
            cardChangePassword.setOnClickListener{ findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)}
        }
    }
}