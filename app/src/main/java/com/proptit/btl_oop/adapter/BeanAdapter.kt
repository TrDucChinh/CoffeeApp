package com.proptit.btl_oop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.databinding.ItemBeansBinding
import com.proptit.btl_oop.model.CoffeeBean


class BeanAdapter(private var beanList: List<CoffeeBean>, private val onBeanClick: (Long) -> Unit) : RecyclerView.Adapter<BeanAdapter.BeanViewHolder>() {

    class BeanViewHolder(private val binding: ItemBeansBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: CoffeeBean) {
            binding.tvBeanName.text = bean.name
            binding.tvBeanDescription.text = bean.description
            binding.tvBeanPrice.text = "${bean.price}đ"
            binding.imgBean.setImageResource(bean.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBeansBinding.inflate(inflater, parent, false)
        return BeanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeanViewHolder, position: Int) {
        val bean = beanList[position]
        holder.bind(bean)
        holder.itemView.setOnClickListener {
            onBeanClick(bean.id)
        }
    }

    override fun getItemCount(): Int = beanList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newBeanList: List<CoffeeBean>) {
        beanList = newBeanList
        notifyDataSetChanged()
    }
}
