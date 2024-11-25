package com.proptit.btl_oop.ui.main_app.favourite


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.FavouriteAdapter
import com.proptit.btl_oop.databinding.FragmentFavouriteScreenBinding
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.viewmodel.FavouriteViewModel
import com.proptit.btl_oop.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class FavouriteScreenFragment : Fragment() {
    private var _binding: FragmentFavouriteScreenBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }
    private val favouriteViewModel: FavouriteViewModel by activityViewModels {
        FavouriteViewModel.FavouriteViewModelFactory(requireActivity().application)
    }
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)
        binding.icOverview.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        setupRecyclerView()
        observeFavourites()
    }

    private fun setupRecyclerView() {
        favouriteAdapter = FavouriteAdapter(
            homeViewModel.coffees.value ?: emptyList(),
            homeViewModel.beans.value ?: emptyList(),
            { items ->
                when (items.type) {
                    Type.COFFEE.toString() -> {
                        findNavController().navigate(
                            FavouriteScreenFragmentDirections.actionFavouriteScreenFragmentToCoffeeDetailsFragment(
                                items.id,
                                true
                            )
                        )
                    }
                    Type.BEANS.toString() -> {
                        findNavController().navigate(
                            FavouriteScreenFragmentDirections.actionFavouriteScreenFragmentToBeanDetailsFragment(
                                items.id,
                                true
                            )
                        )


                    }

                }

            }
        )
        binding.rvFavouriteItems.apply {
            adapter = favouriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeFavourites() {
        // Sử dụng viewLifecycleOwner.lifecycleScope để tự động hủy khi view bị gỡ
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favouriteViewModel.favouriteItem.collect { favourites ->
                    // Đảm bảo binding không null trước khi cập nhật giao diện
                    _binding?.let {
                        if (favourites.isEmpty()) {
                            it.tvEmpty.visibility = View.VISIBLE
                        } else {
                            it.tvEmpty.visibility = View.GONE
                        }
                        favouriteAdapter.submitList(favourites)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

