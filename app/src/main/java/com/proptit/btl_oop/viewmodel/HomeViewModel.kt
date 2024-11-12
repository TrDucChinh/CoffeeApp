package com.proptit.btl_oop.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proptit.btl_oop.Firebase
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeCategory

class HomeViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database
    /*private val preferences: SharedPreferences =
        application.getSharedPreferences("firebase_cache", Context.MODE_PRIVATE)*/

    private val _categories = MutableLiveData<MutableList<CoffeeCategory>>()
    private val _coffees = MutableLiveData<MutableList<Coffee>>()

    val coffees: LiveData<MutableList<Coffee>> = _coffees
    val categories: LiveData<MutableList<CoffeeCategory>> = _categories

    // Hàm generic để lưu vào cache
    /*private inline fun <reified T> saveToCache(key: String, data: T) {
        val jsonString = Gson().toJson(data)
        preferences.edit().putString(key, jsonString).apply()
    }

    // Hàm generic để lấy từ cache
    private inline fun <reified T> getFromCache(key: String): T? {
        val jsonString = preferences.getString(key, null)
        return if (jsonString != null) {
            val type = object : TypeToken<T>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            null
        }
    }*/

    // Hàm sử dụng generic cache cho danh mục
    fun loadCategory() {
        /*val cachedCategories = getFromCache<List<CoffeeCategory>>("category_data")
        if (cachedCategories != null) {
            _categories.value = cachedCategories.toMutableList()
        } else {*/
        val ref = firebaseDatabase.getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CoffeeCategory>()
                for (data in snapshot.children) {
                    val category = data.getValue(CoffeeCategory::class.java)
                    if (category != null) {
                        lists.add(category)
                    }
                }
                _categories.value = lists
                Log.d("HomeViewModel", "Categories loaded: $lists")
//                    saveToCache("category_data", lists) // Lưu cache sau khi tải từ Firebase
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Load category failed: ${error.message}")
            }
        })
//        }
    }

    fun loadCoffee() {
        /* val cachedCoffees = getFromCache<List<Coffee>>("coffee_data")
         if (cachedCoffees != null) {
             _coffees.value = cachedCoffees.toMutableList()
         } else {*/
        val ref = firebaseDatabase.getReference("Items")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<Coffee>()
                for (data in snapshot.children) {
                    val coffee = data.getValue(Coffee::class.java)
                    if (coffee != null) {
                        lists.add(coffee)
                    }
                }
                _coffees.value = lists
//                    saveToCache("coffee_data", lists) // Lưu cache sau khi tải từ Firebase
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Load coffee failed: ${error.message}")
            }
        })
//        }
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
