package com.proptit.btl_oop.ui.sidebar.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.UserProfileChangeRequest
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentProfileBinding
import com.proptit.btl_oop.ui.main_app.dialog.PasswordDialogFragment
import com.proptit.btl_oop.utils.Firebase
import com.proptit.btl_oop.viewmodel.UserProfileViewModel
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let { uri ->
                    updateProfileImage(uri)
                    binding.ivProfileImage.setImageURI(uri)
                }
            }
        }
    private lateinit var userViewModel: UserProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(UserProfileViewModel::class.java)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserInfo()
        setupUI()

    }
    private fun setupUI() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            cardYourProfile.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment) }
            cardChangePassword.setOnClickListener { checkPasswordChangeEligibility() }
            icEdit.setOnClickListener {
                openGallery()
            }
        }
    }
    private fun setUserInfo() {
        val user = Firebase.auth.currentUser
        binding.apply {
            lifecycleScope.launch {
                userViewModel.name.collect() {
                    if (it.isNotEmpty()) {
                        tvProfileName.text = it
                    } else {
                        tvProfileName.text = user?.displayName
                    }
                }
            }
//            tvProfileName.text = user?.displayName
            tvProfileEmail.text = user?.email
        }
        val photoUrl = user?.photoUrl
        Glide.with(this)
            .load(photoUrl)
            .error(R.drawable.ic_logo)
            .into(binding.ivProfileImage)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun updateProfileImage(uri: Uri) {
        val user = Firebase.auth.currentUser
        val profileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(user?.displayName)
            .setPhotoUri(uri)
            .build()
        user?.updateProfile(profileChangeRequest)
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
    }

    private fun checkPasswordChangeEligibility() {
        val user = Firebase.auth.currentUser
        user?.providerData?.let { providerData ->
            val isGoogleAccount = providerData.any { it.providerId == "google.com" }
            if (isGoogleAccount) {
                PasswordDialogFragment().show(
                    parentFragmentManager,
                    "PasswordDialog"
                )
            } else {
                findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
            }
        }
    }
}