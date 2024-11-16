package com.proptit.btl_oop.ui.main_app.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentCoffeeDetailsBinding
import com.proptit.btl_oop.viewmodel.HomeViewModel

class CoffeeDetailsFragment : Fragment() {

    private var _binding: FragmentCoffeeDetailsBinding? = null
    private val binding get() = _binding!!
    private var isFavorited = false
    private val args: CoffeeDetailsFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCoffeeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnFavourite: ImageButton = binding.btnFavourite
        args.coffeeId.let {
            homeViewModel.coffees.observe(viewLifecycleOwner) { coffees ->
                val coffee = coffees.find { it.id == args.coffeeId }
                if (coffee != null) {
                    binding.apply {
                        btnSizeS.text = coffee.size.get(0)
                        btnSizeM.text = coffee.size.get(1)
                        btnSizeL.text = coffee.size.get(2)
                        btnSizeS.isSelected = true
                        tvCoffeeName.text = coffee.name
                        tvDescriptionContent.text = coffee.description
                        tvCoffeeRecipe.text = coffee.ingredients
                        tvPriceProduct.text = "${"%,d".format(coffee.price.get(0))}đ"
                        Glide.with(binding.root)
                            .load(coffee.image_url)
                            .into(imgCoffee)
                    }
                }
            }
        }
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

    private fun setupSeeMoreText() {
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
        when (selectedButton) {
            binding.btnSizeS -> {
                binding.tvPriceProduct.text = "${"%,d".format(homeViewModel.coffees.value?.get(args.coffeeId)?.price?.get(0))}đ"
            }
            binding.btnSizeM -> {
                binding.tvPriceProduct.text = "${"%,d".format(homeViewModel.coffees.value?.get(args.coffeeId)?.price?.get(1))}đ"
            }
            binding.btnSizeL -> {
                binding.tvPriceProduct.text = "${"%,d".format(homeViewModel.coffees.value?.get(args.coffeeId)?.price?.get(2))}đ"
            }
        }
    }
}