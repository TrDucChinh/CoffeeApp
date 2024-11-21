package com.proptit.btl_oop

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.model.Order
import com.proptit.btl_oop.model.User

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
        val oderRef = Firebase.database.reference.child("Users").child(userId).child("Oders")

        oderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderType = object : GenericTypeIndicator<MutableList<Order>>() {}
                val oders = snapshot.getValue(orderType) ?: mutableListOf()

                // Tìm xem đã có sản phẩm cùng id và sizeIdx chưa
                val existingOder = oders.find { it.id == order.id && it.sizeIdx == order.sizeIdx }

                if (existingOder != null) {
                    // Nếu đã tồn tại (cùng id và sizeIdx), cập nhật số lượng
                    existingOder.quantity += order.quantity
                } else {
                    // Nếu khác size hoặc chưa tồn tại, thêm đơn hàng mới
                    oders.add(order)
                }
                // Ghi dữ liệu lại Firebase
                oderRef.setValue(oders)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read orders: ${error.message}")
            }
        })
    }


}