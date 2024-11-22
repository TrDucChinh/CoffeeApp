package com.proptit.btl_oop.model

data class Order(
    val type: String,
    val id: Int,
    val sizeIdx: Int,
    var price: Int,
    var quantity: Int
) {
    constructor() : this("", 0, 0, 0, 0)
}