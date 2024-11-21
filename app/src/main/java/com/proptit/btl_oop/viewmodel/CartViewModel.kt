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
import kotlinx.coroutines.withContext

class CartViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _order = MutableStateFlow<MutableList<Order>>(mutableListOf())
    val order: StateFlow<MutableList<Order>> = _order

    // Load cart data using StateFlow
    fun loadCart() {
        // Fetch the data in a background thread using coroutine
        viewModelScope.launch {
            val ref =
                firebaseDatabase.getReference("Users")
                    .child(Firebase.auth.currentUser?.uid.toString())
                    .child("orders")

            // Use ValueEventListener to listen for data from Firebase
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists = mutableListOf<Order>()
                    for (data in snapshot.children) {
                        val order = data.getValue(Order::class.java)
                        if (order != null) {
                            lists.add(order)
                        }
                    }
                    // Update the MutableStateFlow with new data
                    _order.value = lists
                    Log.e("CartViewModel", "Cart loaded: $lists")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("CartViewModel", "Load cart failed: ${error.message}")
                }
            }

            // Add the listener to the database reference
            ref.addListenerForSingleValueEvent(listener)
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
