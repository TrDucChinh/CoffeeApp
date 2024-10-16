package com.proptit.btl_oop.ui.login.forgot_password

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.graphics.Typeface
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentSendLinkViaEmailBinding

class SendLinkViaEmailFragment : Fragment() {
    private var _binding: FragmentSendLinkViaEmailBinding? = null
    private val binding get() = _binding!!
    private val args: SendLinkViaEmailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendLinkViaEmailBinding.inflate(inflater, container, false)
        val email = args.email
        val message = "We have sent an email to\n$email with instructions to\nreset your password."

        // bold phần email
        val spannable = SpannableStringBuilder(message)
        val emailStartIndex = message.indexOf(email)
        val emailEndIndex = emailStartIndex + email.length
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            emailStartIndex,
            emailEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.apply {
            tvNotifySendLink.text = spannable
            icBack.setOnClickListener { findNavController().popBackStack() }
            btnBackToSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_sendLinkViaEmailFragment_to_signInScreenFragment)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
