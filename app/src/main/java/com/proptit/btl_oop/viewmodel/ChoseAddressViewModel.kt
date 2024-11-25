package com.proptit.btl_oop.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChoseAddressViewModel : ViewModel() {
    private val _address : MutableStateFlow<String> = MutableStateFlow("")
    val address: StateFlow<String> = _address
    fun setAddress(address: String) {
        _address.value = address
    }
}