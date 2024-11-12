package com.proptit.btl_oop.model

data class Coffee(
    val id: Int = 0,
    val name: String = "",
    val image_url: String = "",
    val description: String = "",
    val price: Int = 0,
    val categoryId: Int = 0
){
    constructor(): this(0, "", "", "", 0, 0)
}
