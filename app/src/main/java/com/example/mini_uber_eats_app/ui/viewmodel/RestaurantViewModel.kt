package com.example.mini_uber_eats_app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mini_uber_eats_app.data.Restaurant
import com.example.mini_uber_eats_app.data.RestaurantFormState
import com.example.mini_uber_eats_app.data.RestaurantRepository

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RestaurantRepository(application)

    val restaurants: List<Restaurant>
        get() = repository.restaurants

    fun getRestaurant(id: String?): Restaurant? = repository.getRestaurant(id)

    fun saveRestaurant(formState: RestaurantFormState, existingId: String? = null): Restaurant {
        return repository.saveRestaurant(formState, existingId)
    }

    fun deleteRestaurant(id: String) {
        repository.deleteRestaurant(id)
    }
}
