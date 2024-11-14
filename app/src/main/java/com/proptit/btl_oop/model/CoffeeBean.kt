package com.proptit.btl_oop.model

data class CoffeeBean(
    val id: Long=0,
    val name: String,
    val image_url: String,
    val description: String,
    val quantity: List<String>,
    val price: List<Int>,
    val location: String
){
    constructor(): this(0, "", "", "", listOf(), listOf(), "")
}
