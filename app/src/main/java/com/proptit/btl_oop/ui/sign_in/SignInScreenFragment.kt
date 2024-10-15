package com.proptit.btl_oop.ui.sign_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentSignInScreenBinding


class SignInScreenFragment : Fragment() {
    private var _binding: FragmentSignInScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInScreenBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }
    private fun setupUI(){
        binding.apply{
            tvMoveToSignUp.setOnClickListener{ findNavController().navigate(R.id.action_signInScreenFragment_to_signUpScreenFragment) }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}