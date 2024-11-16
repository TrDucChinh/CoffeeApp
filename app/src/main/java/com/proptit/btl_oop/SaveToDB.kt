package com.proptit.btl_oop

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.proptit.btl_oop.model.FavouriteItem
import com.proptit.btl_oop.model.User
import kotlinx.coroutines.flow.combine

object SaveToDB {
    fun saveUserToDB(user : User){
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(user.id)
        userRef.setValue(user)
    }
}