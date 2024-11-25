package com.proptit.btl_oop.response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoongApiService {
    @GET("Place/AutoComplete")
    fun getAutoCompleteSuggestions(
        @Query("api_key") apiKey: String,
        @Query("location") location: String,
        @Query("input") input: String
    ): Call<AutoCompleteResponse>
}

