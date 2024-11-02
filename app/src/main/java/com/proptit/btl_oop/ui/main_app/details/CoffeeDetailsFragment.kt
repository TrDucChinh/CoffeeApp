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
import com.proptit.btl_oop.databinding.FragmentCoffeeDetailsBinding

class CoffeeDetailsFragment : Fragment() {

    private var _binding: FragmentCoffeeDetailsBinding? = null
    private val binding get() = _binding!!
    private var isFavorited = false
    private val args: CoffeeDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoffeeDetailsBinding.inflate(inflater, container, false)
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
            btnSizeS.setOnClickListener { setSelectedButton(btnSizeS) }
            btnSizeM.setOnClickListener { setSelectedButton(btnSizeM) }
            btnSizeL.setOnClickListener { setSelectedButton(btnSizeL) }

            setupSeeMoreText()

        }
    }
    private fun setupSeeMoreText(){
        binding.apply {
            tvDescriptionContent.post {
                if (tvDescriptionContent.lineCount > 3) {
                    tvSeeMore.visibility = View.VISIBLE
                    tvDescriptionContent.maxLines = 3
                } else {
                    tvSeeMore.visibility = View.GONE
                }
            }

            tvSeeMore.setOnClickListener {
                if (tvDescriptionContent.maxLines == 3) {
                    tvDescriptionContent.maxLines = Int.MAX_VALUE
                    tvSeeMore.text = "See Less"
                } else {
                    tvDescriptionContent.maxLines = 3
                    tvSeeMore.text = "See More"
                }
            }
        }
    }

    private fun setSelectedButton(selectedButton: Button) {
        binding.apply {
            btnSizeS.isSelected = (btnSizeS == selectedButton)
            btnSizeM.isSelected = (btnSizeM == selectedButton)
            btnSizeL.isSelected = (btnSizeL == selectedButton)
        }
    }
}