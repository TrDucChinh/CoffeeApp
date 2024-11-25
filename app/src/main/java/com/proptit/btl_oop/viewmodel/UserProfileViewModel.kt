package com.proptit.btl_oop.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserProfileViewModel : ViewModel() {
    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: StateFlow<String> = _name
    fun setName(name: String) {
        _name.value = name
    }
}