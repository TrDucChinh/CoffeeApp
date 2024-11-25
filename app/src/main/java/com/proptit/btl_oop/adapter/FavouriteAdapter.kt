package com.proptit.btl_oop.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.utils.Type
import com.proptit.btl_oop.databinding.ItemBeanFavouriteBinding
import com.proptit.btl_oop.databinding.ItemCoffeeFavouriteBinding
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.FavouriteItem

class FavouriteAdapter(
    private var coffeeList: List<Coffee>,
    private var beanList: List<CoffeeBean>
) : ListAdapter<FavouriteItem, RecyclerView.ViewHolder>(FavouriteDiffCallback()) {

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
                SaveToDB.updateFavouriteInFirebase(FavouriteItem(Type.COFFEE.toString(), coffee.id), false)
                removeItem(adapterPosition)
            }
        }
    }

    // ViewHolder cho Bean
    inner class BeanViewHolder(private val binding: ItemBeanFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: CoffeeBean) {
            binding.tvBeanName.text = bean.name
            binding.tvDescriptionContent.text = bean.description
            Glide.with(binding.root)
                .load(bean.image_url)
                .into(binding.imgBean)
            binding.btnFavourite.setOnClickListener {
                binding.btnFavourite.setImageResource(R.drawable.ic_heart_default)
                SaveToDB.updateFavouriteInFirebase(FavouriteItem(Type.BEANS.toString(), bean.id), false)
                removeItem(adapterPosition)
            }
        }
    }

    // Phương thức trả về view type cho từng mục
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type == Type.COFFEE.toString()) {
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
        val favouriteItem = getItem(position)
        if (holder is CoffeeViewHolder) {
            val coffee = coffeeList.find { it.id == favouriteItem.id }
            coffee?.let { holder.bind(it) }
        } else if (holder is BeanViewHolder) {
            val bean = beanList.find { it.id == favouriteItem.id }
            bean?.let { holder.bind(it) }
        }
    }

    // Xóa mục khỏi danh sách
    fun removeItem(position: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(position)
        submitList(currentList)
    }

    class FavouriteDiffCallback : DiffUtil.ItemCallback<FavouriteItem>() {
        override fun areItemsTheSame(oldItem: FavouriteItem, newItem: FavouriteItem): Boolean {
            return oldItem.id == newItem.id && oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: FavouriteItem, newItem: FavouriteItem): Boolean {
            return oldItem == newItem
        }
    }

}
