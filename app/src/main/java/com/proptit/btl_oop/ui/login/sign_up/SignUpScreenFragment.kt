package com.proptit.btl_oop.ui.login.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.proptit.btl_oop.Firebase
import com.proptit.btl_oop.MainActivity
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.databinding.FragmentSignUpScreenBinding
import com.proptit.btl_oop.model.User


class SignUpScreenFragment : Fragment() {
    private var _binding: FragmentSignUpScreenBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSignUpScreenBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.apply {
            btnSignUp.setOnClickListener { validateInput() }
            tvMoveToSignInScreen.setOnClickListener { findNavController().popBackStack() }
            icBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun validateInput() {
        val email = binding.etEmail.text.toString().trim()
        val fullName = binding.etFullName.text.toString().trim()
        val password = binding.etSetPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        when {
            TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                confirmPassword
            ) || TextUtils.isEmpty(fullName) -> {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }

            !isEmailValid(email) -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
            }

            password != confirmPassword -> {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            }

            !isPasswordValid(password) -> {
                binding.tvPasswordRequired.visibility = View.VISIBLE
            }

            else -> {
                if (isPasswordValid(password)) {
                    registerUser(email, password, binding.etFullName.text.toString().trim())
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun registerUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { registerTask ->
                if (registerTask.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val user = User(id = userId ?: "", email = email, name = name)
                    SaveToDB.saveUserToDB(user)
                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Email is exits", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}".toRegex()
        return password.matches(passwordPattern)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}