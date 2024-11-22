package com.proptit.btl_oop.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proptit.btl_oop.Firebase
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeBean
import com.proptit.btl_oop.model.CoffeeCategory
import com.proptit.btl_oop.model.FavouriteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : ViewModel() {
    private val firebaseDatabase = Firebase.database

    private val _categories = MutableLiveData<MutableList<CoffeeCategory>>()
    private val _coffees = MutableLiveData<MutableList<Coffee>>()
    private val _beans = MutableLiveData<MutableList<CoffeeBean>>()
    private val _favourites = MutableLiveData<MutableList<FavouriteItem>>()

    val coffees: LiveData<MutableList<Coffee>> = _coffees
    val beans: LiveData<MutableList<CoffeeBean>> = _beans
    val categories: LiveData<MutableList<CoffeeCategory>> = _categories
    val favourites: LiveData<MutableList<FavouriteItem>> = _favourites
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

    // Load favourites
    fun loadFavourite() {
        viewModelScope.launch {
            try {
                val favourites = fetchFavouritesFromFirebase()
                _favourites.postValue(favourites.toMutableList())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading favourites", e)
            }
        }
    }

    // Firebase fetch functions with suspend
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

    private suspend fun fetchFavouritesFromFirebase(): List<FavouriteItem> {
        return withContext(Dispatchers.IO) {
            val snapshot = firebaseDatabase.reference.child("Users").child(Firebase.auth.currentUser?.uid.toString()).child("favourites").get().await()
            snapshot.children.mapNotNull { it.getValue(FavouriteItem::class.java) }
        }
    }
    /*fun loadCategory() {
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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Load category failed: ${error.message}")
            }
        })
    }

    fun loadCoffee() {
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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Load coffee failed: ${error.message}")
            }
        })
//        }
    }
    fun loadFavourite(){
        val ref = firebaseDatabase.getReference("Users").child(Firebase.auth.currentUser?.uid.toString()).child("favourites")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<FavouriteItem>()
                for (data in snapshot.children) {
                    val favourite = data.getValue(FavouriteItem::class.java)
                    if (favourite != null) {
                        lists.add(favourite)
                    }
                }
                _favourites.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Load coffee failed: ${error.message}")
            }
        })
    }
    fun loadCoffeeBean(){
        val ref = firebaseDatabase.getReference("Beans")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CoffeeBean>()
                for (data in snapshot.children) {
                    val bean = data.getValue(CoffeeBean::class.java)
                    if (bean != null) {
                        lists.add(bean)
                    }
                }
                _beans.value = lists
                Log.d("HomeViewModel", "Bean loaded: $lists")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Load coffee failed: ${error.message}")
            }
        })
    }*/

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
