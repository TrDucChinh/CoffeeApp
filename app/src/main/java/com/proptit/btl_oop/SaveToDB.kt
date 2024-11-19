package com.proptit.btl_oop

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.model.FavouriteItem
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

}