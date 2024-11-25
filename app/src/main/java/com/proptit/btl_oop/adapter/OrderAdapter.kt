package com.proptit.btl_oop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.utils.Convert
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.databinding.ItemOrderHistoryBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.OrderHistory

class OrderAdapter(
    private var coffeeList: List<Coffee>,
    private var beanList: List<CoffeeBean>,
    private val onClick: (Int) -> Unit
) : ListAdapter<OrderHistory, RecyclerView.ViewHolder>(OrderDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderHistoryBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = getItem(position)
        (holder as OrderViewHolder).bind(order)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    inner class OrderViewHolder(private val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderHistory) {
            var name = ""
            var totalPrice = 0
            val coffeeOrder = order.cart.filter { it.type == Type.COFFEE.toString() }
            val beanOrder = order.cart.filter { it.type == Type.BEANS.toString() }

            coffeeOrder.forEach { cartItem ->
                val coffee = coffeeList.find { it.id == cartItem.id }
                coffee?.name?.let {
                    if (!name.contains(it)) {
                        if (name.isNotEmpty()) {
                            name += ", "
                        }
                        name += it
                    }
                }
                totalPrice += coffee?.price?.get(cartItem.sizeIdx)?.times(cartItem.quantity!!) ?: 0
            }

            beanOrder.forEach { cartItem ->
                val bean = beanList.find { it.id == cartItem.id }
                bean?.name?.let {
                    if (!name.contains(it)) {
                        if (name.isNotEmpty()) {
                            name += ", "
                        }
                        name += it
                    }
                }
                totalPrice += bean?.price?.get(cartItem.sizeIdx)?.times(cartItem.quantity!!) ?: 0
            }
            binding.orderName.text = name
            binding.orderDateTime.text = Convert.longToDateTime(order.date)
            binding.orderPrice.text = "${"%,d".format(totalPrice)}đ"

        }

    }

    class OrderDiffCallBack : DiffUtil.ItemCallback<OrderHistory>() {
        override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean {
            return oldItem == newItem
        }

    }


}
