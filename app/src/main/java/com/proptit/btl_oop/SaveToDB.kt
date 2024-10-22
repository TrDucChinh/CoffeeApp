package com.proptit.btl_oop

import com.google.firebase.database.FirebaseDatabase
import com.proptit.btl_oop.model.User

object SaveToDB {
    fun saveUserToDB(user : User){
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(user.id)
        userRef.setValue(user)
    }
}