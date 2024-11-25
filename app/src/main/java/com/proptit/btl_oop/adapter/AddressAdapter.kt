package com.proptit.btl_oop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proptit.btl_oop.databinding.ItemPlaceBinding
import com.proptit.btl_oop.model.Address

class AddressAdapter(
    private val suggestions: List<Address>,
    private val onItemClick: (Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = suggestions[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int = suggestions.size

    inner class AddressViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.placeName.text = address.firstAddress
            binding.placeAddress.text = address.fullAddress

            itemView.setOnClickListener {
                onItemClick(address)
            }
        }
    }
}
