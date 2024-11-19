package com.proptit.btl_oop.ui.main_app.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.Firebase
import com.proptit.btl_oop.R
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.TypeFavourite
import com.proptit.btl_oop.databinding.FragmentBeanDetailsBinding
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.viewmodel.HomeViewModel

class BeanDetailsFragment : Fragment() {

    private var _binding: FragmentBeanDetailsBinding? = null
    private val binding get() = _binding!!

    private var isFavourited = false
    private val args: BeanDetailsFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeanDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFavourited = args.isFavourite
        fetchData()
        choseSize()
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnFavourite.setOnClickListener {
            isFavourited = !isFavourited
            updateFavouriteButton(isFavourited)
            val favouriteItem = FavouriteItem(TypeFavourite.BEANS.toString(), args.beanId)
            SaveToDB.updateFavouriteInFirebase(favouriteItem, isFavourited)
        }
        showMoreDescription()
    }

    private fun updateFavouriteButton(isFavourite: Boolean) {
        if (isFavourite) {
            binding.btnFavourite.setImageResource(R.drawable.ic_heart_selected)
        } else {
            binding.btnFavourite.setImageResource(R.drawable.ic_heart_default)
        }
    }

    private fun fetchData() {
        val selectedBean = homeViewModel.beans.value?.find { it.id == args.beanId }
        if (selectedBean != null) {
            binding.apply {
                btnSizeSmall.isSelected = true
                tvBeanName.text = selectedBean.name
                tvDescriptionContent.text = selectedBean.description
                tvPriceProduct.text = "${"%,d".format(selectedBean.price[0])}đ"
                btnFavourite.setImageResource(if (args.isFavourite) R.drawable.ic_heart_selected else R.drawable.ic_heart_default)
                Glide.with(binding.root)
                    .load(selectedBean.image_url)
                    .into(imgBean)
                btnSizeSmall.text = "${selectedBean.quantity[0]}"
                btnSizeMedium.text = "${selectedBean.quantity[1]}"
                btnSizeLarge.text = "${selectedBean.quantity[2]}"
                tvBeanRegion.text = selectedBean.location
            }
        }
    }

    private fun choseSize() {
        binding.apply {
            btnSizeSmall.setOnClickListener {
                setSelectedButton(btnSizeSmall)
                tvPriceProduct.text = "${"%,d".format(homeViewModel.beans.value?.find { it.id == args.beanId }?.price?.get(0))}đ"
            }
            btnSizeMedium.setOnClickListener {
                setSelectedButton(btnSizeMedium)
                tvPriceProduct.text = "${"%,d".format(homeViewModel.beans.value?.find { it.id == args.beanId }?.price?.get(1))}đ"
            }
            btnSizeLarge.setOnClickListener {
                setSelectedButton(btnSizeLarge)
                tvPriceProduct.text = "${"%,d".format(homeViewModel.beans.value?.find { it.id == args.beanId }?.price?.get(2))}đ"
            }
        }
    }

    private fun showMoreDescription() {
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
            btnSizeSmall.isSelected = (btnSizeSmall == selectedButton)
            btnSizeMedium.isSelected = (btnSizeMedium == selectedButton)
            btnSizeLarge.isSelected = (btnSizeLarge == selectedButton)
        }
    }
}