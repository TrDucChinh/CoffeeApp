package com.proptit.btl_oop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.databinding.ItemCoffeeFavouriteBinding
import com.proptit.btl_oop.model.Coffee

class FavouriteAdapter(private var coffeeList: List<Coffee>) : RecyclerView.Adapter<FavouriteAdapter.CoffeeViewHolder>() {

    class CoffeeViewHolder(private val binding: ItemCoffeeFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.name
            binding.tvDescriptionContent.text = coffee.description
            binding.imgCoffee.setImageResource(coffee.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoffeeFavouriteBinding.inflate(inflater, parent, false)
        return CoffeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = coffeeList[position]
        holder.bind(coffee)
    }

    override fun getItemCount(): Int = coffeeList.size
    
    fun updateData(newCoffeeList: List<Coffee>) {
        coffeeList = newCoffeeList
        notifyDataSetChanged()
    }
}
