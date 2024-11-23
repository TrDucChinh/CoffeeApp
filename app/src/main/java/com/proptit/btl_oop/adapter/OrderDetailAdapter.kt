package com.proptit.btl_oop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.databinding.ItemOrderHistoryDetailsBinding
import com.proptit.btl_oop.model.CartItem
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean

class OrderDetailAdapter(
    private val coffeeList: List<Coffee>,
    private val beanList: List<CoffeeBean>
) : ListAdapter <CartItem, RecyclerView.ViewHolder>(OrderDetailDiffCallBack()) {
    private val VIEW_TYPE_COFFEE = 1
    private val VIEW_TYPE_BEAN = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_COFFEE) {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemOrderHistoryDetailsBinding.inflate(inflater, parent, false)
            CoffeeDetailViewHolder(binding)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemOrderHistoryDetailsBinding.inflate(inflater, parent, false)
            BeanDetailViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = getItem(position)
        if (getItemViewType(position) == VIEW_TYPE_COFFEE) {
            val coffee = coffeeList.find { it.id == order.id }
            (holder as CoffeeDetailViewHolder).bind(order, coffee!!)
        } else {
            val bean = beanList.find { it.id == order.id }
            (holder as BeanDetailViewHolder).bind(order, bean!!)
        }
    }



    inner class CoffeeDetailViewHolder(private val binding: ItemOrderHistoryDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: CartItem, coffee: Coffee) {
            binding.apply {
                orderName.text = coffee.name
                orderQuantity.text = "x${cart.quantity}"
                orderPrice.text = "${"%,d".format(coffee.price[cart.sizeIdx] * cart.quantity)}đ"

                orderSize.text = "Size: ${coffee.size[cart.sizeIdx]}"
            }
            Glide.with(binding.root.context)
                .load(coffee.image_url)
                .into(binding.imgProduct)
        }
    }

    inner class BeanDetailViewHolder(private val binding: ItemOrderHistoryDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: CartItem, bean: CoffeeBean) {
            binding.apply {
                orderName.text = bean.name
                orderQuantity.text = "x${cart.quantity}"
                orderPrice.text = "${"%,d".format(bean.price[cart.sizeIdx] * cart.quantity)}đ"
                orderSize.text = "Weight: ${bean.quantity[cart.sizeIdx]}"
            }
            Glide.with(binding.root.context)
                .load(bean.image_url)
                .into(binding.imgProduct)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type == Type.COFFEE.toString()) {
            VIEW_TYPE_COFFEE
        } else {
            VIEW_TYPE_BEAN
        }
    }
    class OrderDetailDiffCallBack: DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}