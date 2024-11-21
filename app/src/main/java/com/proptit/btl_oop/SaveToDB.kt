package com.proptit.btl_oop

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.model.Order
import com.proptit.btl_oop.model.User
import kotlinx.coroutines.flow.combine

object SaveToDB {
    fun saveUserToDB(user : User){
        val database = Firebase.database
        val userRef = database.getReference("Users").child(user.id)
        userRef.setValue(user)
    }
    fun updateFavouriteInFirebase(favouriteItem: FavouriteItem, isFavourite: Boolean) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val favouritesRef = Firebase.database.reference.child("Users").child(userId).child("favourites")

        favouritesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favouriteType = object : GenericTypeIndicator<MutableList<FavouriteItem>>() {}
                val favourites = snapshot.getValue(favouriteType) ?: mutableListOf()

                if (isFavourite) {
                    // Add to favourites if it doesn't already exist
                    if (!favourites.any { it.type == favouriteItem.type && it.id == favouriteItem.id }) {
                        favourites.add(favouriteItem)
                    }
                } else {
                    // Remove from favourites
                    favourites.removeIf { it.type == favouriteItem.type && it.id == favouriteItem.id }
                }

                // Update the favourites list in Firebase
                favouritesRef.setValue(favourites)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read favourites: ${error.message}")
            }
        })
    }
    fun updateOderInFirebase(order: Order) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val oderRef = Firebase.database.reference.child("Users").child(userId).child("orders")

        oderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val oderType = object : GenericTypeIndicator<MutableList<Order>>() {}
                val orders = snapshot.getValue(oderType) ?: mutableListOf()

                // Tìm xem đã có sản phẩm cùng id và sizeIdx chưa
                val existingOder = orders.find { it.id == order.id && it.sizeIdx == order.sizeIdx && it.type == order.type }

                if (existingOder != null) {
                    // Nếu đã tồn tại (cùng id và sizeIdx), cập nhật số lượng
                    existingOder.quantity += order.quantity
                } else {
                    // Nếu khác size hoặc chưa tồn tại, thêm đơn hàng mới
                    orders.add(order)
                }

                // Ghi dữ liệu lại Firebase
                oderRef.setValue(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read orders: ${error.message}")
            }
        })
    }
    fun removeOderInFirebase(oder: Order) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val oderRef = Firebase.database.reference.child("Users").child(userId).child("orders")

        oderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val oderType = object : GenericTypeIndicator<MutableList<Order>>() {}
                val orders = snapshot.getValue(oderType) ?: mutableListOf()

                // Tìm xem đã có sản phẩm cùng id và sizeIdx chưa
                val existingOder = orders.find { it.id == oder.id && it.sizeIdx == oder.sizeIdx && it.type == oder.type }

                if (existingOder != null) {
                    existingOder.quantity -= oder.quantity
                    if (existingOder.quantity <= 0) {
                        orders.remove(existingOder)
                    }
                }
                // Ghi dữ liệu lại Firebase
                oderRef.setValue(orders)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read orders: ${error.message}")
            }
        })
    }


}