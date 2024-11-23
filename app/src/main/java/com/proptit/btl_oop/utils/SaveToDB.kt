package com.proptit.btl_oop.utils

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.model.CartItem
import com.proptit.btl_oop.model.OrderHistory
import com.proptit.btl_oop.model.User

object SaveToDB {
    fun saveUserToDB(user: User) {
        val database = Firebase.database
        val userRef = database.getReference("Users").child(user.id)
        userRef.setValue(user)
    }

    fun updateFavouriteInFirebase(favouriteItem: FavouriteItem, isFavourite: Boolean) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val favouritesRef =
            Firebase.database.reference.child("Users").child(userId).child("favourites")

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
    fun clearCart(){
        val userId = Firebase.auth.currentUser?.uid ?: return
        val cartRef = Firebase.database.reference.child("Users").child(userId).child("carts")
        cartRef.removeValue()
    }

    fun updateOderInFirebase(cartItem: CartItem) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val oderRef = Firebase.database.reference.child("Users").child(userId).child("carts")

        oderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItemTypeIndicator = object : GenericTypeIndicator<MutableList<CartItem>>() {}
                val orders = snapshot.getValue(cartItemTypeIndicator) ?: mutableListOf()

                // Tìm sản phẩm có cùng id, sizeIdx và type
                val existingOrderIndex = orders.indexOfFirst {
                    it.id == cartItem.id && it.sizeIdx == cartItem.sizeIdx && it.type == cartItem.type
                }

                if (existingOrderIndex != -1) {
                    orders[existingOrderIndex].quantity = cartItem.quantity
                } else {
                    orders.add(cartItem)
                }


                oderRef.setValue(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read orders: ${error.message}")
            }
        })
    }


    fun removeOderInFirebase(oder: CartItem) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val oderRef = Firebase.database.reference.child("Users").child(userId).child("carts")

        oderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val oderType = object : GenericTypeIndicator<MutableList<CartItem>>() {}
                val orders = snapshot.getValue(oderType) ?: mutableListOf()

                // Tìm xem đã có sản phẩm cùng id và sizeIdx chưa
                val existingOder =
                    orders.find { it.id == oder.id && it.sizeIdx == oder.sizeIdx && it.type == oder.type }

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
    fun saveOrderHistoryToDB(orderHistory: OrderHistory) {
        val userId = Firebase.auth.currentUser?.uid ?: return
        val orderHistoryRef = Firebase.database.reference.child("Users").child(userId).child("order_history")

        orderHistoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderHistoryType = object : GenericTypeIndicator<MutableList<OrderHistory>>() {}
                val orderHistories = snapshot.getValue(orderHistoryType) ?: mutableListOf()

                orderHistories.add(orderHistory)

                orderHistoryRef.setValue(orderHistories)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read order history: ${error.message}")
            }
        })
    }


}