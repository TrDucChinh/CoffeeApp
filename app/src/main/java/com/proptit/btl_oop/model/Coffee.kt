package com.proptit.btl_oop.model

data class Coffee(
    val id: Int = 0,
    val name: String = "",
    val ingredients: String = "",
    val image_url: String = "",
    val description: String = "",
    val price: List<Int>,
    val size: List<String>,
    val categoryId: Int = 0

){
    constructor(): this(0, "", "", "","", listOf(), listOf(), 0)
}
