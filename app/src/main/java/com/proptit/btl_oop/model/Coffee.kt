package com.proptit.btl_oop.model

data class Coffee(
    val id: Long=0,
    val name: String,
    val imageResId: Int,
    val description: String,
    val price: Int,
    val categoryId: Long,
    var isFavorite: Boolean = false
)
