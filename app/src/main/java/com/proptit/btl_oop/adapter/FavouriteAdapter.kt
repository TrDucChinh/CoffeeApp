package com.proptit.btl_oop.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proptit.btl_oop.R
import com.proptit.btl_oop.SaveToDB
import com.proptit.btl_oop.Type
import com.proptit.btl_oop.databinding.ItemBeanFavouriteBinding
import com.proptit.btl_oop.databinding.ItemCoffeeFavouriteBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.FavouriteItem

class FavouriteAdapter(private var itemList: List<FavouriteItem>,
                       private var coffeeList: List<Coffee>,
                       private var beanList: List<CoffeeBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_COFFEE = 1
    private val VIEW_TYPE_BEAN = 2

    // ViewHolder cho Coffee
    inner class CoffeeViewHolder(private val binding: ItemCoffeeFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.name
            binding.tvDescriptionContent.text = coffee.description
            Glide.with(binding.root)
                .load(coffee.image_url)
                .into(binding.imgCoffee)
            binding.btnFavourite.setOnClickListener {
                binding.btnFavourite.setImageResource(R.drawable.ic_heart_default)
                SaveToDB.updateFavouriteInFirebase(FavouriteItem(Type.COFFEE.toString(),coffee.id), false)
                removeItem(adapterPosition)
            }
        }
    }

    // ViewHolder cho Bean
    inner class BeanViewHolder(private val binding: ItemBeanFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: CoffeeBean) {
            Log.e("FavouriteScreen", bean.image_url)
            binding.tvBeanName.text = bean.name
            binding.tvDescriptionContent.text = bean.description
            Glide.with(binding.root)
                .load(bean.image_url)
                .into(binding.imgBean)
            binding.btnFavourite.setOnClickListener {
                binding.btnFavourite.setImageResource(R.drawable.ic_heart_default)
                SaveToDB.updateFavouriteInFirebase(FavouriteItem(Type.BEANS.toString(),bean.id), false)
                removeItem(adapterPosition)
            }

        }
    }

    // Phương thức trả về view type cho từng mục
    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].type == Type.COFFEE.toString()) {
            VIEW_TYPE_COFFEE
        } else {
            VIEW_TYPE_BEAN
        }
    }

    // Tạo ViewHolder cho mỗi loại mục yêu thích
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COFFEE -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCoffeeFavouriteBinding.inflate(inflater, parent, false)
                CoffeeViewHolder(binding)
            }
            VIEW_TYPE_BEAN -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemBeanFavouriteBinding.inflate(inflater, parent, false)
                BeanViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Gắn dữ liệu vào viewholder tương ứng
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val favouriteItem = itemList[position]
        if (holder is CoffeeViewHolder) {
            // Tìm Coffee trong danh sách dựa trên id
            val coffee = coffeeList.find { it.id == favouriteItem.id }
            coffee?.let { holder.bind(it) }
        } else if (holder is BeanViewHolder) {
            // Tìm CoffeeBean trong danh sách dựa trên id
            val bean = beanList.find { it.id == favouriteItem.id }
            bean?.let { holder.bind(it) }
        }
    }

    // Trả về số lượng mục trong danh sách
    override fun getItemCount(): Int = itemList.size
    fun removeItem(position: Int) {
        itemList = itemList.filterIndexed { index, _ -> index != position }
        notifyItemRemoved(position)
    }
    // Cập nhật lại dữ liệu
    fun updateData(newList: List<FavouriteItem>, newCoffeeList: List<Coffee>, newBeanList: List<CoffeeBean>) {
        itemList = newList
        coffeeList = newCoffeeList
        beanList = newBeanList
        notifyDataSetChanged()
    }
}
