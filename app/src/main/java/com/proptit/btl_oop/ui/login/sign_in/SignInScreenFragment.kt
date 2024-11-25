package com.proptit.btl_oop.ui.login.sign_in

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.proptit.btl_oop.utils.Firebase
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.databinding.FragmentSignInScreenBinding
import com.proptit.btl_oop.model.User


class SignInScreenFragment : Fragment() {
    private var _binding: FragmentSignInScreenBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    private lateinit var googleSignInClient: GoogleSignInClient
    private val progessDialog: ProgressDialog by lazy {
        ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setMessage("Please wait...")
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("LoginFragment", "Google sign-in failed", e)
                showToast("Google sign-in failed. Please try again.")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInScreenBinding.inflate(inflater, container, false)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToHomeScreen()
        } else {
            setupGoogleSignIn()
            setupUI()
        }

        return binding.root
    }

    private fun setupGoogleSignIn() {
        googleSignInClient = GoogleSignIn.getClient(
            requireContext(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    private fun setupUI() {
        binding.apply {
            tvMoveToSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_signInScreenFragment_to_signUpScreenFragment)
            }
            tvForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_signInScreenFragment_to_forgotPasswordFragment)
            }
            btnSignIn.setOnClickListener { validateInput() }
            btnGoogleSignIn.setOnClickListener { googleSignIn() }
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        progessDialog.show()
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                progessDialog.dismiss()
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val email = it.email ?: "unknown@example.com"
                        val name = email.substringBefore("@")
                        val saveUser = User(it.uid, email, name)
                        SaveToDB.saveUserToDB(saveUser)
                    }
                    showToast("Google sign-in successful")
                    navigateToHomeScreen()
                } else {
                    Log.w("LoginFragment", "Google sign-in failed", task.exception)
                    showToast("Authentication failed. Please try again.")
                }
            }
    }

    private fun validateInput() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            TextUtils.isEmpty(email) || TextUtils.isEmpty(password) -> {
                showToast("Please fill in all fields")
            }
            !isEmailValid(email) -> {
                showToast("Please enter a valid email address")
            }
            else -> {
                progessDialog.show()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        progessDialog.dismiss()
                        if (task.isSuccessful) {
                            showToast("Sign in successful")
                            navigateToHomeScreen()
                        } else {
                            showToast("Sign in failed")
                        }
                    }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun navigateToHomeScreen() {
        findNavController().navigate(R.id.action_signInScreenFragment_to_homeScreenFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
