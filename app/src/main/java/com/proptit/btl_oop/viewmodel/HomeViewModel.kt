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
import com.proptit.btl_oop.Firebase
import com.proptit.btl_oop.model.Coffee
import com.proptit.btl_oop.model.CoffeeCategory

class HomeViewModel(application: Application): ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _categories = MutableLiveData<MutableList<CoffeeCategory>>()
    private val _coffees = MutableLiveData<MutableList<Coffee>>()

    val coffees: LiveData<MutableList<Coffee>> = _coffees
    val categories: LiveData<MutableList<CoffeeCategory>> = _categories


    fun loadCategory(){
        val ref = firebaseDatabase.getReference("categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CoffeeCategory>()
                for (data in snapshot.children) {
                    val category = data.getValue(CoffeeCategory::class.java)
                    if (category != null) {
                        lists.add(category)
                    }
                }
                _categories.value = lists
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun loadCoffee(){

    }

    class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}