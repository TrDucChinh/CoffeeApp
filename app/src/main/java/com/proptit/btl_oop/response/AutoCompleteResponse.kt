package com.proptit.btl_oop.response

data class AutoCompleteResponse(
    val status: String,
    val predictions: List<Prediction>
)

data class Prediction(
    val description: String
)
