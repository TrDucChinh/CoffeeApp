package com.proptit.btl_oop.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Firebase {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
}