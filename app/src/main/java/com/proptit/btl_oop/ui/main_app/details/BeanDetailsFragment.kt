package com.proptit.btl_oop.ui.main_app.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentBeanDetailsBinding

class BeanDetailsFragment : Fragment() {

    private var _binding: FragmentBeanDetailsBinding? = null
    private val binding get() = _binding!!

    private var isFavorited = false
    private val args: BeanDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeanDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnFavourite: ImageButton = view.findViewById(R.id.btnFavourite)
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnFavourite.setOnClickListener {
                isFavorited = !isFavorited
                if (isFavorited) {
                    btnFavourite.setImageResource(R.drawable.ic_heart_selected)
                } else {
                    btnFavourite.setImageResource(R.drawable.ic_heart_default)
                }
            }

            btnSizeSmall.setOnClickListener { setSelectedButton(btnSizeSmall) }
            btnSizeMedium.setOnClickListener { setSelectedButton(btnSizeMedium) }
            btnSizeLarge.setOnClickListener { setSelectedButton(btnSizeLarge) }
        }
    }
    private fun setSelectedButton(selectedButton: Button) {
        binding.apply {
            btnSizeSmall.isSelected = (btnSizeSmall == selectedButton)
            btnSizeMedium.isSelected = (btnSizeMedium == selectedButton)
            btnSizeLarge.isSelected = (btnSizeLarge == selectedButton)
        }
    }
}