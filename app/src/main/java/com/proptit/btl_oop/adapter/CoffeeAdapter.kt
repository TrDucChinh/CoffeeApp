package com.proptit.btl_oop.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.databinding.ItemCoffeeBinding

class CoffeeAdapter(private var coffeeList: List<Coffee>, private val onCoffeeClick: (Int) -> Unit) : RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    class CoffeeViewHolder(private val binding: ItemCoffeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.name
            binding.tvCoffeeIngredients.text = coffee.ingredients
            binding.tvCoffeePrice.text = "${"%,d".format(coffee.price)}đ"
            Glide.with(binding.root)
                .load(coffee.image_url)
                .into(binding.imgCoffee)
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
        holder.itemView.setOnClickListener {
            onCoffeeClick(coffee.id)
        }
    }

    override fun getItemCount(): Int = coffeeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCoffeeList: List<Coffee>) {
        coffeeList = newCoffeeList
        notifyDataSetChanged()
    }
}
