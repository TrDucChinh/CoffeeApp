package com.proptit.btl_oop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.R
import com.proptit.btl_oop.model.CoffeeBean

class CoffeeBeanAdapter(private var coffeeBeans: MutableList<CoffeeBean>) :
    RecyclerView.Adapter<CoffeeBeanAdapter.CoffeeBeanViewHolder>() {

    class CoffeeBeanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeBeanImage: ImageView = itemView.findViewById(R.id.imgBean)
        val coffeeBeanName: TextView = itemView.findViewById(R.id.tvBeanName)
        val coffeeBeanDescription: TextView = itemView.findViewById(R.id.tvBeanDescription)
        val coffeeBeanPrice: TextView = itemView.findViewById(R.id.tvBeanPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeBeanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_beans, parent, false)
        return CoffeeBeanViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeBeanViewHolder, position: Int) {
        val coffeeBean = coffeeBeans[position]
        holder.coffeeBeanName.text = coffeeBean.name
        holder.coffeeBeanDescription.text = coffeeBean.description
        holder.coffeeBeanImage.setImageResource(coffeeBean.imageResId)
        holder.coffeeBeanPrice.text = "${coffeeBean.price}đ"
    }

    override fun getItemCount(): Int = coffeeBeans.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCoffeeBeans: List<CoffeeBean>) {
        coffeeBeans.clear()
        coffeeBeans.addAll(newCoffeeBeans)
        notifyDataSetChanged()
    }
}
