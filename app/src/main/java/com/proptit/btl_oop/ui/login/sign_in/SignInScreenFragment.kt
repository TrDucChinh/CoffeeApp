package com.proptit.btl_oop.ui.login.sign_in

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.proptit.btl_oop.R
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.databinding.FragmentSignInScreenBinding
import com.proptit.btl_oop.model.User


class SignInScreenFragment : Fragment() {
    private var _binding: FragmentSignInScreenBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInScreenBinding.inflate(inflater, container, false)
//        //Đăng nhập 1 lần duy nhất nếu không đăng xuất không cần đăng nhập lại
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_signInScreenFragment_to_homeScreenFragment)
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
    private fun setupUI(){
        binding.apply{
            tvMoveToSignUp.setOnClickListener{ findNavController().navigate(R.id.action_signInScreenFragment_to_signUpScreenFragment) }
            tvForgotPassword.setOnClickListener{ findNavController().navigate(R.id.action_signInScreenFragment_to_forgotPasswordFragment)}
            btnSignIn.setOnClickListener { validateInput() }
            btnGoogleSignIn.setOnClickListener { googleSignIn() }
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("LoginFragment", "Google sign-in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let{
                        val name = it.email?.substringBefore("@")
                        val saveUser = name?.let { it1 -> User(it.uid, it.email!!, it1) }
                        SaveToDB.saveUserToDB(saveUser!!)
                    }
                    Toast.makeText(requireContext(), "Google sign-in successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signInScreenFragment_to_homeScreenFragment)
                } else {
                    Log.w("LoginFragment", "Google sign-in failed", task.exception)
                }
            }
    }

    private fun validateInput() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            TextUtils.isEmpty(email) || TextUtils.isEmpty(password)-> {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            !isEmailValid(email) -> {
                Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Đăng nhập
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signInScreenFragment_to_homeScreenFragment)
                        } else {
                            Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
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