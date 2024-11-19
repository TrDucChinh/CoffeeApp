package com.proptit.btl_oop.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val favoriteItems: List<FavouriteItem> = emptyList(),  // Rỗng ban đầu
//    val orderHistory: List<Order> = emptyList()     // Rỗng ban đầu
)
