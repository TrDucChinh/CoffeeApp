package com.proptit.btl_oop.model

data class OrderHistory(val date: String, val cart: MutableList<CartItem>, val totalPrice: Int) {
    constructor() : this("", mutableListOf(), 0)
}