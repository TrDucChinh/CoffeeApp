package com.proptit.btl_oop.ui.main_app.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.databinding.FragmentCoffeeDetailsBinding
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.viewmodel.HomeViewModel

class CoffeeDetailsFragment : Fragment() {

    private var _binding: FragmentCoffeeDetailsBinding? = null
    private val binding get() = _binding!!
    private var isFavourited = false
    private val args: CoffeeDetailsFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }
    private var sizeIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCoffeeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFavourited = args.isFavourite
        choseSize()
        fetchData()
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnFavourite.setOnClickListener {
                isFavourited = !isFavourited
                updateFavouriteButton(isFavourited)
                val favouriteItem = FavouriteItem(Type.COFFEE.toString(), args.coffeeId)
                SaveToDB.updateFavouriteInFirebase(favouriteItem, isFavourited)
            }
            btnAddToCart.setOnClickListener {
                val action = CoffeeDetailsFragmentDirections.actionCoffeeDetailsFragmentToAddToCartFragment(args.coffeeId, Type.COFFEE.toString(), sizeIdx)
                findNavController().navigate(action)
            }
        }
        setupSeeMoreText()
    }

    private fun choseSize() {
        binding.apply {
            btnSizeS.setOnClickListener {
                setSelectedButton(btnSizeS)
                tvPriceProduct.text = "${"%,d".format(homeViewModel.coffees.value?.find { it.id == args.coffeeId }?.price?.get(0))}đ"
            }
            btnSizeM.setOnClickListener {
                setSelectedButton(btnSizeM)
                tvPriceProduct.text = "${"%,d".format(homeViewModel.coffees.value?.find { it.id == args.coffeeId }?.price?.get(1))}đ"
            }
            btnSizeL.setOnClickListener {
                setSelectedButton(btnSizeL)
                tvPriceProduct.text = "${"%,d".format(homeViewModel.coffees.value?.find { it.id == args.coffeeId }?.price?.get(2))}đ"
            }
        }
    }

    private fun fetchData() {
        val selectedCoffee = homeViewModel.coffees.value?.find { it.id == args.coffeeId}
        if (selectedCoffee != null) {
            binding.apply {
                btnSizeS.isSelected = true
                tvCoffeeName.text = selectedCoffee.name
                tvDescriptionContent.text = selectedCoffee.description
                tvPriceProduct.text = "${"%,d".format(selectedCoffee.price[0])}đ"
                btnFavourite.setImageResource(if (args.isFavourite) R.drawable.ic_heart_selected else R.drawable.ic_heart_default)
                Glide.with(binding.root)
                    .load(selectedCoffee.image_url)
                    .into(imgCoffee)
                btnSizeS.text = "${selectedCoffee.size[0]}"
                btnSizeM.text = "${selectedCoffee.size[1]}"
                btnSizeL.text = "${selectedCoffee.size[2]}"
                tvCoffeeRecipe.text = selectedCoffee.ingredients
            }
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
                sizeIdx = 0
                binding.tvPriceProduct.text =
                    "${"%,d".format(homeViewModel.coffees.value?.get(args.coffeeId)?.price?.get(0))}đ"
            }

            binding.btnSizeM -> {
                sizeIdx = 1
                binding.tvPriceProduct.text =
                    "${"%,d".format(homeViewModel.coffees.value?.get(args.coffeeId)?.price?.get(1))}đ"
            }

            binding.btnSizeL -> {
                sizeIdx = 2
                binding.tvPriceProduct.text =
                    "${"%,d".format(homeViewModel.coffees.value?.get(args.coffeeId)?.price?.get(2))}đ"
            }
        }
    }
    private fun updateFavouriteButton(isFavourite: Boolean) {
        if (isFavourite) {
            binding.btnFavourite.setImageResource(R.drawable.ic_heart_selected)
        } else {
            binding.btnFavourite.setImageResource(R.drawable.ic_heart_default)
        }
    }
}