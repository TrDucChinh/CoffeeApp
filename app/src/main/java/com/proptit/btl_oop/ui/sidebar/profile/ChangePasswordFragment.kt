package com.proptit.btl_oop.ui.sidebar.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentChangePasswordBinding
import com.proptit.btl_oop.utils.Check
import com.proptit.btl_oop.utils.Firebase


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveChanges.setOnClickListener {
            changePassword()
        }
    }
    private fun changePassword() {
        val user = Firebase.auth.currentUser
        val newPass = binding.etNewPassword.text.toString()
        val oldPass = binding.etCurrentPassword.text.toString()
        val confirmPass = binding.etConfirmPassword.text.toString()

        when {
            newPass.isEmpty() || oldPass.isEmpty() || confirmPass.isEmpty() -> {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return
            }
            !Check.isPasswordValid(newPass) -> {
                binding.etNewPassword.error = "Password must contain at least 6 characters, including UPPER/lowercase and numbers"
                return
            }
            newPass != confirmPass -> {
                Toast.makeText(requireContext(), "New passwords do not match", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                val credential = EmailAuthProvider.getCredential(user?.email!!, oldPass)
                user.reauthenticate(credential)
                    .addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            user.updatePassword(newPass)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
                                        findNavController().popBackStack()  // Quay lại màn hình trước
                                    } else {
                                        Toast.makeText(requireContext(), "Password update failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // Nếu mật khẩu cũ không đúng
                            Toast.makeText(requireContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


}