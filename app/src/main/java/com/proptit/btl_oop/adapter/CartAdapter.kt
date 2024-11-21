package com.proptit.btl_oop.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.Type
import com.proptit.btl_oop.databinding.ItemBeanCartBinding
import com.proptit.btl_oop.databinding.ItemCoffeeCartBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.Order


class CartAdapter(
    private var coffeeList: List<Coffee>,
    private var coffeeBeanList: List<CoffeeBean>
) : ListAdapter<Order, RecyclerView.ViewHolder>(OrderDiffCallback()) {

    private val VIEW_TYPE_COFFEE = 1
    private val VIEW_TYPE_BEAN = 2

    inner class CoffeeViewHolder(private val binding: ItemCoffeeCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order, coffee: Coffee, payload: Map<String, Any>? = null) {
            if (payload == null) {
                binding.tvCoffeeName.text = coffee.name
                binding.tvCoffeeDescription.text = coffee.ingredients
                binding.tvSize.text = coffee.size[order.sizeIdx]
                Glide.with(binding.root)
                    .load(coffee.image_url)
                    .into(binding.imgProduct)
            }

            payload?.get("quantity")?.let {
                binding.tvQuantity.text = it.toString()
            } ?: run {
                binding.tvQuantity.text = order.quantity.toString()
            }

            payload?.get("price")?.let {
                binding.tvCoffeePrice.text = "${"%,d".format(it as Int)}đ"
            } ?: run {
                val totalPrice = coffee.price[order.sizeIdx] * order.quantity
                binding.tvCoffeePrice.text = "${"%,d".format(totalPrice)}đ"
            }

            binding.btnIncrease.setOnClickListener {
                val newQuantity = order.quantity + 1
                updateOrderQuantity(adapterPosition, newQuantity)
            }

            binding.btnDecrease.setOnClickListener {
                val newQuantity = order.quantity - 1
                updateOrderQuantity(adapterPosition, newQuantity)

            }
        }
    }

    inner class CoffeeBeanViewHolder(private val binding: ItemBeanCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order, coffeeBean: CoffeeBean, payload: Map<String, Any>? = null) {
            if (payload == null) {
                binding.tvBeanName.text = coffeeBean.name
                binding.tvBeanDescription.text = coffeeBean.location
                binding.tvSize.text = coffeeBean.quantity[order.sizeIdx]
                Glide.with(binding.root)
                    .load(coffeeBean.image_url)
                    .into(binding.imgProduct)
            }

            payload?.get("quantity")?.let {
                binding.tvQuantity.text = it.toString()
            } ?: run {
                binding.tvQuantity.text = order.quantity.toString()
            }

            payload?.get("price")?.let {
                binding.tvCoffeePrice.text = "${"%,d".format(it as Int)}đ"
            } ?: run {
                val totalPrice = coffeeBean.price[order.sizeIdx] * order.quantity
                binding.tvCoffeePrice.text = "${"%,d".format(totalPrice)}đ"
            }

            binding.btnIncrease.setOnClickListener {
                val newQuantity = order.quantity + 1
                updateOrderQuantity(adapterPosition, newQuantity)
            }

            binding.btnDecrease.setOnClickListener {
                val newQuantity = order.quantity - 1
                updateOrderQuantity(adapterPosition, newQuantity)
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

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val order = getItem(position)
        val payload = if (payloads.isNotEmpty()) payloads[0] as? Map<String, Any> else null

        when (holder) {
            is CoffeeViewHolder -> {
                val coffee = coffeeList.find { it.id == order.id }
                coffee?.let { holder.bind(order, it, payload) }
            }

            is CoffeeBeanViewHolder -> {
                val coffeeBean = coffeeBeanList.find { it.id == order.id }
                coffeeBean?.let { holder.bind(order, it, payload) }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type == Type.COFFEE.toString()) {
            VIEW_TYPE_COFFEE
        } else {
            VIEW_TYPE_BEAN
        }
    }

    private fun updateOrderQuantity(position: Int, newQuantity: Int) {
        val currentOrder = getItem(position)

        // Kiểm tra nếu số lượng mới là 0 thì xóa sản phẩm khỏi giỏ và Firebase
        if (newQuantity == 0) {
            removeOrder(position)

            // Xóa sản phẩm khỏi Firebase
            when (currentOrder.type) {
                Type.COFFEE.toString() -> {
                    // Tìm sản phẩm Coffee và xóa khỏi Firebase
                    val coffee = coffeeList.find { it.id == currentOrder.id }
                    coffee?.let {
                        SaveToDB.removeOderInFirebase(currentOrder)
                    }
                }

                Type.BEANS.toString() -> {
                    // Tìm sản phẩm CoffeeBean và xóa khỏi Firebase
                    val coffeeBean = coffeeBeanList.find { it.id == currentOrder.id }
                    coffeeBean?.let {
                        SaveToDB.removeOderInFirebase(currentOrder)
                    }
                }
            }
        } else {
            // Cập nhật số lượng và giá sản phẩm nếu không phải 0
            val updatedOrder = currentOrder.copy(quantity = newQuantity)
            val totalPrice = if (currentOrder.type == Type.COFFEE.toString()) {
                coffeeList.find { it.id == currentOrder.id }!!.price[currentOrder.sizeIdx] * newQuantity
            } else {
                coffeeBeanList.find { it.id == currentOrder.id }!!.price[currentOrder.sizeIdx] * newQuantity
            }

            // Cập nhật lại danh sách đơn hàng
            val updatedList = currentList.toMutableList()
            updatedList[position] = updatedOrder
            submitList(updatedList) {
                // Cập nhật lại RecyclerView với payload để chỉ cập nhật phần tử cần thay đổi
                notifyItemChanged(position, mapOf("quantity" to newQuantity, "price" to totalPrice))
            }

            // Đồng bộ lại với Firebase (cập nhật số lượng mới)
            when (currentOrder.type) {
                Type.COFFEE.toString() -> {
                    val coffee = coffeeList.find { it.id == currentOrder.id }
                    coffee?.let {
                        SaveToDB.updateOderInFirebase(currentOrder.copy(quantity = newQuantity))
                    }
                }

                Type.BEANS.toString() -> {
                    val coffeeBean = coffeeBeanList.find { it.id == currentOrder.id }
                    coffeeBean?.let {
                        SaveToDB.updateOderInFirebase(currentOrder.copy(quantity = newQuantity))
                    }
                }
            }
        }
    }



    private fun removeOrder(position: Int) {
        val currentOrder = getItem(position)

        // Xóa sản phẩm khỏi RecyclerView
        val updatedList = currentList.toMutableList()
        updatedList.removeAt(position)
        submitList(updatedList)

        // Đồng bộ với Firebase (xóa sản phẩm khỏi giỏ hàng)
        when (currentOrder.type) {
            Type.COFFEE.toString() -> {
                val coffee = coffeeList.find { it.id == currentOrder.id }
                coffee?.let {
                    SaveToDB.removeOderInFirebase(currentOrder)
                }
            }

            Type.BEANS.toString() -> {
                val coffeeBean = coffeeBeanList.find { it.id == currentOrder.id }
                coffeeBean?.let {
                    SaveToDB.removeOderInFirebase(currentOrder)
                }
            }
        }
    }


    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Order, newItem: Order): Any? {
            val diff = mutableMapOf<String, Any>()
            if (oldItem.quantity != newItem.quantity) {
                diff["quantity"] = newItem.quantity
            }
            if (oldItem.price != newItem.price) {
                diff["price"] = newItem.price
            }
            return if (diff.isNotEmpty()) diff else null
        }
    }
}
