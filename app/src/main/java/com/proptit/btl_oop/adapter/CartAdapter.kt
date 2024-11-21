package com.proptit.btl_oop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.Type
import com.proptit.btl_oop.databinding.ItemBeanCartBinding
import com.proptit.btl_oop.databinding.ItemCoffeeCartBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.Order

class CartAdapter(
    private var orderList: List<Order>,
    private val coffeeList: List<Coffee>,
    private val coffeeBeanList: List<CoffeeBean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_COFFEE = 1
    private val VIEW_TYPE_BEAN = 2

    inner class CoffeeViewHolder(private val binding: ItemCoffeeCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order, coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.name
            binding.tvCoffeeDescription.text = coffee.ingredients
            binding.tvQuantity.text = order.quantity.toString()
            binding.tvCoffeePrice.text = "${order.price}đ"

            // Tăng/Giảm số lượng
            binding.btnIncrease.setOnClickListener {
                val newQuantity = order.quantity + 1
                updateOrderQuantity(adapterPosition, newQuantity)
            }

            binding.btnDecrease.setOnClickListener {
                if (order.quantity > 1) {
                    val newQuantity = order.quantity - 1
                    updateOrderQuantity(adapterPosition, newQuantity)
                }
            }
        }
    }

    inner class CoffeeBeanViewHolder(private val binding: ItemBeanCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order, coffeeBean: CoffeeBean) {
            binding.tvBeanName.text = coffeeBean.name
            binding.tvBeanDescription.text = coffeeBean.location
            binding.tvQuantity.text = order.quantity.toString()
            binding.tvCoffeePrice.text = "${order.price}đ"

            // Tăng/Giảm số lượng
            binding.btnIncrease.setOnClickListener {
                val newQuantity = order.quantity + 1
                updateOrderQuantity(adapterPosition, newQuantity)
            }

            binding.btnDecrease.setOnClickListener {
                if (order.quantity > 1) {
                    val newQuantity = order.quantity - 1
                    updateOrderQuantity(adapterPosition, newQuantity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COFFEE -> {
                val binding = ItemCoffeeCartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CoffeeViewHolder(binding)
            }
            VIEW_TYPE_BEAN -> {
                val binding = ItemBeanCartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CoffeeBeanViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = orderList[position]
        when (holder) {
            is CoffeeViewHolder -> {
                val coffee = coffeeList.find { it.id == order.id }
                coffee?.let { holder.bind(order, it) }
            }
            is CoffeeBeanViewHolder -> {
                val coffeeBean = coffeeBeanList.find { it.id == order.id }
                coffeeBean?.let { holder.bind(order, it) }
            }
        }
    }

    override fun getItemCount(): Int = orderList.size

    override fun getItemViewType(position: Int): Int {
        return if (orderList[position].type == Type.COFFEE.toString()) {
            VIEW_TYPE_COFFEE
        } else {
            VIEW_TYPE_BEAN
        }
    }

    private fun updateOrderQuantity(position: Int, newQuantity: Int) {
        val updatedOrder = orderList[position].copy(quantity = newQuantity)
        orderList = orderList.toMutableList().apply {
            this[position] = updatedOrder
        }
        notifyItemChanged(position)
    }
}
