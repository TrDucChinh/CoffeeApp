package com.proptit.btl_oop.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.utils.Firebase
import com.proptit.btl_oop.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _cartItem = MutableStateFlow<MutableList<CartItem>>(mutableListOf())
    val cartItem: StateFlow<MutableList<CartItem>> = _cartItem
    init {
        loadCart()
    }
    fun loadCart() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val ref = firebaseDatabase.getReference("Users")
                .child(userId)
                .child("carts")

            // Lắng nghe sự thay đổi trong giỏ hàng
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cartItems = mutableListOf<CartItem>()
                    for (data in snapshot.children) {
                        val cartItem = data.getValue(CartItem::class.java)
                        if (cartItem != null) {
                            cartItems.add(cartItem)
                        }
                    }
                    _cartItem.value = cartItems // Cập nhật giỏ hàng
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
