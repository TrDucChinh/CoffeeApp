package com.proptit.btl_oop.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proptit.btl_oop.utils.Firebase
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.CoffeeCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database

    private val _categories = MutableLiveData<MutableList<CoffeeCategory>>()
    private val _coffees = MutableLiveData<MutableList<Coffee>>()
    private val _beans = MutableLiveData<MutableList<CoffeeBean>>()

    val coffees: LiveData<MutableList<Coffee>> = _coffees
    val beans: LiveData<MutableList<CoffeeBean>> = _beans
    val categories: LiveData<MutableList<CoffeeCategory>> = _categories

    fun loadCategory() {
        viewModelScope.launch {
            try {
                val categories = fetchCategoriesFromFirebase()
                _categories.postValue(categories.toMutableList())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading categories", e)
            }
        }
    }

    // Load coffees
    fun loadCoffee() {
        viewModelScope.launch {
            try {
                val coffees = fetchCoffeesFromFirebase()
                _coffees.postValue(coffees.toMutableList())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading coffees", e)
            }
        }
    }

    // Load coffee beans
    fun loadCoffeeBean() {
        viewModelScope.launch {
            try {
                val beans = fetchCoffeeBeansFromFirebase()
                _beans.postValue(beans.toMutableList())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading coffee beans", e)
            }
        }
    }

    // Bất đồng bộ load
    private suspend fun fetchCategoriesFromFirebase(): List<CoffeeCategory> {
        return withContext(Dispatchers.IO) {
            val snapshot = firebaseDatabase.reference.child("Categories").get().await()
            snapshot.children.mapNotNull { it.getValue(CoffeeCategory::class.java) }
        }
    }

    private suspend fun fetchCoffeesFromFirebase(): List<Coffee> {
        return withContext(Dispatchers.IO) {
            val snapshot = firebaseDatabase.reference.child("Items").get().await()
            snapshot.children.mapNotNull { it.getValue(Coffee::class.java) }
        }
    }

    private suspend fun fetchCoffeeBeansFromFirebase(): List<CoffeeBean> {
        return withContext(Dispatchers.IO) {
            val snapshot =firebaseDatabase.reference.child("Beans").get().await()
            snapshot.children.mapNotNull { it.getValue(CoffeeBean::class.java) }
        }
    }

    class HomeViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
