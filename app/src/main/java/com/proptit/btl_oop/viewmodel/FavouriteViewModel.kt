package com.proptit.btl_oop.viewmodel

import com.proptit.btl_oop.model.FavouriteItem
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.proptit.btl_oop.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavouriteViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _favouriteItem = MutableStateFlow<MutableList<FavouriteItem>>(mutableListOf())
    val favouriteItem: StateFlow<MutableList<FavouriteItem>> = _favouriteItem

    init {
        loadFavourite()
    }

    fun loadFavourite() {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val ref = firebaseDatabase.getReference("Users")
                .child(userId)
                .child("favourites")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favouriteItems = mutableListOf<FavouriteItem>()
                    for (data in snapshot.children) {
                        val favouriteItem = data.getValue(FavouriteItem::class.java)
                        if (favouriteItem != null) {
                            favouriteItems.add(favouriteItem)
                        }
                    }
                    _favouriteItem.value = favouriteItems
                    Log.e("FavouriteViewModel", "Cart updated: $favouriteItems")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FavouriteViewModel", "Error loading cart: ${error.message}")
                }
            })
        }
    }


    class FavouriteViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavouriteViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
