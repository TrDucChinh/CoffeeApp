package com.proptit.btl_oop.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.databinding.ItemCoffeeBinding

class CoffeeAdapter(private var coffeeList: List<Coffee>) : RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    class CoffeeViewHolder(private val binding: ItemCoffeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.name
            binding.tvCoffeeDescription.text = coffee.description
            binding.tvCoffeePrice.text = "${coffee.price}đ"
            binding.imgCoffee.setImageResource(coffee.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoffeeBinding.inflate(inflater, parent, false)
        return CoffeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = coffeeList[position]
        holder.bind(coffee)
    }

    override fun getItemCount(): Int = coffeeList.size
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCoffeeList: List<Coffee>) {
        coffeeList = newCoffeeList
        notifyDataSetChanged()
    }
}
