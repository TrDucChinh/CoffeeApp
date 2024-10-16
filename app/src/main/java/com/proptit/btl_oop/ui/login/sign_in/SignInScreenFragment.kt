package com.proptit.btl_oop.ui.login.sign_in

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentSignInScreenBinding


class SignInScreenFragment : Fragment() {
    private var _binding: FragmentSignInScreenBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInScreenBinding.inflate(inflater, container, false)
        /* Đăng nhập 1 lần duy nhất nếu không đăng xuất không cần đăng nhập lại
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_signInScreenFragment_to_forgotPasswordFragment)
        } else {
            setupUI()
        }
         */
        setupUI()

        return binding.root
    }
    private fun setupUI(){
        binding.apply{
            tvMoveToSignUp.setOnClickListener{ findNavController().navigate(R.id.action_signInScreenFragment_to_signUpScreenFragment) }
            tvForgotPassword.setOnClickListener{ findNavController().navigate(R.id.action_signInScreenFragment_to_forgotPasswordFragment)}
            btnSignIn.setOnClickListener { validateInput() }
        }
    }
    private fun validateInput() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
            }
            !isEmailValid(email) -> {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Đăng nhập
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
//                            findNavController().navigate(R.id.action_signInScreenFragment_to_forgotPasswordFragment)
                        } else {
                            Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
//                findNavController().popBackStack()
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