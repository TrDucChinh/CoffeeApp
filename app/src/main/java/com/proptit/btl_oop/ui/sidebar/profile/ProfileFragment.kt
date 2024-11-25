package com.proptit.btl_oop.ui.sidebar.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

    // Launcher cho phép chọn ảnh từ thư viện
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                updateProfileImage(it)
                // Sử dụng Glide để tối ưu hiển thị hình ảnh
                Glide.with(this)
                    .load(it)
                    .override(300, 300)
                    .error(R.drawable.ic_logo)
                    .into(binding.ivProfileImage)
            }
        }

    // Launcher để yêu cầu quyền đọc bộ nhớ
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private lateinit var userViewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            icEdit.setOnClickListener { requestGalleryAccess() }
        }
    }

    private fun setUserInfo() {
        val user = Firebase.auth.currentUser
        binding.apply {
            lifecycleScope.launch {
                userViewModel.name.collect { name ->
                    tvProfileName.text = if (name.isNotEmpty()) name else user?.displayName
                }
            }
            tvProfileEmail.text = user?.email
        }
        Glide.with(this)
            .load(user?.photoUrl)
            .error(R.drawable.ic_logo)
            .into(binding.ivProfileImage)
    }

    private fun requestGalleryAccess() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun updateProfileImage(uri: Uri) {
        val user = Firebase.auth.currentUser
        val profileChangeRequest = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        user?.updateProfile(profileChangeRequest)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cập nhật hình ảnh trên UI
                    Glide.with(this)
                        .load(uri)
                        .override(300, 300)
                        .error(R.drawable.ic_logo)
                        .into(binding.ivProfileImage)
                } else {
                    task.exception?.printStackTrace()
                }
            }
    }

    private fun checkPasswordChangeEligibility() {
        val user = Firebase.auth.currentUser
        user?.providerData?.let { providerData ->
            val isGoogleAccount = providerData.any { it.providerId == "google.com" }
            if (isGoogleAccount) {
                PasswordDialogFragment().show(parentFragmentManager, "PasswordDialog")
            } else {
                findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
