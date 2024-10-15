package com.proptit.btl_oop.ui.login.sign_up

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.databinding.FragmentSignUpScreenBinding


class SignUpScreenFragment : Fragment() {
    private var _binding: FragmentSignUpScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpScreenBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.apply {
            btnSignUp.setOnClickListener { validateInput() }
            tvMoveToSignInScreen.setOnClickListener{ findNavController().popBackStack()}
            icBack.setOnClickListener{ findNavController().popBackStack()}
        }
    }

    private fun validateInput() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etSetPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
            }
            !isEmailValid(email) -> {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) -> {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            password != confirmPassword -> {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}