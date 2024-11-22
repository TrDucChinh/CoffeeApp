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
import com.proptit.btl_oop.model.OrderHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderHistoryViewModel(application: Application): ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _orderHistory = MutableStateFlow<MutableList<OrderHistory>>(mutableListOf())
    val orderHistory: StateFlow<MutableList<OrderHistory>> = _orderHistory

    fun loadOrderHistory() {
        viewModelScope.launch {
            try {
                val orderHistory = fetchOrderHistory()
                _orderHistory.value = orderHistory.toMutableList()
            } catch (e: Exception) {
                Log.e("OrderHistoryViewModel", "Error loading order history", e)
            }
        }
    }
    private suspend fun fetchOrderHistory(): List<OrderHistory>{
        return withContext(Dispatchers.IO){
            val snapshot = firebaseDatabase.reference.child("Users").child(Firebase.auth.currentUser?.uid.toString()).child("order_history").get().await()
            snapshot.children.mapNotNull { it.getValue(OrderHistory::class.java) }
        }
    }
    class OrderHistoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderHistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderHistoryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}