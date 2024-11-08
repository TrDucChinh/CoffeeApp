package com.proptit.btl_oop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.databinding.ItemCoffeeCartBinding
import com.proptit.btl_oop.model.Coffee

class CartAdapter(private var coffeeList: List<Coffee>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val coffeeQuantities = mutableMapOf<Long, Long>()

    inner class CartViewHolder(private val binding: ItemCoffeeCartBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.name
            binding.tvCoffeeDescription.text = coffee.description
            val currentQuantity = coffeeQuantities[coffee.id] ?: 1
            binding.tvQuantity.text = currentQuantity.toString()

            binding.btnIncrease.setOnClickListener {
                val newQuantity = currentQuantity + 1
                coffeeQuantities[coffee.id] = newQuantity
                binding.tvQuantity.text = newQuantity.toString()
            }

            binding.btnDecrease.setOnClickListener {
                if (currentQuantity > 1) {
                    val newQuantity = currentQuantity - 1
                    coffeeQuantities[coffee.id] = newQuantity
                    binding.tvQuantity.text = newQuantity.toString()
                }
                else{
                    binding.tvQuantity.setText(0)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoffeeCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val coffee = coffeeList[position]
        holder.bind(coffee)
    }

    override fun getItemCount(): Int = coffeeList.size

    fun updateCoffeeList(newCoffeeList: List<Coffee>) {
        coffeeList = newCoffeeList
        notifyDataSetChanged()
    }
}
