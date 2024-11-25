package com.proptit.btl_oop.ui.login.forgot_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.utils.Firebase
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val mAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                updateSendButtonState(email)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSend.setOnClickListener {
            val email = binding.etPassword.text.toString().trim()

            if (isValidEmail(email)) {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val action =
                                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSendLinkViaEmailFragment(email)
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(requireContext(), "Email not found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun updateSendButtonState(email: String) {
        if (isValidEmail(email)) {
            binding.btnSend.isEnabled = true
            binding.btnSend.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.orange)
            binding.btnSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            binding.btnSend.isEnabled = false
            binding.btnSend.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.light_orange)
            binding.btnSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
