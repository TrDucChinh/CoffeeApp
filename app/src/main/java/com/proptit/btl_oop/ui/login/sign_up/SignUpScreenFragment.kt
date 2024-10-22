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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.databinding.FragmentSignUpScreenBinding
import com.proptit.btl_oop.model.User


class SignUpScreenFragment : Fragment() {
    private var _binding: FragmentSignUpScreenBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()

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
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT)
                    .show()
            }

            !isEmailValid(email) -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) -> {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }

            password != confirmPassword -> {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                registerUser(email, password, binding.etFullName.text.toString().trim())
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun registerUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val user = User(id = userId ?: "", email = email, name = name)
                    SaveToDB.saveUserToDB(user)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
    private fun saveUserToDatabase(user: User) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(user.id)
        userRef.setValue(user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}