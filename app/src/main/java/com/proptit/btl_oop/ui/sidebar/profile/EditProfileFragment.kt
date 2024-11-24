package com.proptit.btl_oop.ui.sidebar.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.UserProfileChangeRequest

import com.proptit.btl_oop.databinding.FragmentEditProfileBinding
import com.proptit.btl_oop.utils.Firebase
import com.proptit.btl_oop.viewmodel.UserProfileViewModel


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(UserProfileViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.icBack.setOnClickListener { findNavController().popBackStack() }

        setUpUI()
        binding.btnSaveChanges.setOnClickListener {
            updateProfile()
            findNavController().popBackStack()
        }
    }

    private fun setUpUI() {
        val user = Firebase.auth.currentUser
        binding.apply {
            etEmail.setText(user?.email)
            etFullName.setText(user?.displayName)
            etEmail.isEnabled = false
        }
    }

    private fun updateProfile() {
        val user = Firebase.auth.currentUser
        userViewModel.setName(binding.etFullName.text.toString())
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(binding.etFullName.text.toString())
            .build()
        user?.updateProfile(profileUpdates)
    }

}