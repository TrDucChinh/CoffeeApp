package com.proptit.btl_oop.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.Firebase
import com.proptit.btl_oop.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _order = MutableStateFlow<MutableList<Order>>(mutableListOf())
    val order: StateFlow<MutableList<Order>> = _order
    init {
        loadCart()
    }
    fun loadCart() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val ref = firebaseDatabase.getReference("Users")
                .child(userId)
                .child("orders")

            // Lắng nghe sự thay đổi trong giỏ hàng
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cartItems = mutableListOf<Order>()
                    for (data in snapshot.children) {
                        val order = data.getValue(Order::class.java)
                        if (order != null) {
                            cartItems.add(order)
                        }
                    }
                    _order.value = cartItems // Cập nhật giỏ hàng
                    Log.e("CartViewModel", "Cart updated: $cartItems")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("CartViewModel", "Error loading cart: ${error.message}")
                }
            })
        }
    }



    class CartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
