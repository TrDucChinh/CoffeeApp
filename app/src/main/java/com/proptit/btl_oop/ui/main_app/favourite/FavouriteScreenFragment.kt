package com.proptit.btl_oop.ui.main_app.favourite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.FavouriteAdapter
import com.proptit.btl_oop.databinding.FragmentFavouriteScreenBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.viewmodel.HomeViewModel

class FavouriteScreenFragment : Fragment() {
    private var _binding: FragmentFavouriteScreenBinding? = null
    private val binding get() = _binding!!

    private val favouriteViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }
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
        fetchData()
        setupRecyclerView()
    }
    private fun fetchData() {
        favouriteViewModel.favourites.observe(viewLifecycleOwner, Observer{
            val adapter = FavouriteAdapter(it, favouriteViewModel.coffees.value!!, favouriteViewModel.beans.value!!)
            binding.rvFavouriteItems.adapter = adapter
            adapter.updateData(it, favouriteViewModel.coffees.value!!, favouriteViewModel.beans.value!!)
        })
    }

    private fun setupRecyclerView() {
        val adapter = FavouriteAdapter(listOf(), listOf(), listOf())
        if (favouriteViewModel.favourites.value==null){
            binding.tvEmpty.visibility = View.VISIBLE
        }
        else{
            binding.rvFavouriteItems.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
            }
            favouriteViewModel.favourites.observe(viewLifecycleOwner, Observer {
                adapter.updateData(it, favouriteViewModel.coffees.value!!, favouriteViewModel.beans.value!!)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
