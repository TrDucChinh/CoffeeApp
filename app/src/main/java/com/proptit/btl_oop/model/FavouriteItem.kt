package com.proptit.btl_oop.model

data class FavouriteItem(val type: String, val id: Int) {
    constructor() : this("", 0 )
}