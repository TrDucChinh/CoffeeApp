package com.proptit.btl_oop.model

data class OrderHistory(val date: Long, val cart: MutableList<CartItem>, val totalPrice: Int, val payment: String, val address: String, val sdt: String) {
    constructor() : this(0, mutableListOf(), 0, "", "", "")
}